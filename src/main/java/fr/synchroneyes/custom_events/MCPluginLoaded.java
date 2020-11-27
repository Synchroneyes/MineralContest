package fr.synchroneyes.custom_events;

import fr.synchroneyes.custom_plugins.CustomPlugin;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MCPluginLoaded extends MCEvent {

    private CustomPlugin plugin;

    public MCPluginLoaded(CustomPlugin plugin) {
        this.plugin = plugin;
    }

    public CustomPlugin getPlugin() {
        return plugin;
    }
}
