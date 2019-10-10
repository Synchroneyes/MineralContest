package fr.mineral.Core.Zones;

import fr.mineral.Teams.Equipe;
import fr.mineral.Utils.CouplePlayer;
import fr.mineral.Utils.PlayerUtils;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;


import java.util.LinkedList;

/*
    Classe représentant la deathzone
 */
public class DeathZone {
    /*
        Un "CouplePlayer" est une classe ayant les attributs suivants:
            - Player joueur
            - int valeur
     */
    LinkedList<CouplePlayer> joueurs;

    // Temps en seconde
    private int timeInDeathzone = 10;
    private Location spawnLocation;

    public DeathZone() {
        this.joueurs = new LinkedList<CouplePlayer>();
    }
    public LinkedList<CouplePlayer> getPlayers() { return this.joueurs; }

    public void setSpawnLocation(Location pos) {
        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + "Position de la deathzone ajoutée");
        this.spawnLocation = pos;
    }

    public Location getSpawnLocation() throws Exception {
        if(spawnLocation == null) {
            throw new Exception("La position de spawn de la deathzone n'est pas défini");
        }

        return this.spawnLocation;
    }

    // Cette fonction réduit le temps des joueurs d'une seconde
    // Elle sera appelée dans le runnable bukkit qui gère le temps
    public void reducePlayerTimer() throws Exception {

        // SI on a des joueurs dans la deathZone
        if(joueurs.size() != 0) {
            for(CouplePlayer joueur : this.joueurs) {

                // Si le joueur a fini sa peine
                if(joueur.getValeur() <= 0)
                    libererJoueur(joueur);

                // ON réduit son temps de 1
                if(joueur.getValeur() >= 1) joueur.getJoueur().performCommand("title " + joueur.getJoueur().getDisplayName() + " title {\"text\":\"Réapparition dans\",\"color\":\"orange\",\"extra\":[{\"text\":\" " +  joueur.getValeur() + " secondes\",\"color\":\"yellow\"}]}");
                joueur.setValeur(joueur.getValeur()-1);
                //mineralcontest.plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "title @a title [{\"text\":\"Blah blah\",\"color\":\"gold\"}]"); //JSON formatting is invalid!
            }
        }
    }

    public void add(Player joueur) {
        this.joueurs.add(new CouplePlayer(joueur, timeInDeathzone));
        joueur.setGameMode(GameMode.ADVENTURE);
        joueur.getInventory().clear();
        joueur.sendMessage(mineralcontest.prefixPrive + "Vous êtes mort. Vous avez été placé dans la deathzone pendant " + timeInDeathzone + " secondes.");
        joueur.teleport(this.spawnLocation);
    }

    private void libererJoueur(CouplePlayer DeathZonePlayer) throws Exception {

        // SI le joueur n'a plus de temps à passer ici
        if(DeathZonePlayer.getValeur() <= 0) {
            Player joueur = DeathZonePlayer.getJoueur();
            joueur.setGameMode(GameMode.SURVIVAL);

            Equipe team = mineralcontest.plugin.getGame().getPlayerTeam(joueur);
            if(team == null) {
                // On le téléporte vers l'arene
                throw new Exception("TODO: Redirecte vers spawn arene quand le joueur a fini son temps en deathzone");
            } else {
                // ON le TP vers son spawn equipe
                joueur.teleport(team.getHouseLocation());
            }

            // On rend le stuff du joueur
            PlayerUtils.givePlayerBaseItems(joueur);
            DeathZonePlayer.getJoueur().performCommand("title " + DeathZonePlayer.getJoueur().getDisplayName() + " title {\"text\":\"Vous êtes libre\"}");

            // ON le supprime de la liste
            this.joueurs.remove(DeathZonePlayer);


        }
    }
}
