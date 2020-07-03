package fr.synchroneyes.mineral.Core.Parachute;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class ParachuteBlock {

    private Location location;
    private Material material;

    public ParachuteBlock(Location location, Material material) {
        this.location = location;
        this.material = material;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public static ParachuteBlock fromBlock(Block b) {
        return new ParachuteBlock(b.getLocation(), b.getType());
    }

    /**
     * Permet de supprimer un bloc
     */
    public void remove() {
        this.location.getBlock().setType(Material.AIR);
    }
}
