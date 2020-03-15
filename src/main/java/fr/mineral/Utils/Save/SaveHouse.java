package fr.mineral.Utils.Save;

import fr.mineral.Core.House;
import fr.mineral.Utils.Door.DisplayBlock;
import fr.mineral.Utils.SaveableBlock;
import fr.mineral.mineralcontest;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.material.Ladder;
import org.bukkit.material.MaterialData;

import java.io.File;
import java.io.IOException;
import java.util.Stack;

public class SaveHouse {

    private House house;
    FileConfiguration configuration;
    File configFile;

    FileConfiguration data;
    File dataFile;

    public static Stack<SaveableBlock> revert;
    // WATER, LADDER ...
    public static Stack<SaveableBlock> specialBlock;
    private static World world;

    public SaveHouse() {
        this.house = mineralcontest.plugin.getGame().getBlueHouse();
        this.configFile = new File(mineralcontest.plugin.getDataFolder() + File.separator + "house.yml");
        this.configuration = YamlConfiguration.loadConfiguration(configFile);
        SaveHouse.revert = new Stack<>();
        SaveHouse.specialBlock = new Stack<>();

    }


    public void loadHouse(String fileName, Player p) throws IOException {

        SaveHouse.world = p.getWorld();
        ClassLoader classLoader = getClass().getClassLoader();
        File config = new File(fileName);
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


        for(int i = 0; i < blockCount; ++i) {


            int newX, newY, newZ;
            newX = (int) (x + Double.parseDouble(this.data.get("house.blocks." + i + ".block.location.x").toString()));
            newY = (int) (y + Double.parseDouble(this.data.get("house.blocks." + i + ".block.location.y").toString()));
            newZ = (int) (z + Double.parseDouble(this.data.get("house.blocks." + i + ".block.location.z").toString()));
            Location locTMP = new Location(loc.getWorld(), newX, newY, newZ);

            SaveHouse.revert.add(new SaveableBlock(locTMP.getBlock()));


            Material blockMaterial = Material.valueOf(this.data.get("house.blocks." + i + ".block.type").toString());
            Byte blockByte = Byte.parseByte(this.data.get("house.blocks." + i + ".block.data").toString());

            locTMP.getBlock().setType(blockMaterial);
            locTMP.getBlock().getLocation().setX(locTMP.getX());
            locTMP.getBlock().getLocation().setY(locTMP.getY());
            locTMP.getBlock().getLocation().setZ(locTMP.getZ());
            locTMP.getBlock().getState().getData().setData(blockByte);
        }

        for(int i = 0; i < doorCount; ++i) {


            int newX, newY, newZ;
            newX = (int) (x + Double.parseDouble(this.data.get("house.door.blocks." + i + ".block.location.x").toString()));
            newY = (int) (y + Double.parseDouble(this.data.get("house.door.blocks." + i + ".block.location.y").toString()));
            newZ = (int) (z + Double.parseDouble(this.data.get("house.door.blocks." + i + ".block.location.z").toString()));
            Location locTMP = new Location(loc.getWorld(), newX, newY, newZ);
            SaveHouse.revert.add(new SaveableBlock(locTMP.getBlock()));



            Material blockMaterial = Material.valueOf(this.data.get("house.door.blocks." + i + ".block.type").toString());
            Byte blockByte = Byte.parseByte(this.data.get("house.door.blocks." + i + ".block.data").toString());

            locTMP.getBlock().setType(blockMaterial);
            locTMP.getBlock().getLocation().setX(locTMP.getX());
            locTMP.getBlock().getLocation().setY(locTMP.getY());
            locTMP.getBlock().getLocation().setZ(locTMP.getZ());
            locTMP.getBlock().getState().getData().setData(blockByte);
        }

        int newX, newY, newZ;
        newX = (int) (x + Double.parseDouble(this.data.get("house.chest.location.x").toString()));
        newY = (int) (y + Double.parseDouble(this.data.get("house.chest.location.y").toString()));
        newZ = (int) (z + Double.parseDouble(this.data.get("house.chest.location.z").toString()));
        Location locTMP = new Location(loc.getWorld(), newX, newY, newZ);

        locTMP.getBlock().setType(Material.CHEST);
        mineralcontest.plugin.getGame().getBlueHouse().setCoffreEquipe(locTMP);

        newX = (int) (x + Double.parseDouble(this.data.get("house.spawn.location.x").toString()));
        newY = (int) (y + Double.parseDouble(this.data.get("house.spawn.location.y").toString()));
        newZ = (int) (z + Double.parseDouble(this.data.get("house.spawn.location.z").toString()));
        Location spawnTMP = new Location(loc.getWorld(), newX, newY, newZ);
        mineralcontest.plugin.getGame().getBlueHouse().setHouseLocation(spawnTMP);

    }

    public void addSpecialBlock(Location l) {
        specialBlock.add(new SaveableBlock(l.getBlock()));
        world = l.getWorld();
    }

    public void addBlock(Location l) {
        if(     l.getBlock().getType().equals(Material.LADDER) ||
                l.getBlock().getType().equals(Material.WATER) ||
                l.getBlock().getType().equals(Material.LAVA) ||
                l.getBlock().getType().equals(Material.IRON_DOOR) ||
                l.getBlock().getType().toString().toLowerCase().contains("iron")
          ) {

            if(l.getBlock().getType().equals(Material.WATER)) return;
            addSpecialBlock(l);
            return;
        }
        revert.add(new SaveableBlock(l.getBlock()));
        world = l.getWorld();
    }

    public void reset() {
        this.house.getBlocks().clear();
        this.house.getPorte().getPorte().clear();
        mineralcontest.plugin.getServer().broadcastMessage("SaveHouse Reset");
    }

    private void handleLadder(Location ladderLocation) {
        /*Block b = ladderLocation.getBlock();
        b.setType(block.getMaterial());
        b.getState().getData().setData(block.getBlockByte());*/

        /*Location[] location = new Location[4];
        location[0] = new Location(ladder.getWorld(), ladderLocation.getX(), ladderLocation.getY(), ladderLocation.getZ()-1); // N
        location[1] = new Location(ladder.getWorld(), ladderLocation.getX(), ladderLocation.getY(), ladderLocation.getZ()+1); // S
        location[2] = new Location(ladder.getWorld(), ladderLocation.getX()-1, ladderLocation.getY(), ladderLocation.getZ()); // E
        location[3] = new Location(ladder.getWorld(), ladderLocation.getX()+1, ladderLocation.getY(), ladderLocation.getZ()); // W

        BlockFace[] facingDirections = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};


        for(int direction = 0; direction < location.length; ++direction) {
            if(! location[direction].getBlock().getType().equals(Material.AIR)){
                Block foundBlock = location[direction].getBlock();

                foundBlock.getRelative(facingDirections[direction]).setType(Material.LADDER);

                //location[direction].getBlock().setType(Material.LADDER);
                break;

            }
        }*/


    }

    private void clearStack(Stack s) {
        int doorCount = 0;
        while(!s.empty()) {
            SaveableBlock block = (SaveableBlock) s.pop();


            if(block.getMaterial().toString().toLowerCase().contains("door") && doorCount == 0) {
                doorCount++;
                block.setBlock();
                Bukkit.broadcastMessage(doorCount + " doorcount");
            }else if(!block.getMaterial().toString().toLowerCase().contains("door")) {
                block.setBlock();

            }

        }

        s.clear();
    }

    public void revert() {
        clearStack(revert);

        Bukkit.getScheduler().scheduleSyncDelayedTask(mineralcontest.plugin, new Runnable() {
            public void run() {

                clearStack(specialBlock);
                Bukkit.broadcastMessage("SPECIAL BLOCK DONE");
            }
        }, 20*1);
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
