package fr.synchroneyes.special_events.halloween2024;

import fr.synchroneyes.special_events.SpecialEvent;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;

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
        return true;
    }
}
