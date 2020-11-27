package fr.synchroneyes.custom_events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MCPlayerRespawnEvent extends MCEvent {


    private Player joueur;


    public MCPlayerRespawnEvent(Player joueur) {
        this.joueur = joueur;
    }

    public Player getJoueur() {
        return joueur;
    }

}
