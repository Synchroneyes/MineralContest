package fr.mineral.Core;

import fr.mineral.Scoreboard.ScoreboardUtil;
import fr.mineral.Teams.Equipe;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

/*
    Classe représentant une partie MineralContest
 */
public class Game {
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

    // Temps en minute
    private int tempsPartie = 60;
    public static int SCORE_IRON = 10;
    public static int SCORE_GOLD = 50;
    public static int SCORE_DIAMOND = 150;
    public static int SCORE_EMERALD = 300;

    private boolean GameStarted = false;
    private boolean GamePaused = false;

    public boolean isGameStarted() { return this.GameStarted; }
    public boolean isGamePaused() { return this.GamePaused; }

    public Arena getArene() { return this.arene; }

    public Game() {
        this.teamRouge = new Equipe("Rouge", ChatColor.RED);
        this.teamBleu = new Equipe("Bleu", ChatColor.BLUE);
        this.teamJaune = new Equipe("Jaune", ChatColor.YELLOW);

        this.arene = new Arena();

        // On démarre le timer de la game

        new BukkitRunnable() {
            public void run() {
                if(isGameStarted()) {
                    if(isGamePaused()) {
                        // La game est en pause
                    } else {
                        // La game est en cours
                        // Si le temps atteins 0, alors on arrête la game
                        if(tempsPartie == 0) {
                            terminerPartie();
                        }


                        // Si le temps n'est pas à zéro, on continue
                        if(tempsPartie > 0) tempsPartie--;
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

    private void terminerPartie() {
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
                if(online.isOnline()) {
                    online.sendMessage(mineralcontest.prefixPrive + "Pour reprendre la partie, il faut faire /resume");
                    online.sendMessage(mineralcontest.prefixPrive + "Pour switch un joueur qui s'est reconnecté, il faut faire /switch <joueur> <team>");
                }
            }
        }
    }

    private boolean demarrerPartie() throws Exception {

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


        GameStarted = true;
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

}
