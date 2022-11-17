package fr.synchroneyes.special_events.halloween2022.boss;

import fr.synchroneyes.mineral.Core.Boss.Boss;
import fr.synchroneyes.mineral.mineralcontest;
import fr.synchroneyes.special_events.halloween2022.weapons.ShulkerStick;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;


import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ArenaChestDefenderBoss extends Boss {

    private AtomicInteger cooldownAttackPlayer = new AtomicInteger(0);

    private BukkitTask cooldownTask;


    public ArenaChestDefenderBoss() {
        cooldownTask = Bukkit.getScheduler().runTaskTimer(mineralcontest.plugin, () -> {
            if(cooldownAttackPlayer.get() > 0) cooldownAttackPlayer.decrementAndGet();
            ((Wolf) this.entity).setOwner(null);

        }, 0, 20);
    }


    @Override
    public String getName() {
        return "Chien de ???";
    }

    @Override
    public double getSanteMax() {
        return 100;
    }

    @Override
    public double getDegatsParAttaque() {
        return 5;
    }

    @Override
    public EntityType getMobType() {
        return EntityType.WOLF;
    }

    @Override
    public int getRayonDetectionJoueur() {
        return 5;
    }

    @Override
    public void onPlayerTarget(Player targetedPlayer) {

        if(cooldownAttackPlayer.get() > 0) return;

        ShulkerBullet shulker = (ShulkerBullet) this.entity.getWorld().spawnEntity(this.entity.getLocation(), EntityType.SHULKER_BULLET);
        shulker.setTarget(targetedPlayer);
        shulker.getVelocity().multiply(3);

        this.entity.setTarget(targetedPlayer);

        ((Wolf)this.entity).setAngry(true);

        cooldownAttackPlayer.set(5);

    }

    @Override
    public List<ItemStack> getKillRewards() {
        List<ItemStack> items = new LinkedList<>();
        items.add(ShulkerStick.getItem());
        return items;
    }

    @Override
    public boolean shouldEntityGlow() {
        return true;
    }

    @Override
    public BarColor getBossBarColor() {
        return BarColor.RED;
    }

    @Override
    public BarStyle getBarStyle() {
        return BarStyle.SOLID;
    }

    @Override
    public void doMobSpecialAttack() {
        this.entity.getNearbyEntities(this.getRayonDetectionJoueur(), this.getRayonDetectionJoueur(), this.getRayonDetectionJoueur()).forEach((entity) -> {
            if(entity instanceof Player){
                ShulkerBullet shulker = (ShulkerBullet) this.entity.getWorld().spawnEntity(this.entity.getLocation(), EntityType.SHULKER_BULLET);
                shulker.setTarget(entity);
                shulker.getVelocity().multiply(3);
            }
        });
    }

    @Override
    public int getSpecialAttackTimer() {
        return 10;
    }

    @Override
    public int getBossBarDetectionRadius() {
        return 10;
    }

    @Override
    public void defineCustomAttributes() {

    }

    @Override
    public void onBossDeath() {
        this.cooldownTask.cancel();
    }

    @Override
    public void onBossSpawn() {
        ((Wolf)this.entity).setAngry(true);
    }

    @Override
    protected void performAnnouncement() {

    }

    @Override
    protected boolean canSpawnMobs() {
        return false;
    }
}
