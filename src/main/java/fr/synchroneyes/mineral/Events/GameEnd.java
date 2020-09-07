package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.custom_events.MCGameEndEvent;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.House;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Utils.GamePodium;
import fr.synchroneyes.mineral.Utils.Metric.SendInformation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

public class GameEnd implements Listener {

    @EventHandler
    public void onGameEnd(MCGameEndEvent endEvent) {
        SendInformation.sendGameData(SendInformation.ended, endEvent.getGame());

        // On fait apparaitre le podium
        Game partie = endEvent.getGame();

        // On récupère le centre de l'arène
        Location centreArene = partie.getArene().getCoffre().getLocation();

        // On supprime le coffre si il existe
        centreArene.getBlock().setType(Material.AIR);

        // On récupère les équipes
        List<House> maisons = (List<House>) partie.getHouses().clone();

        List<Equipe> equipes = new LinkedList<>();
        for(House h : maisons) equipes.add(h.getTeam());

        equipes.sort(Comparator.comparingInt(Equipe::getScore));

        Collections.reverse(equipes);

        // On retire si il n'y a personne dans l'équipe
        equipes.removeIf(equipe -> equipe.getJoueurs().isEmpty());


        // On fait apparaitre le podium
        GamePodium.spawn(centreArene, equipes);
    }
}
