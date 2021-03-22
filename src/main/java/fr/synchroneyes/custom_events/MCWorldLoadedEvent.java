package fr.synchroneyes.custom_events;

import fr.synchroneyes.groups.Core.Groupe;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event appelé lorsqu'un monde mineral contest est chargé
 */
public class MCWorldLoadedEvent extends MCEvent {

    private World monde;
    private String world_name = "";
    private Groupe groupe;

    public MCWorldLoadedEvent(String world_name, World w, Groupe groupe) {
        this.monde = w;
        this.world_name = world_name;
        this.groupe = groupe;
    }


    public World getMonde() {
        return monde;
    }

    public String getWorld_name() {
        return world_name;
    }

    public Groupe getGroupe() {
        return groupe;
    }
}
