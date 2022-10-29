package fr.synchroneyes.special_events.halloween2022;

import fr.synchroneyes.mineral.Core.Boss.BossManager;
import fr.synchroneyes.mineral.Core.Boss.BossType.CrazyZombie;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.DeathAnimations.Animations.GroundFreezingAnimation;
import fr.synchroneyes.mineral.DeathAnimations.Animations.HalloweenHurricaneAnimation;
import fr.synchroneyes.mineral.mineralcontest;
import fr.synchroneyes.special_events.SpecialEvent;
import fr.synchroneyes.special_events.halloween2022.boss.PillagerBoss;
import fr.synchroneyes.special_events.halloween2022.boss.PillagerBrotherBoss;
import fr.synchroneyes.special_events.halloween2022.events.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HalloweenEvent extends SpecialEvent {

    private BossManager bossManager;


    @Override
    public String getEventName() {
        return ChatColor.RED + "Halloween 2022" + ChatColor.RESET;
    }

    @Override
    public String getDescription() {
        return "Cet évenement est prévu spécialement pour Halloween. Il sera actif entre le 28 octobre et le 6 novembre. Pensez à activer le son de votre jeu pour profiter plainement :)";
    }

    @Override
    public boolean isEnabled() {

        long startTimeStamp = 1666908000;
        long endTimeStamp = 1667775540;

        long current = Instant.now().getEpochSecond();

        return (startTimeStamp < current && current < endTimeStamp);
    }

    @Override
    public void startEvent(Game partie) {

        List<Player> joueurs = partie.groupe.getPlayers();

        // register boss
        this.bossManager = new BossManager(partie);

        // register events
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDeathEvent(), mineralcontest.plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaEvent(bossManager), mineralcontest.plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new AirdropEvent(bossManager), mineralcontest.plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteractEvent(), mineralcontest.plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new ArrowEvent(), mineralcontest.plugin);


        // plugin logic
        // On impose la nuit sur le serveur
        FreezeWorldTime.setFrozenWorld(partie.groupe.getMonde());
        FreezeWorldTime.freezeWorld();

        // boucle des sons, un son chaque 2 minutes
        Bukkit.getScheduler().runTaskTimer(mineralcontest.plugin, () -> partie.groupe.getPlayers().forEach(this::playRandomSound), 0, 2*60*20);

        partie.groupe.getPlayers().forEach((p) -> p.sendTitle(ChatColor.WHITE + "\u2620 " + ChatColor.RED + "Mineral" + ChatColor.RED +" Contest" + ChatColor.WHITE + " \u2620", "Mode Halloween " + ChatColor.GREEN + "activé!", 20, 5*20, 20));

        // 1st event: faire spawns des loups aggressif, ils foncent sur le joueur et sont supprimé lorsqu'ils sont à 1 bloc du joueur
        // apparition au bout de 30 secondes
        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {

            List<Entity> wolves = new ArrayList<>();

            joueurs.forEach(joueur -> {
                joueur.sendTitle(ChatColor.RED + "???", "Comment osez-vous entrer dans mon monde?", 20, 5*20, 20);
                joueur.playSound(joueur.getLocation(), Sound.ENTITY_WOLF_HOWL, 0.8f, 1);
                joueur.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5*20, 5));

                int distanceJoueur = 5;
                List<Entity> pWolves = new ArrayList<>();
                World pWorld = joueur.getWorld();
                pWolves.add(pWorld.spawnEntity(new Location(pWorld, joueur.getLocation().getX() + distanceJoueur, joueur.getLocation().getY(), joueur.getLocation().getZ() + distanceJoueur), EntityType.WOLF));
                pWolves.add(pWorld.spawnEntity(new Location(pWorld, joueur.getLocation().getX() + distanceJoueur, joueur.getLocation().getY(), joueur.getLocation().getZ() - distanceJoueur), EntityType.WOLF));
                pWolves.add(pWorld.spawnEntity(new Location(pWorld, joueur.getLocation().getX() - distanceJoueur, joueur.getLocation().getY(), joueur.getLocation().getZ() + distanceJoueur), EntityType.WOLF));
                pWolves.add(pWorld.spawnEntity(new Location(pWorld, joueur.getLocation().getX() - distanceJoueur, joueur.getLocation().getY(), joueur.getLocation().getZ() + distanceJoueur), EntityType.WOLF));

                pWolves.forEach((entity) -> {
                    Wolf wolf = (Wolf) entity;
                    wolf.setAngry(true);
                    wolf.setAdult();
                    wolf.setTarget(joueur);
                    wolf.setMetadata("isBoss", new FixedMetadataValue(mineralcontest.plugin, true));
                    wolf.setCustomName("Chien de ???");
                    wolf.setCustomNameVisible(true);
                    wolves.add(wolf);
                });
            });


            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
                wolves.forEach((entity) -> {
                    Wolf wolf = (Wolf) entity;

                    HalloweenHurricaneAnimation animation = new HalloweenHurricaneAnimation();
                    animation.playAnimation(wolf);


                    Location location = wolf.getLocation();
                    if(!wolf.isDead()) {
                        wolf.setHealth(0);
                    }


                });
            }, 10*20);
            // On tue tous les loups en vie, et on invoque une animation de mort halloween

        }, 30*20);

        // 2nd event: On gèle le sol des joueurs
        // apparition au bout de 3mn
        // on joue un son juste avant
        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
            partie.groupe.getPlayers().forEach((player) -> {
                player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 0.8f, 1);
            });
        }, 1*55*20);
        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
            partie.groupe.getPlayers().forEach((player) -> {
                player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 0.8f, 1);
            });
        }, 1*56*20);
        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {

            partie.groupe.getPlayers().forEach((player) -> {
                GroundFreezingAnimation animation = new GroundFreezingAnimation();
                animation.playAnimation(player);

                // On envoie un titre
                player.sendTitle(ChatColor.BLUE + "???", "Un coup de froid?", 20, 5*20, 20);

                // On rend les jouuers aveugle pendant 5 secondes
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5*20, 10));
            });


        }, 1*60*20);

        // 3rd event: on fait spawn un enderman sans danger devant chaque joueur
        // apparition: 6mn
        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
            partie.groupe.getPlayers().forEach((player) -> {
                // récupération position du joueur
                Enderman enderman = (Enderman) player.getWorld().spawnEntity(player.getLocation(), EntityType.ENDERMAN);
                enderman.setAI(false);
                enderman.setTarget(player);
                enderman.setCustomName(ChatColor.RED + "???");
                enderman.setGlowing(true);

                // on joue un son strident
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 0.8f, 1);

                // on envoie un message au joueur
                player.sendTitle(ChatColor.GOLD + "???", "@#&$`£*¨^?/_", 20, 20*5, 20);
            });
        }, 6*60*20);


        // 4eme event - On fait spawn des chauves souris où le joueur est, une araignée, et du bruit
        // apparition: 11mn
        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
            partie.groupe.getPlayers().forEach((player) -> {
                HalloweenHurricaneAnimation animation = new HalloweenHurricaneAnimation();
                animation.playAnimation(player);

                // araignée
                Spider spider = (Spider) player.getWorld().spawnEntity(player.getLocation(), EntityType.SPIDER);
                spider.setTarget(player);
                spider.setHealth(2);
                spider.setCustomName(ChatColor.RED + "???");

                // bruit
                player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.8f, 1);
            });
        }, 11*60*20);

        // 5eme event - grosse pluie
        // apparition: 15mn
        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
            partie.groupe.getMonde().setStorm(true);
            partie.groupe.getMonde().setThundering(true);

            partie.groupe.sendToEveryone(ChatColor.RED + "???: " + ChatColor.RESET + "Les signaux que je vous ai envoyé jusque là ne vous suffisent pas? Vous en voulez plus?");
        }, 15*60*20);

        // 6eme event - attaque de boss pillager
        // apparition: 20mn
        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
            partie.groupe.getPlayers().forEach((player) -> {
                player.playSound(player.getLocation(), Sound.ENTITY_PILLAGER_AMBIENT, 0.8f, 1);
                bossManager.spawnNewBoss(player.getLocation(), new PillagerBoss());
                player.sendTitle(ChatColor.RED + "???", "Un petit cadeau de ma part ...", 20, 5*20, 20);
            });
        }, 20*60*20);

        // 7eme event: réapparition de loups
        // apparition: 32mn
        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {

            List<Entity> wolves = new ArrayList<>();

            joueurs.forEach(joueur -> {
                joueur.sendTitle(ChatColor.RED + "???", "Comment osez-vous entrer dans mon monde?", 20, 5*20, 20);
                joueur.playSound(joueur.getLocation(), Sound.ENTITY_WOLF_HOWL, 0.8f, 1);
                joueur.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5*20, 2));

                int distanceJoueur = 5;
                List<Entity> pWolves = new ArrayList<>();
                World pWorld = joueur.getWorld();
                pWolves.add(pWorld.spawnEntity(new Location(pWorld, joueur.getLocation().getX() + distanceJoueur, joueur.getLocation().getY(), joueur.getLocation().getZ() + distanceJoueur), EntityType.WOLF));
                pWolves.add(pWorld.spawnEntity(new Location(pWorld, joueur.getLocation().getX() + distanceJoueur, joueur.getLocation().getY(), joueur.getLocation().getZ() - distanceJoueur), EntityType.WOLF));
                pWolves.add(pWorld.spawnEntity(new Location(pWorld, joueur.getLocation().getX() - distanceJoueur, joueur.getLocation().getY(), joueur.getLocation().getZ() + distanceJoueur), EntityType.WOLF));
                pWolves.add(pWorld.spawnEntity(new Location(pWorld, joueur.getLocation().getX() - distanceJoueur, joueur.getLocation().getY(), joueur.getLocation().getZ() + distanceJoueur), EntityType.WOLF));

                pWolves.forEach((entity) -> {
                    Wolf wolf = (Wolf) entity;
                    wolf.setAngry(true);
                    wolf.setAdult();
                    wolf.setTarget(joueur);
                    wolf.setMetadata("isBoss", new FixedMetadataValue(mineralcontest.plugin, true));
                    wolf.setCustomName("Chien de ???");
                    wolf.setCustomNameVisible(true);
                    wolves.add(wolf);
                });
            });


            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
                wolves.forEach((entity) -> {
                    Wolf wolf = (Wolf) entity;

                    HalloweenHurricaneAnimation animation = new HalloweenHurricaneAnimation();
                    animation.playAnimation(wolf);


                    Location location = wolf.getLocation();
                    if(!wolf.isDead()) {
                        wolf.setHealth(0);
                    }


                });
            }, 10*20);
            // On tue tous les loups en vie, et on invoque une animation de mort halloween

        }, 32*60*20);

        // 8eme event: On fait tomber le joueur dans le vide
        // apparition: 36mn
        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
            partie.groupe.getPlayers().forEach((player) -> {
                int defaultXLocation = player.getLocation().getBlockX();
                int defaultYLocation = player.getLocation().getBlockY();
                int defaultZLocation = player.getLocation().getBlockZ();


                player.teleport(new Location(player.getWorld(), defaultXLocation, 320, defaultZLocation));
                player.playSound(player.getLocation(), Sound.ITEM_ELYTRA_FLYING, 0.8f, 1);
                player.sendMessage(ChatColor.RED + "???" + ChatColor.RESET + ": ARRÊTE CETTE PARTIE !!");
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20*6, 1));



                Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
                    player.setVelocity(new Vector(0,0,0));
                    player.setFallDistance(0F);
                    player.teleport(new Location(player.getWorld(), defaultXLocation, defaultYLocation, defaultZLocation));
                    player.sendTitle(ChatColor.RED + "???", "T'as eu peur, avoue?", 20, 20*5, 20);
                }, 20*5);
            });
        }, 36*60*20);

        // 9eme event: apparition du frère du pillager
        // apparition: 41mn - avant les poulets
        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
            partie.groupe.getPlayers().forEach((player) -> {

                player.getWorld().strikeLightningEffect(player.getLocation());
                bossManager.spawnNewBoss(player.getLocation(), new PillagerBrotherBoss());
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*3, 10));
            });
        }, 41*60*20);
    }

    private void playRandomSound(Player joueur) {
        Sound[] sounds = new Sound[]{
                Sound.ENTITY_ENDERMAN_SCREAM,
                Sound.AMBIENT_CAVE,
                Sound.ENTITY_WOLF_HOWL,
                Sound.ENTITY_ENDERMAN_TELEPORT,
                Sound.ENTITY_BAT_TAKEOFF,
                Sound.BLOCK_GLASS_BREAK,
                Sound.ENTITY_TNT_PRIMED,
                Sound.ENTITY_ZOMBIE_INFECT,
                Sound.ENTITY_ZOMBIE_AMBIENT,
                Sound.ENTITY_GHAST_SCREAM,
                Sound.ENTITY_GHAST_SHOOT,
                Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,
                Sound.ENTITY_GENERIC_BURN,
                Sound.ENTITY_LIGHTNING_BOLT_THUNDER
        };

        joueur.playSound(joueur.getLocation(), sounds[new Random().nextInt(sounds.length)], 0.8f, 1);
    }
}
