package fr.synchroneyes.mineral.Utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Door;

/*
    Class used to save the blocks and to restore them at the end of the game
 */
public class BlockSaver {
    public enum Type {PLACED, DESTROYED}

    ;
    private Type method;
    private int posX, posY, posZ;

    private Material material;
    private Byte blockByte;
    private World world;
    private BlockData blockData;


    public BlockSaver(Block b, Type t) {
        this.method = t;

        /* Save block infos */
        this.posX = (int) b.getLocation().getX();
        this.posY = (int) b.getLocation().getY();
        this.posZ = (int) b.getLocation().getZ();
        this.world = b.getLocation().getWorld();
        this.material = b.getType();
        this.blockByte = b.getState().getData().getData();
        this.blockData = b.getBlockData();


    }

    public Block getBlock() {
        Location l = new Location(world, posX, posY, posZ);
        return l.getBlock();
    }


    public void setBlockType(Material m) {
        this.material = m;
    }

    public void applyMethod() {
        Location blockLocation = new Location(this.world, this.posX, this.posY, this.posZ);
        Block block = blockLocation.getBlock();
        if (this.method == Type.PLACED) {
            // Remove the block
            block.setType(Material.AIR);
        } else {
            // Restore the block
            block.setType(this.material);


            if (this.blockData != null) {
                block.setBlockData(this.blockData, false);
                if (block.getBlockData() instanceof Door && block.getRelative(BlockFace.DOWN, 1).getType().equals(Material.AIR)) {
                    block.setBlockData(this.blockData, false);
                    Door door = (Door) block.getBlockData();
                    door.setHalf(Bisected.Half.BOTTOM);
                    door.setOpen(false);
                    door.setFacing(((Door) blockData).getFacing());
                    block.getRelative(BlockFace.DOWN, 1).setType(this.material);
                    block.getRelative(BlockFace.DOWN, 1).setBlockData(door, false);

                    door.setHalf(Bisected.Half.TOP);
                    door.setOpen(false);
                    block.setBlockData(door, false);

                }
            }


            block.getState().getData().setData(this.blockByte);
        }
    }


    public Type getMethod() {
        return method;
    }

    public void setMethod(Type method) {
        this.method = method;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getPosZ() {
        return posZ;
    }

    public void setPosZ(int posZ) {
        this.posZ = posZ;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Byte getBlockByte() {
        return blockByte;
    }

    public void setBlockByte(Byte blockByte) {
        this.blockByte = blockByte;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public BlockData getBlockData() {
        return blockData;
    }

    public void setBlockData(BlockData blockData) {
        this.blockData = blockData;
    }
}
