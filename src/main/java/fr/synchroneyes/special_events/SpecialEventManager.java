package fr.synchroneyes.special_events;

import fr.synchroneyes.custom_events.MCGameStartedEvent;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.mineralcontest;
import fr.synchroneyes.special_events.halloween2022.HalloweenEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Cette classe sert à gérer les différents evenement spéciaux
 */
public class SpecialEventManager implements Listener {

    private List<SpecialEvent> events;

    /**
     * Constructeur des evenements spéciaux
     */
    public SpecialEventManager() {
        this.events = new ArrayList<>();
    }

    /**
     * Enregistre un evenement
     * @param event à enregister
     */
    public void registerEvent(SpecialEvent event) {
        if(this.events.contains(event)) return;
        this.events.add(event);
    }

    /**
     * Méthode permettant de démarrer tous les évenements
     */
    public void startEvents(Game partie){
       this.events.stream()
               .filter(SpecialEvent::isEnabled)
               .forEach(event -> Bukkit.getScheduler().runTask(mineralcontest.plugin, () -> {
                   event.startEvent(partie);

                   String sb = "[" +
                           event.getEventName() +
                           "] " +
                           event.getDescription();
                   partie.groupe.sendToEveryone(sb);
               }));
    }


    @EventHandler
    public void onGameStart(MCGameStartedEvent event) {
        registerEvent(new HalloweenEvent());
        startEvents(event.getGame());
    }
}
