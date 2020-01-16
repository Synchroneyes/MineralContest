package fr.mineral.Utils.Save;

import fr.mineral.Core.House;
import fr.mineral.Utils.Door.DisplayBlock;
import fr.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.material.MaterialData;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class SaveHouse {

    private House house;
    FileConfiguration configuration;
    File configFile;

    FileConfiguration data;
    File dataFile;

    public SaveHouse() {
        this.house = mineralcontest.plugin.getGame().getBlueHouse();
        this.configFile = new File(mineralcontest.plugin.getDataFolder() + File.separator + "house.yml");
        this.configuration = YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveToFile() throws Exception {
        this.configuration.set("house.name", this.house.getTeam().getNomEquipe());

        int blockCounts = 0;
        /*
                We need to save the blocks
         */
        for (Map.Entry<Block, MaterialData> data : this.house.getBlocks().entrySet()) {
            Block foreachBlock;
            MaterialData foreachMaterialData;
            foreachBlock = data.getKey();
            foreachMaterialData = data.getValue();
            this.configuration.set("house.blocks." + blockCounts + ".block.type", foreachBlock.getType());
            this.configuration.set("house.blocks." + blockCounts + ".block.location.x", foreachBlock.getLocation().getX());
            this.configuration.set("house.blocks." + blockCounts + ".block.location.y", foreachBlock.getLocation().getY());
            this.configuration.set("house.blocks." + blockCounts + ".block.location.z", foreachBlock.getLocation().getZ());
            this.configuration.set("house.blocks." + blockCounts + ".block.materialData", foreachMaterialData);
            ++blockCounts;
        }

        this.configuration.set("house.blocks.count", blockCounts);

        int doorCounts = 0;
        for(DisplayBlock db : this.house.getPorte().getPorte()) {
            this.configuration.set("house.door.blocks." + doorCounts + ".block.location.x", db.getBlock().getLocation().getX());
            this.configuration.set("house.door.blocks." + doorCounts + ".block.location.y", db.getBlock().getLocation().getY());
            this.configuration.set("house.door.blocks." + doorCounts + ".block.location.z", db.getBlock().getLocation().getZ());
            this.configuration.set("house.door.blocks." + doorCounts + ".block.materialData", db.getBlock().getState().getData());
            ++doorCounts;
        }

        this.configuration.set("house.door.count", doorCounts);


        this.configuration.set("house.chest.location.x", this.house.getCoffreEquipeLocation().getX());
        this.configuration.set("house.chest.location.y", this.house.getCoffreEquipeLocation().getY());
        this.configuration.set("house.chest.location.z", this.house.getCoffreEquipeLocation().getZ());
        this.configuration.set("house.spawn.location.x", this.house.getHouseLocation().getX());
        this.configuration.set("house.spawn.location.y", this.house.getHouseLocation().getY());
        this.configuration.set("house.spawn.location.z", this.house.getHouseLocation().getZ());



        try {
            this.configuration.save(this.configFile);
        }catch(IOException ioe) {
            ioe.printStackTrace();
        }

    }

}
