package fr.synchroneyes.special_events.halloween2022.events;

import fr.synchroneyes.custom_events.MCAirDropSpawnEvent;
import fr.synchroneyes.custom_events.MCArenaChestSpawnEvent;
import fr.synchroneyes.mineral.Core.Boss.BossManager;
import fr.synchroneyes.mineral.DeathAnimations.Animations.HalloweenHurricaneAnimation;
import fr.synchroneyes.mineral.mineralcontest;
import fr.synchroneyes.special_events.halloween2022.boss.AirdropDefenderBoss;
import fr.synchroneyes.special_events.halloween2022.boss.ArenaChestDefenderBoss;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ArenaEvent implements Listener {

    private BossManager bossManager;

    public ArenaEvent(BossManager manager){
        this.bossManager = manager;
    }

    @EventHandler
    public void onArenaChestSpawn(MCArenaChestSpawnEvent event){
        Location groundLocation = getGroundLocation(event.getGame().getArene().getCoffre().getLocation());
        bossManager.spawnNewBoss(groundLocation, new ArenaChestDefenderBoss());
    }


    private Location getGroundLocation(Location dropLocation) {
        while(dropLocation.getBlock().getType() == Material.AIR) {
            dropLocation = dropLocation.getBlock().getRelative(BlockFace.DOWN).getLocation();
        }

        return dropLocation.getBlock().getRelative(BlockFace.UP).getLocation();
    }
}
