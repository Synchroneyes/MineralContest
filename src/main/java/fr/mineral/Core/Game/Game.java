package fr.mineral.Core.Game;

import fr.groups.Core.Groupe;
import fr.groups.Utils.Etats;
import fr.mineral.Core.Arena.Arene;
import fr.mineral.Core.House;
import fr.mineral.Events.PlayerMove;
import fr.mineral.Settings.GameSettingsCvarOLD;
import fr.mineral.Teams.Equipe;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.BlockSaver;
import fr.mineral.Utils.ChatColorString;
import fr.mineral.Utils.Door.AutomaticDoors;
import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.Utils.Log.GameLogger;
import fr.mineral.Utils.Log.Log;
import fr.mineral.Utils.Metric.SendInformation;
import fr.mineral.Utils.MobKiller;
import fr.mineral.Utils.Player.CouplePlayerTeam;
import fr.mineral.Utils.Player.PlayerBaseItem;
import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.Utils.Radius;
import fr.mineral.mineralcontest;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

/*
    Classe représentant une partie MineralContest
 */
public class Game implements Listener {
    /*
        Une game possède:
            - Une arene
            - Un temps de jeu
            -
     */
    private Arene arene;


    private LinkedList<Player> playersReady;

    // Temps de la partie en minute
    private static int DUREE_PARTIE = 60;

    // Temps en minute
    private int tempsPartie = 60 * DUREE_PARTIE;
    public int PreGameTimeLeft = 10;

    private boolean GameStarted = false;
    private boolean GamePaused = false;
    private boolean PreGame = false;
    private boolean GameEnded = false;
    private boolean GameForced = false;
    public boolean isGameInitialized = false;
    public int killCounter = 0;
    private AutomaticDoors portes;
    private LinkedList<CouplePlayerTeam> disconnectedPlayers;
    // <username, allowed to login>
    private HashMap<String, Boolean> PlayerThatTriedToLogIn;
    private LinkedList<Block> addedChests;


    // Group of the game
    public Groupe groupe;

    private LinkedList<House> equipes;


    public Equipe getWinningTeam() {
        int maxScore = Integer.MIN_VALUE;
        Equipe winner = null;
        for (House maison : equipes) {
            if (maison.getTeam().getScore() > maxScore) {
                winner = maison.getTeam();
                maxScore = winner.getScore();
            }
        }

        return winner;
    }

    // Save the blocks
    public LinkedList<BlockSaver> affectedBlocks;

    private LinkedList<Player> referees;

    public Game(Groupe g) {

        this.arene = new Arene(g);

        //votemap.enableVote();
        this.disconnectedPlayers = new LinkedList<CouplePlayerTeam>();
        this.affectedBlocks = new LinkedList<>();
        this.referees = new LinkedList<>();
        this.playersReady = new LinkedList<>();
        this.PlayerThatTriedToLogIn = new HashMap<>();

        this.equipes = new LinkedList<>();

        this.addedChests = new LinkedList<>();
        this.groupe = g;

        try {
            DUREE_PARTIE = (int) groupe.getParametresPartie().getCVARValeur("game_time");
            tempsPartie = DUREE_PARTIE * 60;
            PreGameTimeLeft = (int) groupe.getParametresPartie().getCVARValeur("pre_game_timer");
        } catch (Exception e) {
            Error.Report(e, this);
        }

    }

    public void enableVote() {
        this.groupe.initVoteMap();
    }


    public LinkedList<House> getHouses() {
        return equipes;
    }

    public void setGroupe(Groupe g) {
        this.groupe = g;
    }

    public int getDisconnectedPlayersCount() { return disconnectedPlayers.size();}

    public boolean isTheBlockAChest(Block b) {
        return (b.getState() instanceof Chest);
    }

    public void addAChest(Block block) {
        if(isTheBlockAChest(block)) {
            if (!this.addedChests.contains(block)) {
                this.addedChests.add(block);
                GameLogger.addLog(new Log("ChestSaverAdd", "A chest got " + "added", "block_event"));
            }
        }
    }

    /**
     * Retourne une maison à partir d'un nom, ou d'une couleur
     *
     * @param name
     * @return
     */
    public House getHouseFromName(String name) {
        for (House maison : equipes)
            if (maison.getTeam().getNomEquipe().equalsIgnoreCase(name) ||
                    ChatColorString.toString(maison.getTeam().getCouleur()).equalsIgnoreCase(name))
                return maison;
        return null;
    }


    public void addEquipe(House t) {
        if (!this.equipes.contains(t)) this.equipes.add(t);
    }

    public void removeEquipe(House h) {
        if (this.equipes.contains(h)) this.equipes.add(h);
    }


    public boolean isThisBlockAGameChest(Block b) {
        if(!isTheBlockAChest(b)) return false;

        return addedChests.contains(b);
    }

    public void remove(Block block) {
        if(!this.addedChests.contains(block)) this.addedChests.remove(block);
    }

    public boolean areAllPlayerLoggedIn() {

        try {
            return ((groupe.getPlayers().size() - getRefereeCount()) >= ((int) groupe.getParametresPartie().getCVARValeur("mp_team_max_player")));
        } catch (Exception e) {
            e.printStackTrace();
            Error.Report(e, this);
        }
        return false;
    }

    public void teleportToLobby(Player player) {
        Location spawnLocation = mineralcontest.plugin.pluginWorld.getSpawnLocation();

        Vector playerVelocity = player.getVelocity();

        player.setFallDistance(0);
        playerVelocity.setY(0.05);

        player.setVelocity(playerVelocity);
        PlayerUtils.teleportPlayer(player, spawnLocation);


    }

    public boolean isThereAnAdminLoggedIn() {
        for (Player player : groupe.getPlayers())
            if(player.isOp()) return true;
        return false;
    }

    public void addPlayerTriedToLogin(String playerDisplayName) {
        if( ! havePlayerTriedToLogin(playerDisplayName)) this.PlayerThatTriedToLogIn.put(playerDisplayName, false);
    }

    public boolean havePlayerTriedToLogin(String playerDisplayName) {
        for(Map.Entry<String, Boolean> entry : PlayerThatTriedToLogIn.entrySet()){
            if(entry.getKey().toLowerCase().equals(playerDisplayName.toLowerCase())) return true;
        }
        return false;
    }

    public boolean allowPlayerLogin(String playerDisplayName) {
        if(havePlayerTriedToLogin(playerDisplayName)) {
            for(Map.Entry<String, Boolean> entry : PlayerThatTriedToLogIn.entrySet())
                if(entry.getKey().toLowerCase().equals(playerDisplayName.toLowerCase())) entry.setValue(true);
            return true;
        }
        return false;
    }

    public void removePlayerLoginAttempt(String playerDisplayName) {
        if(havePlayerTriedToLogin(playerDisplayName)) this.PlayerThatTriedToLogIn.remove(playerDisplayName);
    }

    public boolean isPlayerAllowedToLogIn(String playerName) {
        if(havePlayerTriedToLogin(playerName)) return this.PlayerThatTriedToLogIn.get(playerName);
        return false;
    }

    public boolean areAllPlayersReady() {
        return (playersReady.size() == groupe.getPlayers().size());
    }

    public boolean isPlayerReady(Player p) {
        return (playersReady.contains(p));
    }

    public void removePlayerReady(Player p) {
        if(isPlayerReady(p)) {
            playersReady.remove(p);
            mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.player_is_no_longer_ready.toString(), p), groupe);

        }
    }

    public void setPlayerReady(Player p) throws Exception {
        if(!isPlayerReady(p)) {
            playersReady.add(p);
            mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.player_is_now_ready.toString(), p), groupe);
            if(areAllPlayersReady()) {
                if(!allPlayerHaveTeam()) {

                    if ((int) groupe.getParametresPartie().getCVARValeur("mp_randomize_team") == 1) {
                        randomizeTeam(true);
                        demarrerPartie(false);
                        return;
                    } else {
                        warnPlayerWithNoTeam();
                        startAllPlayerHaveTeamTimer();
                        return;
                    }

                }

                if(isGamePaused()) resumeGame();
                else demarrerPartie(false);
            }
        }
    }


    private void startAllPlayerHaveTeamTimer() {
        Game instance = this;
        new BukkitRunnable(){

            @Override
            public void run() {
                if(allPlayerHaveTeam()) {
                    try {
                        demarrerPartie(false);
                        this.cancel();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Error.Report(e, instance);
                    }
                } else {
                    warnPlayerWithNoTeam();
                }
            }
        }.runTaskTimer(mineralcontest.plugin, 0, 20*5);

    }

    public boolean isGameStarted() { return this.GameStarted; }
    public boolean isGamePaused() { return this.GamePaused; }
    public boolean isPreGame() { return this.PreGame; }
    public boolean isGameEnded() { return this.GameEnded; }
    public boolean isGameForced() { return this.GameForced;}


    public Arene getArene() { return this.arene; }

    public void clear() {
        isGameInitialized = false;
        this.arene.clear();
        this.referees.clear();
        this.disconnectedPlayers.clear();
        this.playersReady.clear();

        BlockManager instance = BlockManager.getInstance();
        for (Block block : instance.getPlacedBlocks())
            block.setType(Material.AIR);

        if (mineralcontest.plugin.pluginWorld != null && !mineralcontest.debug) {
            for (Player player : groupe.getPlayers()) {
                teleportToLobby(player);
                PlayerUtils.clearPlayer(player);
            }
        }

        this.equipes.clear();
    }

    public void addBlock(Block b, BlockSaver.Type type) {
        //Bukkit.getLogger().info("A new block has been saved");
        this.affectedBlocks.add(new BlockSaver(b, type));
        GameLogger.addLog(new Log("BlockSaverAdd", "A block got " + type + " (Type: " + b.getType().toString() + " - Loc: " + b.getLocation().toVector().toString() + ")", "block_event"));

    }

    public void addReferee(Player player) {
        if(!isReferee(player)) {
            player.sendMessage(mineralcontest.prefixPrive + Lang.now_referee.toString());
            this.referees.add(player);
            PlayerUtils.equipReferee(player);

            if(!isGameStarted()) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(mineralcontest.plugin, () -> {
                    mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.hud_awaiting_players.toString(), groupe);
                }, 20);
            }
        }
    }

    public void removeReferee(Player player) throws Exception {
        if(isReferee(player)) {
            player.sendMessage(mineralcontest.prefixPrive + Lang.no_longer_referee.toString());
            this.referees.remove(player);
            PlayerUtils.clearPlayer(player);
            PlayerBaseItem.givePlayerItems(player, PlayerBaseItem.onFirstSpawnName);
            if(!isGameStarted()) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(mineralcontest.plugin, () -> {
                    mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.hud_awaiting_players.toString(), groupe);
                }, 20);
            }
        }

    }

    public int getRefereeCount() { return this.referees.size();}

    public LinkedList<Player> getReferees() { return this.referees;}

    public boolean isReferee(Player p) {
        return this.referees.contains(p);
    }

    public void resetMap() {
        if(isGameInitialized) {
            for(BlockSaver block : affectedBlocks) {
                block.applyMethod();
            }
            removeAllDroppedItems();

            mineralcontest.broadcastMessage(Lang.map_has_been_restored.toString(), groupe);
        }

        mineralcontest.plugin.setDefaultWorldBorder();
        clear();
    }

    public void cancelPreGame() {
        if(!isPreGame()) return;
        this.PreGame = false;
        try {
            this.PreGameTimeLeft = (int) groupe.getParametresPartie().getCVARValeur("pre_game_timer");
        } catch (Exception e) {
            e.printStackTrace();
            Error.Report(e, this);
        }
        mineralcontest.broadcastMessage("Pregame cancelled", groupe);
    }


    /*
    Credit: https://bukkit.org/threads/remove-dropped-items-on-ground.100750/
     */
    private void removeAllDroppedItems() {
        World world = null;//get the world
        try {
            world = Bukkit.getServer().getWorld((String) groupe.getParametresPartie().getCVARValeur("world_name"));
        } catch (Exception e) {
            e.printStackTrace();
            Error.Report(e, this);
        }

        List<Entity> entList = world.getEntities();//get all entities in the world

        for(Entity current : entList) {//loop through the list
            if (current instanceof Item) {//make sure we aren't deleting mobs/players
                current.remove();//remove it
            }
        }
    }

    public void addDisconnectedPlayer(String joueur, Equipe team) {
        // Si le joueur est déjà marqué comme déconnecté, on le supprime et on le réajoute
        disconnectedPlayers.add(new CouplePlayerTeam(joueur, team));
    }


    public Equipe getDisconnectedPlayerTeam(String joueur) {
        for(CouplePlayerTeam player : disconnectedPlayers)
            if(player.getJoueur().equals(joueur))
                return player.getTeam();
        return null;
    }

    public CouplePlayerTeam getDisconnectedPlayerInfo(String joueur) {
        for(CouplePlayerTeam player : disconnectedPlayers)
            if(player.getJoueur().equals(joueur))
                return player;

        // Le joueur n'est pas dans la liste
        return null;
    }


    public boolean havePlayerDisconnected(String joueur) {
        for(CouplePlayerTeam player : disconnectedPlayers)
            if(player.getJoueur().equals(joueur))
                return true;

        return false;
    }

    public void removePlayerFromDisconnected(Player joueur) {
        for(CouplePlayerTeam player : disconnectedPlayers)
            if(player.getJoueur().equals(joueur))
                disconnectedPlayers.remove(player);
    }

    public AutomaticDoors getPortes() { return portes; }

    public void handleDoors() {

        int rayonPorte = 2;
        int nomrbeTicks = 5;

        new BukkitRunnable() {
            public void run() {

                if(isGameStarted() && !isPreGame() && !isGamePaused()) {


                    for (House maisons : equipes) {
                        Equipe team = maisons.getTeam();

                    }

                    for (Player online : groupe.getPlayers()) {
                        for (House maison : equipes) {
                            Equipe equipe = maison.getTeam();
                            if (isReferee(online) || equipe.isPlayerInTeam(online)) {
                                if (Radius.isBlockInRadius(maison.getPorte().getMiddleBlockLocation(), online.getLocation(), rayonPorte))
                                    maison.getPorte().playerIsNearDoor(online);
                                else
                                    maison.getPorte().playerIsNotNearDoor(online);
                            }
                        }
                    }

                    /*for(Player online : redTeam) {
                        Location blockCentralPorte = redHouse.getPorte().getMiddleBlockLocation();
                        if(Radius.isBlockInRadius(blockCentralPorte, online.getLocation(), rayonPorte)) {
                            // Si le joueur est proche de la porte
                            redHouse.getPorte().playerIsNearDoor(online);
                        } else {
                            redHouse.getPorte().playerIsNotNearDoor(online);
                        }
                    }

                    for(Player online : yellowTeam) {
                        Location blockCentralPorte = yellowHouse.getPorte().getMiddleBlockLocation();
                        if(Radius.isBlockInRadius(blockCentralPorte, online.getLocation(), rayonPorte)) {
                            // Si le joueur est proche de la porte
                            yellowHouse.getPorte().playerIsNearDoor(online);
                        } else {
                            yellowHouse.getPorte().playerIsNotNearDoor(online);
                        }
                    }

                    for(Player online : blueTeam) {
                        Location blockCentralPorte = blueHouse.getPorte().getMiddleBlockLocation();
                        if(Radius.isBlockInRadius(blockCentralPorte, online.getLocation(), rayonPorte)) {
                            // Si le joueur est proche de la porte
                            blueHouse.getPorte().playerIsNearDoor(online);
                        } else {
                            blueHouse.getPorte().playerIsNotNearDoor(online);
                        }
                    }*/
                }





            }

        }.runTaskTimer(mineralcontest.plugin, 0, nomrbeTicks);

    }


    public void init() {

        Game instance = this;
        new BukkitRunnable() {
            public void run() {

                if(isPreGame() && !isGamePaused()) {
                    Bukkit.getLogger().info("L481");

                    // ON DEMARRE LA PARTIE !
                    if(PreGameTimeLeft <= 0) {
                        PreGame = false;

                        Bukkit.getLogger().info("DEMARRAGE PARTIE");

                        if(tempsPartie == DUREE_PARTIE * 60) {
                            // METRIC
                            // On envoie les informations de la partie
                            SendInformation.sendGameData(SendInformation.start, instance);
                        }

                        Bukkit.getLogger().info("GameData SENT");


                        for (Player online : groupe.getMonde().getPlayers()) {

                            if(isGamePaused()) {
                                // La partie était en cours, elle reprend
                                online.sendTitle(Lang.game_resumed.toString(), "", 0, 20*5, 0);
                                online.playNote(online.getLocation(), Instrument.PIANO, new Note(24));
                                GamePaused = false;
                            }else {
                                // Début de partie
                                if(tempsPartie == DUREE_PARTIE * 60) {
                                    GameStarted = true;
                                    online.setHealth(20);
                                    online.setGameMode(GameMode.SURVIVAL);
                                    online.getInventory().clear();
                                    //PlayerUtils.givePlayerBaseItems(online);
                                    try {
                                        PlayerBaseItem.givePlayerItems(online, PlayerBaseItem.onFirstSpawnName);
                                    } catch (Exception e) {
                                        mineralcontest.broadcastMessage(mineralcontest.prefixErreur + "An error occured, please check server console", groupe);
                                        e.printStackTrace();
                                        Error.Report(e, instance);
                                    }

                                    online.sendTitle(ChatColor.GOLD + Lang.game_successfully_started.toString(), "", 0, 20*5, 0);
                                    PlayerMove.handlePushs();

                                    // On TP le joueur dans sa maison
                                    online.sendMessage("teleportation maison");
                                    try {
                                        if (!isReferee(online))
                                            PlayerUtils.teleportPlayer(online, groupe.getMonde(), getPlayerHouse(online).getHouseLocation());
                                        else {
                                            PlayerUtils.teleportPlayer(online, groupe.getMonde(), getArene().getCoffre().getPosition());
                                            online.setGameMode(GameMode.CREATIVE);
                                            PlayerUtils.equipReferee(online);
                                        }
                                        MobKiller.killMobNearArena(80, instance);

                                    }catch (Exception e) {
                                        e.printStackTrace();
                                        Error.Report(e, instance);
                                    }

                                } else {
                                    // La partie reprend
                                    online.sendTitle(Lang.game_resumed.toString(), "", 0, 20*5, 0);
                                    online.playNote(online.getLocation(), Instrument.PIANO, new Note(24));
                                }
                            }
                        }

                    } else {
                        for (Player online : groupe.getMonde().getPlayers()) {
                            online.sendTitle(Lang.translate(Lang.hud_game_starting.toString()), "", 0, 20*2, 0);
                            if(tempsPartie == DUREE_PARTIE * 60) online.getInventory().clear();
                        }
                    }
                    for (Player online : groupe.getMonde().getPlayers())
                        if(PreGameTimeLeft > 0) online.playNote(online.getLocation(), Instrument.PIANO, new Note(1));
                        else online.playNote(online.getLocation(), Instrument.PIANO, new Note(24));

                    PreGameTimeLeft--;

                }

                // FIN PREGAME


                if(isGameStarted() && !isPreGame()) {
                    if(isGamePaused()) {
                        // La game est en pause
                        for (Player online : groupe.getMonde().getPlayers())
                            if(!online.isOp()) online.sendTitle(Lang.hud_player_paused.toString(), Lang.hud_player_resume_soon.toString(), 0, 20*10, 0);
                            else online.sendTitle(Lang.hud_player_paused.toString(), Lang.hud_admin_resume_help.toString(), 0, 20*10, 0);
                    } else {
                        // La game est en cours
                        // Si le temps atteins 0, alors on arrête la game

                        //


                        try {

                            if(tempsPartie == 0) {
                                terminerPartie();
                            }

                            // On gère la deathzone
                            arene.getDeathZone().reducePlayerTimer();


                        }catch (Exception e) {
                            e.printStackTrace();
                            Error.Report(e, instance);
                        }
                        // Si le temps n'est pas à zéro, on continue
                        if(tempsPartie > 0) tempsPartie--;

                    }
                }


            }

        }.runTaskTimer(mineralcontest.plugin, 0, 20);
    }

    /**
     * Récupèe une équipe non pleine
     * @return
     */
    public Equipe getEquipeNonPleine() {
        int mp_team_max_player = 0;

        try {
            mp_team_max_player = (int) groupe.getParametresPartie().getCVARValeur("mp_randomize_team");
        } catch (Exception e) {
            e.printStackTrace();
            Error.Report(e, this);
        }

        for (House maison : equipes)
            if (maison.getTeam().getJoueurs().size() < mp_team_max_player)
                return maison.getTeam();

        return null;
    }

    /**
     * Affiche dans le chat le score de toutes les équipes
     */
    private void afficherScores() {
        for (House house : equipes)
            mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_score.toString(), house.getTeam()), groupe);
    }


    /**
     * Affiche dans le chat le gagnat de la partie
     * @return Equipe gagnante
     */
    private Equipe afficherGagnant() {
        // Initialisation des équipes
        LinkedList<Equipe> _equipes = new LinkedList<>();
        for (House house : equipes)
            _equipes.add(house.getTeam());

        Equipe gagnante = null;
        int scoreMax = Integer.MIN_VALUE;
        for (Equipe equipe : _equipes)
            if (equipe.getScore() > scoreMax) {
                gagnante = equipe;
                scoreMax = equipe.getScore();
            }
        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_winning.toString(), gagnante), groupe);
        return gagnante;

    }


    /**
     * Retourne l'équipe d'un joueur
     * @param j - Le joueur en question
     * @return Equipe si il en possède une, null sinon
     */
    public Equipe getPlayerTeam(Player j) {
        for (House house : equipes)
            if (house.getTeam().getJoueurs().contains(j)) return house.getTeam();

        return null;
    }

    /**
     * Retourne la maison d'un joueur
     * @param j - Le joueur en question
     * @return House si il possède une maison, null sinon
     */
    public House getPlayerHouse(Player j) {
        for (House house : equipes)
            if (house.getTeam().getJoueurs().contains(j)) return house;

        return null;
    }

    public void terminerPartie() throws Exception {


        SendInformation.sendGameData(SendInformation.ended, this);
        if (groupe.getPlayers().size() == 0) return;
        /* Teleport everyone to the hub */

        for (Player player : groupe.getPlayers()) {
            teleportToLobby(player);
            PlayerUtils.clearPlayer(player);

        }


        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.game_over.toString(), groupe);

        // ON affiche le score des équipes
        this.afficherScores();
        // On affiche l'équipe gagnante
        // Et on récupère le gagnant
        Equipe gagnant = this.afficherGagnant();


        for (Player online : groupe.getPlayers()) {
            if(!isReferee(online))
                if(getPlayerTeam(online).equals(gagnant))
                    PlayerUtils.setFirework(online, gagnant.toColor());
        }

        if (mineralcontest.communityVersion) {
            Bukkit.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.group_finished_their_game_winner_display.toString(), this));
        }


        this.resetMap();
        this.clear();
        this.GamePaused = false;
        this.GameStarted = false;
        this.GameEnded = true;

        this.groupe.setEtat(Etats.EN_ATTENTE);
        this.groupe.setGroupLocked(false);
        this.groupe.enableVote();

    }


    /**
     * Mets la partie en pause
     */
    public void pauseGame() {
        // Only si game started
        if(isGameStarted() || isPreGame()) {
            this.GamePaused = true;

            this.playersReady.clear();

            // On averti les joueurs
            for (Player online : groupe.getPlayers()) {
                online.sendMessage(mineralcontest.prefixPrive + Lang.hud_game_paused.toString());
                online.sendMessage(mineralcontest.prefixPrive + Lang.set_yourself_as_ready_to_start_game.toString());
                if(online.isOp()) online.sendMessage(mineralcontest.prefixAdmin + Lang.hud_admin_resume_help.toString());
            }
        }
    }

    /**
     * Reprendre la partie
     */
    public void resumeGame() {

        if(isPreGame() && isGamePaused()) {
            this.PreGame = true;
            this.GamePaused = false;
            return;
        }

        if(isGamePaused()) {
            Equipe team = null; //getEquipeNonPleine();
            if(team != null && !isGameForced()) {
                mineralcontest.broadcastMessage(mineralcontest.prefixErreur + "Impossible de reprendre la partie, il manque des joueurs dans l'équipe " + team.getCouleur() + team.getNomEquipe(), groupe);
            } else {
                // On refait un "pregame"
                mineralcontest.plugin.getLogger().info("ON RESUME LA PARTIE");
                this.PreGame = true;
                this.PreGameTimeLeft = 5;
                this.GamePaused = false;


            }
        }
    }

    /**
     * Retourne si la partie va reprendre et que la partie était en cours
     * @return true|false
     */
    public boolean isPreGameAndGameStarted() {
        return (isPreGame() && (tempsPartie != DUREE_PARTIE * 60));
    }

    /**
     * Retourne vrai si tous les joueurs ont une équipe, faux sinon
     * @return
     */
    public boolean allPlayerHaveTeam() {
        LinkedList<Player> playersOnline = new LinkedList<>();
        int playerWithTeamCount = 0;
        for (Player player : groupe.getMonde().getPlayers())
            if(!isReferee(player)){
                playersOnline.add(player);
                if(getPlayerTeam(player) != null) {
                    playerWithTeamCount++;
                }
            }

        return (playerWithTeamCount == playersOnline.size());
    }

    /**
     * Envoie un message d'avertissements aux joueurs sans équipes
     */
    private void warnPlayerWithNoTeam() {
        for (Player player : groupe.getMonde().getPlayers())
            if(!isReferee(player)){
                if(getPlayerTeam(player) == null)
                    player.sendMessage(mineralcontest.prefixPrive + Lang.warn_player_you_dont_have_a_team.toString());
            }
    }

    /**
     * Démarre la partie
     * @param forceGameStart - Boolean (force le démarrage de la partie)
     * @return boolean, true si tout se passe bien, faux sinon
     * @throws Exception
     */
    public boolean demarrerPartie(boolean forceGameStart) throws Exception {

        Bukkit.getLogger().info("STARTING, FORCE: " + forceGameStart);

        if(isGameStarted()) {
            throw new Exception(Lang.get("game_already_started"));
        }


        if(forceGameStart){
            GameForced = true;
            tempsPartie = 60 * 60;
            PreGameTimeLeft = (int) groupe.getParametresPartie().getCVARValeur("pre_game_timer");

            if(!allPlayerHaveTeam()) randomizeTeam(forceGameStart);
        }


        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info(mineralcontest.plugin.prefixGlobal + Lang.get("game_starting"));
        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info("=============================");
        // Pour démarrer la partie, il faut:
        // Tous les spawn maison défini
        // Spawn coffre arene définit
        // Spawn arene définit
        // Toutes les equipes soient pleine

        // SPAWN MAISON
        for (House house : equipes) {
            if (house.getHouseLocation() == null) {
                mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + "[Verification] spawn maison equipe " + house.getTeam().getNomEquipe() + ": " + ChatColor.RED + "X", groupe);
                return false;
            }

            if (house.getCoffreEquipeLocation() == null) {
                mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + "[Verification] spawn coffre maison equipe " + house.getTeam().getNomEquipe() + ": " + ChatColor.RED + "X", groupe);
                return false;
            }
        }
        if (mineralcontest.debug)
            mineralcontest.plugin.getServer().getLogger().info(mineralcontest.prefixGlobal + "[Verification] Spawn coffre arene: " + ChatColor.GREEN + "OK");


        // SPAWN COFFRE ARENE
        if(this.arene.getCoffre().getPosition() == null) {
            mineralcontest.broadcastMessage(mineralcontest.plugin.prefixGlobal + "[check] spawn coffre arene: " + ChatColor.RED + "X", groupe);
            return false;
        }
        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info(mineralcontest.plugin.prefixGlobal + "[Verification] Spawn coffre arene: " + ChatColor.GREEN + "OK");


        // SPAWN ARENE
        if(this.arene.getTeleportSpawn() == null) {
            mineralcontest.broadcastMessage(mineralcontest.plugin.prefixGlobal + "[check] spawn arene: " + ChatColor.RED + "X", groupe);
            return false;
        }
        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info(mineralcontest.plugin.prefixGlobal + "[Verification] Spawn arene: " + ChatColor.GREEN + "OK");


        if ((int) groupe.getParametresPartie().getCVARValeur("mp_randomize_team") == 1 && (!forceGameStart && !allPlayerHaveTeam()))
            randomizeTeam(forceGameStart);


        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info(mineralcontest.plugin.prefixGlobal + "GAME_SUCCESSFULLY_STARTED");
        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info("=============================");


        for (Player online : groupe.getPlayers()) {

            if(!isReferee(online)) {
                PlayerUtils.setMaxHealth(online);
                online.setGameMode(GameMode.SURVIVAL);
                online.getInventory().clear();
                //PlayerUtils.givePlayerBaseItems(online);
                //PlayerBaseItem.givePlayerItems(online, PlayerBaseItem.onFirstSpawnName);
            }


        }


        // On spawn les coffres
        for (House house : equipes) {
            house.spawnCoffreEquipe();
        }

        // On clear l'arene
        getArene().clear();

        removeAllDroppedItems();

        PreGame = true;
        GameStarted = false;
        this.tempsPartie = 60 * DUREE_PARTIE;
        getArene().startArena();
        getArene().startAutoMobKill();

        // On set le world border
        mineralcontest.plugin.setWorldBorder();

        // On démarre les portes
        handleDoors();
        removeAllDroppedItems();

        Bukkit.getLogger().info("pregame: " + isPreGame());

        return true;

    }


    /**
     * Permet d'attribuer à chaque joueur une équipe
     * @param force
     * @throws Exception
     */
    public void randomizeTeam(boolean force) throws Exception {

        Bukkit.getLogger().info("RANDOMIZING !");
        LinkedList<House> equipesDispo = new LinkedList<>();
        LinkedList<Player> joueursEnAttente = new LinkedList<>(groupe.getMonde().getPlayers());

        for (int index = 0; index < joueursEnAttente.size(); ++index) {
            equipesDispo.add(equipes.get(index % equipes.size()));
        }

        Random randomisateur = new Random();
        int numeroJoueurRandom = -1;
        int numeroEquipeRandom = -1;

        while (!equipesDispo.isEmpty()) {
            numeroJoueurRandom = randomisateur.nextInt(joueursEnAttente.size());
            numeroEquipeRandom = randomisateur.nextInt(equipesDispo.size());

            Player joueuraAttribuer = joueursEnAttente.get(numeroJoueurRandom);
            House equipeAAttribuer = equipesDispo.get(numeroEquipeRandom);
            equipeAAttribuer.getTeam().addPlayerToTeam(joueuraAttribuer, false);

            equipesDispo.remove(numeroEquipeRandom);
            joueursEnAttente.remove(numeroJoueurRandom);
        }


    }


    public void switchPlayer(Player joueur, String teamName) throws Exception {
        Equipe team = getPlayerTeam(joueur);
        StringBuilder nomEquipes = new StringBuilder();
        for (House house : equipes) {
            if (ChatColorString.toString(house.getTeam().getCouleur()).equalsIgnoreCase(teamName)) {
                if (team != null) team.removePlayer(joueur);
                house.getTeam().addPlayerToTeam(joueur, true);
                return;
            }

            nomEquipes.append(ChatColorString.toString(house.getTeam().getCouleur()) + ",");
        }

        groupe.sendToadmin(mineralcontest.prefixErreur + Lang.error_switch_fail_team_doesnt_exists.toString());
        groupe.sendToadmin(mineralcontest.prefixAdmin + Lang.team_available_list_text.toString());

        String nomEquipesdispo = nomEquipes.toString();
        nomEquipesdispo = nomEquipesdispo.substring(0, nomEquipesdispo.length() - 2);
        groupe.sendToadmin(mineralcontest.prefixAdmin + nomEquipesdispo);


    }

    public String getTempsRestant() {
        int minutes, secondes;
        minutes = (tempsPartie % 3600) / 60;
        secondes = tempsPartie % 60;
        return String.format("%02d:%02d", minutes, secondes);
    }
}
