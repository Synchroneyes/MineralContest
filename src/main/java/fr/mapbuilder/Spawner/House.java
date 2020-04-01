package fr.mapbuilder.Spawner;

import fr.mapbuilder.Blocks.SaveableBlock;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Stack;

public class House {

    private static Stack<SaveableBlock> blocksModified = new Stack<>();

    public static void addModifiedBlock(Block b) {
        SaveableBlock blockToAdd = new SaveableBlock(b);
        if(!blocksModified.contains(blockToAdd)) blocksModified.add(blockToAdd);
    }

    public static void spawnHouseFromPlayer(Player player) {
        // First, we
    }

}
