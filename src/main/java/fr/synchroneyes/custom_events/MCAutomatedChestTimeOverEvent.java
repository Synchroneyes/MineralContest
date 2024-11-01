package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.Coffre.AutomatedChestAnimation;
import fr.synchroneyes.mineral.Core.Game.Game;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class MCAutomatedChestTimeOverEvent extends MCEvent {


    private AutomatedChestAnimation automatedChest;

    private Player openingPlayer;


    public MCAutomatedChestTimeOverEvent(AutomatedChestAnimation automatedChestAnimation, Player openingPlayer) {
        this.automatedChest = automatedChestAnimation;
        this.openingPlayer = openingPlayer;
    }

    public AutomatedChestAnimation getAutomatedChest() {
        return automatedChest;
    }

}
