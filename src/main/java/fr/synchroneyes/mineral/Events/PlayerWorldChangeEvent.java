package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.custom_events.MCPlayerChangedWorldEvent;
import fr.synchroneyes.custom_events.MCPlayerJoinEvent;
import fr.synchroneyes.custom_events.MCPlayerLeaveWorldPluginEvent;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;

public class PlayerWorldChangeEvent implements Listener {


    /**
     * Méthode appelée lorsqu'un joueur du plugin change de monde
     * @param event
     */
    @EventHandler
    public void onPlayerWorldChangeEvent(MCPlayerChangedWorldEvent event) {
        // On regarde la destination
        // Si on va dans le nether ou dans l'end, on cancel l'event
        if(event.getTo().getEnvironment() != World.Environment.NORMAL) {

            // On regarde si c'est le nether
            if(event.getTo().getEnvironment() == World.Environment.NETHER) {
                // On regarde si le nether est actif, si oui on le tp dans le nether de la map

                // à ajouter plus tard
                /*if(event.getJoueur().getGroupe().getParametresPartie().getCVAR("enable_nether").getValeurNumerique() == 1) {
                    // On TP le joueur dans le nether de son groupe
                    event.getJoueur().sendPrivateMessage(event.getTo().getName());
                    Location worldSpawn = event.getJoueur().getGroupe().getNether().getSpawnLocation();
                    return;

                }*/
            }

            event.setCancelled(true);
            event.getJoueur().sendPrivateMessage(mineralcontest.prefixErreur + "L'accès à ce monde n'est pas autorisé. Vous devez rester dans le monde normal et non dans le monde: " + event.getTo().getEnvironment().name());
            return;
        }

        // On regarde si il quitte le monde
        // Si le joueur va dans un monde qui ne fait plus partie du plugin
        if(!mineralcontest.isAMineralContestWorld(event.getTo())) {
            // Le joueur quitte le "plugin"
            MCPlayerLeaveWorldPluginEvent event1 = new MCPlayerLeaveWorldPluginEvent(event.getJoueur().getJoueur());
            Bukkit.getServer().getPluginManager().callEvent(event1);
            return;
        }else {
            // Le joueur va rejoindre le plugin à nouveau

            // Si le joueur faisait déjà parti du plugin; on ne fait rien
            if(mineralcontest.isAMineralContestWorld(event.getFrom())) {
                //Bukkit.broadcastMessage("Le joueur vient d'un monde MC; on fait rien");
                return;
            }
            MCPlayerJoinEvent event1 = new MCPlayerJoinEvent(event.getJoueur().getJoueur());
            Bukkit.getPluginManager().callEvent(event1);

        }



    }

    private void convertToNether(Location l) {
        double x,y,z;
        x = l.getX();
        y = l.getY();
        z = l.getZ();

        x /= 8;
        z /= 8;

        l.setX(x);
        l.setZ(z);

    }

}
