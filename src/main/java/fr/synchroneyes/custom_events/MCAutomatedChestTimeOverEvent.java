package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.Coffre.AutomatedChestAnimation;
import fr.synchroneyes.mineral.Core.Game.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MCAutomatedChestTimeOverEvent extends Event {


    private AutomatedChestAnimation automatedChest;

    private static final HandlerList handlers = new HandlerList();

    public MCAutomatedChestTimeOverEvent(AutomatedChestAnimation automatedChestAnimation) {
        this.automatedChest = automatedChestAnimation;
    }

    public AutomatedChestAnimation getAutomatedChest() {
        return automatedChest;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
