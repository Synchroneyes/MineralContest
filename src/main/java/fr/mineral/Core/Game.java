package fr.mineral.Core;

import fr.mineral.Teams.Equipe;
import fr.mineral.Utils.AutomaticDoors;
import fr.mineral.Utils.FreezeLibrary;
import fr.mineral.Utils.PlayerUtils;
import fr.mineral.Utils.Radius;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
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
    private Arena arene;
    private Equipe teamRouge;
    private Equipe teamJaune;
    private Equipe teamBleu;

    // Temps de la partie en minute
    private static int DUREE_PARTIE = 60;

    // Temps en minute
    private int tempsPartie = 60 * DUREE_PARTIE;
    public static int SCORE_IRON = 10;
    public static int SCORE_GOLD = 50;
    public static int SCORE_DIAMOND = 150;
    public static int SCORE_EMERALD = 300;

    private boolean GameStarted = false;
    private boolean GamePaused = false;

    private AutomaticDoors portes;

    public boolean isGameStarted() { return this.GameStarted; }
    public boolean isGamePaused() { return this.GamePaused; }

    public Arena getArene() { return this.arene; }
    public Equipe getTeamRouge() { return this.teamRouge; }
    public Equipe getTeamJaune() { return this.teamJaune; }
    public Equipe getTeamBleu() { return this.teamBleu; }

    public Game() {
        this.teamRouge = new Equipe("Rouge", ChatColor.RED);
        this.teamBleu = new Equipe("Bleu", ChatColor.BLUE);
        this.teamJaune = new Equipe("Jaune", ChatColor.YELLOW);

        this.arene = new Arena();
        this.portes = new AutomaticDoors(this.teamBleu);

        // On démarre le timer de la game

    }

    public AutomaticDoors getPortes() { return portes; }

    public void init() {
        new BukkitRunnable() {
            public void run() {

                //PlayerUtils.drawPlayersHUD(isGameStarted(), isGamePaused());

                if(isGameStarted()) {
                    if(isGamePaused()) {
                        // La game est en pause

                    } else {
                        // La game est en cours
                        // Si le temps atteins 0, alors on arrête la game
                        if(tempsPartie == 0) {
                            terminerPartie();
                        }

                        // On gère la deathzone
                        try {
                            arene.getDeathZone().reducePlayerTimer();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        // Si le temps n'est pas à zéro, on continue
                        if(tempsPartie > 0) tempsPartie--;
                    }
                }


                if(portes.isSet()) {
                    for(Player online : teamBleu.getJoueurs()) {
                        if(Radius.isBlockInRadius(portes.getMiddleBlockLocation(), online.getLocation(), 4)) {
                            portes.openDoor();
                        } else {
                            portes.closeDoor();
                        }
                    }
                }


            }

        }.runTaskTimer(mineralcontest.plugin, 0, 20);
    }

    private void afficherScores() {
        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + "Score de l'équipe " + teamJaune.getCouleur() + teamJaune.getNomEquipe() + ChatColor.WHITE + ": " + teamJaune.getScore() + " points.");
        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + "Score de l'équipe " + teamRouge.getCouleur() + teamRouge.getNomEquipe() + ChatColor.WHITE + ": " + teamRouge.getScore() + " points.");
        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + "Score de l'équipe " + teamBleu.getCouleur() + teamBleu.getNomEquipe() + ChatColor.WHITE + ": " + teamBleu.getScore() + " points.");
    }

    private void afficherGagnant() {
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

    }

    public Equipe getPlayerTeam(Player j) {
        if(teamRouge.isPlayerInTeam(j)) return teamRouge;
        if(teamBleu.isPlayerInTeam(j)) return teamBleu;
        if(teamJaune.isPlayerInTeam(j)) return teamJaune;

        return null;
    }

    public void terminerPartie() {
        this.GamePaused = false;
        this.GameStarted = false;

        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + "La partie est terminée !");

        // ON affiche le score des équipes
        this.afficherScores();
        // On affiche l'équipe gagnante
        this.afficherGagnant();
    }

    public void pauseGame() {
        // Only si game started
        if(isGameStarted()) {
            this.GamePaused = true;
            // On averti les joueurs
            for(Player online : mineralcontest.plugin.getServer().getOnlinePlayers()) {
                online.sendMessage(mineralcontest.prefixPrive + "La partie a été mise en pause !");
                //FreezeLibrary.freezePlayer(online);
                if(online.isOnline()) {
                    online.sendMessage(mineralcontest.prefixPrive + "Pour reprendre la partie, il faut faire /resume");
                    online.sendMessage(mineralcontest.prefixPrive + "Pour switch un joueur qui s'est reconnecté, il faut faire /switch <joueur> <team>");
                }
            }
        }
    }

    public void resumeGame() {
        if(isGamePaused()) {
            if(!teamRouge.isTeamFull() || !teamBleu.isTeamFull() || !teamJaune.isTeamFull()) {
                mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixErreur + "Impossible de reprendre la partie, il manque des joueurs");
            } else {
                this.GamePaused = false;
                for(Player online : mineralcontest.plugin.getServer().getOnlinePlayers()) {
                    online.sendMessage(mineralcontest.prefixPrive + "La partie a repris !");
                    //FreezeLibrary.unfreezePlayer(online);
                }
            }
        }
    }

    public boolean demarrerPartie() throws Exception {

        if(isGameStarted()) {
            throw new Exception(mineralcontest.plugin.ERROR_GAME_ALREADY_STARTED);
        }

        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + mineralcontest.plugin.GAME_STARTING);
        mineralcontest.plugin.getServer().broadcastMessage("=============================");
        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + mineralcontest.plugin.GAME_STARTING_CHECKS);
        // Pour démarrer la partie, il faut:
        // Tous les spawn maison défini
        // Spawn coffre arene définit
        // Spawn arene définit
        // Toutes les equipes soient pleine

        // SPAWN MAISON
        if(this.teamBleu.getHouseLocation() == null) {
            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] Spawn maison bleu: " + ChatColor.RED + "X");
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

        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] Spawn maison: " + ChatColor.GREEN + "OK");

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
        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] Spawn coffre arene: " + ChatColor.GREEN + "OK");

        // SPAWN COFFRE ARENE
        if(this.arene.getCoffre().getPosition() == null) {
            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] spawn coffre arene: " + ChatColor.RED + "X");
            return false;
        }
        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] Spawn coffre arene: " + ChatColor.GREEN + "OK");



        // SPAWN ARENE
        if(this.arene.getTeleportSpawn() == null) {
            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] spawn arene: " + ChatColor.RED + "X");
            return false;
        }
        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] Spawn arene: " + ChatColor.GREEN + "OK");


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
        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + "[Verification] Equipes pleines: " + ChatColor.GREEN + "OK");


        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + mineralcontest.plugin.GAME_SUCCESSFULLY_STARTED);
        mineralcontest.plugin.getServer().broadcastMessage("=============================");


        for(Player online : mineralcontest.plugin.getServer().getOnlinePlayers())
        {
            online.setHealth(20);
            online.setGameMode(GameMode.SURVIVAL);
            online.getInventory().clear();
            PlayerUtils.givePlayerBaseItems(online);
            online.teleport(getPlayerTeam(online).getHouseLocation());
        }

        GameStarted = true;
        this.tempsPartie = 60 * DUREE_PARTIE;
        return true;

    }

    // Créer les equipes aléatoirement
    public void randomizeTeam() throws Exception {
        if(isGameStarted()) {
            throw new Exception(mineralcontest.plugin.ERROR_GAME_ALREADY_STARTED);
        }

        if(mineralcontest.teamMaxPlayers*3 != mineralcontest.plugin.getServer().getOnlinePlayers().size())
            throw new Exception(mineralcontest.plugin.ERROR_NOT_ENOUGHT_PLAYER);

        ArrayList<String> team = new ArrayList<String>();


        mineralcontest.plugin.getServer().broadcastMessage("================");


        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + mineralcontest.plugin.RANDOMIZE_TEAM_BEGIN);
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

        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.plugin.prefixGlobal + mineralcontest.plugin.RANDOMIZE_TEAM_END);
        mineralcontest.plugin.getServer().broadcastMessage("================");

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
                        break;

                    case "jaune":
                    case "yellow":
                    case "j":
                    case "y":
                        this.teamJaune.addPlayerToTeam(joueur);
                        break;

                    case "blue":
                    case "bleu":
                    case "b":
                        this.teamBleu.addPlayerToTeam(joueur);
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
