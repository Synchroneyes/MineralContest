package fr.mineral.Events;

import fr.mineral.Core.Game.BlockManager;
import fr.mineral.Settings.GameSettingsCvar;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.Radius;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class BucketEvent implements Listener {

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) throws Exception {
        World currentWorld = event.getPlayer().getWorld();
        if(currentWorld.equals(mineralcontest.plugin.pluginWorld)) {
            boolean allowedToBePlaced = true;
            Player p = event.getPlayer();
            if(Radius.isBlockInRadius(mineralcontest.plugin.getGame().getArene().getCoffre().getPosition(), event.getPlayer().getLocation(), mineralcontest.plugin.getGame().getArene().arenaRadius)){
                if((int) GameSettingsCvar.getValueFromCVARName("mp_enable_block_adding") == 1) {
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
