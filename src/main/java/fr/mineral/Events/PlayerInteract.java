package fr.mineral.Events;

import fr.mineral.Translation.Lang;
import fr.mineral.Utils.Door.AutomaticDoors;
import fr.mineral.Utils.Metric.SendInformation;
import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.Utils.Setup;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
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

            if (Setup.premierLancement) {
                if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    if (!event.getClickedBlock().getType().equals(Material.AIR)) {
                        Setup.setEmplacementTemporaire(event.getClickedBlock().getLocation());
                    }
                }

                if (Setup.addDoors) {
                    if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                        if (event.getClickedBlock().getType().equals(Material.AIR)) return;
                        // Ajout porte
                        if (Setup.getEtape() == 10) {
                            Setup.addBlockToPorte("bleu", event.getClickedBlock());
                        }

                        if (Setup.getEtape() == 11) {
                            Setup.addBlockToPorte("rouge", event.getClickedBlock());
                        }

                        if (Setup.getEtape() == 12) {
                            Setup.addBlockToPorte("jaune", event.getClickedBlock());
                        }
                    }
                }
                return;
            }

            Player joueur = (Player) event.getPlayer();

            if(event.getClickedBlock() != null) {
                Block listCommandBlock = worldEvent.getBlockAt(new Location(worldEvent, 111, 169, -168));
                if(listCommandBlock.getLocation().equals(event.getClickedBlock().getLocation()) && listCommandBlock.getType().equals(Material.LIME_STAINED_GLASS)) {
                    PlayerUtils.sendPluginCommandsToPlayer(joueur);
                }
            }

            if(!mineralcontest.plugin.getGame().isGameStarted() && (event.getAction().equals(Action.LEFT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && !Setup.premierLancement) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(mineralcontest.prefixPrive + Lang.cant_interact_block_pre_game.toString());
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
