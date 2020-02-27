package fr.mineral.Events;

import fr.mineral.Translation.Lang;
import fr.mineral.Utils.BlockSaver;
import fr.mineral.Utils.Radius;
import fr.mineral.Utils.Setup;
import fr.mineral.mineralcontest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaced implements Listener {
    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event) {
        if(mineralcontest.plugin.getGame().isGamePaused()) {
            event.setCancelled(true);
            return;
        }

        if(mineralcontest.plugin.getGame().isGameStarted()) {
            try {
                if(Radius.isBlockInRadius(event.getBlock().getLocation(), mineralcontest.plugin.getGame().getArene().getCoffre().getPosition(), mineralcontest.plugin.getGame().getArene().arenaRadius)) {
                    //event.setCancelled(true);
                    event.getPlayer().sendMessage(mineralcontest.prefixErreur + Lang.cant_break_block_here.toString());
                    return;
                }
            }catch(Exception e) {
                e.printStackTrace();
            }


        } else if(!Setup.premierLancement) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(mineralcontest.prefixPrive + Lang.cant_interact_block_pre_game.toString());
            return;
        }

        // Save the block
        mineralcontest.plugin.getGame().addBlock(event.getBlock(), BlockSaver.Type.PLACED);
    }
}
