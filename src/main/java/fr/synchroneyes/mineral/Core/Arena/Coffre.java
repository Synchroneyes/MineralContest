package fr.synchroneyes.mineral.Core.Arena;

import fr.synchroneyes.groups.Core.Groupe;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;

public class Coffre {
    private Location position;
    private Groupe groupe;

    public Coffre(Groupe g) {
        this.groupe = g;
    }

    public void setPosition(Location p) {
        this.position = p;
    }

    public Location getPosition() {
        return this.position;
    }

    public void clear() {
        if (this.position != null) {
            Block b = this.position.getBlock();
            if (b.getState() instanceof Chest) ((Chest) b.getState()).getInventory().clear();
            if (!b.getType().equals(Material.AIR)) b.breakNaturally();
        }
    }


}
