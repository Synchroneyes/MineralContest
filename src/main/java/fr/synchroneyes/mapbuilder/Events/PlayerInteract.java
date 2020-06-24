package fr.synchroneyes.mapbuilder.Events;

import fr.synchroneyes.mapbuilder.Commands.mcteam;
import fr.synchroneyes.mapbuilder.MapBuilder;
import fr.synchroneyes.mineral.Core.House;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.LinkedList;
import java.util.Map;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onPlayerClickOnBlock(PlayerInteractEvent event) {
        if (!MapBuilder.getInstance().isBuilderModeEnabled) return;

        if (event.getClickedBlock() != null && event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Player joueur = event.getPlayer();
            House maisonJoueur = mcteam.getPlayerAllocatedHouse(joueur);
            if (maisonJoueur != null) {
                // Si le bloc est déjà présent, on le retire
                // Sinon, on l'ajoute
                Block clickedBlock = event.getClickedBlock();
                Map.Entry<House, LinkedList<Block>> couple = mcteam.getPorteMaison(maisonJoueur);

                LinkedList<Block> porte = couple.getValue();
                if (porte.contains(clickedBlock)) {
                    porte.remove(clickedBlock);
                    joueur.sendMessage(ChatColor.RED + "- bloc retiré de la porte");
                } else {
                    porte.add(clickedBlock);
                    joueur.sendMessage(ChatColor.GREEN + "+ bloc ajouté à la porte");

                }

                couple.setValue(porte);
                event.setCancelled(true);
            }
        }
    }
}
