package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.Teams.Equipe;
import org.bukkit.event.Cancellable;

public class MCTeamScoreUpdated extends MCEvent implements Cancellable {

    private int oldScore;
    private int newScore;
    private Equipe equipe;


    private boolean cancelled = false;

    public MCTeamScoreUpdated(int oldScore, int newScore, Equipe equipe) {
        this.oldScore = oldScore;
        this.newScore = newScore;
        this.equipe = equipe;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }


    public int getOldScore() {
        return oldScore;
    }

    public int getNewScore() {
        return newScore;
    }

    public Equipe getEquipe() {
        return equipe;
    }
}
