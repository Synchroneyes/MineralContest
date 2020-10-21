package fr.synchroneyes.mineral.Core.Boss.BossType;

import fr.synchroneyes.mineral.Core.Boss.Boss;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;

public class CrazyZombie extends Boss {
    @Override
    public String getName() {
        return "CrazyZombie";
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



        for(int i = 0; i < 3; ++i) {
            Zombie spider = (Zombie) this.entity.getWorld().spawnEntity(this.entity.getLocation(), EntityType.ZOMBIE);
            if(spider.isBaby())spider.setAdult();

        }

        this.entity.getWorld().strikeLightningEffect(this.entity.getLocation());
        this.entity.getWorld().playEffect(this.entity.getLocation(), Effect.END_GATEWAY_SPAWN, 1);

    }

    @Override
    public int getSpecialAttackTimer() {
        return 10;
    }

    @Override
    public int getBossBarDetectionRadius() {
        return 20;
    }

    @Override
    public void defineCustomAttributes() {
        this.entity.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK).setBaseValue(15);
    }
}
