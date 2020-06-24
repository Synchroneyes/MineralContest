package fr.synchroneyes.mineral.Utils.Door;

import fr.synchroneyes.mineral.Utils.ErrorReporting.Error;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;

public class DisplayBlock {
    private Block baseBlock;
    private Location position;
    private Material materiel;
    private MaterialData data;

    public DisplayBlock(Block baseBlock) {

        try {
            this.baseBlock = baseBlock;
            this.position = baseBlock.getLocation();
            this.materiel = baseBlock.getState().getType();
            this.data = baseBlock.getState().getData();
        } catch (Exception e) {
            e.printStackTrace();
            Error.Report(e, null);
        }


    }


    public Block getBlock() {
        return baseBlock;
    }

    public void display() {


        try {
            position.getBlock().setType(materiel);
            position.getBlock().setBlockData(baseBlock.getBlockData());
            position.getBlock().getState().setData(this.data);

            position.getBlock().getState().update();
        } catch (Exception e) {
            e.printStackTrace();
            Error.Report(e, null);
        }

    }

    public void hide() {

        position.getBlock().setType(Material.AIR);
    }
}
