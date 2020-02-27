package fr.mineral.Utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/*
    Class used to save the blocks and to restore them at the end of the game
 */
public class BlockSaver {
    public enum Type  {PLACED, DESTROYED};
    private Type method;
    private int posX, posY, posZ;

    private Material material;
    private Byte blockByte;
    private World world;
    private Location location;

    public BlockSaver(Block b, Type t) {
        this.method = t;

        /* Save block infos */
        this.posX = (int) b.getLocation().getX();
        this.posY = (int) b.getLocation().getY();
        this.posZ = (int) b.getLocation().getZ();
        this.world = b.getLocation().getWorld();
        this.location = b.getLocation();
        this.material = b.getType();
        this.blockByte = b.getState().getData().getData();
    }

    public void applyMethod() {
        Location blockLocation = new Location(this.world, this.posX, this.posY, this.posZ);
        Block block = blockLocation.getBlock();
        if(this.method == Type.PLACED) {
            // Remove the block
            block.setType(Material.AIR);
        } else {
            // Restore the block
            block.setType(this.material);
            block.getState().getData().setData(this.blockByte);
        }
    }
}
