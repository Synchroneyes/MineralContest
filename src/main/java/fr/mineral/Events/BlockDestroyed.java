package fr.mineral.Events;

import fr.mineral.Core.Game.BlockManager;
import fr.mineral.Core.Game.Game;
import fr.mineral.Settings.GameSettingsCvar;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.BlockSaver;
import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.Utils.Radius;
import fr.mineral.Utils.Setup;
import fr.mineral.mineralcontest;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockDestroyed implements Listener {

    @EventHandler
    public void onBlockDestroyed(BlockBreakEvent event) {
        World worldEvent = event.getPlayer().getWorld();
        Game game = mineralcontest.plugin.getGame();

        if(worldEvent.equals(mineralcontest.plugin.pluginWorld)) {


            if (Setup.premierLancement) {
                event.setCancelled(true);
                return;
            }

            if(mineralcontest.plugin.getGame().isGamePaused()) {
                event.setCancelled(true);
                return;
            }

            if(mineralcontest.plugin.getGame().isGameStarted()) {
                try {
                    if(Radius.isBlockInRadius(event.getBlock().getLocation(), mineralcontest.plugin.getGame().getArene().getCoffre().getPosition(), mineralcontest.plugin.getGame().getArene().arenaRadius)) {

                        if((int) GameSettingsCvar.getValueFromCVARName("mp_enable_block_adding") == 1) {
                            BlockManager blockManager = BlockManager.getInstance();
                            if(blockManager.wasBlockAdded(event.getBlock()))
                                blockManager.removeBlock(event.getBlock());
                            else {
                                event.setCancelled(true);
                                event.getPlayer().sendMessage(mineralcontest.prefixErreur + Lang.cant_break_block_here.toString());
                            }
                        } else {
                            event.setCancelled(true);
                            event.getPlayer().sendMessage(mineralcontest.prefixErreur + Lang.cant_break_block_here.toString());
                        }


                        return;
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                    Error.Report(e);
                    return;
                }

                if(!game.isThisBlockAGameChest(event.getBlock())) {
                    if(game.isTheBlockAChest(event.getBlock())) {
                        Chest chest = (Chest) event.getBlock().getState();
                        chest.getInventory().clear();
                    }
                }

            } else {
                event.setCancelled(true);
                event.getPlayer().sendMessage(mineralcontest.prefixPrive + Lang.cant_interact_block_pre_game.toString());
                return;
            }

            mineralcontest.plugin.getGame().addBlock(event.getBlock(), BlockSaver.Type.DESTROYED);
        }
    }
}
