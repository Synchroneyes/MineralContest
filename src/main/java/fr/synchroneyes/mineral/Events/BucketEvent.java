package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.mapbuilder.MapBuilder;
import fr.synchroneyes.mineral.Core.Game.BlockManager;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Settings.GameSettings;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.Radius;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class BucketEvent implements Listener {

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) throws Exception {
        if (MapBuilder.getInstance().isBuilderModeEnabled) return;
        World currentWorld = event.getPlayer().getWorld();
        if (mineralcontest.isAMineralContestWorld(currentWorld)) {
            boolean allowedToBePlaced = true;
            Player p = event.getPlayer();
            Game partie = mineralcontest.getWorldGame(currentWorld);

            if (partie == null) {
                event.setCancelled(true);
                p.sendMessage(mineralcontest.prefixPrive + Lang.block_not_allowed_to_be_placed.toString());
                return;
            }

            GameSettings settings = partie.groupe.getParametresPartie();

            if (Radius.isBlockInRadius(partie.getArene().getCoffre().getPosition(), event.getPlayer().getLocation(), partie.getArene().arenaRadius)) {
                if (settings.getCVAR("mp_enable_block_adding").getValeurNumerique() == 1) {
                    BlockManager blockManager = BlockManager.getInstance();
                    if (blockManager.isBlockAllowedToBeAdded(event.getBucket())) {
                        blockManager.addBlock(event.getBlock());
                    } else {
                        allowedToBePlaced = false;
                    }
                } else {
                    allowedToBePlaced = false;
                }
            }

            if (!allowedToBePlaced) {
                event.setCancelled(true);
                p.sendMessage(mineralcontest.prefixPrive + Lang.block_not_allowed_to_be_placed.toString());
            }
        }
    }
}
