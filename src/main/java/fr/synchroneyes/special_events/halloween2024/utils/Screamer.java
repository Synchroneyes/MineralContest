package fr.synchroneyes.special_events.halloween2024.utils;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.DeathAnimations.Animations.GroundFreezingAnimation;
import fr.synchroneyes.mineral.mineralcontest;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;



public class Screamer {

    /**
     * Plays a warden screamer effect for the specified player.
     * This includes playing a sound, applying blindness and slowness effects,
     * sending a title message, striking lightning, and spawning a warden entity.
     *
     * @param player the player to play the warden screamer effect for
     */
    public static void playWarden(Player player) {
        player.playSound(player.getLocation(), "minecraft:entity.enderman.scream", 1, 1);
        player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.BLINDNESS, 20*5, 1));
        player.sendTitle("§4§l???", "§c§lAttention à toi...", 20, 20, 20);
        Location playerLocation = player.getLocation();
        player.getWorld().strikeLightningEffect(playerLocation);

        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*5, 1000));

        Location spawnLocation = player.getLocation().add(player.getLocation().getDirection().multiply(4));
        Warden warden = (Warden) player.getWorld().spawnEntity(spawnLocation, EntityType.WARDEN);
        warden.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 10));
        warden.setAI(false);
        warden.setTarget(player);

        GroundFreezingAnimation animationMort = new GroundFreezingAnimation();
        animationMort.setSendNotification(false);
        animationMort.playAnimation(player);

        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, warden::remove, 20 * 5);


    }


    /**
     * Plays a creeper screamer effect for the specified player.
     * This includes playing a sound, applying blindness effect,
     * sending a title message, and spawning 10 creepers around the player.
     *
     * @param player the player to play the creeper screamer effect for
     */
    public static void playCreeper(Player player) {
        player.playSound(player.getLocation(), "minecraft:entity.enderman.scream", 1, 1);
        player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.BLINDNESS, 20*5, 1));
        player.sendTitle("§4§l???", "§c§lMmmh, ça sent la poudre non?", 20, 20, 20);
        Location playerLocation = player.getLocation();



        for (int i = 0; i < 10; i++) {
            double angle = 2 * Math.PI * i / 10;
            double xOffset = Math.cos(angle) * 2;
            double zOffset = Math.sin(angle) * 2;
            Location spawnLocation = playerLocation.clone().add(xOffset, 0, zOffset);
            Creeper creeper = (Creeper) player.getWorld().spawnEntity(spawnLocation, EntityType.CREEPER);
            creeper.setMaxFuseTicks(20*60);
            creeper.setFuseTicks(20*10);
            creeper.ignite();
            creeper.setExplosionRadius(0);
            creeper.setAI(false);
            creeper.setGlowing(true);
            creeper.setTarget(player);
            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, creeper::remove, 20 * 5);
        }
    }

    /**
     * Plays an anvil rain screamer effect for the specified player.
     * This includes playing a sound, sending a title message, and spawning 100 falling anvil blocks around the player.
     *
     * @param player the player to play the anvil rain screamer effect for
     */
    public static void playAnvilRain(Player player) {
        List<FallingBlock> anvilList = new LinkedList<>();

        int radius = 10;
        int duration = 20 * 10; // 10 seconds

        player.sendTitle("§4§l???", "§c§lAttention à la pluie...", 20, 20, 20);


        BukkitTask task = Bukkit.getScheduler().runTaskTimer(mineralcontest.plugin, () -> {
            Location playerLocation = player.getLocation();
            for (int i = 0; i < 100; i++) {
                double angle = 2 * Math.PI * Math.random();
                double distance = Math.random() * radius;
                double xOffset = Math.cos(angle) * distance;
                double zOffset = Math.sin(angle) * distance;
                Location spawnLocation = playerLocation.clone().add(xOffset, 30, zOffset);
                FallingBlock anvil = player.getWorld().spawnFallingBlock(spawnLocation, Material.ANVIL.createBlockData());
                anvil.setDropItem(false);
                anvil.setHurtEntities(false);
                anvil.setVisualFire(true);
                anvilList.add(anvil);

                anvil.setMetadata("anvilRain", new FixedMetadataValue(mineralcontest.plugin, true));
            }
        }, 0, 20);

        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
            task.cancel();
            for(FallingBlock anvil : anvilList) {
                if (anvil.isDead() && anvil.hasMetadata("anvilRain")) {
                    anvil.getLocation().getBlock().setType(Material.AIR);
                } else {
                    anvil.remove();
                }
            }
        }, duration);

    }

    /**
     * Plays a monster roulette screamer effect for the specified player.
     * This includes applying blindness effect, sending a title message,
     * and spawning a random monster every 5 ticks for 5 seconds.
     *
     * @param player the player to play the monster roulette screamer effect for
     */
    public static void playMonsterRoulette(Player player) {

        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*5, 1));
        player.sendTitle("§4§l???", "§c§lAttention à la roulette...", 20, 20, 20);

        List<EntityType> monsters = toList(
                EntityType.BLAZE,
                EntityType.CAVE_SPIDER,
                EntityType.CREEPER,
                EntityType.DROWNED,
                EntityType.ELDER_GUARDIAN,
                EntityType.ENDERMAN,
                EntityType.ENDERMITE,
                EntityType.EVOKER,
                EntityType.GIANT,
                EntityType.GUARDIAN,
                EntityType.HUSK,
                EntityType.ILLUSIONER,
                EntityType.PIGLIN,
                EntityType.PIGLIN_BRUTE,
                EntityType.PILLAGER,
                EntityType.RAVAGER,
                EntityType.SKELETON,
                EntityType.STRAY,
                EntityType.VEX,
                EntityType.VINDICATOR,
                EntityType.WARDEN,
                EntityType.WITCH,
                EntityType.WITHER,
                EntityType.WITHER_SKELETON
        );

        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {

            BukkitTask task = Bukkit.getScheduler().runTaskTimer(mineralcontest.plugin, new Runnable() {
                private int ticks = 0;
                private Entity currentMonster = null;

                @Override
                public void run() {
                    if (ticks >= 100) {
                        if (currentMonster != null) {
                            currentMonster.remove();
                        }
                        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
                            EntityType finalMonster = toList(EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER).get((int) (Math.random() * 3));
                            player.getWorld().spawnEntity(player.getLocation().add(player.getLocation().getDirection().multiply(2)).add(0, 1, 0), finalMonster);                        }, 1);
                        return;
                    }

                    if (currentMonster != null) {
                        currentMonster.remove();
                    }

                    EntityType monsterType = monsters.get((int) (Math.random() * monsters.size()));
                    currentMonster = player.getWorld().spawnEntity(player.getLocation().add(player.getLocation().getDirection().multiply(2)).add(0, 1, 0), monsterType);
                    ((Monster) currentMonster).setAI(false);
                    ((Monster) currentMonster).setTarget(player);
                    ticks += 5;

                }
            }, 0, 5);

            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
                player.removePotionEffect(PotionEffectType.SLOW);
                task.cancel();
            }, 20 * 5);
        }, 0);


    }

    private static List<EntityType> toList(EntityType... types) {
        List<EntityType> list = new ArrayList<>();
        list.addAll(Arrays.asList(types));
        return list;
    }

    public static void playCreeperSound(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1, 1);
    }

    public static void playZombieSound(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_AMBIENT, 1, 1);
    }

    public static void playWardenSound(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_AMBIENT, 1, 1);
    }

    public static void playThunderSound(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
    }

    public static void playEndermanSound(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1, 1);
    }

    /**
     * Plays a random screamer effect for the specified player.
     * This includes applying blindness effect, sending a title message,
     * and playing a random screamer effect.
     *
     * @param effectName Valid values: "playWarden", "playCreeper", "playAnvilRain", "playMonsterRoulette"
     * @param partie the game being played
     */
    public static void playEffectToAllPlayers(String effectName, Game partie) {
        try {
            Method method = Screamer.class.getMethod(effectName, Player.class);
            for(Player joueur : partie.groupe.getPlayers()) {
                    method.invoke(null, joueur);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
