package fr.mineral.Utils.Save;

import fr.mineral.Core.House;
import fr.mineral.Utils.Door.DisplayBlock;
import fr.mineral.Utils.SaveableBlock;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
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


    public void load(String fileName, Player p) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File config = new File(mineralcontest.plugin.getDataFolder() + "/" + fileName + ".yml");
        if(!config.exists())  {
            mineralcontest.plugin.getServer().broadcastMessage("ERREUR " + config.getName() + " --- " + config.getAbsolutePath());
            return;
        }

        double x,y,z;
        Location loc = p.getLocation();
        x = p.getLocation().getX();
        y = p.getLocation().getY();
        z = p.getLocation().getZ();

        this.data = YamlConfiguration.loadConfiguration(config);

        int blockCount, doorCount;
        blockCount = Integer.parseInt(this.data.get("house.blocks.count").toString());
        doorCount = Integer.parseInt(this.data.get("house.door.count").toString());

        p.sendMessage("blockCount: " + blockCount + " - doorCOunt: " + doorCount);

        for(int i = 0; i < blockCount; ++i) {


            int newX, newY, newZ;
            newX = (int) (x + Double.parseDouble(this.data.get("house.blocks." + i + ".block.location.x").toString()));
            newY = (int) (y + Double.parseDouble(this.data.get("house.blocks." + i + ".block.location.y").toString()));
            newZ = (int) (z + Double.parseDouble(this.data.get("house.blocks." + i + ".block.location.z").toString()));
            Location locTMP = new Location(loc.getWorld(), newX, newY, newZ);


            Material blockMaterial = Material.valueOf(this.data.get("house.blocks." + i + ".block.type").toString());
            mineralcontest.plugin.getServer().broadcastMessage(i + " - Material:" + blockMaterial.toString());
            Byte blockByte = Byte.parseByte(this.data.get("house.blocks." + i + ".block.data").toString());


            mineralcontest.plugin.getServer().broadcastMessage(i + " - X:" + locTMP.getX() + ", Y:" + locTMP.getY() + ", Z:" + locTMP.getZ());
            mineralcontest.plugin.getServer().broadcastMessage(i + " - Byte:" + blockByte);



            locTMP.getBlock().setType(blockMaterial);
            locTMP.getBlock().getLocation().setX(locTMP.getX());
            locTMP.getBlock().getLocation().setY(locTMP.getY());
            locTMP.getBlock().getLocation().setZ(locTMP.getZ());
            locTMP.getBlock().getState().getData().setData(blockByte);
            mineralcontest.plugin.getServer().broadcastMessage("================");

        }



    }

    public void saveToFile() throws Exception {
        this.configuration.set("house.name", this.house.getTeam().getNomEquipe());

        double baseX, baseY, baseZ;
        baseX = this.house.getHouseLocation().getX();
        baseY = this.house.getHouseLocation().getY();
        baseZ = this.house.getHouseLocation().getZ();

        int blockCounts = 0;
        /*
                We need to save the blocks
         */
        for (SaveableBlock saveableBlock : this.house.getBlocks()) {
            double blockX, blockY, blockZ;
            blockX = saveableBlock.getPosX() - baseX;
            blockY = saveableBlock.getPosY() - baseY;
            blockZ = saveableBlock.getPosZ() - baseZ;

            mineralcontest.log.info("SAVING " + saveableBlock.getMaterial().toString() + " AT " + blockX + ", " + blockY + ", " + blockZ);
            this.configuration.set("house.blocks." + blockCounts + ".block.type", saveableBlock.getMaterial().toString());
            this.configuration.set("house.blocks." + blockCounts + ".block.location.x", blockX);
            this.configuration.set("house.blocks." + blockCounts + ".block.location.y", blockY);
            this.configuration.set("house.blocks." + blockCounts + ".block.location.z", blockZ);
            this.configuration.set("house.blocks." + blockCounts + ".block.data", saveableBlock.getBlockByte());
            ++blockCounts;
        }

        this.configuration.set("house.blocks.count", blockCounts);

        int doorCounts = 0;
        for(DisplayBlock db : this.house.getPorte().getPorte()) {
            double blockX, blockY, blockZ;
            blockX = db.getSaveableBlock().getPosX() - baseX;
            blockY = db.getSaveableBlock().getPosY() - baseY;
            blockZ = db.getSaveableBlock().getPosZ() - baseZ;

            this.configuration.set("house.door.blocks." + doorCounts + ".block.type", db.getSaveableBlock().getMaterial().toString());
            this.configuration.set("house.door.blocks." + doorCounts + ".block.location.x", blockX);
            this.configuration.set("house.door.blocks." + doorCounts + ".block.location.y", blockY);
            this.configuration.set("house.door.blocks." + doorCounts + ".block.location.z", blockZ);
            this.configuration.set("house.door.blocks." + doorCounts + ".block.data", db.getSaveableBlock().getBlockByte());
            ++doorCounts;
        }

        this.configuration.set("house.door.count", doorCounts);


        this.configuration.set("house.chest.location.x", this.house.getCoffreEquipeLocation().getX() - baseX);
        this.configuration.set("house.chest.location.y", this.house.getCoffreEquipeLocation().getY() - baseY);
        this.configuration.set("house.chest.location.z", this.house.getCoffreEquipeLocation().getZ() - baseZ);
        this.configuration.set("house.spawn.location.x", 0);
        this.configuration.set("house.spawn.location.y", 0);
        this.configuration.set("house.spawn.location.z", 0);



        try {
            this.configuration.save(this.configFile);
        }catch(IOException ioe) {
            ioe.printStackTrace();
        }

    }

}
