package fr.mineral.Core.Arena.Zones;

import fr.mineral.Core.Equipe;
import fr.mineral.Utils.Player.CouplePlayer;
import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


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
        mineralcontest.plugin.getLogger().info(mineralcontest.prefixGlobal + "Position de la deathzone ajoutée");
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

                if(joueur.getValeur() >= 1) joueur.getJoueur().sendTitle(ChatColor.RED + "Vous êtes mort.", "Réapparition dans " + joueur.getValeur() + " secondes", 0, 20, 0);
                joueur.setValeur(joueur.getValeur()-1);
                joueur.getJoueur().setFireTicks(0);

            }
        }
    }

    public void add(Player joueur) {
        this.joueurs.add(new CouplePlayer(joueur, timeInDeathzone));
        joueur.setGameMode(GameMode.ADVENTURE);
        joueur.getInventory().clear();
        joueur.sendMessage(mineralcontest.prefixPrive + "Vous êtes mort. Vous avez été placé dans la deathzone pendant " + timeInDeathzone + " secondes.");
        //joueur.teleport(this.spawnLocation);
        try {
            joueur.teleport(mineralcontest.plugin.getGame().getPlayerTeam(joueur).getHouseLocation());

        }catch(Exception e) {
            e.printStackTrace();
        }

        joueur.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20*15, 1));
        joueur.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*15, 1));

    }

    public boolean isPlayerDead(Player joueur) {
        for(CouplePlayer cp : getPlayers()) {
            if(cp.getJoueur().equals(joueur))
                return true;
        }

        return false;
    }

    private void libererJoueur(CouplePlayer DeathZonePlayer) throws Exception {

        // SI le joueur n'a plus de temps à passer ici
        if(DeathZonePlayer.getValeur() <= 0) {
            Player joueur = DeathZonePlayer.getJoueur();
            joueur.setGameMode(GameMode.SURVIVAL);
            joueur.setFireTicks(0);
            joueur.setHealth(20f);

            Equipe team = mineralcontest.plugin.getGame().getPlayerTeam(joueur);
            if(team == null) {
                // On le téléporte vers l'arene
                throw new Exception("TODO: Redirecte vers spawn arene quand le joueur a fini son temps en deathzone");
            } else {
                // ON le TP vers son spawn equipe
                joueur.teleport(team.getHouseLocation());
                joueur.removePotionEffect(PotionEffectType.INVISIBILITY);
                joueur.removePotionEffect(PotionEffectType.BLINDNESS);


            }

            // On rend le stuff du joueur
            PlayerUtils.givePlayerBaseItems(joueur);
            DeathZonePlayer.getJoueur().sendTitle(ChatColor.GREEN + "Vous êtes libre", "", 1, 2*20, 1);

            // ON le supprime de la liste
            this.joueurs.remove(DeathZonePlayer);



        }
    }
}
