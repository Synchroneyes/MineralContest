package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.custom_events.MCPlayerJoinEvent;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.Utils.DisconnectedPlayer;
import fr.synchroneyes.mineral.Utils.Player.PlayerUtils;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;



public class PlayerJoin implements Listener {



    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws Exception {

        // On commence par récuperer le joueur
        Player joueur = event.getPlayer();

        Bukkit.broadcastMessage("Le joueur " + joueur.getDisplayName() + " s'est reconnecté au serveur");

        // On regarde si le joueur s'est déconnecté avant du plugin
        DisconnectedPlayer disconnectedPlayer = mineralcontest.plugin.wasPlayerDisconnected(joueur);

        // SI le joueur faisait parti du plugin
        if (disconnectedPlayer != null) {

            Bukkit.broadcastMessage("Le joueur faisait parti du plugin");

            // On le reconnecte
            mineralcontest.plugin.addNewPlayer(joueur);

            // On récupère son instance
            MCPlayer mcPlayer = mineralcontest.plugin.getMCPlayer(joueur);

            // Si MCPlayer est null; il ne s'était jamais connecté
            if(mcPlayer == null){
                Bukkit.broadcastMessage("Le joueur ne s'était jamais connecté");
                return;
            }

            // On remet ses informations
            mcPlayer.reconnectPlayer(disconnectedPlayer);


            return;
        }

        Bukkit.broadcastMessage("Le joueur est nouveau !");
    }




}
