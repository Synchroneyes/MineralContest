package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.mineral.Utils.ErrorReporting.Error;
import fr.synchroneyes.mineral.Utils.Player.PlayerUtils;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldLoaded implements Listener {

    @EventHandler
    public void onWorldLoaded(WorldLoadEvent event) {

        if (mineralcontest.plugin.pluginWorld == null) {
            String world_name = "";
            try {
                world_name = mineralcontest.getPluginConfigValue("world_name").toString();
            } catch (Exception e) {
                e.printStackTrace();
                Error.Report(e, null);
            }

            if (event.getWorld().getName().equalsIgnoreCase(world_name)) {
                mineralcontest.plugin.pluginWorld = PlayerUtils.getPluginWorld();
                mineralcontest.plugin.defaultSpawn = event.getWorld().getSpawnLocation();
                //mineralcontest.plugin.setDefaultWorldBorder();
                Bukkit.getLogger().info("[MINERALC] Default spawn location set");
                Bukkit.getLogger().info("[MINERALC] Plugin enabled for world: " + world_name);
            }
        }


    }

}
