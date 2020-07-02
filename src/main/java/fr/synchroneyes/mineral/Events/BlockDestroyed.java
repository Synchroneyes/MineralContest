package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mapbuilder.MapBuilder;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Statistics.Class.MinerStat;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.BlockSaver;
import fr.synchroneyes.mineral.Utils.Radius;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockDestroyed implements Listener {

    /**
     * Evenement appelé lors de la destruction d'un bloc
     *
     * @param event
     */
    @EventHandler
    public void _onBlockDestroyed(BlockBreakEvent event) {

        // Si on est en mode création de map, on ignore l'event
        if (MapBuilder.getInstance().isBuilderModeEnabled) return;

        // On récupère le joueur ayant cassé le bloc
        Player joueur = event.getPlayer();

        // Si le joueur n'est pas dans un monde mineral contest, on s'en fou
        if (!mineralcontest.isInAMineralContestWorld(joueur)) return;

        // Le joueur se trouve dans un monde mineral contest
        // On récupère son groupe
        Groupe playerGroupe = mineralcontest.getPlayerGroupe(joueur);

        // Si le group est null, on annule l'event.
        // ça veut dire que le joueur est dans le lobby & version communautaire activée
        if (playerGroupe == null) {
            joueur.sendMessage(mineralcontest.prefixErreur + Lang.cant_break_block_here.toString());
            event.setCancelled(true);
            return;
        }

        // on récupère la partie du joueur
        Game partie = playerGroupe.getGame();

        // Si la partie est non démarrée, on annule l'event, on ne veut pas casser de bloc
        if (!partie.isGameStarted() || partie.isPreGame() || partie.isGamePaused()) {
            joueur.sendMessage(mineralcontest.prefixErreur + Lang.cant_break_block_here.toString());
            event.setCancelled(true);
            return;
        }

        // Maintenant, on doit vérifier si la partie est en cours
        if (partie.isGameStarted()) {
            // On doit vérifier si on se trouve autour de la zone protegée
            int rayonZoneProtege = playerGroupe.getParametresPartie().getCVAR("protected_zone_area_radius").getValeurNumerique();
            Block blockDetruit = event.getBlock();
            Location centreArene = playerGroupe.getGame().getArene().getCoffre().getPosition();

            // Si le block détruit est dans le rayon de la zone protegé, on annule l'event
            if (Radius.isBlockInRadius(centreArene, blockDetruit.getLocation(), rayonZoneProtege)) {
                event.setCancelled(true);
                joueur.sendMessage(mineralcontest.prefixErreur + Lang.cant_break_block_here.toString());
                return;
            }

            // Sinon, le block détruit n'est pas dans la zone protégé, on autorise la destruction
            playerGroupe.getGame().addBlock(event.getBlock(), BlockSaver.Type.DESTROYED);

            // On enregistre la destruction pour les stats
            playerGroupe.getGame().getStatsManager().register(MinerStat.class, event.getPlayer(), null);

        } else {
            // La partie n'est pas démarrée, on annule l'event
            joueur.sendMessage(mineralcontest.prefixErreur + Lang.cant_break_block_here.toString());
            event.setCancelled(true);
        }


    }
}
