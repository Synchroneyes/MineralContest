package fr.synchroneyes.special_events;

import fr.synchroneyes.special_events.halloween2024.Halloween2024Event;
import org.bukkit.Bukkit;

import java.util.LinkedList;
import java.util.List;

public class SpecialEventManager {
    private List<SpecialEvent> eventList;

    public SpecialEventManager() {
        this.eventList = new LinkedList<>();
    }

    public void init() {
        this.eventList.add(new Halloween2024Event());


        for(SpecialEvent event : this.eventList) {
            if(event.isEventEnabled()) {
                Bukkit.getLogger().info("[MineralContest] Initialising event: " + event.getEventName());
                event.init();
            }
        }
    }


}
