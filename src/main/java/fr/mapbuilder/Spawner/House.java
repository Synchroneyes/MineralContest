package fr.mapbuilder.Spawner;

import fr.file_manager.FileList;
import fr.mapbuilder.Blocks.BlocksColorChanger;
import fr.mapbuilder.Blocks.BlocksDataColor;
import fr.mapbuilder.MapBuilder;
import fr.mapbuilder.Util;
import fr.mineral.Utils.BlockSaver;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Stack;

public class House {

    private static mineralcontest plugin = mineralcontest.plugin;
    private static Stack<BlockSaver> blocksModified = new Stack<>();

    public static void addModifiedBlock(Block b, BlockSaver.Type type) {
        BlockSaver blockToAdd = new BlockSaver(b, type);
        if(!blocksModified.contains(blockToAdd)) blocksModified.add(blockToAdd);
    }

    public static void spawnHouseFromPlayer(Player player) {
        // First, we
    }

    public static void spawn(Location location, BlocksDataColor color, Player player) {
        String houseName = getHouseDirectionBasedOnPlayer(player);

        String houseToLoad = houseName;
        switch (houseName) {
            case "east":
                houseToLoad = FileList.CustomMap_east_house_scheme.toString();
                break;

            case "north":
                houseToLoad = FileList.CustomMap_north_house_scheme.toString();
                break;

            case "south":
                houseToLoad = FileList.CustomMap_south_house_scheme.toString();
                break;

            case "west":
                houseToLoad = FileList.CustomMap_west_house_scheme.toString();
                break;
        }

        Bukkit.getLogger().info(houseToLoad);
        File houseFileToLoad = new File(plugin.getDataFolder(), houseToLoad);



        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(houseFileToLoad);
        ConfigurationSection blocks = yamlConfiguration.getConfigurationSection("house.blocks");

        // Soit bloc inexistants, soit pas une maison
        if(blocks == null) {
            player.sendMessage("Impossible de récuperer les blocs du fichier");
            return;
        }

        double x,y,z;
        Location loc = player.getLocation();
        x = player.getLocation().getX();
        y = player.getLocation().getY();
        z = player.getLocation().getZ();

        int blockCount = Integer.parseInt(yamlConfiguration.get("house.blocks.count").toString());
        int doorCount = Integer.parseInt(yamlConfiguration.get("house.door.count").toString());


        for(int i = 0; i < blockCount; ++i) {

            int newX, newY, newZ;
            newX = (int) (x + Double.parseDouble(yamlConfiguration.get("house.blocks." + i + ".block.location.x").toString()));
            newY = (int) (y + Double.parseDouble(yamlConfiguration.get("house.blocks." + i + ".block.location.y").toString()));
            newZ = (int) (z + Double.parseDouble(yamlConfiguration.get("house.blocks." + i + ".block.location.z").toString()));
            Location locTMP = new Location(loc.getWorld(), newX, newY, newZ);

            Material blockMaterial = Material.valueOf(yamlConfiguration.get("house.blocks." + i + ".block.type").toString());
            Byte blockByte = Byte.parseByte(yamlConfiguration.get("house.blocks." + i + ".block.data").toString());

            addModifiedBlock(locTMP.getBlock(), BlockSaver.Type.DESTROYED);

            locTMP.getBlock().setType(blockMaterial);
            locTMP.getBlock().getLocation().setX(locTMP.getX());
            locTMP.getBlock().getLocation().setY(locTMP.getY());
            locTMP.getBlock().getLocation().setZ(locTMP.getZ());
            locTMP.getBlock().getState().getData().setData(blockByte);

            BlocksColorChanger.changeBlockColor(locTMP.getBlock(), color);
        }

        for(int i = 0; i < doorCount; ++i) {


            int newX, newY, newZ;
            newX = (int) (x + Double.parseDouble(yamlConfiguration.get("house.door.blocks." + i + ".block.location.x").toString()));
            newY = (int) (y + Double.parseDouble(yamlConfiguration.get("house.door.blocks." + i + ".block.location.y").toString()));
            newZ = (int) (z + Double.parseDouble(yamlConfiguration.get("house.door.blocks." + i + ".block.location.z").toString()));
            Location locTMP = new Location(loc.getWorld(), newX, newY, newZ);
            //SaveHouse.revert.add(new SaveableBlock(locTMP.getBlock()));
            BlocksColorChanger.changeBlockColor(locTMP.getBlock(), color);




            Material blockMaterial = Material.valueOf(yamlConfiguration.get("house.door.blocks." + i + ".block.type").toString());
            Byte blockByte = Byte.parseByte(yamlConfiguration.get("house.door.blocks." + i + ".block.data").toString());

            addModifiedBlock(locTMP.getBlock(), BlockSaver.Type.DESTROYED);

            locTMP.getBlock().setType(blockMaterial);
            locTMP.getBlock().getLocation().setX(locTMP.getX());
            locTMP.getBlock().getLocation().setY(locTMP.getY());
            locTMP.getBlock().getLocation().setZ(locTMP.getZ());
            locTMP.getBlock().getState().getData().setData(blockByte);
            BlocksColorChanger.changeBlockColor(locTMP.getBlock(), color);

        }

        MapBuilder.modifications.push((Stack<BlockSaver>) blocksModified.clone());
        blocksModified.clear();

    }


    private static String getHouseDirectionBasedOnPlayer(Player p) {
        String playerLookingDirection = Util.getLookingDirection(p);

        switch(playerLookingDirection) {
            case "NE":
            case "NW":
            case "N":
                playerLookingDirection = "north";
                break;

            case "SE":
            case "SW":
            case "S":
                playerLookingDirection = "south";
                break;

            case "E":
                playerLookingDirection = "east";
                break;
            case "W":
                playerLookingDirection = "west";
                break;
        }

        return playerLookingDirection;
    }

}
