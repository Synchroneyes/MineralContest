package fr.mineral.Events;

import fr.mineral.Core.Game.BlockManager;
import fr.mineral.Settings.GameSettingsCvar;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.BlockSaver;
import fr.mineral.Utils.Radius;
import fr.mineral.Utils.Setup;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaced implements Listener {
    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event) {

        World worldEvent = event.getPlayer().getWorld();
        if(worldEvent.equals(mineralcontest.plugin.pluginWorld)) {


            if(!mineralcontest.plugin.getGame().isGameStarted()) {
                event.getPlayer().sendMessage(mineralcontest.prefixPrive + Lang.cant_interact_block_pre_game.toString());
                event.setCancelled(true);
                return;
            }

            if (mineralcontest.plugin.getGame().isGamePaused()) {
                event.setCancelled(true);
                return;
            }

            if (mineralcontest.plugin.getGame().isGameStarted()) {
                try {
                    if (Radius.isBlockInRadius(event.getBlock().getLocation(), mineralcontest.plugin.getGame().getArene().getCoffre().getPosition(), mineralcontest.plugin.getGame().getArene().arenaRadius)) {

                        // We are in the radius of the arena
                        if((int) GameSettingsCvar.getValueFromCVARName("mp_enable_block_adding") == 1) {
                            BlockManager blockManager = BlockManager.getInstance();

                            if(! blockManager.isBlockAllowedToBeAdded(event.getBlock())) {
                                event.setCancelled(true);
                                event.getPlayer().sendMessage(mineralcontest.prefixErreur + Lang.block_not_allowed_to_be_placed.toString());
                            }
                            blockManager.addBlock(event.getBlock());
                        } else {
                            // We block the block adding
                            event.setCancelled(true);
                            event.getPlayer().sendMessage(mineralcontest.prefixErreur + Lang.cant_break_block_here.toString());
                        }
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else if (!Setup.premierLancement) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(mineralcontest.prefixPrive + Lang.cant_interact_block_pre_game.toString());

                return;
            }


            mineralcontest.plugin.getGame().addAChest(event.getBlock());
            // Save the block
            mineralcontest.plugin.getGame().addBlock(event.getBlock(), BlockSaver.Type.PLACED);
        }
    }

}

