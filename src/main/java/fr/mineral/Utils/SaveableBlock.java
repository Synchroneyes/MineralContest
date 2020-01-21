package fr.mineral.Utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/*

    Class used to "serialize" blocks

 */
public class SaveableBlock {

    private int posX, posY, posZ;
    private Material material;
    private Byte blockByte;
    private World world;
    private Location location;

    public SaveableBlock(Block b) {
        this.posX = (int) b.getLocation().getX();
        this.posY = (int) b.getLocation().getY();
        this.posZ = (int) b.getLocation().getZ();
        this.world = b.getLocation().getWorld();
        this.location = b.getLocation();

        this.material = b.getType();
        this.blockByte = b.getState().getData().getData();

    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getPosZ() {
        return posZ;
    }

    public Material getMaterial() {
        return material;
    }

    public Byte getBlockByte() {
        return blockByte;
    }

    public Location getLocation() { return this.location;}

    public void setBlock() {
        Location location = new Location(this.world, this.posX, this.posY, this.posZ);
        Block block = location.getBlock();
        block.setType(this.material);
        block.getState().getData().setData(this.blockByte);
    }
}
