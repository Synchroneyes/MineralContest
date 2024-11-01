package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.Boss.Boss;
import lombok.Getter;
import org.bukkit.entity.Player;

public class MCBossKilledByPlayerEvent extends MCEvent{

    @Getter
    private Boss boss;
    @Getter
    private Player player;

    public MCBossKilledByPlayerEvent(Boss boss, Player player) {
        this.boss = boss;
        this.player = player;
    }

}
