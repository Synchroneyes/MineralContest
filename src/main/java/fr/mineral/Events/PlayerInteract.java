package fr.mineral.Events;

import fr.mineral.Utils.AutomaticDoors;
import fr.mineral.Utils.Setup;
import fr.mineral.mineralcontest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player joueur = (Player) event.getPlayer();

        if(Setup.getEtape() > 0 && Setup.premierLancement) {
            if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                event.setCancelled(true);
                Setup.setEmplacementTemporaire(event.getClickedBlock().getLocation());
            }
        }

        if(mineralcontest.plugin.getGame().getTeamBleu().getPorte().getPorte().size() < AutomaticDoors.maxDoorSize) {
            mineralcontest.plugin.getGame().getTeamBleu().getPorte().addToDoor(event.getClickedBlock());
            event.setCancelled(true);

        } else if(mineralcontest.plugin.getGame().getTeamJaune().getPorte().getPorte().size() < AutomaticDoors.maxDoorSize){
            mineralcontest.plugin.getGame().getTeamJaune().getPorte().addToDoor(event.getClickedBlock());
            event.setCancelled(true);
        } else if(mineralcontest.plugin.getGame().getTeamRouge().getPorte().getPorte().size() < AutomaticDoors.maxDoorSize) {
            mineralcontest.plugin.getGame().getTeamRouge().getPorte().addToDoor(event.getClickedBlock());
            event.setCancelled(true);
        } else {
            mineralcontest.plugin.getServer().broadcastMessage("DONE");
        }

    }
}
