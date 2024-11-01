package fr.synchroneyes.mineral.Utils;

import fr.synchroneyes.mineral.mineralcontest;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MassBlockSpawner {

    @Setter
    private int blockPerBatch = 100;

    @Setter
    private int delayPerBatch = 20;

    private BukkitTask timer;

    private List<Block> spawnedBlocks;

    private HashMap<Location, Material> blocks;

    private HashMap<Location, Material> blockToAddLater;

    public MassBlockSpawner() {
        spawnedBlocks = new ArrayList<>();
        this.blocks = new HashMap<>();
        this.blockToAddLater = new HashMap<>();
    }


    public void spawnBlocks() {
        List<Location> blocksToTreat = new ArrayList<>();
        blocksToTreat.addAll(blocks.keySet());
        blocksToTreat.addAll(blockToAddLater.keySet());

        AtomicInteger currentIndex = new AtomicInteger();
        timer = Bukkit.getScheduler().runTaskTimer(mineralcontest.plugin, () -> {
            for(int i = currentIndex.get(); i < currentIndex.get() +blockPerBatch; i++) {
                if(i >= blocksToTreat.size()) { blockSpawnEnded(); return; }
                if(blocksToTreat.get(i) == null) { blockSpawnEnded(); return; }
                Location location = blocksToTreat.get(i);
                Block block = location.getBlock();

                Material type = null;
                if(i < blocks.size()) type = blocks.get(location);
                else type = blockToAddLater.get(location);

                block.setType(type);
                this.spawnedBlocks.add(block);
            }
            currentIndex.addAndGet(blockPerBatch);

            Bukkit.getLogger().info("[MassBlockSpawner] " + currentIndex + "/" + blocksToTreat.size());
        }, 0, delayPerBatch);
    }

    private void blockSpawnEnded() {
        if(timer != null) timer.cancel();
        Bukkit.getPluginManager().callEvent(new fr.synchroneyes.custom_events.MCMassBlockSpawnEndedEvent(this));
    }


    public void removeSpawnedBlocks() {
        for(Block block : spawnedBlocks) {
            block.setType(Material.AIR);
        }
    }

    public void addBlock(Location location, Material material) {
        if(material == Material.LAVA) this.blockToAddLater.put(location, material);
        else this.blocks.put(location, material);
    }

}
