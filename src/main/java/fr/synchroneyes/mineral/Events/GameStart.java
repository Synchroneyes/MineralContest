package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.custom_events.MCGameStartedEvent;
import fr.synchroneyes.mineral.Utils.Metric.SendInformation;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameStart implements Listener {

    @EventHandler
    public void onGameStart(MCGameStartedEvent event) {
        SendInformation.sendGameData(SendInformation.start, event.getGame());
    }
}
