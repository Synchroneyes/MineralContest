package fr.synchroneyes.mineral.Core.Boss.BossType;

import fr.synchroneyes.mineral.Core.Boss.Boss;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CrazyZombie extends Boss {

    private int maxSbire = 5;

    private List<LivingEntity> list_sbire;

    public CrazyZombie() {
        list_sbire = new ArrayList<>();
    }


    @Override
    public String getName() {
        return "CrazyZombie";
    }

    @Override
    public double getSanteMax() {
        return 250;
    }

    @Override
    public double getDegatsParAttaque() {
        return 10;
    }

    @Override
    public EntityType getMobType() {
        return EntityType.ZOMBIE;
    }

    @Override
    public int getRayonDetectionJoueur() {
        return 5;
    }

    @Override
    public void onPlayerTarget(Player targetedPlayer) {
    }

    @Override
    public List<ItemStack> getKillRewards() {
        List<ItemStack> items = new LinkedList<>();

        for(int i = 0; i < 10; ++i)
            items.add(new ItemStack(Material.EMERALD, 64));

        return items;
    }

    @Override
    public Material getChestType() {
        return Material.CHEST;
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

        if(list_sbire.size() >= maxSbire) return;

        for(int i = 0; i < 3; ++i) {

            if(list_sbire.size() >= maxSbire) break;

            ZombieVillager zombieSbire = (ZombieVillager) this.entity.getWorld().spawnEntity(this.entity.getLocation(), EntityType.ZOMBIE_VILLAGER);
            if(zombieSbire.isBaby())zombieSbire.setAdult();
            zombieSbire.setCustomNameVisible(true);
            zombieSbire.setCustomName("Sbire");
            zombieSbire.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getSanteMax()/3);
            zombieSbire.setHealth(getSanteMax()/3);

            zombieSbire.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(getDegatsParAttaque()/3);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if(zombieSbire.isDead()) {
                        this.cancel();
                    }

                    zombieSbire.setCustomName("Sbire " + ((int)zombieSbire.getHealth()) + ChatColor.RED + "♥" + ChatColor.RESET);
                }
            }.runTaskTimer(mineralcontest.plugin, 0, 5);

            list_sbire.add(zombieSbire);

        }

        this.entity.getWorld().strikeLightningEffect(this.entity.getLocation());
        this.entity.getWorld().playEffect(this.entity.getLocation(), Effect.END_GATEWAY_SPAWN, 1);

    }

    @Override
    public int getSpecialAttackTimer() {
        return 15;
    }

    @Override
    public int getBossBarDetectionRadius() {
        return 20;
    }

    @Override
    public void defineCustomAttributes() {
        this.entity.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK).setBaseValue(15);
    }

    @Override
    public void onBossDeath() {
        for(LivingEntity sbire : list_sbire)
            sbire.setHealth(0);
    }
}
