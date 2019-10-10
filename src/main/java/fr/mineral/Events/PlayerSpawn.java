package fr.mineral.Events;

import fr.mineral.Teams.Equipe;
import fr.mineral.Utils.PlayerUtils;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerSpawn implements Listener {

    public PlayerSpawn() {

    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) throws Exception {
        if(mineralcontest.plugin.getGame().isGameStarted()) {
            // Si la game est démarrée
            Player joueur = e.getPlayer();
            // Si le joueur était dans la deathzone

            PlayerUtils.resetPlayerDeathZone(joueur);

        }
    }
}
