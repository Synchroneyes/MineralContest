package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.Game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Evenement appelé lors de la mort par un autre joueur
 */
public class PlayerDeathByPlayerEvent extends MCEvent {

    private Player playerDead;
    private Player killer;
    private Game partie;



    public PlayerDeathByPlayerEvent(Player dead, Player killer, Game partie) {
        this.playerDead = dead;
        this.killer = killer;
        this.partie = partie;
    }
    @NotNull
    public Game getPartie() {
        return partie;
    }

    @NotNull
    public Player getPlayerDead() {
        return playerDead;
    }

    @NotNull
    public Player getKiller() {
        return killer;
    }
}
