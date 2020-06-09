package fr.mineral.Events;

import fr.mineral.Core.Game.BlockManager;
import fr.mineral.Core.Game.Game;
import fr.mineral.Settings.GameSettings;
import fr.mineral.Settings.GameSettingsCvarOLD;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.Radius;
import fr.mineral.mineralcontest;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class BucketEvent implements Listener {

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) throws Exception {
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
                    }  else {
                        allowedToBePlaced = false;
                    }
                } else {
                    allowedToBePlaced = false;
                }
            }

            if(!allowedToBePlaced) {
                event.setCancelled(true);
                p.sendMessage(mineralcontest.prefixPrive + Lang.block_not_allowed_to_be_placed.toString());
            }
        }
    }
}
