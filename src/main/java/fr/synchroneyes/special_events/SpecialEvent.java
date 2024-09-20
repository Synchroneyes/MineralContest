package fr.synchroneyes.special_events;

public abstract class SpecialEvent {
    public abstract void init();
    public abstract String getEventName();
    public abstract boolean isEventEnabled();
}
