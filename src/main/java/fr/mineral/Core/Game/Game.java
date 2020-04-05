package fr.mineral.Core.Game;

import fr.mineral.Core.Arena.Arene;
import fr.mineral.Core.House;
import fr.mineral.Core.Votemap;
import fr.mineral.Settings.GameSettingsCvar;
import fr.mineral.Teams.Equipe;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.BlockSaver;
import fr.mineral.Utils.Door.AutomaticDoors;
import fr.mineral.Utils.Metric.SendInformation;
import fr.mineral.Utils.MobKiller;
import fr.mineral.Utils.Player.CouplePlayerTeam;
import fr.mineral.Utils.Player.PlayerBaseItem;
import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.Utils.Radius;
import fr.mineral.Utils.Save.FileToGame;
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
            - Trois équipes
            - Un temps de jeu
            -
     */
    private Arene arene;
    private House redHouse;
    private House yellowHouse;
    private House blueHouse;

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

    public Votemap votemap;

    // Save the blocks
    public LinkedList<BlockSaver> affectedBlocks;

    private LinkedList<Player> referees;

    public Game() {
        this.redHouse = new House("Rouge", ChatColor.RED);
        this.blueHouse = new House("Bleu", ChatColor.BLUE);
        this.yellowHouse = new House("Jaune", ChatColor.YELLOW);

        this.arene = new Arene();
        this.votemap = new Votemap();

        //votemap.enableVote();
        this.disconnectedPlayers = new LinkedList<CouplePlayerTeam>();
        this.affectedBlocks = new LinkedList<>();
        this.referees = new LinkedList<>();
        this.playersReady = new LinkedList<>();
        this.PlayerThatTriedToLogIn = new HashMap<>();

        this.addedChests = new LinkedList<>();

        DUREE_PARTIE = (int) GameSettingsCvar.getValueFromCVARName("game_time");
        tempsPartie = DUREE_PARTIE * 60;
        PreGameTimeLeft = (int) GameSettingsCvar.getValueFromCVARName("pre_game_timer");
    }

    public boolean isTheBlockAChest(Block b) {
        return (b.getState() instanceof Chest);
    }

    public void addAChest(Block block) {
        if(isTheBlockAChest(block))
            if(!this.addedChests.contains(block)) this.addedChests.add(block);
    }

    public boolean isThisBlockAGameChest(Block b) {
        if(!isTheBlockAChest(b)) return false;
        return this.addedChests.contains(b);
    }

    public void remove(Block block) {
        if(!this.addedChests.contains(block)) this.addedChests.remove(block);
    }

    public boolean areAllPlayerLoggedIn() {
        int number_of_team = 3;
        return ((mineralcontest.plugin.pluginWorld.getPlayers().size() - getRefereeCount()) >= ((int) GameSettingsCvar.getValueFromCVARName("mp_team_max_player") * number_of_team));
    }

    public void teleportToLobby(Player player) {
        Location spawnLocation = mineralcontest.plugin.pluginWorld.getSpawnLocation();

        Vector playerVelocity = player.getVelocity();

        player.setFallDistance(0);
        playerVelocity.setY(0.05);

        player.setVelocity(playerVelocity);
        player.teleport(spawnLocation);

    }

    public boolean isThereAnAdminLoggedIn() {
        for(Player player : mineralcontest.plugin.pluginWorld.getPlayers())
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
        return (playersReady.size() == mineralcontest.plugin.pluginWorld.getPlayers().size());
    }

    public boolean isPlayerReady(Player p) {
        return (playersReady.contains(p));
    }

    public void removePlayerReady(Player p) {
        if(isPlayerReady(p)) {
            playersReady.remove(p);
            mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.player_is_no_longer_ready.toString(), p));

        }
    }

    public void setPlayerReady(Player p) throws Exception {
        if(!isPlayerReady(p)) {
            playersReady.add(p);
            mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.player_is_now_ready.toString(), p));
            if(areAllPlayersReady()) {
                if(!allPlayerHaveTeam()) {

                    if((int) GameSettingsCvar.mp_randomize_team.getValueInt() == 1) {
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
        new BukkitRunnable(){

            @Override
            public void run() {
                if(allPlayerHaveTeam()) {
                    try {
                        demarrerPartie(false);
                        this.cancel();
                    } catch (Exception e) {
                        e.printStackTrace();
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
    public House getRedHouse() { return this.redHouse; }
    public House getYellowHouse() { return this.yellowHouse; }
    public House getBlueHouse() { return this.blueHouse; }

    public void clear() {
        isGameInitialized = false;
        this.redHouse.clearHouse();
        this.blueHouse.clearHouse();
        this.yellowHouse.clearHouse();
        this.arene.clear();
        this.referees.clear();
        this.disconnectedPlayers.clear();
        this.playersReady.clear();
    }

    public void addBlock(Block b, BlockSaver.Type type) {
        //Bukkit.getLogger().info("A new block has been saved");
        this.affectedBlocks.add(new BlockSaver(b, type));
    }

    public void addReferee(Player player) {
        if(!isReferee(player)) {
            player.sendMessage(mineralcontest.prefixPrive + Lang.now_referee.toString());
            this.referees.add(player);
            PlayerUtils.equipReferee(player);

            if(!isGameStarted()) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(mineralcontest.plugin, () -> {
                    mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.hud_awaiting_players.toString());
                    if(mineralcontest.plugin.getGame().areAllPlayerLoggedIn()) mineralcontest.plugin.getGame().votemap.enableVote(false);
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
                    mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.hud_awaiting_players.toString());
                    if(mineralcontest.plugin.getGame().areAllPlayerLoggedIn()) mineralcontest.plugin.getGame().votemap.enableVote(false);
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

            mineralcontest.broadcastMessage(Lang.map_has_been_restored.toString());
        }

        mineralcontest.plugin.setDefaultWorldBorder();
        clear();
    }

    public void cancelPreGame() {
        if(!isPreGame()) return;
        this.PreGame = false;
        this.PreGameTimeLeft = (int) GameSettingsCvar.getValueFromCVARName("pre_game_timer");
        mineralcontest.broadcastMessage("Pregame cancelled");
    }


    /*
    Credit: https://bukkit.org/threads/remove-dropped-items-on-ground.100750/
     */
    private void removeAllDroppedItems() {
        World world = Bukkit.getServer().getWorld((String) GameSettingsCvar.getValueFromCVARName("world_name"));//get the world
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

                    LinkedList<Player> blueTeam = new LinkedList<>(); //LinkedList<Player>) blueHouse.getTeam().getJoueurs().clone();
                    LinkedList<Player> redTeam = new LinkedList<>();//(LinkedList<Player>) redHouse.getTeam().getJoueurs().clone();
                    LinkedList<Player> yellowTeam = new LinkedList<>();//(LinkedList<Player>) yellowHouse.getTeam().getJoueurs().clone();

                    for(Player p : redHouse.getTeam().getJoueurs()) if (!redTeam.contains(p))  redTeam.add(p);
                    for(Player p : blueHouse.getTeam().getJoueurs()) if (!blueTeam.contains(p))  blueTeam.add(p);
                    for(Player p : yellowHouse.getTeam().getJoueurs()) if (!yellowTeam.contains(p))  yellowTeam.add(p);

                    for(Player p : mineralcontest.plugin.getGame().referees) {
                        if (!redTeam.contains(p))  redTeam.add(p);
                        if (!blueTeam.contains(p))  blueTeam.add(p);
                        if (!yellowTeam.contains(p))  yellowTeam.add(p);
                    }

                    for(Player online : redTeam) {
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
                    }
                }





            }

        }.runTaskTimer(mineralcontest.plugin, 0, nomrbeTicks);

    }


    public void init() {

        new BukkitRunnable() {
            public void run() {

                PlayerUtils.drawPlayersHUD();



                if(isPreGame() && !isGamePaused()) {

                    // ON DEMARRE LA PARTIE !
                    if(PreGameTimeLeft <= 0) {
                        PreGame = false;

                        if(tempsPartie == DUREE_PARTIE * 60) {
                            // METRIC
                            // On envoie les informations de la partie
                            SendInformation.sendGameData(SendInformation.start);
                        }

                        for(Player online : mineralcontest.plugin.pluginWorld.getPlayers()) {

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
                                        mineralcontest.broadcastMessage(mineralcontest.prefixErreur + "An error occured, please check server console");
                                        e.printStackTrace();
                                    }

                                    online.sendTitle(ChatColor.GOLD + Lang.game_successfully_started.toString(), "", 0, 20*5, 0);

                                    // On TP le joueur dans sa maison
                                    try {
                                        if(!isReferee(online)) online.teleport(getPlayerHouse(online).getHouseLocation());
                                        else {
                                            online.teleport(getArene().getCoffre().getPosition());
                                            online.setGameMode(GameMode.CREATIVE);
                                            PlayerUtils.equipReferee(online);
                                        }
                                        MobKiller.killMobNearArena(80);

                                    }catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    // La partie reprend
                                    online.sendTitle(Lang.game_resumed.toString(), "", 0, 20*5, 0);
                                    online.playNote(online.getLocation(), Instrument.PIANO, new Note(24));
                                }
                            }
                        }

                    } else {
                        for(Player online : mineralcontest.plugin.pluginWorld.getPlayers()) {
                            online.sendTitle(Lang.translate(Lang.hud_game_starting.toString()), "", 0, 20*2, 0);
                            if(tempsPartie == DUREE_PARTIE * 60) online.getInventory().clear();
                        }
                    }
                    for(Player online : mineralcontest.plugin.pluginWorld.getPlayers())
                        if(PreGameTimeLeft > 0) online.playNote(online.getLocation(), Instrument.PIANO, new Note(1));
                        else online.playNote(online.getLocation(), Instrument.PIANO, new Note(24));

                    PreGameTimeLeft--;

                }

                // FIN PREGAME


                if(isGameStarted() && !isPreGame()) {
                    if(isGamePaused()) {
                        // La game est en pause
                        for(Player online : mineralcontest.plugin.pluginWorld.getPlayers())
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
                        }
                        // Si le temps n'est pas à zéro, on continue
                        if(tempsPartie > 0) tempsPartie--;

                    }
                }





            }

        }.runTaskTimer(mineralcontest.plugin, 0, 20);
    }

    public Equipe getEquipeNonPleine() {
        int mp_team_max_player = (int)GameSettingsCvar.mp_randomize_team.getValue();
        if(yellowHouse.getTeam().getJoueurs().size() < mp_team_max_player)
            return yellowHouse.getTeam();

        if(redHouse.getTeam().getJoueurs().size() < mp_team_max_player)
            return redHouse.getTeam();

        if(blueHouse.getTeam().getJoueurs().size() < mp_team_max_player)
            return blueHouse.getTeam();

        return null;
    }

    private void afficherScores() {
        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_score.toString(), yellowHouse.getTeam()));
        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_score.toString(), redHouse.getTeam()));
        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_score.toString(), blueHouse.getTeam()));
    }

    private Equipe afficherGagnant() {
        Equipe[] equipes = new Equipe[3];
        equipes[0] = blueHouse.getTeam();
        equipes[1] = redHouse.getTeam();
        equipes[2] = yellowHouse.getTeam();

        int[] resultats = new int[3];
        int max = 0;
        int index = 0;
        for(int i = 0; i < 3; i++) {
            resultats[i] = equipes[i].getScore();
            if (resultats[i] >= max) {
                max = resultats[i];
                index = i;
            }
        }

        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_winning.toString(), equipes[index]));
        return equipes[index];
    }

    public Equipe getPlayerTeam(Player j) {
        if(redHouse.getTeam().isPlayerInTeam(j)) return redHouse.getTeam();
        if(blueHouse.getTeam().isPlayerInTeam(j)) return blueHouse.getTeam();
        if(yellowHouse.getTeam().isPlayerInTeam(j)) return yellowHouse.getTeam();

        return null;
    }

    public House getPlayerHouse(Player j) {
        if(redHouse.getTeam().isPlayerInTeam(j)) return redHouse;
        if(blueHouse.getTeam().isPlayerInTeam(j)) return blueHouse;
        if(yellowHouse.getTeam().isPlayerInTeam(j)) return yellowHouse;
        return null;
    }

    public void terminerPartie() throws Exception {



        SendInformation.sendGameData(SendInformation.ended);
        if(mineralcontest.plugin.pluginWorld.getPlayers().size() == 0) return;
        /* Teleport everyone to the hub */

        for(Player player : mineralcontest.plugin.pluginWorld.getPlayers()) {
            teleportToLobby(player);
            PlayerUtils.clearPlayer(player);

        }



        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.game_over.toString());

        // ON affiche le score des équipes
        this.afficherScores();
        // On affiche l'équipe gagnante
        // Et on récupère le gagnant
        Equipe gagnant = this.afficherGagnant();


        for(Player online : mineralcontest.plugin.pluginWorld.getPlayers()) {
            if(!isReferee(online))
                if(getPlayerTeam(online).equals(gagnant))
                    PlayerUtils.setFirework(online, gagnant.toColor());
        }


        this.resetMap();
        this.clear();
        this.GamePaused = false;
        this.GameStarted = false;

    }

    public void pauseGame() {
        // Only si game started
        if(isGameStarted() || isPreGame()) {
            this.GamePaused = true;

            this.playersReady.clear();

            // On averti les joueurs
            for(Player online : mineralcontest.plugin.pluginWorld.getPlayers()) {
                online.sendMessage(mineralcontest.prefixPrive + Lang.hud_game_paused.toString());
                online.sendMessage(mineralcontest.prefixPrive + Lang.set_yourself_as_ready_to_start_game.toString());
                if(online.isOp()) online.sendMessage(mineralcontest.prefixAdmin + Lang.hud_admin_resume_help.toString());
            }
        }
    }

    public void resumeGame() {

        if(isPreGame() && isGamePaused()) {
            this.PreGame = true;
            this.GamePaused = false;
            return;
        }

        if(isGamePaused()) {
            Equipe team = null; //getEquipeNonPleine();
            if(team != null && !isGameForced()) {
                mineralcontest.broadcastMessage(mineralcontest.prefixErreur + "Impossible de reprendre la partie, il manque des joueurs dans l'équipe " + team.getCouleur() + team.getNomEquipe());
            } else {
                // On refait un "pregame"
                mineralcontest.plugin.getLogger().info("ON RESUME LA PARTIE");
                this.PreGame = true;
                this.PreGameTimeLeft = 5;
                this.GamePaused = false;


            }
        }
    }

    public boolean isPreGameAndGameStarted() {
        return (isPreGame() && (tempsPartie != DUREE_PARTIE * 60));
    }

    public boolean allPlayerHaveTeam() {
        LinkedList<Player> playersOnline = new LinkedList<>();
        int playerWithTeamCount = 0;
        for(Player player : mineralcontest.plugin.pluginWorld.getPlayers())
            if(!isReferee(player)){
                playersOnline.add(player);
                if(getPlayerTeam(player) != null) {
                    playerWithTeamCount++;
                }
            }

       return (playerWithTeamCount == playersOnline.size());
    }

    private void warnPlayerWithNoTeam() {
        for(Player player : mineralcontest.plugin.pluginWorld.getPlayers())
            if(!isReferee(player)){
                if(getPlayerTeam(player) == null)
                    player.sendMessage(mineralcontest.prefixPrive + Lang.warn_player_you_dont_have_a_team.toString());
            }
    }

    public boolean demarrerPartie(boolean forceGameStart) throws Exception {


        if(isGameStarted()) {
            throw new Exception(Lang.get("game_already_started"));
        }

        if(!votemap.isVoteEnded() && !forceGameStart) {
            throw new Exception(Lang.vote_required_to_start_game.toString());
        }

        if(votemap.voteEnabled && !forceGameStart) {
            throw new Exception(Lang.vote_is_in_progress_cant_start_game.toString());
        }

        if(forceGameStart){
            GameForced = true;
            tempsPartie = 60*60;
            PreGameTimeLeft = (int) GameSettingsCvar.getValueFromCVARName("pre_game_timer");
            votemap.selectedBiome = Integer.parseInt(votemap.getWinnerBiome());
            votemap.disableVote();

            if(!allPlayerHaveTeam()) randomizeTeam(forceGameStart);
        }

        // Si on force le démarrage, et que le vote n'a pas été fait
        if(forceGameStart && !mineralcontest.plugin.getGame().isGameInitialized) {
            int random;
            if(votemap.selectedBiome == -1)  random = new Random().nextInt(6);
            else random = votemap.selectedBiome;
            new FileToGame().readFile("" + random);
            mineralcontest.log.info("Randomly loaded world #" + random);
        }


        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info(mineralcontest.plugin.prefixGlobal + Lang.get("game_starting"));
        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info("=============================");
        // Pour démarrer la partie, il faut:
        // Tous les spawn maison défini
        // Spawn coffre arene définit
        // Spawn arene définit
        // Toutes les equipes soient pleine

        // SPAWN MAISON
        if(this.blueHouse.getHouseLocation() == null) {
            mineralcontest.broadcastMessage(mineralcontest.plugin.prefixGlobal + "[check] Spawn maison bleu: " + ChatColor.RED + "X");
            return false;
        }

        if(this.redHouse.getHouseLocation() == null) {
            mineralcontest.broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] Spawn maison rouge: " + ChatColor.RED + "X");
            return false;
        }
        if(this.yellowHouse.getHouseLocation() == null) {
            mineralcontest.broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] Spawn maison jaune: " + ChatColor.RED + "X");
            return false;
        }

        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info(mineralcontest.plugin.prefixGlobal + "[Verification] Spawn maison: " + ChatColor.GREEN + "OK");

        // SPAWN COFFRE MAISON
        if(this.yellowHouse.getCoffreEquipeLocation() == null) {
            mineralcontest.broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] spawn coffre maison equipe " + this.yellowHouse.getTeam().getNomEquipe() + ": " + ChatColor.RED + "X");
            return false;
        }

        if(this.redHouse.getCoffreEquipeLocation() == null) {
            mineralcontest.broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] spawn coffre maison equipe " + this.redHouse.getTeam().getNomEquipe() + ": " + ChatColor.RED + "X");
            return false;
        }

        if(this.blueHouse.getCoffreEquipeLocation() == null) {
            mineralcontest.broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] spawn coffre maison equipe " + this.blueHouse.getTeam().getNomEquipe() + ": " + ChatColor.RED + "X");
            return false;
        }
        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info(mineralcontest.plugin.prefixGlobal + "[Verification] Spawn coffre arene: " + ChatColor.GREEN + "OK");

        // SPAWN COFFRE ARENE
        if(this.arene.getCoffre().getPosition() == null) {
            mineralcontest.broadcastMessage(mineralcontest.plugin.prefixGlobal + "[check] spawn coffre arene: " + ChatColor.RED + "X");
            return false;
        }
        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info(mineralcontest.plugin.prefixGlobal + "[Verification] Spawn coffre arene: " + ChatColor.GREEN + "OK");



        // SPAWN ARENE
        if(this.arene.getTeleportSpawn() == null) {
            mineralcontest.broadcastMessage(mineralcontest.plugin.prefixGlobal + "[check] spawn arene: " + ChatColor.RED + "X");
            return false;
        }
        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info(mineralcontest.plugin.prefixGlobal + "[Verification] Spawn arene: " + ChatColor.GREEN + "OK");


        if((int) GameSettingsCvar.mp_randomize_team.getValue() == 1 && (!forceGameStart && !allPlayerHaveTeam())) randomizeTeam(forceGameStart);


        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info(mineralcontest.plugin.prefixGlobal + "GAME_SUCCESSFULLY_STARTED");
        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info("=============================");


        for(Player online : mineralcontest.plugin.pluginWorld.getPlayers())
        {

            if(!isReferee(online)) {
                PlayerUtils.setMaxHealth(online);
                online.setGameMode(GameMode.SURVIVAL);
                online.getInventory().clear();
                //PlayerUtils.givePlayerBaseItems(online);
                //PlayerBaseItem.givePlayerItems(online, PlayerBaseItem.onFirstSpawnName);
            }


        }


        // On spawn les coffres
        mineralcontest.plugin.getGame().getYellowHouse().spawnCoffreEquipe();
        mineralcontest.plugin.getGame().getRedHouse().spawnCoffreEquipe();
        mineralcontest.plugin.getGame().getBlueHouse().spawnCoffreEquipe();

        // On clear l'arene
        mineralcontest.plugin.getGame().getArene().clear();

        removeAllDroppedItems();

        PreGame = true;
        GameStarted = false;
        this.tempsPartie = 60 * DUREE_PARTIE;
        mineralcontest.plugin.getGame().getArene().startArena();
        mineralcontest.plugin.getGame().getArene().startAutoMobKill();

        // On set le world border
        mineralcontest.plugin.setWorldBorder();

        // On démarre les portes
        mineralcontest.plugin.getGame().handleDoors();
        removeAllDroppedItems();

        return true;

    }

    // Créer les equipes aléatoirement
    public void randomizeTeam(boolean force) throws Exception {
        String[] equipes = {"red", "blue", "yellow"};
        LinkedList<String> equipesTMP = new LinkedList<>();
        int indexEquipe = 0;
        int randomTeamIndex = 0;
        Random randomObject = new Random();
        LinkedList<Player> playersOnline = new LinkedList<>();
        for(Player player : mineralcontest.plugin.pluginWorld.getPlayers())
            if(!isReferee(player) && getPlayerTeam(player) == null) playersOnline.add(player);

        while(!playersOnline.isEmpty()){
            if(indexEquipe >= equipes.length) {
                indexEquipe = 0;
            }

            if(equipesTMP.size() == equipes.length) {
             while(!equipesTMP.isEmpty() && !playersOnline.isEmpty()){
                Player playerRandomized = playersOnline.pop();
                randomTeamIndex = randomObject.nextInt(equipesTMP.size());
                switch(equipesTMP.get(randomTeamIndex)) {
                    case "red":
                        this.redHouse.getTeam().addPlayerToTeam(playerRandomized, false);
                        break;
                    case "blue":
                        this.blueHouse.getTeam().addPlayerToTeam(playerRandomized, false);
                        break;
                    case "yellow":
                        this.yellowHouse.getTeam().addPlayerToTeam(playerRandomized, false);
                        break;
                    default:
                        mineralcontest.broadcastMessage("erreur");
                        break;
                }

                equipesTMP.remove(randomTeamIndex);
                playersOnline.remove(playerRandomized);
             }
            }

            equipesTMP.add(equipes[indexEquipe]);
            indexEquipe++;
        }

    }


    public void switchPlayer(Player joueur, String teamName) throws Exception {
        Equipe team = getPlayerTeam(joueur);

        if(team != null)
            team.removePlayer(joueur);
        String[] equipes = {"rouge", "red", "bleu", "blue", "yellow", "jaune", "r", "b", "j", "y"};
        // On fait un foreach pour parcourir le tableau d'équipe
        for(String equipe : equipes) {
            // Le nom de l'équipe passé en commentaire existe
            if (teamName.toLowerCase().equalsIgnoreCase(equipe)) {
                switch (equipe) {
                    // On va vérifier si l'équipe est pleine ou non
                    // Si elle l'est, on retourne FALSE
                    // Sinon, on l'ajoute et on le supprime de son équipe initiale
                    case "red":
                    case "rouge":
                    case "r":
                        this.redHouse.getTeam().addPlayerToTeam(joueur, true);
                        if(mineralcontest.plugin.getGame().isGamePaused()) mineralcontest.plugin.getGame().resumeGame();
                        joueur.teleport(getPlayerHouse(joueur).getHouseLocation());
                        break;

                    case "jaune":
                    case "yellow":
                    case "j":
                    case "y":
                        this.yellowHouse.getTeam().addPlayerToTeam(joueur, true);
                        if(mineralcontest.plugin.getGame().isGamePaused()) mineralcontest.plugin.getGame().resumeGame();
                        joueur.teleport(getPlayerHouse(joueur).getHouseLocation());
                        break;

                    case "blue":
                    case "bleu":
                    case "b":
                        this.blueHouse.getTeam().addPlayerToTeam(joueur, true);
                        if(mineralcontest.plugin.getGame().isGamePaused()) mineralcontest.plugin.getGame().resumeGame();
                        joueur.teleport(getPlayerHouse(joueur).getHouseLocation());
                        break;
                }
            }
        }

    }

    public String getTempsRestant() {
        int minutes, secondes;
        minutes = (tempsPartie % 3600) / 60;
        secondes = tempsPartie % 60;
        return String.format("%02d:%02d", minutes, secondes);
    }
}
