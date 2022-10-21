package fr.synchroneyes.special_events.halloween2022.events;

import fr.synchroneyes.custom_events.MCArenaChestSpawnEvent;
import fr.synchroneyes.mineral.Core.Boss.BossType.CrazyZombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ArenaEvent implements Listener {

    @EventHandler
    public void onChestSpawn(MCArenaChestSpawnEvent event){
        event.getGame().groupe.sendToEveryone("BOSS SPAWN");
        event.getGame().getBossManager().spawnNewBoss(event.getGame().getArene().getCoffre().getLocation(), new CrazyZombie());
    }
}
