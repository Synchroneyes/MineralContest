package fr.synchroneyes.custom_events;

import fr.synchroneyes.custom_plugins.CustomPlugin;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MCPluginLoaded extends Event {

    private static final HandlerList handlers = new HandlerList();
    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private CustomPlugin plugin;

    public MCPluginLoaded(CustomPlugin plugin) {
        this.plugin = plugin;
    }

    public CustomPlugin getPlugin() {
        return plugin;
    }
}
