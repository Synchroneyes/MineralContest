package fr.mineral.Events;

import fr.mineral.Translation.Lang;
import fr.mineral.Utils.Door.AutomaticDoors;
import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.Utils.Setup;
import fr.mineral.mineralcontest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        World worldEvent = event.getPlayer().getWorld();
        if(worldEvent.equals(mineralcontest.plugin.pluginWorld)) {
            Player joueur = (Player) event.getPlayer();

            if(event.getClickedBlock() != null) {
                Block listCommandBlock = worldEvent.getBlockAt(new Location(worldEvent, 111, 169, -168));
                Location clickedBlock = event.getClickedBlock().getLocation();
                if(listCommandBlock.getLocation().equals(event.getClickedBlock().getLocation()) && listCommandBlock.getType().equals(Material.LIME_STAINED_GLASS)) {
                    PlayerUtils.sendPluginCommandsToPlayer(joueur);
                }
            }


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
                    mineralcontest.broadcastMessage("DONE");
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

    @EventHandler
    public void blockVillagerTrades(PlayerInteractAtEntityEvent entityEvent) {
        World current_world = entityEvent.getPlayer().getWorld();

        if(current_world.equals(mineralcontest.plugin.pluginWorld)) {
            Player p = entityEvent.getPlayer();

            if(entityEvent.getRightClicked() instanceof Villager ||
                    entityEvent.getRightClicked() instanceof Witch    ||
                    entityEvent.getRightClicked() instanceof TraderLlama ||
                    entityEvent.getRightClicked() instanceof WanderingTrader ||
                    entityEvent.getRightClicked() instanceof Golem ||
                    entityEvent.getRightClicked() instanceof IronGolem) {
                entityEvent.setCancelled(true);
                entityEvent.getPlayer().closeInventory();
                entityEvent.getRightClicked().remove();
            }
        }

    }
}
