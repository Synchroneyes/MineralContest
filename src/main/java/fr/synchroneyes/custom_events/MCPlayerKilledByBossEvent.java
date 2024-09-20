package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.Boss.Boss;
import fr.synchroneyes.mineral.Core.Game.Game;
import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * Evenement envoyé lorsqu'un joueur est tué par un boss
 */
public class MCPlayerKilledByBossEvent extends MCEvent{
    @Getter
    private Player player;
    @Getter
    private Game game;
    @Getter
    private Boss boss;

    /**
     * Constructeur
     * @param player
     * @param game
     * @param boss
     */
    public MCPlayerKilledByBossEvent(Player player, Game game, Boss boss) {
        this.player = player;
        this.game = game;
        this.boss = boss;
    }

}
