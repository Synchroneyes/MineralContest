package fr.mineral.Events;

import fr.mineral.Core.GameSettingsCvar;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldLoaded implements Listener {

    @EventHandler
    public void onWorldLoaded(WorldLoadEvent event) {
        String world_name = (String) GameSettingsCvar.getValueFromCVARName("world_name");
        if(event.getWorld().getName().equalsIgnoreCase(world_name)) {
            mineralcontest.plugin.pluginWorld = event.getWorld();
        }
    }

}
