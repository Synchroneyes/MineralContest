package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.Teams.Equipe;
import org.bukkit.event.Cancellable;

/**
 * Event appelé lorsqu'un joueur quitte une équipe
 */
public class MCPlayerLeaveTeamEvent extends MCEvent implements Cancellable {


    private boolean cancelled;

    private final MCPlayer mcPlayer;
    private final Equipe oldTeam;

    public MCPlayerLeaveTeamEvent(MCPlayer mcPlayer, Equipe joinedTeam) {
        this.mcPlayer = mcPlayer;
        this.oldTeam = joinedTeam;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = true;
    }

    public MCPlayer getMcPlayer() {
        return mcPlayer;
    }

    public Equipe getOldTeam() {
        return oldTeam;
    }
}
