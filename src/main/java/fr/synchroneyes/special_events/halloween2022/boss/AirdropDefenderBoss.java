package fr.synchroneyes.special_events.halloween2022.boss;

import fr.synchroneyes.mineral.Core.Boss.Boss;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class AirdropDefenderBoss extends Boss {

    @Override
    public String getName() {
        return "Défenseur du largage aérien";
    }

    @Override
    public double getSanteMax() {
        return 500;
    }

    @Override
    public double getDegatsParAttaque() {
        return 5;
    }

    @Override
    public EntityType getMobType() {
        return EntityType.RAVAGER;
    }

    @Override
    public int getRayonDetectionJoueur() {
        return 10;
    }

    @Override
    public void onPlayerTarget(Player targetedPlayer) {

    }

    @Override
    public List<ItemStack> getKillRewards() {
        ArrayList<ItemStack> items = new ArrayList<>();
        items.add(new ItemStack(Material.EMERALD, 1));
        items.add(new ItemStack(Material.DIAMOND, 15));
        items.add(new ItemStack(Material.REDSTONE, 64));
        items.add(new ItemStack(Material.REDSTONE, 64));
        return items;
    }

    @Override
    public boolean shouldEntityGlow() {
        return true;
    }

    @Override
    public BarColor getBossBarColor() {
        return BarColor.PINK;
    }

    @Override
    public BarStyle getBarStyle() {
        return BarStyle.SEGMENTED_20;
    }

    @Override
    public void doMobSpecialAttack() {
        this.entity.getNearbyEntities(this.getRayonDetectionJoueur(), this.getRayonDetectionJoueur(), this.getRayonDetectionJoueur()).forEach((entity) -> {
            if(entity instanceof Player){
                Player player = (Player) entity;
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 5*20, 10));
                player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 5*20, 10));
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5*20, 2));
            }
        });
    }

    @Override
    public int getSpecialAttackTimer() {
        return 15;
    }

    @Override
    public int getBossBarDetectionRadius() {
        return 30;
    }

    @Override
    public void defineCustomAttributes() {
        this.entity.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK).setBaseValue(2);
    }

    @Override
    public void onBossDeath() {

    }

    @Override
    public void onBossSpawn() {
        List<Player> joueurs_cible = entity.getWorld().getPlayers();

        int duree_annonce = 5;

        // Pour chaque joueur du monde
        for(Player joueur : joueurs_cible) {
            // On joue un son
            joueur.playSound(joueur.getLocation(), Sound.AMBIENT_CAVE, 1,1);

            // On en joue un second un peu après
            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> joueur.playSound(joueur.getLocation(), Sound.ENTITY_WITCH_CELEBRATE, 1,1), 20);

            // On aveugle temporairement le joueur pendant 3 secondes
            joueur.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duree_annonce*20, 10));
            joueur.sendMessage(ChatColor.GOLD + "???: " + ChatColor.RESET + "Vous pensez pouvoir récupérer ce largage facilement? On verra ça ...");

        }
    }

    @Override
    protected void performAnnouncement() {

    }

    @Override
    protected boolean canSpawnMobs() {
        return false;
    }
}
