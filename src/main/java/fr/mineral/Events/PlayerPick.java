package fr.mineral.Events;

import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.mineralcontest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerPick implements Listener {

    @EventHandler
    public void onPlayerPickEvent(PlayerPickupItemEvent event) {
        Player p = event.getPlayer();
        if(p.getWorld().equals(mineralcontest.plugin.pluginWorld))
            if(PlayerUtils.isPlayerInDeathZone(p)) {
                event.setCancelled(true);
            }
    }

}
