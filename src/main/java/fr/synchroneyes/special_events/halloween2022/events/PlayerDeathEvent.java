package fr.synchroneyes.special_events.halloween2022.events;

import fr.synchroneyes.special_events.halloween2022.animations.TNTEndermanThunderAnimation;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerDeathEvent implements Listener {

    @EventHandler
    public void onPlayerDeath(org.bukkit.event.entity.PlayerDeathEvent event) {
        new TNTEndermanThunderAnimation().playAnimation(event.getEntity());
    }

}
