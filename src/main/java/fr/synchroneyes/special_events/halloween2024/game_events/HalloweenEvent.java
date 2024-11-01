package fr.synchroneyes.special_events.halloween2024.game_events;

import fr.synchroneyes.special_events.halloween2024.utils.HalloweenTitle;
import fr.synchroneyes.mineral.Core.Game.Game;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class HalloweenEvent {

    @Getter
    private Game partie;


    public HalloweenEvent(Game partie) {
        this.partie = partie;
    }

    public abstract String getEventName();
    public abstract void executionContent();
    public abstract void beforeExecute();
    public abstract void afterExecute();

    public abstract String getEventTitle();
    public abstract String getEventDescription();
    public abstract boolean isTextMessageNotificationEnabled();

    public abstract boolean isNotificationDelayed();


    public void execute() {

        Bukkit.getLogger().info("[MineralContest][Halloween2024] Executing event: " + getEventName());
        beforeExecute();
        if(!isNotificationDelayed()) sendEventNotification();
        executionContent();
        afterExecute();
    }

    public void sendEventNotification() {
        for(Player player : partie.groupe.getPlayers()) {
            HalloweenTitle.sendTitle(player, getEventTitle(), getEventDescription(), 1, 5, 1, isTextMessageNotificationEnabled());
        }
    }

}
