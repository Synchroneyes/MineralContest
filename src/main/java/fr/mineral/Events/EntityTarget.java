package fr.mineral.Events;

import fr.mineral.Core.Game.Game;
import fr.mineral.mineralcontest;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityTarget implements Listener {

    @EventHandler
    public void OnEntityTarget(EntityTargetEvent event) {
        World worldEvent = event.getEntity().getWorld();
        if (mineralcontest.isAMineralContestWorld(worldEvent)) {
            Game partie = mineralcontest.getWorldGame(worldEvent);
            if (event.getTarget() instanceof Player) {
                if (partie != null && partie.isGamePaused())
                    event.setCancelled(true);
            }

        }
    }
}
