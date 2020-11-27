package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.Coffre.AutomatedChestAnimation;
import fr.synchroneyes.mineral.Core.Game.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MCAutomatedChestTimeOverEvent extends MCEvent {


    private AutomatedChestAnimation automatedChest;


    public MCAutomatedChestTimeOverEvent(AutomatedChestAnimation automatedChestAnimation) {
        this.automatedChest = automatedChestAnimation;
    }

    public AutomatedChestAnimation getAutomatedChest() {
        return automatedChest;
    }

}
