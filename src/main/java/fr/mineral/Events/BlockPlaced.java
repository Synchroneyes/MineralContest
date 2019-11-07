package fr.mineral.Events;

import fr.mineral.Utils.Radius;
import fr.mineral.Utils.Setup;
import fr.mineral.mineralcontest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaced implements Listener {
    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event) {
        if(mineralcontest.plugin.getGame().isGamePaused())
            event.setCancelled(true);

        if(mineralcontest.plugin.getGame().isGameStarted()) {
            try {
                if(Radius.isBlockInRadius(event.getBlock().getLocation(), mineralcontest.plugin.getGame().getArene().getCoffre().getPosition(), mineralcontest.plugin.getGame().getArene().arenaRadius)) {
                    //event.setCancelled(true);
                    event.getPlayer().sendMessage(mineralcontest.prefixErreur + "Vous ne pouvez pas placer de block dans cette zone.");
                }
            }catch(Exception e) {
                e.printStackTrace();
            }


        } else if(!Setup.premierLancement) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(mineralcontest.prefixPrive + "Vous ne pouvez pas interagir avec des blocs avant le début d'une partie");
        }
    }
}
