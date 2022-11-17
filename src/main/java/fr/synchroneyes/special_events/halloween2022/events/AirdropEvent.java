package fr.synchroneyes.special_events.halloween2022.events;

import fr.synchroneyes.custom_events.MCAirDropBreakEvent;
import fr.synchroneyes.custom_events.MCAirDropSpawnEvent;
import fr.synchroneyes.mineral.Core.Boss.BossManager;
import fr.synchroneyes.mineral.Core.Boss.BossType.CrazyZombie;
import fr.synchroneyes.mineral.mineralcontest;
import fr.synchroneyes.special_events.halloween2022.boss.AirdropDefenderBoss;
import fr.synchroneyes.special_events.halloween2022.boss.ArenaChestDefenderBoss;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AirdropEvent implements Listener {

    private BossManager bossManager;

    public AirdropEvent(BossManager bossManager){
        this.bossManager = bossManager;
    }

    @EventHandler
    public void onAirdropSpawn(MCAirDropBreakEvent event){

        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
            Location groundLocation = getGroundLocation(event.getParachuteLocation());
            bossManager.spawnNewBoss(groundLocation, new AirdropDefenderBoss());
        }, 5*20);

    }


    private Location getGroundLocation(Location dropLocation) {
        while(dropLocation.getBlock().getType() == Material.AIR) {
            dropLocation = dropLocation.getBlock().getRelative(BlockFace.DOWN).getLocation();
        }

        return dropLocation.getBlock().getRelative(BlockFace.UP).getLocation();
    }
}
