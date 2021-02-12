package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class PlayerLocationSaverLoop implements Runnable{

    int secondsDelay = 2;


    private JavaPlugin plugin;

    public PlayerLocationSaverLoop(JavaPlugin plugin, int delayBetweenEachCheck) {
        this.plugin = plugin;
        this.secondsDelay = delayBetweenEachCheck;
    }

    // Variable qui définit le temps entre chaque vérif

    @Override
    public synchronized void run() {

        new BukkitRunnable() {
            @Override
            public synchronized void run() {
                // On regarde chaque joueur
                for (Player online_player : plugin.getServer().getOnlinePlayers()) {
                    // Si le joueur fait parti du plugin
                    MCPlayer mcPlayer = mineralcontest.plugin.getMCPlayer(online_player);
                    if(mcPlayer != null) {
                        // On update sa position
                        mcPlayer.setPlayerWorldLocation(online_player.getWorld(), online_player.getLocation());
                    }
                }
            }
        }.runTaskTimer(plugin, 0, secondsDelay * 20);
    }

}
