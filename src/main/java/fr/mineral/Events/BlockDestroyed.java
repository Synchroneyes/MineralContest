package fr.mineral.Events;

import fr.mineral.Utils.Radius;
import fr.mineral.mineralcontest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockDestroyed implements Listener {

    @EventHandler
    public void onBlockDestroyed(BlockBreakEvent event) {
        if(mineralcontest.plugin.getGame().isGamePaused())
            event.setCancelled(true);

        if(mineralcontest.plugin.getGame().isGameStarted()) {
            try {
                if(Radius.isBlockInRadius(event.getBlock().getLocation(), mineralcontest.plugin.getGame().getArene().getCoffre().getPosition(), mineralcontest.plugin.getGame().getArene().arenaRadius)) {
                    //event.setCancelled(true);
                    event.getPlayer().sendMessage(mineralcontest.prefixErreur + "Vous ne pouvez pas casser de block dans cette zone.");
                }
            }catch(Exception e) {
                e.printStackTrace();
            }

        } else {
            event.setCancelled(true);
            event.getPlayer().sendMessage(mineralcontest.prefixPrive + "Vous ne pouvez pas interagir avec des blocs avant le début d'une partie");
        }
    }
}
