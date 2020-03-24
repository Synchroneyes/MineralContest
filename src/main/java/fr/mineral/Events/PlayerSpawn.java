package fr.mineral.Events;

import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.mineralcontest;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerSpawn implements Listener {


    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) throws Exception {

        World worldEvent = e.getPlayer().getWorld();
        if(worldEvent.equals(mineralcontest.plugin.pluginWorld)) {
            if(mineralcontest.plugin.getGame().isGameStarted()) {
                // Si la game est démarrée
                Player joueur = e.getPlayer();
                // Si le joueur était dans la deathzone
                PlayerUtils.resetPlayerDeathZone(joueur);

            }
        }

    }
}
