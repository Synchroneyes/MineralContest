package fr.mineral.Events;

import fr.mineral.Utils.Setup;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player joueur = (Player) event.getPlayer();

        if(Setup.getEtape() > 0 && Setup.premierLancement) {
            if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                event.setCancelled(true);
                Setup.setEmplacementTemporaire(event.getClickedBlock().getLocation());
            }
        }
    }
}
