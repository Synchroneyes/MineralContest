package fr.mineral.Events;

import fr.mineral.Settings.GameSettingsCvarOLD;
import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldLoaded implements Listener {

    @EventHandler
    public void onWorldLoaded(WorldLoadEvent event) {
        String world_name = (String) GameSettingsCvarOLD.getValueFromCVARName("world_name");
        if(event.getWorld().getName().equalsIgnoreCase(world_name)) {
            mineralcontest.plugin.pluginWorld = PlayerUtils.getPluginWorld();
            mineralcontest.plugin.defaultSpawn = event.getWorld().getSpawnLocation();
            mineralcontest.plugin.setDefaultWorldBorder();
            Bukkit.getLogger().info("[MINERALC] Default spawn location set");
            Bukkit.getLogger().info("[MINERALC] Plugin enabled for world: " + world_name);
        }
    }

}
