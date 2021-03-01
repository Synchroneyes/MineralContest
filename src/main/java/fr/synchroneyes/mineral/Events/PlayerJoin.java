package fr.synchroneyes.mineral.Events;



import fr.synchroneyes.custom_events.MCPlayerJoinEvent;

import fr.synchroneyes.mineral.mineralcontest;

import org.bukkit.Bukkit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class PlayerJoin implements Listener {



    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws Exception {

        // On regarde si le joueur se connecte à un monde mineral contest ou non
        Player joueur = event.getPlayer();
        if(mineralcontest.isInAMineralContestWorld(joueur)){
            MCPlayerJoinEvent event1 = new MCPlayerJoinEvent(event.getPlayer());
            Bukkit.getPluginManager().callEvent(event1);
        }

    }




}
