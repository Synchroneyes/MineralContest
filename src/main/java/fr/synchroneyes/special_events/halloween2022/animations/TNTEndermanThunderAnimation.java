package fr.synchroneyes.special_events.halloween2022.animations;

import fr.synchroneyes.mineral.DeathAnimations.DeathAnimation;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.type.TNT;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.metadata.FixedMetadataValue;

public class TNTEndermanThunderAnimation extends DeathAnimation {
    @Override
    public String getAnimationName() {
        return "Halloween 2022 - Death Animation";
    }

    @Override
    public Material getIcone() {
        return Material.PUMPKIN;
    }

    @Override
    public void playAnimation(LivingEntity player) {
        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> player.getWorld().strikeLightningEffect(player.getLocation()), 1);
        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> player.getWorld().strikeLightningEffect(player.getLocation()), 10);
        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> player.getWorld().strikeLightningEffect(player.getLocation()), 15);
        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> player.getWorld().strikeLightningEffect(player.getLocation()), 20);

        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
            Enderman enderman = (Enderman) player.getWorld().spawnEntity(player.getLocation(), EntityType.ENDERMAN);
            enderman.setAI(false);
            enderman.setGlowing(true);
            enderman.setCustomNameVisible(true);
            enderman.setCustomName("Âme de " + player.getName());
            enderman.setHealth(40);
            enderman.setMetadata("isBoss", new FixedMetadataValue(mineralcontest.plugin, true));

            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, enderman::remove, 5*20);

            // spawn tnt
            int rayon = 2;
            for(double x = player.getLocation().getX()-rayon; x < player.getLocation().getX()+rayon; x++) {
                for(double z = player.getLocation().getZ()-rayon; z < player.getLocation().getZ()+rayon; z++){
                    TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(new Location(player.getWorld(), x, player.getLocation().getY(), z), EntityType.PRIMED_TNT);
                    tnt.setFuseTicks(10*20);
                    tnt.setSilent(false);
                    tnt.setMetadata("isBoss", new FixedMetadataValue(mineralcontest.plugin, true));
                    Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, tnt::remove, 5*20);
                }
            }

        }, 1);

    }
}
