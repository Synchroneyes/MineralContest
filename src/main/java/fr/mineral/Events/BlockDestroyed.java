package fr.mineral.Events;

import fr.mineral.Core.Game.BlockManager;
import fr.mineral.Core.Game.Game;
import fr.mineral.Settings.GameSettingsCvarOLD;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.BlockSaver;
import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.Utils.Radius;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockDestroyed implements Listener {

    @EventHandler
    public void onBlockDestroyed(BlockBreakEvent event) {
        World worldEvent = event.getPlayer().getWorld();
        Game game = mineralcontest.getPlayerGame(event.getPlayer());

        if (mineralcontest.isAMineralContestWorld(worldEvent)) {
            // SI on est dans le HUB, on ne peut pas casser de bloc
            if (worldEvent.equals(mineralcontest.plugin.pluginWorld)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(mineralcontest.prefixPrive + Lang.cant_interact_block_pre_game.toString());
                return;
            }

            // Si on est pas dans une game, on arrête là, ne devrait pas arriver
            if (game == null) {
                Bukkit.getLogger().severe("[MineralContest] Block got destroyed, but not inside a game ...");
                return;
            }

            if (game.isGameStarted() && !game.isGamePaused()) {
                try {
                    if (Radius.isBlockInRadius(event.getBlock().getLocation(), game.getArene().getCoffre().getPosition(), game.getArene().arenaRadius)) {
                        if ((int) GameSettingsCvarOLD.getValueFromCVARName("mp_enable_block_adding") == 1) {
                            BlockManager blockManager = BlockManager.getInstance();
                            if (blockManager.wasBlockAdded(event.getBlock()))
                                blockManager.removeBlock(event.getBlock());
                            else {
                                event.setCancelled(true);
                                event.getPlayer().sendMessage(mineralcontest.prefixErreur + Lang.cant_break_block_here.toString());
                            }
                        } else {
                            event.setCancelled(true);
                            event.getPlayer().sendMessage(mineralcontest.prefixErreur + Lang.cant_break_block_here.toString());
                        }
                    }
                } catch (Exception e) {
                    Error.Report(e, game);
                    event.setCancelled(true);
                }
            } else {
                event.setCancelled(true);
                event.getPlayer().sendMessage(mineralcontest.prefixPrive + Lang.cant_break_block_here.toString());
                return;
            }

            game.addBlock(event.getBlock(), BlockSaver.Type.DESTROYED);
        }


        /*if(game == null || mineralcontest.isAMineralContestWorld(worldEvent)) {
            event.setCancelled(true);
            return;
        }

        if(worldEvent.equals(mineralcontest.plugin.pluginWorld)) {


            if (Setup.instance != null && Setup.premierLancement) {
                event.setCancelled(true);
                return;
            }

            if(game.isGamePaused()) {
                Bukkit.getLogger().severe("GAME IS PAUSED, BLOCK CANT BE DESTROYED");
                event.setCancelled(true);
                return;
            }

            if(game.isGameStarted()) {
                try {
                    if(Radius.isBlockInRadius(event.getBlock().getLocation(), game.getArene().getCoffre().getPosition(), game.getArene().arenaRadius)) {

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
                    Error.Report(e, mineralcontest.getWorldGame(event.getBlock().getWorld()));
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

            game.addBlock(event.getBlock(), BlockSaver.Type.DESTROYED);
        }*/
    }
}
