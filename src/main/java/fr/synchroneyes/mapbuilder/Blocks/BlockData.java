package fr.synchroneyes.mapbuilder.Blocks;

import org.bukkit.Material;

public class BlockData {
    public Material blockMaterial;
    public int blockData;

    public BlockData(Material material, int blockData) {
        this.blockData = blockData;
        this.blockMaterial = material;
    }

    public BlockData getBlockData() {
        return this;
    }
}
