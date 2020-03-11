package fr.mineral.Core;

import fr.mineral.Core.Arena.Arene;
import fr.mineral.Translation.Lang;
import fr.mineral.Teams.Equipe;
import fr.mineral.Utils.BlockSaver;
import fr.mineral.Utils.Door.AutomaticDoors;
import fr.mineral.Utils.MobKiller;
import fr.mineral.Utils.Player.CouplePlayerTeam;
import fr.mineral.Utils.Metric.SendInformation;
import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.Utils.Radius;
import fr.mineral.Utils.Save.FileToGame;
import fr.mineral.mineralcontest;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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

    /*
        CVAR List
        mp_randomize_team = 0 | 1
                            0: false, player doit rejoindre soit même sa team
                            1: true, team random

        mp_iron_score <value>
        mp_gold_score <value>
        mp_diamond_score <value>
        mp_emerald_score <value>
        mp_team_max_players <value>

     */

    // CVAR

    public int mp_randomize_team = 1;
    public int mp_enable_item_drop = 2;

    // Temps de la partie en minute
    private static int DUREE_PARTIE = 60;

    // Temps en minute
    private int tempsPartie = 60 * DUREE_PARTIE;
    public int PreGameTimeLeft = 10;
    public static int SCORE_IRON = 10;
    public static int SCORE_GOLD = 50;
    public static int SCORE_DIAMOND = 150;
    public static int SCORE_EMERALD = 300;

    private boolean GameStarted = false;
    private boolean GamePaused = false;
    private boolean PreGame = false;
    private boolean GameEnded = false;

    public boolean isGameInitialized = false;

    public int killCounter = 0;

    private AutomaticDoors portes;

    private LinkedList<CouplePlayerTeam> disconnectedPlayers;

    public boolean isGameStarted() { return this.GameStarted; }
    public boolean isGamePaused() { return this.GamePaused; }
    public boolean isPreGame() { return this.PreGame; }
    public boolean isGameEnded() { return this.GameEnded; }


    public Arene getArene() { return this.arene; }
    public House getRedHouse() { return this.redHouse; }
    public House getYellowHouse() { return this.yellowHouse; }
    public House getBlueHouse() { return this.blueHouse; }
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

    }

    public void addBlock(Block b, BlockSaver.Type type) {
        Bukkit.getLogger().info("A new block has been saved");
        this.affectedBlocks.add(new BlockSaver(b, type));
    }

    public void addReferee(Player player) {
        if(!isReferee(player)) this.referees.add(player);
    }

    public void removeReferee(Player player) {
        this.referees.remove(player);
    }

    public int getRefereeCount() { return this.referees.size();}

    private LinkedList<Player> getReferees() { return this.referees;}

    public boolean isReferee(Player p) {
        return this.referees.contains(p);
    }

    private void resetMap() {
        for(BlockSaver block : affectedBlocks) {
            block.applyMethod();
        }
        removeAllDroppedItems();

        Bukkit.broadcastMessage("Map has been restored");
    }

    /*
    Credit: https://bukkit.org/threads/remove-dropped-items-on-ground.100750/
     */
    private void removeAllDroppedItems() {
        World world = Bukkit.getServer().getWorld(mineralcontest.world_name);//get the world
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



                if(isPreGame()) {

                    // ON DEMARRE LA PARTIE !
                    if(PreGameTimeLeft <= 0) {
                        PreGame = false;
                        for(Player online : mineralcontest.plugin.getServer().getOnlinePlayers()) {

                            if(isGamePaused()) {
                                // La partie était en cours, elle reprend
                                online.sendTitle(Lang.game_resumed.toString(), "", 0, 20*5, 0);
                                online.playNote(online.getLocation(), Instrument.PIANO, new Note(24));
                                GamePaused = false;
                            }else {
                                // Début de partie
                                if(tempsPartie == DUREE_PARTIE * 60) {

                                    online.setHealth(20);
                                    online.setGameMode(GameMode.SURVIVAL);
                                    online.getInventory().clear();
                                    PlayerUtils.givePlayerBaseItems(online);
                                    online.sendTitle(ChatColor.GOLD + Lang.game_successfully_started.toString(), "", 0, 20*5, 0);


                                    // On TP le joueur dans sa maison
                                    try {
                                        if(!isReferee(online)) online.teleport(getPlayerHouse(online).getHouseLocation());

                                        online.teleport(getPlayerHouse(online).getHouseLocation());

                                        // METRIC
                                        // On envoie les informations de la partie
                                        SendInformation.sendGameData("started");
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
                        for(Player online : mineralcontest.plugin.getServer().getOnlinePlayers()) {
                            online.sendTitle(Lang.translate(Lang.hud_game_starting.toString()), "", 0, 20*2, 0);
                            if(tempsPartie == DUREE_PARTIE * 60) online.getInventory().clear();
                        }
                    }
                    for(Player online : mineralcontest.plugin.getServer().getOnlinePlayers())
                        if(PreGameTimeLeft > 0) online.playNote(online.getLocation(), Instrument.PIANO, new Note(1));
                        else online.playNote(online.getLocation(), Instrument.PIANO, new Note(24));

                    PreGameTimeLeft--;
                }

                // FIN PREGAME


                if(isGameStarted() && !isPreGame()) {
                    if(isGamePaused()) {
                        // La game est en pause
                        for(Player online : mineralcontest.plugin.getServer().getOnlinePlayers())
                            if(!online.isOp()) online.sendTitle(Lang.hud_player_paused.toString(), Lang.hud_player_resume_soon.toString(), 0, 20*10, 0);
                            else online.sendTitle(Lang.hud_player_paused.toString(), Lang.hud_admin_resume_help.toString(), 0, 20*10, 0);
                    } else {
                        // La game est en cours
                        // Si le temps atteins 0, alors on arrête la game

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
        if(yellowHouse.getTeam().getJoueurs().size() < mineralcontest.teamMaxPlayers)
            return yellowHouse.getTeam();

        if(redHouse.getTeam().getJoueurs().size() < mineralcontest.teamMaxPlayers)
            return redHouse.getTeam();

        if(blueHouse.getTeam().getJoueurs().size() < mineralcontest.teamMaxPlayers)
            return blueHouse.getTeam();

        return null;
    }

    private void afficherScores() {
        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_score.toString(), yellowHouse.getTeam()));
        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_score.toString(), redHouse.getTeam()));
        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_score.toString(), blueHouse.getTeam()));
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

        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_winning.toString(), equipes[index]));
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
        this.GamePaused = false;
        this.GameStarted = false;
        this.GameEnded = true;

        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + Lang.game_over.toString());

        // ON affiche le score des équipes
        this.afficherScores();
        // On affiche l'équipe gagnante
        // Et on récupère le gagnant
        Equipe gagnant = this.afficherGagnant();


        for(Player online : Bukkit.getServer().getOnlinePlayers()) {
            if(getPlayerTeam(online).equals(gagnant))
                PlayerUtils.setFirework(online, gagnant.toColor());
        }

        SendInformation.sendGameData("ended");
    }

    public void pauseGame() {
        // Only si game started
        if(isGameStarted()) {
            this.GamePaused = true;
            // On averti les joueurs
            for(Player online : mineralcontest.plugin.getServer().getOnlinePlayers()) {
                online.sendMessage(mineralcontest.prefixPrive + Lang.hud_game_paused.toString());
                if(online.isOp()) online.sendMessage(mineralcontest.prefixAdmin + Lang.hud_admin_resume_help.toString());
            }
        }
    }

    public void resumeGame() {
        if(isGamePaused()) {
            Equipe team = getEquipeNonPleine();
            if(team != null) {
                mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixErreur + "Impossible de reprendre la partie, il manque des joueurs dans l'équipe " + team.getCouleur() + team.getNomEquipe());
            } else {
                // On refait un "pregame"
                mineralcontest.plugin.getLogger().info("ON RESUME LA PARTIE");
                this.PreGame = true;
                this.PreGameTimeLeft = 5;



            }
        }
    }

    public boolean demarrerPartie(boolean force) throws Exception {

        if(isGameEnded()) {
            throw new Exception("Please, " + ChatColor.RED + ChatColor.BOLD + "restart your server to avoid any issue.");
        }

        if(isGameStarted()) {
            throw new Exception(Lang.get("game_already_started"));
        }

        if(force){
            tempsPartie = 60*60;
            PreGameTimeLeft = 10;
        }

        // Si on force le démarrage, et que le vote n'a pas été fait
        if(force && !mineralcontest.plugin.getGame().isGameInitialized) {
            int random = new Random().nextInt(6);
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
            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[check] Spawn maison bleu: " + ChatColor.RED + "X");
            return false;
        }

        if(this.redHouse.getHouseLocation() == null) {
            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] Spawn maison rouge: " + ChatColor.RED + "X");
            return false;
        }
        if(this.yellowHouse.getHouseLocation() == null) {
            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] Spawn maison jaune: " + ChatColor.RED + "X");
            return false;
        }

        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info(mineralcontest.plugin.prefixGlobal + "[Verification] Spawn maison: " + ChatColor.GREEN + "OK");

        // SPAWN COFFRE MAISON
        if(this.yellowHouse.getCoffreEquipeLocation() == null) {
            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] spawn coffre maison equipe " + this.yellowHouse.getTeam().getNomEquipe() + ": " + ChatColor.RED + "X");
            return false;
        }

        if(this.redHouse.getCoffreEquipeLocation() == null) {
            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] spawn coffre maison equipe " + this.redHouse.getTeam().getNomEquipe() + ": " + ChatColor.RED + "X");
            return false;
        }

        if(this.blueHouse.getCoffreEquipeLocation() == null) {
            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] spawn coffre maison equipe " + this.blueHouse.getTeam().getNomEquipe() + ": " + ChatColor.RED + "X");
            return false;
        }
        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info(mineralcontest.plugin.prefixGlobal + "[Verification] Spawn coffre arene: " + ChatColor.GREEN + "OK");

        // SPAWN COFFRE ARENE
        if(this.arene.getCoffre().getPosition() == null) {
            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[check] spawn coffre arene: " + ChatColor.RED + "X");
            return false;
        }
        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info(mineralcontest.plugin.prefixGlobal + "[Verification] Spawn coffre arene: " + ChatColor.GREEN + "OK");



        // SPAWN ARENE
        if(this.arene.getTeleportSpawn() == null) {
            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[check] spawn arene: " + ChatColor.RED + "X");
            return false;
        }
        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info(mineralcontest.plugin.prefixGlobal + "[Verification] Spawn arene: " + ChatColor.GREEN + "OK");


        if(mp_randomize_team == 1) randomizeTeam(force);

        // EQUIPES PLEINE

        if(!force) {
            if(!this.redHouse.getTeam().isTeamFull()) {
                mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] Equipe rouge pleine: " + ChatColor.RED + "X");
                return false;
            }
            if(!this.blueHouse.getTeam().isTeamFull()) {
                mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] Equipe bleu pleine: " + ChatColor.RED + "X");
                return false;
            }
            if(!this.yellowHouse.getTeam().isTeamFull()) {
                mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] Equipe jaune pleine: " + ChatColor.RED + "X");
                return false;
            }
        }

        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info(mineralcontest.plugin.prefixGlobal + "[Verification] Equipes pleines: " + ChatColor.GREEN + "OK");


        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info(mineralcontest.plugin.prefixGlobal + "GAME_SUCCESSFULLY_STARTED");
        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info("=============================");


        for(Player online : mineralcontest.plugin.getServer().getOnlinePlayers())
        {
            online.setHealth(20);
            online.setGameMode(GameMode.SURVIVAL);
            online.getInventory().clear();
            PlayerUtils.givePlayerBaseItems(online);

        }


        // On spawn les coffres
        mineralcontest.plugin.getGame().getYellowHouse().spawnCoffreEquipe();
        mineralcontest.plugin.getGame().getRedHouse().spawnCoffreEquipe();
        mineralcontest.plugin.getGame().getBlueHouse().spawnCoffreEquipe();

        PreGame = true;
        GameStarted = true;
        this.tempsPartie = 60 * DUREE_PARTIE;
        mineralcontest.plugin.getGame().getArene().startArena();
        mineralcontest.plugin.getGame().getArene().startAutoMobKill();




        // On démarre les portes
        mineralcontest.plugin.getGame().handleDoors();

        return true;

    }

    // Créer les equipes aléatoirement
    public void randomizeTeam(boolean force) throws Exception {
        if(isGameStarted()) {
            throw new Exception("gameAlreadyStarted");
        }

        if((mineralcontest.teamMaxPlayers*3 != mineralcontest.plugin.getServer().getOnlinePlayers().size()) && !force)
            throw new Exception("NotEnoughtPlayer");

        ArrayList<String> team = new ArrayList<String>();


        mineralcontest.plugin.getServer().broadcastMessage("================");


        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info(mineralcontest.plugin.prefixGlobal + "randomizeTeamBegin");
        for(int i = 0; i < mineralcontest.teamMaxPlayers; i++){
            team.add("jaune");
            team.add("rouge");
            team.add("bleu");
        }

        for(Player joueur : this.redHouse.getTeam().getJoueurs()) {
            this.redHouse.getTeam().removePlayer(joueur);
        }

        for(Player joueur : this.yellowHouse.getTeam().getJoueurs()) {
            this.yellowHouse.getTeam().removePlayer(joueur);
        }

        for(Player joueur : this.blueHouse.getTeam().getJoueurs()) {
            this.blueHouse.getTeam().removePlayer(joueur);
        }

        int random;
        Random r = new Random();
        String result;
        int indexJoueur = 0;
        Object[] joueurs = Bukkit.getServer().getOnlinePlayers().toArray();

        while(team.size() > 0 && indexJoueur != joueurs.length) {
            random = r.nextInt(team.size());
            result = team.get(random);

            if(result.equals("jaune")) {
                this.yellowHouse.getTeam().addPlayerToTeam((Player) joueurs[indexJoueur]);
                team.remove(random);
            }

            if(result.equals("rouge")) {
                this.redHouse.getTeam().addPlayerToTeam((Player) joueurs[indexJoueur]);
                team.remove(random);
            }

            if(result.equals("bleu")) {
                this.blueHouse.getTeam().addPlayerToTeam((Player) joueurs[indexJoueur]);
                team.remove(random);
            }

            indexJoueur++;
        }

        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info(mineralcontest.plugin.prefixGlobal + "randomizeTeamEnd");
        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info("================");

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
                        this.redHouse.getTeam().addPlayerToTeam(joueur);
                        if(mineralcontest.plugin.getGame().isGamePaused()) mineralcontest.plugin.getGame().resumeGame();
                        break;

                    case "jaune":
                    case "yellow":
                    case "j":
                    case "y":
                        this.yellowHouse.getTeam().addPlayerToTeam(joueur);
                        if(mineralcontest.plugin.getGame().isGamePaused()) mineralcontest.plugin.getGame().resumeGame();
                        break;

                    case "blue":
                    case "bleu":
                    case "b":
                        this.blueHouse.getTeam().addPlayerToTeam(joueur);
                        if(mineralcontest.plugin.getGame().isGamePaused()) mineralcontest.plugin.getGame().resumeGame();
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
