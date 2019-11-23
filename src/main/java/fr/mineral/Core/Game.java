package fr.mineral.Core;

import fr.mineral.Core.Arena.Arene;
import fr.mineral.Translation.Lang;
import fr.mineral.Teams.Equipe;
import fr.mineral.Utils.Door.AutomaticDoors;
import fr.mineral.Utils.Player.CouplePlayerTeam;
import fr.mineral.Utils.Metric.SendInformation;
import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.Utils.Radius;
import fr.mineral.mineralcontest;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.LinkedList;
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
    private Equipe teamRouge;
    private Equipe teamJaune;
    private Equipe teamBleu;

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

    public boolean isGameInitialized = false;

    public int killCounter = 0;

    private AutomaticDoors portes;

    private LinkedList<CouplePlayerTeam> disconnectedPlayers;

    public boolean isGameStarted() { return this.GameStarted; }
    public boolean isGamePaused() { return this.GamePaused; }
    public boolean isPreGame() { return this.PreGame; }


    public Arene getArene() { return this.arene; }
    public Equipe getTeamRouge() { return this.teamRouge; }
    public Equipe getTeamJaune() { return this.teamJaune; }
    public Equipe getTeamBleu() { return this.teamBleu; }
    public Votemap votemap;



    public Game() {
        this.teamRouge = new Equipe("Rouge", ChatColor.RED);
        this.teamBleu = new Equipe("Bleu", ChatColor.BLUE);
        this.teamJaune = new Equipe("Jaune", ChatColor.YELLOW);

        this.arene = new Arene();
        this.portes = new AutomaticDoors(this.teamBleu);
        this.votemap = new Votemap();

        //votemap.enableVote();

        this.disconnectedPlayers = new LinkedList<CouplePlayerTeam>();

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
                PlayerUtils.drawPlayersHUD(isGameStarted(), isGamePaused(), isPreGame(), votemap.voteEnabled);
                if(isGameStarted() && !isPreGame() && !isGamePaused()) {
                    for(Player online : teamRouge.getJoueurs()) {
                        Location blockCentralPorte = teamRouge.getPorte().getMiddleBlockLocation();
                        if(Radius.isBlockInRadius(blockCentralPorte, online.getLocation(), rayonPorte)) {
                            // Si le joueur est proche de la porte
                            teamRouge.getPorte().playerIsNearDoor(online);
                        } else {
                            teamRouge.getPorte().playerIsNotNearDoor(online);
                        }
                    }

                    for(Player online : teamJaune.getJoueurs()) {
                        Location blockCentralPorte = teamJaune.getPorte().getMiddleBlockLocation();
                        if(Radius.isBlockInRadius(blockCentralPorte, online.getLocation(), rayonPorte)) {
                            // Si le joueur est proche de la porte
                            teamJaune.getPorte().playerIsNearDoor(online);
                        } else {
                            teamJaune.getPorte().playerIsNotNearDoor(online);
                        }
                    }

                    for(Player online : teamBleu.getJoueurs()) {
                        Location blockCentralPorte = teamBleu.getPorte().getMiddleBlockLocation();
                        if(Radius.isBlockInRadius(blockCentralPorte, online.getLocation(), rayonPorte)) {
                            // Si le joueur est proche de la porte
                            teamBleu.getPorte().playerIsNearDoor(online);
                        } else {
                            teamBleu.getPorte().playerIsNotNearDoor(online);
                        }
                    }
                }





            }

        }.runTaskTimer(mineralcontest.plugin, 0, nomrbeTicks);

    }

    public void init() {

        new BukkitRunnable() {
            public void run() {
                PlayerUtils.drawPlayersHUD(isGameStarted(), isGamePaused(), isPreGame(), votemap.voteEnabled);



                if(isPreGame()) {

                    // ON DEMARRE LA PARTIE !
                    if(PreGameTimeLeft <= 0) {
                        PreGame = false;
                        for(Player online : mineralcontest.plugin.getServer().getOnlinePlayers()) {

                            if(isGamePaused()) {
                                // La partie était en cours, elle reprend
                                online.sendTitle("La partie a " + ChatColor.BLUE  + "repris !", "", 0, 20*5, 0);
                                online.playNote(online.getLocation(), Instrument.PIANO, new Note(24));
                                GamePaused = false;
                            }else {
                                // Début de partie
                                if(tempsPartie == DUREE_PARTIE * 60) {

                                    online.setHealth(20);
                                    online.setGameMode(GameMode.SURVIVAL);
                                    online.getInventory().clear();
                                    PlayerUtils.givePlayerBaseItems(online);
                                    online.sendTitle(ChatColor.GOLD + "Go go go !", "", 0, 20*5, 0);


                                    // On TP le joueur dans sa maison
                                    try {
                                        online.teleport(getPlayerTeam(online).getHouseLocation());

                                        // METRIC
                                        // On envoie les informations de la partie
                                        SendInformation.sendGameData("started");


                                    }catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    // La partie reprend
                                    online.sendTitle("La partie a " + ChatColor.BLUE  + "repris !", "", 0, 20*5, 0);
                                    online.playNote(online.getLocation(), Instrument.PIANO, new Note(24));
                                }
                            }
                        }

                    } else {
                        for(Player online : mineralcontest.plugin.getServer().getOnlinePlayers()) {
                            online.sendTitle("Démarrage dans " + ChatColor.GOLD + PreGameTimeLeft, "Attention ça va commencer !!", 0, 20*2, 0);
                            if(tempsPartie == DUREE_PARTIE * 60) online.getInventory().clear();
                        }
                    }
                    PreGameTimeLeft--;
                    for(Player online : mineralcontest.plugin.getServer().getOnlinePlayers())
                        if(PreGameTimeLeft > 0) online.playNote(online.getLocation(), Instrument.PIANO, new Note(1));

                }

                // FIN PREGAME


                if(isGameStarted() && !isPreGame()) {
                    if(isGamePaused()) {
                        // La game est en pause
                        for(Player online : mineralcontest.plugin.getServer().getOnlinePlayers())
                            if(!online.isOp()) online.sendTitle(ChatColor.GOLD + "PAUSE", "La partie reprendra bientôt", 0, 20*10, 0);
                            else online.sendTitle(ChatColor.GOLD + "PAUSE", "Pour reprendre la partie, faites /resume", 0, 20*10, 0);
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
        if(teamJaune.getJoueurs().size() < mineralcontest.teamMaxPlayers)
            return teamJaune;

        if(teamRouge.getJoueurs().size() < mineralcontest.teamMaxPlayers)
            return teamRouge;

        if(teamBleu.getJoueurs().size() < mineralcontest.teamMaxPlayers)
            return teamBleu;

        return null;
    }

    private void afficherScores() {
        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + "Score de l'équipe " + teamJaune.getCouleur() + teamJaune.getNomEquipe() + ChatColor.WHITE + ": " + teamJaune.getScore() + " points.");
        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + "Score de l'équipe " + teamRouge.getCouleur() + teamRouge.getNomEquipe() + ChatColor.WHITE + ": " + teamRouge.getScore() + " points.");
        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + "Score de l'équipe " + teamBleu.getCouleur() + teamBleu.getNomEquipe() + ChatColor.WHITE + ": " + teamBleu.getScore() + " points.");
    }

    private Equipe afficherGagnant() {
        Equipe[] equipes = new Equipe[3];
        equipes[0] = teamBleu;
        equipes[1] = teamRouge;
        equipes[2] = teamJaune;

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

        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + "L'équipe " + equipes[index].getCouleur() + equipes[index].getNomEquipe() + ChatColor.WHITE + " remporte la partie avec " + equipes[index].getScore());
        return equipes[index];
    }

    public Equipe getPlayerTeam(Player j) {
        if(teamRouge.isPlayerInTeam(j)) return teamRouge;
        if(teamBleu.isPlayerInTeam(j)) return teamBleu;
        if(teamJaune.isPlayerInTeam(j)) return teamJaune;

        return null;
    }

    public void terminerPartie() throws Exception {
        this.GamePaused = false;
        this.GameStarted = false;

        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + "La partie est terminée !");

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
                online.sendMessage(mineralcontest.prefixPrive + "La partie a été mise en pause !");
                if(online.isOp()) online.sendMessage(mineralcontest.prefixAdmin + "Pour reprendre la partie, il faut faire /resume");
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

    public boolean demarrerPartie() throws Exception {

        if(isGameStarted()) {
            throw new Exception(Lang.get("game_already_started"));
        }

        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info(mineralcontest.plugin.prefixGlobal + Lang.get("game_starting"));
        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info("=============================");
        // Pour démarrer la partie, il faut:
        // Tous les spawn maison défini
        // Spawn coffre arene définit
        // Spawn arene définit
        // Toutes les equipes soient pleine

        // SPAWN MAISON
        if(this.teamBleu.getHouseLocation() == null) {
            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[check] Spawn maison bleu: " + ChatColor.RED + "X");
            return false;
        }

        if(this.teamRouge.getHouseLocation() == null) {
            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] Spawn maison rouge: " + ChatColor.RED + "X");
            return false;
        }
        if(this.teamJaune.getHouseLocation() == null) {
            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] Spawn maison jaune: " + ChatColor.RED + "X");
            return false;
        }

        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info(mineralcontest.plugin.prefixGlobal + "[Verification] Spawn maison: " + ChatColor.GREEN + "OK");

        // SPAWN COFFRE MAISON
        if(this.teamJaune.getCoffreEquipeLocation() == null) {
            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] spawn coffre maison equipe " + this.teamJaune.getNomEquipe() + ": " + ChatColor.RED + "X");
            return false;
        }

        if(this.teamRouge.getCoffreEquipeLocation() == null) {
            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] spawn coffre maison equipe " + this.teamRouge.getNomEquipe() + ": " + ChatColor.RED + "X");
            return false;
        }

        if(this.teamBleu.getCoffreEquipeLocation() == null) {
            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] spawn coffre maison equipe " + this.teamBleu.getNomEquipe() + ": " + ChatColor.RED + "X");
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


        randomizeTeam();

        // EQUIPES PLEINE
        if(!this.teamRouge.isTeamFull()) {
            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] Equipe rouge pleine: " + ChatColor.RED + "X");
            return false;
        }
        if(!this.teamBleu.isTeamFull()) {
            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] Equipe bleu pleine: " + ChatColor.RED + "X");
            return false;
        }
        if(!this.teamJaune.isTeamFull()) {
            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] Equipe jaune pleine: " + ChatColor.RED + "X");
            return false;
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
        mineralcontest.plugin.getGame().getTeamJaune().spawnCoffreEquipe();
        mineralcontest.plugin.getGame().getTeamRouge().spawnCoffreEquipe();
        mineralcontest.plugin.getGame().getTeamBleu().spawnCoffreEquipe();

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
    public void randomizeTeam() throws Exception {
        if(isGameStarted()) {
            throw new Exception("gameAlreadyStarted");
        }

        if(mineralcontest.teamMaxPlayers*3 != mineralcontest.plugin.getServer().getOnlinePlayers().size())
            throw new Exception("NotEnoughtPlayer");

        ArrayList<String> team = new ArrayList<String>();


        mineralcontest.plugin.getServer().broadcastMessage("================");


        if(mineralcontest.debug) mineralcontest.plugin.getServer().getLogger().info(mineralcontest.plugin.prefixGlobal + "randomizeTeamBegin");
        for(int i = 0; i < mineralcontest.teamMaxPlayers; i++){
            team.add("jaune");
            team.add("rouge");
            team.add("bleu");
        }

        for(Player joueur : this.teamRouge.getJoueurs()) {
            this.teamRouge.removePlayer(joueur);
        }

        for(Player joueur : this.teamJaune.getJoueurs()) {
            this.teamJaune.removePlayer(joueur);
        }

        for(Player joueur : this.teamBleu.getJoueurs()) {
            this.teamBleu.removePlayer(joueur);
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
                this.teamJaune.addPlayerToTeam((Player) joueurs[indexJoueur]);
                team.remove(random);
            }

            if(result.equals("rouge")) {
                this.teamRouge.addPlayerToTeam((Player) joueurs[indexJoueur]);
                team.remove(random);
            }

            if(result.equals("bleu")) {
                this.teamBleu.addPlayerToTeam((Player) joueurs[indexJoueur]);
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
                        this.teamRouge.addPlayerToTeam(joueur);
                        if(mineralcontest.plugin.getGame().isGamePaused()) mineralcontest.plugin.getGame().resumeGame();
                        break;

                    case "jaune":
                    case "yellow":
                    case "j":
                    case "y":
                        this.teamJaune.addPlayerToTeam(joueur);
                        if(mineralcontest.plugin.getGame().isGamePaused()) mineralcontest.plugin.getGame().resumeGame();
                        break;

                    case "blue":
                    case "bleu":
                    case "b":
                        this.teamBleu.addPlayerToTeam(joueur);
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
