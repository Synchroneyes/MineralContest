package fr.mapbuilder.Spawner;

import fr.file_manager.FileList;
import fr.mapbuilder.MapBuilder;
import fr.mineral.Utils.BlockSaver;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Door;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Stack;

public class Arene {

    private static mineralcontest plugin = mineralcontest.plugin;
    private static Stack<BlockSaver> blocksModified = new Stack<>();

    public static void addModifiedBlock(Block b, BlockSaver.Type type) {
        BlockSaver blockToAdd = new BlockSaver(b, type);
        if (!blocksModified.contains(blockToAdd)) blocksModified.add(blockToAdd);
    }

    public static void spawn(Location location, Player player) {

        String houseToLoad = "arene.yml";
        String path = FileList.CustomMap_arena_scheme.toString();
        File houseFileToLoad = new File(plugin.getDataFolder(), path);


        if (!houseFileToLoad.exists()) {
            player.sendMessage(path + " n'existe pas.");
            return;
        }

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(houseFileToLoad);
        ConfigurationSection blocks = yamlConfiguration.getConfigurationSection("arena.normal_blocks");

        // Soit bloc inexistants, soit pas une maison
        if (blocks == null) {
            player.sendMessage("Impossible de récuperer les blocs du fichier");
            return;
        }

        double x, y, z;
        Location loc = player.getLocation();
        x = player.getLocation().getX();
        y = player.getLocation().getY();
        z = player.getLocation().getZ();

        int normalBlockCount = yamlConfiguration.getConfigurationSection("arena.normal_blocks").getKeys(false).size();
        int specialBlockCount = yamlConfiguration.getConfigurationSection("arena.special_block").getKeys(false).size();

        int max = normalBlockCount + specialBlockCount;
        int index = 0;

        for (; index < normalBlockCount; ++index) {

            int newX, newY, newZ;
            newX = (int) (x + Double.parseDouble(yamlConfiguration.get("arena.normal_blocks." + index + ".location.x").toString()));
            newY = (int) (y + Double.parseDouble(yamlConfiguration.get("arena.normal_blocks." + index + ".location.y").toString()));
            newZ = (int) (z + Double.parseDouble(yamlConfiguration.get("arena.normal_blocks." + index + ".location.z").toString()));
            Location locTMP = new Location(loc.getWorld(), newX, newY, newZ);

            Material blockMaterial = Material.valueOf(yamlConfiguration.get("arena.normal_blocks." + index + ".material").toString());
            Byte blockByte = Byte.parseByte(yamlConfiguration.get("arena.normal_blocks." + index + ".blockByte").toString());

            BlockData blockData = mineralcontest.plugin.getServer().createBlockData(yamlConfiguration.get("arena.normal_blocks." + index + ".blockdata").toString());

            addModifiedBlock(locTMP.getBlock(), BlockSaver.Type.DESTROYED);

            locTMP.getBlock().setType(blockMaterial);
            locTMP.getBlock().setBlockData(blockData, false);
            locTMP.getBlock().getLocation().setX(locTMP.getX());
            locTMP.getBlock().getLocation().setY(locTMP.getY());
            locTMP.getBlock().getLocation().setZ(locTMP.getZ());
            locTMP.getBlock().getState().getData().setData(blockByte);
        }

        Bukkit.getLogger().info(index + " < " + normalBlockCount);

        Bukkit.getLogger().info(index + " < " + specialBlockCount);
        for (; index < max; ++index) {

            int newX, newY, newZ;
            newX = (int) (x + Double.parseDouble(yamlConfiguration.get("arena.special_block." + index + ".location.x").toString()));
            newY = (int) (y + Double.parseDouble(yamlConfiguration.get("arena.special_block." + index + ".location.y").toString()));
            newZ = (int) (z + Double.parseDouble(yamlConfiguration.get("arena.special_block." + index + ".location.z").toString()));
            Location locTMP = new Location(loc.getWorld(), newX, newY, newZ);

            addModifiedBlock(locTMP.getBlock(), BlockSaver.Type.DESTROYED);

            Material blockMaterial = Material.valueOf(yamlConfiguration.get("arena.special_block." + index + ".material").toString());
            Byte blockByte = Byte.parseByte(yamlConfiguration.get("arena.special_block." + index + ".blockByte").toString());

            BlockData blockData = mineralcontest.plugin.getServer().createBlockData(yamlConfiguration.get("arena.special_block." + index + ".blockdata").toString());

            if ((blockData instanceof Door || blockMaterial.toString().equalsIgnoreCase("DOOR"))) {
                locTMP.getBlock().setBlockData(blockData, false);

                Door door = (Door) blockData;
                door.setHalf(Bisected.Half.BOTTOM);
                door.setOpen(false);
                door.setFacing(((Door) blockData).getFacing());

                if (!locTMP.getBlock().getRelative(BlockFace.DOWN, 1).getType().equals(Material.AIR)) {
                    locTMP.getBlock().getRelative(BlockFace.DOWN, 1).setType(blockMaterial);
                    locTMP.getBlock().getRelative(BlockFace.DOWN, 1).setBlockData(door, false);
                }


                door.setHalf(Bisected.Half.TOP);
                door.setOpen(false);
                locTMP.getBlock().setBlockData(door, false);
            } else {
                locTMP.getBlock().setType(blockMaterial);
                locTMP.getBlock().setBlockData(blockData, false);
            }


            locTMP.getBlock().getLocation().setX(locTMP.getX());
            locTMP.getBlock().getLocation().setY(locTMP.getY());
            locTMP.getBlock().getLocation().setZ(locTMP.getZ());
            locTMP.getBlock().getState().getData().setData(blockByte);
        }

        MapBuilder.modifications.push((Stack<BlockSaver>) blocksModified.clone());
        blocksModified.clear();
    }


}
