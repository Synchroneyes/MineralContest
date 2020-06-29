package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.mapbuilder.MapBuilder;
import fr.synchroneyes.mineral.Core.Game.BlockManager;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Settings.GameSettings;
import fr.synchroneyes.mineral.Statistics.Class.MinerStat;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.BlockSaver;
import fr.synchroneyes.mineral.Utils.ErrorReporting.Error;
import fr.synchroneyes.mineral.Utils.Radius;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockDestroyed implements Listener {

    @EventHandler
    public void onBlockDestroyed(BlockBreakEvent event) {

        if (MapBuilder.getInstance().isBuilderModeEnabled) return;


        World worldEvent = event.getPlayer().getWorld();
        Game game = mineralcontest.getPlayerGame(event.getPlayer());

        if (mineralcontest.isAMineralContestWorld(worldEvent)) {
            // SI on est dans le HUB, on ne peut pas casser de bloc

            if (mineralcontest.isInMineralContestHub(event.getPlayer())) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(mineralcontest.prefixPrive + Lang.cant_interact_block_pre_game.toString());
                return;
            }

            if (worldEvent.equals(mineralcontest.plugin.pluginWorld)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(mineralcontest.prefixPrive + Lang.cant_interact_block_pre_game.toString());
                return;
            }


            // Si on est pas dans une game, on arrête là, ne devrait pas arriver
            if (mineralcontest.isInMineralContestHub(event.getPlayer())) {
                event.setCancelled(true);
                Bukkit.getLogger().severe("[MineralContest] Block got destroyed, but not inside a game ...");
                return;
            }

            GameSettings settings = game.groupe.getParametresPartie();


            if (game.isGameStarted() && !game.isGamePaused()) {
                try {
                    //protected_zone_area_radius arena_safezone_radius enable_monster_in_protected_zone

                    if (Radius.isBlockInRadius(event.getBlock().getLocation(), game.getArene().getCoffre().getPosition(), settings.getCVAR("protected_zone_area_radius").getValeurNumerique())) {
                        if (settings.getCVAR("mp_enable_block_adding").getValeurNumerique() == 1) {
                            BlockManager blockManager = BlockManager.getInstance();
                            if (blockManager.wasBlockAdded(event.getBlock()))
                                blockManager.removeBlock(event.getBlock());
                            else {

                                Block destroyedBlock = event.getBlock();
                                if (destroyedBlock.getType() != Material.GRASS && destroyedBlock.getType() != Material.DEAD_BUSH && destroyedBlock.getType() != Material.TALL_GRASS) {
                                    event.setCancelled(true);
                                    event.getPlayer().sendMessage(mineralcontest.prefixErreur + Lang.cant_break_block_here.toString());
                                    return;
                                }

                                return;

                            }
                        } else {
                            event.setCancelled(true);
                            event.getPlayer().sendMessage(mineralcontest.prefixErreur + Lang.cant_break_block_here.toString());
                            return;
                        }
                    }
                } catch (Exception e) {
                    Error.Report(e, game);
                    event.setCancelled(true);
                    return;
                }
            } else {
                event.setCancelled(true);
                event.getPlayer().sendMessage(mineralcontest.prefixPrive + Lang.cant_break_block_here.toString());
                return;
            }

            game.addBlock(event.getBlock(), BlockSaver.Type.DESTROYED);

            game.getStatsManager().register(MinerStat.class, event.getPlayer(), null);
        }


    }
}
