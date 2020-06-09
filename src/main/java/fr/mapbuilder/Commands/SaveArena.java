package fr.mapbuilder.Commands;

import fr.groups.Commands.CommandTemplate;
import fr.mapbuilder.Blocks.SaveableBlock;
import fr.mineral.Utils.BlockSaver;
import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Ladder;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Stack;

public class SaveArena extends CommandTemplate {

    static Stack<BlockSaver> blocks;
    static Stack<BlockSaver> specialsblocks;
    static Location centerLocation;

    public SaveArena() {
        constructArguments();
        if (blocks == null) blocks = new Stack<>();
        if (specialsblocks == null) specialsblocks = new Stack<>();

    }

    @Override
    public String getCommand() {
        return "savearena";
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        Player joueur = (Player) commandSender;

        SaveArena instance = new SaveArena();
        if (args.length == 1) {
            instance.revert();
        } else {

            instance.save(joueur);
            instance.writeToFile();

        }

        return false;
    }

    @Override
    public String getDescription() {
        return "Sauvegarde arene";
    }

    @Override
    public String getPermissionRequise() {
        return null;
    }

    private void save(Player p) {
        Block enDessous = p.getLocation().getBlock();

        centerLocation = enDessous.getLocation();
        p.sendMessage(enDessous.getType().toString());

        int blockX, blockY, blockZ;
        int rayon = 16;
        int hauteur = 10;

        blockX = enDessous.getX();
        blockY = enDessous.getY();
        blockZ = enDessous.getZ();

        Bukkit.getLogger().severe((blockX - rayon) + "; " + (blockX - rayon) + " < " + (blockX + rayon) + "; x++");
        Bukkit.getLogger().severe((blockY - 2) + "; " + (blockY - 2) + " < " + (blockY - 2 + hauteur) + "; y++");
        Bukkit.getLogger().severe((blockZ - rayon) + "; " + (blockZ - rayon) + " < " + (blockZ + rayon) + "; z++");

        for (int i = 0; i < 3; ++i) {
            for (int x = blockX - rayon; x < blockX + rayon; x++)
                for (int y = blockY - 4; y < blockY + hauteur; y++)
                    for (int z = blockZ - rayon; z < blockZ + rayon; z++) {
                        Location loc = new Location(p.getWorld(), x, y, z);
                        Block b = loc.getBlock();

                        if (i == 0) {
                            if (b.getBlockData() instanceof Ladder || b.getBlockData() instanceof Door) {
                                specialsblocks.add(new BlockSaver(b, BlockSaver.Type.DESTROYED));
                            }
                        } else if (i == 1) {

                            if (!((b.getBlockData() instanceof Ladder || b.getBlockData() instanceof Door)) && !b.getType().equals(Material.STONE)) {
                                blocks.add(new BlockSaver(b, BlockSaver.Type.DESTROYED));
                            }
                        } else {
                            b.breakNaturally();
                        }
                    }
        }


        p.sendMessage(blocks.size() + " ");
    }

    private void revert() {
        BlockSaver saveableBlock;
        for (BlockSaver s : blocks) {
            s.applyMethod();
        }

        for (BlockSaver s : specialsblocks) {
            s.applyMethod();
        }
    }

    public void writeToFile() {
        File fichierArene = new File(mineralcontest.plugin.getDataFolder() + File.separator + "arene.yml");

        FileConfiguration customConfig = YamlConfiguration.loadConfiguration(fichierArene);

        int blockIndex = 0;

        /*
            private Type method;
    private int posX, posY, posZ;

    private Material material;
    private Byte blockByte;
    private World world;
         */

        int defaultX = centerLocation.getBlockX();
        int defaultY = centerLocation.getBlockY() + 7;
        int defaultZ = centerLocation.getBlockZ();

        for (BlockSaver block : blocks) {
            customConfig.set("arena.normal_blocks." + blockIndex + ".material", block.getMaterial().toString());
            customConfig.set("arena.normal_blocks." + blockIndex + ".blockByte", block.getBlockByte());
            customConfig.set("arena.normal_blocks." + blockIndex + ".blockdata", block.getBlockData().getAsString());
            customConfig.set("arena.normal_blocks." + blockIndex + ".location.x", block.getPosX() - defaultX);
            customConfig.set("arena.normal_blocks." + blockIndex + ".location.y", block.getPosY() - defaultY);
            customConfig.set("arena.normal_blocks." + blockIndex + ".location.z", block.getPosZ() - defaultZ);
            blockIndex++;
        }

        for (BlockSaver block : specialsblocks) {
            customConfig.set("arena.special_block." + blockIndex + ".material", block.getMaterial().toString());
            customConfig.set("arena.special_block." + blockIndex + ".blockByte", block.getBlockByte());
            customConfig.set("arena.special_block." + blockIndex + ".blockdata", block.getBlockData().getAsString());
            customConfig.set("arena.special_block." + blockIndex + ".location.x", block.getPosX() - defaultX);
            customConfig.set("arena.special_block." + blockIndex + ".location.y", block.getPosY() - defaultY);
            customConfig.set("arena.special_block." + blockIndex + ".location.z", block.getPosZ() - defaultZ);
            blockIndex++;
        }


        try {
            customConfig.save(fichierArene);
        } catch (IOException e) {
            e.printStackTrace();
            Error.Report(e, null);
        }


    }


}
