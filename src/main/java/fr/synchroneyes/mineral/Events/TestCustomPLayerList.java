package fr.synchroneyes.mineral.Events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class TestCustomPLayerList implements Listener {

    @EventHandler
    public void playerList(ServerListPingEvent event) {
        event.setMotd("TEST ! :)");
        event.setMaxPlayers(64);
        Bukkit.broadcastMessage(event.getAddress() + "");
    }
}
