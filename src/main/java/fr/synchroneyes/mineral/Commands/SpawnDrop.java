package fr.synchroneyes.mineral.Commands;

import fr.synchroneyes.file_manager.FileList;
import fr.synchroneyes.groups.Commands.CommandTemplate;
import fr.synchroneyes.mineral.Utils.Pair;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SpawnDrop extends CommandTemplate {

    public SpawnDrop() {
        accessCommande.add(PLAYER_COMMAND);
        this.accessCommande.add(PLAYER_ADMIN);

        addArgument("tempsEnTick", true);
        addArgument("hauteur", true);
        addArgument("distanceMax", true);
    }

    @Override
    public String getCommand() {
        return "spawndrop";
    }

    @Override
    public String getDescription() {
        return "Spawn un coffre";
    }

    @Override
    public String getPermissionRequise() {
        return null;
    }

    @Override
    public boolean performCommand(CommandSender commandSender, String command, String[] args) {
        Player joueur = (Player) commandSender;

        int tempsEntick = Integer.parseInt(args[0]);
        int hauteur = Integer.parseInt(args[1]);

        int distanceMax = Integer.parseInt(args[2]);

        int xMin, xMax, zMin, zMax;

        xMin = ((Player) (commandSender)).getLocation().getBlockX();
        zMin = ((Player) (commandSender)).getLocation().getBlockZ();

        xMax = xMin + distanceMax;
        zMax = zMin + distanceMax;


        int xGenere, zGenere;
        Random random = new Random();
        xGenere = random.nextInt((xMax - xMin) - 1) + xMin;
        zGenere = random.nextInt((zMax - zMin) - 1) + zMin;



        //ArrayList<Block> parachute = new ArrayList<>();


        HashMap<String, Pair<Location, Material>> parachute = new HashMap<>();


        YamlConfiguration fichierDrop = YamlConfiguration.loadConfiguration(new File(mineralcontest.plugin.getDataFolder(), FileList.AirDrop_model.toString()));
        Bukkit.getLogger().info(FileList.AirDrop_model.toString());

        Location dropLocation = null;

        for (String id : fichierDrop.getKeys(false)) {

            ConfigurationSection configurationSectionID = fichierDrop.getConfigurationSection(id);
            double x, y, z;
            Material material = null;

            x = Double.parseDouble(configurationSectionID.get("x").toString()) + xGenere;
            y = Double.parseDouble(configurationSectionID.get("y").toString()) + hauteur + ((Player) commandSender).getLocation().getY();
            z = Double.parseDouble(configurationSectionID.get("z").toString()) + zGenere;
            material = Material.valueOf(configurationSectionID.get("material").toString());


            Location blockLocation = new Location(((Player) commandSender).getWorld(), x, y, z);
            if (material == Material.CHEST) {
                dropLocation = blockLocation;
            }
            blockLocation.getBlock().setType(material);

            parachute.put(id, new Pair<>(blockLocation, material));
        }


        joueur.sendTitle("Largage aérien", "Un largage est apparu en X: " + ChatColor.GREEN + dropLocation.getX() + ChatColor.WHITE + " Z: " + ChatColor.GREEN + dropLocation.getZ(), 20, 20 * 5, 20);
        joueur.sendMessage(dropLocation.toVector().toString());


        Location finalDropLocation = dropLocation;
        new BukkitRunnable() {
            @Override
            public void run() {

                for (Map.Entry<String, Pair<Location, Material>> block : parachute.entrySet()) {
                    String nomBlock = block.getKey();
                    Location blockLocation = block.getValue().getKey();
                    Material blockMaterial = block.getValue().getValue();


                    Block blockEnDessous = blockLocation.getBlock().getRelative(BlockFace.DOWN);
                    Bukkit.getLogger().info("BLock: " + blockLocation.getBlock().getType().toString() + " - Dessous: " + blockEnDessous.getType().toString());

                    if (blockMaterial == Material.CHEST && blockEnDessous.getType() != Material.AIR) {
                        for (Map.Entry<String, Pair<Location, Material>> blocks : parachute.entrySet()) {
                            Pair<Location, Material> _infoBlock = blocks.getValue();
                            Material parachuteBlockMaterial = _infoBlock.getValue();
                            Location parachuteBlockLocation = _infoBlock.getKey();

                            if (parachuteBlockMaterial != Material.CHEST)
                                parachuteBlockLocation.getBlock().setType(Material.AIR);
                        }

                        blockLocation.getWorld().playEffect(blockLocation, Effect.END_GATEWAY_SPAWN, 20);
                        blockLocation.getWorld().playSound(blockLocation, Sound.ENTITY_GENERIC_EXPLODE, 5, 1);
                        this.cancel();
                        Bukkit.broadcastMessage("ON GROUND");
                        break;
                    }

                    if (blockMaterial == Material.CHEST) {
                        finalDropLocation.getWorld().spawnParticle(Particle.REDSTONE, blockLocation, 0, 0.001, 0, 0, 0, new Particle.DustOptions(Color.BLACK, 10));
                    }

                    if (blockEnDessous.getType() == Material.AIR) {


                        blockLocation.getBlock().setType(Material.AIR);
                        blockLocation.setY(blockLocation.getY() - 1);

                        blockEnDessous.setType(blockMaterial);

                        Pair<Location, Material> newPair = new Pair<>(blockLocation, blockMaterial);
                        parachute.replace(block.getKey(), newPair);
                    }
                }


            }
        }.runTaskTimer(mineralcontest.plugin, 0, tempsEntick);


        return false;
    }
}
