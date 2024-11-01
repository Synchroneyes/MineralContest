package fr.synchroneyes.special_events.halloween2024;

import fr.synchroneyes.special_events.SpecialEvent;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Halloween2024Event extends SpecialEvent {
    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(new OnGameStart(), mineralcontest.plugin);
    }

    @Override
    public String getEventName() {
        return "Halloween";
    }

    @Override
    public boolean isEventEnabled() {
        LocalDateTime startDate = LocalDateTime.of(2024, 10, 30, 1, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 11, 10, 23, 53);

        LocalDateTime currentDate = LocalDateTime.now(ZoneId.systemDefault());

        return currentDate.isAfter(startDate) && currentDate.isBefore(endDate);
    }
}
