package fr.mineral.Utils;

import fr.mineral.mineralcontest;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.GlassPane;
import org.bukkit.material.Dye;
import org.bukkit.material.Wool;

public class DisplayBlock {
    private Block baseBlock;
    private Location position;
    private Material materiel;

    public DisplayBlock(Block baseBlock) {
        this.baseBlock = baseBlock;
        this.position = baseBlock.getLocation();
        this.materiel = baseBlock.getType();

    }



    public Block getBlock() { return baseBlock;}

    public void display() {

        position.getBlock().setType(materiel);

    }
    public void hide() {

        position.getBlock().setType(Material.AIR);


    }
}
