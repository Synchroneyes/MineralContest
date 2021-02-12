package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.custom_events.MCPlayerChangedWorldEvent;
import fr.synchroneyes.custom_events.MCPlayerJoinEvent;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerEvent;

public class Spigot_WorldChangeEvent implements Listener {

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {


        World monde_source, monde_destination;
        monde_source = event.getFrom();
        monde_destination = event.getPlayer().getWorld();

        // On ne traite que les changements de monde du plugin
        Player joueur = event.getPlayer();

        // On regarde si le joueur fait déjà parti du plugin
        MCPlayer mcPlayer = mineralcontest.plugin.getMCPlayer(joueur);

        // Si le mcPlayer est null; le joueur ne vient pas du plugin.
        // On doit alors regarder si il se dirige vers un monde du plugin
        if(mcPlayer == null) {
            // Vérification du monde de destination
            if(mineralcontest.isAMineralContestWorld(monde_destination)) {
                // Le joueur se rend dans un monde mineral contest pour la première fois
                // On appelle l'event "MCPlayerJoinEvent"
                MCPlayerJoinEvent joinEvent = new MCPlayerJoinEvent(joueur, new MCPlayer(joueur));
                Bukkit.getServer().getPluginManager().callEvent(joinEvent);
                return;
            }

            // Le joueur ne se dirige pas vers un monde mineral contest; on s'en fou
            return;
        }

        // Le joueur fait parti du plugin mineral contest
        MCPlayerChangedWorldEvent mcPlayerChangedWorldEvent = new MCPlayerChangedWorldEvent(monde_source, monde_destination, mcPlayer);
        Bukkit.getServer().getPluginManager().callEvent(mcPlayerChangedWorldEvent);

        if(mcPlayerChangedWorldEvent.isCancelled()) {
            // On téléporte le joueur à son ancienne position
            event.getPlayer().teleport(mcPlayer.getPLayerLocationFromWorld(monde_source));
        }
        return;


    }
}
