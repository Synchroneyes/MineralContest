package fr.synchroneyes.mineral.Exception;

/**
 * Exception levée lorsque l'on doit quitter un evenement depuis une fonction externe
 */
public class EventAlreadyHandledException extends Exception {
    public EventAlreadyHandledException(String message) {
        super(message);
    }

    public EventAlreadyHandledException() {
        super();
    }
}
