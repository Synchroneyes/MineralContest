package fr.synchroneyes.mapbuilder.Blocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

/*

    Class used to "serialize" blocks

 */
public class SaveableBlock {

    private int posX, posY, posZ;
    private Material material;
    private Byte blockByte;
    private World world;
    private Location location;
    private BlockData blockData;

    public SaveableBlock(Block b) {
        this.posX = (int) b.getLocation().getX();
        this.posY = (int) b.getLocation().getY();
        this.posZ = (int) b.getLocation().getZ();
        this.world = b.getLocation().getWorld();
        this.location = b.getLocation();

        this.material = b.getType();
        this.blockByte = b.getState().getData().getData();
        this.blockData = b.getBlockData();

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

    public Location getLocation() {
        return this.location;
    }

    public void setBlock() {
        Location location = new Location(this.world, this.posX, this.posY, this.posZ);
        Block block = location.getBlock();
        block.setType(this.material);
        block.setBlockData(this.blockData);
        block.getState().getData().setData(this.blockByte);
        block.getState().update();
    }
}