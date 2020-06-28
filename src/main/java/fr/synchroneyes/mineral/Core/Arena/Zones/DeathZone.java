package fr.synchroneyes.mineral.Core.Arena.Zones;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.House;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.ErrorReporting.Error;
import fr.synchroneyes.mineral.Utils.Player.CouplePlayer;
import fr.synchroneyes.mineral.Utils.Player.PlayerUtils;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ConcurrentLinkedQueue;

/*
    Classe représentant la deathzone
 */
public class DeathZone {
    /*
        Un "CouplePlayer" est une classe ayant les attributs suivants:
            - Player joueur
            - int valeur
     */
    ConcurrentLinkedQueue<CouplePlayer> joueurs;

    // Temps en seconde
    private int timeInDeathzone = 0;
    private Location spawnLocation;
    private Groupe groupe;

    public DeathZone(Groupe g) {
        this.joueurs = new ConcurrentLinkedQueue<CouplePlayer>();
        this.groupe = g;
        try {

            timeInDeathzone = g.getParametresPartie().getCVAR("death_time").getValeurNumerique();
        } catch (Exception e) {
            Error.Report(e, g.getGame());
        }
    }

    public ConcurrentLinkedQueue<CouplePlayer> getPlayers() {
        return this.joueurs;
    }

    public void setSpawnLocation(Location pos) {
        mineralcontest.plugin.getLogger().info(mineralcontest.prefixGlobal + Lang.translate(Lang.deathzone_spawn_location_added.toString()));
        this.spawnLocation = pos;
    }

    public CouplePlayer getPlayerInfo(Player p) {
        for (CouplePlayer playerInfo : getPlayers())
            if (playerInfo.getJoueur().equals(p)) return playerInfo;
        return null;
    }


    public Location getSpawnLocation() throws Exception {
        if (spawnLocation == null) {
            throw new Exception(Lang.translate(Lang.deathzone_spawn_location_undefined.toString()));
        }

        return this.spawnLocation;
    }

    // Cette fonction réduit le temps des joueurs d'une seconde
    // Elle sera appelée dans le runnable bukkit qui gère le temps
    public synchronized void reducePlayerTimer() throws Exception {

        // SI on a des joueurs dans la deathZone
        if (joueurs.size() != 0) {
            for (CouplePlayer joueur : this.joueurs) {

                if (joueur.getJoueur() == null || !joueur.getJoueur().isOnline()) {
                    this.joueurs.remove(joueur);
                    return;
                }
                // Si le joueur a fini sa peine
                if (joueur.getValeur() <= 0)
                    libererJoueur(joueur);

                // ON réduit son temps de 1

                if (joueur.getValeur() >= 1)
                    joueur.getJoueur().sendTitle(ChatColor.RED + Lang.translate(Lang.deathzone_you_are_dead.toString()), Lang.translate(Lang.deathzone_respawn_in.toString(), joueur.getJoueur()), 0, 20, 0);
                joueur.setValeur(joueur.getValeur() - 1);
                joueur.getJoueur().setFireTicks(0);

            }
        }
    }

    public synchronized int getPlayerDeathTime(Player joueur) {
        if (isPlayerDead(joueur))
            for (CouplePlayer cp : getPlayers())
                if (cp.getJoueur().equals(joueur))
                    return cp.getValeur();

        return 0;
    }

    public synchronized void add(Player joueur) throws Exception {
        timeInDeathzone = groupe.getParametresPartie().getCVAR("death_time").getValeurNumerique();
        this.joueurs.add(new CouplePlayer(joueur, timeInDeathzone));
        applyDeathEffectToPlayer(joueur);

    }

    public synchronized void add(CouplePlayer couplePlayer) throws Exception {

        Player joueur = couplePlayer.getJoueur();
        this.joueurs.add(couplePlayer);
        applyDeathEffectToPlayer(joueur);

    }


    private void applyDeathEffectToPlayer(Player joueur) throws Exception {

        timeInDeathzone = groupe.getParametresPartie().getCVAR("death_time").getValeurNumerique();
        Game partie = mineralcontest.getPlayerGame(joueur);

        if (partie.isReferee(joueur) && partie.isGameStarted()) {
            joueur.setGameMode(GameMode.SURVIVAL);
            joueur.setFireTicks(0);
            joueur.setHealth(20f);
            PlayerUtils.teleportPlayer(joueur, partie.groupe.getMonde(), partie.getArene().getCoffre().getPosition());
            return;
        }

        joueur.setGameMode(GameMode.ADVENTURE);
        joueur.getInventory().clear();
        joueur.sendMessage(mineralcontest.prefixPrive + Lang.translate(Lang.deathzone_respawn_in.toString(), joueur));
        //PlayerUtils.teleportPlayer(this.spawnLocation);
        try {
            PlayerUtils.teleportPlayer(joueur, partie.groupe.getMonde(), partie.getPlayerHouse(joueur).getHouseLocation());

        } catch (Exception e) {
            e.printStackTrace();
            Error.Report(e, partie);
        }

        joueur.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * (timeInDeathzone * 3), 1));
        joueur.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * (timeInDeathzone * 3), 1));
    }

    public synchronized boolean isPlayerDead(Player joueur) {
        for (CouplePlayer cp : getPlayers()) {
            if (cp.getJoueur().equals(joueur))
                return true;
        }

        return false;
    }

    private synchronized void libererJoueur(CouplePlayer DeathZonePlayer) throws Exception {

        // SI le joueur n'a plus de temps à passer ici
        if (DeathZonePlayer.getValeur() <= 0) {

            Player joueur = DeathZonePlayer.getJoueur();

            if (!joueur.isOnline()) {
                this.joueurs.remove(DeathZonePlayer);
                return;
            }

            joueur.setGameMode(GameMode.SURVIVAL);
            joueur.setFireTicks(0);
            joueur.setHealth(20f);

            Game partie = mineralcontest.getPlayerGame(joueur);

            Equipe team = mineralcontest.getPlayerGame(joueur).getPlayerTeam(joueur);
            House teamHouse = mineralcontest.getPlayerGame(joueur).getPlayerHouse(joueur);
            if (team == null) {
                // On le téléporte vers l'arene
                // On le téléporte vers l'arene
                mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + "Le joueur " + joueur.getDisplayName() + " a été TP au centre de l'arène car il n'a pas d'équipe et vient de réapparaitre suite à une mort", partie.groupe);
                mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + "Le joueur " + joueur.getDisplayName() + " a également été mis spectateur. Vous devez changer son gamemode", partie.groupe);
                mineralcontest.getPlayerGame(joueur).teleportToLobby(joueur);
                joueur.setGameMode(GameMode.SPECTATOR);

            } else {
                // ON le TP vers son spawn equipe
                PlayerUtils.teleportPlayer(joueur, partie.groupe.getMonde(), teamHouse.getHouseLocation());
                joueur.removePotionEffect(PotionEffectType.INVISIBILITY);
                joueur.removePotionEffect(PotionEffectType.BLINDNESS);


            }

            // On rend le stuff du joueur
            try {
                groupe.getPlayerBaseItem().giveItemsToPlayer(joueur);
            } catch (Exception e) {
                mineralcontest.broadcastMessage(mineralcontest.prefixErreur + e.getMessage(), partie.groupe);
                e.printStackTrace();
                Error.Report(e, partie);
            }
            DeathZonePlayer.getJoueur().sendTitle(ChatColor.GREEN + Lang.translate(Lang.deathzone_respawned.toString()), "", 1, 2 * 20, 1);

            // ON le supprime de la liste
            this.joueurs.remove(DeathZonePlayer);


        }
    }
}
