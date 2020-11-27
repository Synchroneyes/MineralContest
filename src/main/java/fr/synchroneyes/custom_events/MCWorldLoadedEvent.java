package fr.synchroneyes.custom_events;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MCWorldLoadedEvent extends MCEvent {

    private World monde;
    private String world_name = "";

    public MCWorldLoadedEvent(String world_name, World w) {
        this.monde = w;
        this.world_name = world_name;
    }


    public World getMonde() {
        return monde;
    }

    public String getWorld_name() {
        return world_name;
    }
}
