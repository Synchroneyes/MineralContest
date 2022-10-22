package fr.synchroneyes.special_events.halloween2022;

import fr.synchroneyes.mineral.Core.Boss.BossManager;
import fr.synchroneyes.mineral.Core.Boss.BossType.CrazyZombie;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.DeathAnimations.Animations.HalloweenHurricaneAnimation;
import fr.synchroneyes.mineral.mineralcontest;
import fr.synchroneyes.special_events.SpecialEvent;
import fr.synchroneyes.special_events.halloween2022.events.AirdropEvent;
import fr.synchroneyes.special_events.halloween2022.events.ArenaEvent;
import fr.synchroneyes.special_events.halloween2022.events.PlayerDeathEvent;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class HalloweenEvent extends SpecialEvent {

    private BossManager bossManager;


    @Override
    public String getEventName() {
        return ChatColor.RED + "Halloween 2022" + ChatColor.RESET;
    }

    @Override
    public String getDescription() {
        return "Cet évenement est prévu spécialement pour Halloween. Il sera actif entre le 28 octobre et le 2 novembre.";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void startEvent(Game partie) {

        List<Player> joueurs = partie.groupe.getPlayers();

        // register boss
        this.bossManager = new BossManager(partie);

        // register events
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDeathEvent(), mineralcontest.plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaEvent(), mineralcontest.plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new AirdropEvent(bossManager), mineralcontest.plugin);


        // plugin logic

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
    }
}
