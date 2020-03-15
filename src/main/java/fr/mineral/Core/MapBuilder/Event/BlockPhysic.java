package fr.mineral.Core.MapBuilder.Event;

import fr.mineral.Core.Arena.Arene;
import fr.mineral.Core.MapBuilder.Commands.listMaps;
import fr.mineral.Utils.Save.SaveHouse;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.type.Ladder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

public class BlockPhysic implements Listener {
    private static int ladders = 0;
    @EventHandler
    public void onBlockPhysic(BlockPhysicsEvent event) throws Exception {

        if(listMaps.saving) {
            if(event.getBlock().getType().equals(Material.LADDER) || event.getBlock().getType().equals(Material.WATER)) {
                Arene arene = mineralcontest.plugin.getGame().getArene();
                SaveHouse sh = mineralcontest.plugin.getSaveHouse();
                sh.addBlock(event.getBlock().getLocation());
                arene.addBlock(event.getBlock().getLocation());
            }
        }
    }
}
