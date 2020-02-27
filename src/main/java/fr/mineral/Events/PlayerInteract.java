package fr.mineral.Events;

import fr.mineral.Translation.Lang;
import fr.mineral.Utils.Door.AutomaticDoors;
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

        if(Setup.addDoors) {
            if(mineralcontest.plugin.getGame().getBlueHouse().getPorte().getPorte().size() < AutomaticDoors.maxDoorSize) {
                mineralcontest.plugin.getGame().getBlueHouse().getPorte().addToDoor(event.getClickedBlock());
                joueur.sendMessage("porte bleu added");
                event.setCancelled(true);

            } else if(mineralcontest.plugin.getGame().getYellowHouse().getPorte().getPorte().size() < AutomaticDoors.maxDoorSize){
                mineralcontest.plugin.getGame().getYellowHouse().getPorte().addToDoor(event.getClickedBlock());
                joueur.sendMessage("porte jaune added");

                event.setCancelled(true);
            } else if(mineralcontest.plugin.getGame().getRedHouse().getPorte().getPorte().size() < AutomaticDoors.maxDoorSize) {
                mineralcontest.plugin.getGame().getRedHouse().getPorte().addToDoor(event.getClickedBlock());
                joueur.sendMessage("porte rouge added");

                event.setCancelled(true);
            } else {
                mineralcontest.plugin.getServer().broadcastMessage("DONE");
            }
        }

        if(!mineralcontest.plugin.getGame().isGameStarted() && (event.getAction().equals(Action.LEFT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && !Setup.premierLancement) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(mineralcontest.prefixPrive + Lang.cant_interact_block_pre_game.toString());
        }


        if(Setup.getEtape() > 0 && Setup.premierLancement) {
            if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                event.setCancelled(true);
                Setup.setEmplacementTemporaire(event.getClickedBlock().getLocation());
            }
        }









    }
}
