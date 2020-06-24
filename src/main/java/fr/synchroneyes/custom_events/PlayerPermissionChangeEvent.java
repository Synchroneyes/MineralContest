package fr.synchroneyes.custom_events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerPermissionChangeEvent extends Event {

    private Player player;
    private String oldPermission;
    private String newPermission;

    private static final HandlerList handlers = new HandlerList();


    public PlayerPermissionChangeEvent(Player p, String old, String new_p) {
        this.player = p;
        this.oldPermission = old;
        this.newPermission = new_p;
    }


    public Player getPlayer() {
        return player;
    }

    public String getOldPermission() {
        return oldPermission;
    }

    public String getNewPermission() {
        return newPermission;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
