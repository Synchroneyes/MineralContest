package fr.synchroneyes.halloween_event;

import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

/**
 * Classe permettant de geler le temps d'un monde
 */
public class FreezeWorldTime {

    private static int midnight = 18000;

    private static int currentWorldTime = midnight;

    private static boolean isWorldFrozen = false;

    private static World frozenWorld = null;

    private static BukkitTask boucle = null;

    public static void setFrozenWorld(World w) {
        frozenWorld = w;
    }

    public static void freezeWorld() {


        if(boucle != null) {
            boucle.cancel();
        }

        frozenWorld.setTime(midnight);
        boucle = Bukkit.getScheduler().runTaskTimer(mineralcontest.plugin, () -> { frozenWorld.setTime(midnight); }, 0, 10*20);
    }

    public static void unfreezeWorld() {
        boucle.cancel();
    }
}
