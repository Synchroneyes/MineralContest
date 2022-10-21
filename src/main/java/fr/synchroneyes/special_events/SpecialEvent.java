package fr.synchroneyes.special_events;

import fr.synchroneyes.mineral.Core.Game.Game;

/**
 * Classe qui représente un évenement sur le plugin
 */
public abstract class SpecialEvent {

    /**
     * Défini le nom de l'evenement
     * @return le nom de l'evenement
     */
    public abstract String getEventName();

    /**
     * Défini la description de l'evenement
     * @return la description de l'evenement
     */
    public abstract String getDescription();

    /**
     * Défini si l'evenement est actif ou non
     * @return event activé ou non
     */
    public abstract boolean isEnabled();

    /**
     * Fonction d'entrée d'un evenement, executé dans un nouveau thread
     */
    public abstract void startEvent(Game partie);

}
