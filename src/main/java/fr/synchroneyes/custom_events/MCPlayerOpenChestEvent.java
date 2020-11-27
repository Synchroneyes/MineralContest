package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.Coffre.AutomatedChestAnimation;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MCPlayerOpenChestEvent extends MCEvent {

    private Player joueur;

    private AutomatedChestAnimation coffre;


    public MCPlayerOpenChestEvent(AutomatedChestAnimation coffre, Player joueur) {
        this.joueur = joueur;
        this.coffre = coffre;
    }

    public Player getJoueur() {
        return joueur;
    }



    public AutomatedChestAnimation getCoffre() {
        return coffre;
    }
}
