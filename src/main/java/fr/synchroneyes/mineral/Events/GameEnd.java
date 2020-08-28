package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.custom_events.MCGameEndEvent;
import fr.synchroneyes.mineral.Utils.Metric.SendInformation;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class GameEnd implements Listener {

    @EventHandler
    public void onGameEnd(MCGameEndEvent endEvent) {
        SendInformation.sendGameData(SendInformation.ended, endEvent.getGame());
    }
}
