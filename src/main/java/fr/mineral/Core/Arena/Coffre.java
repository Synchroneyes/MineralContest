package fr.mineral.Core.Arena;

import fr.groups.Core.Groupe;
import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.Utils.Range;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Coffre {
    private Location position;
    private Groupe groupe;

    public Coffre(Groupe g) {
        this.groupe = g;
    }

    public void setPosition(Location p) {
        this.position = p;
    }

    public Location getPosition() throws Exception {
        if(this.position == null)
            throw new Exception("chestNotDefined");
        return this.position;
    }

    public void clear() {
        if(this.position != null) {
            Block b = this.position.getBlock();
            if(b.getState() instanceof Chest)((Chest)b.getState()).getInventory().clear();
            if(!b.getType().equals(Material.AIR)) b.breakNaturally();
        }
    }


}
