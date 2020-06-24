package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Utils.Player.PlayerUtils;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerPick implements Listener {

    @EventHandler
    public void onPlayerPickEvent(PlayerPickupItemEvent event) {
        Player p = event.getPlayer();
        if (mineralcontest.isInAMineralContestWorld(p)) {
            Game partie = mineralcontest.getPlayerGame(p);
            if (partie == null) return;
            if (PlayerUtils.isPlayerInDeathZone(p)) {
                event.setCancelled(true);
            }
        }
    }

}
