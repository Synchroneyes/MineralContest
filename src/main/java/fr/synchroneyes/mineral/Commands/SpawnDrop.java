package fr.synchroneyes.mineral.Commands;

import fr.synchroneyes.groups.Commands.CommandTemplate;
import fr.synchroneyes.mineral.mineralcontest;
import javafx.util.Pair;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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


        //ArrayList<Block> parachute = new ArrayList<>();

        HashMap<String, Pair<Material, Location>> parachute = new HashMap<>();

        Block coffre;
        Block gauche, dessus1, dessus2, dessus3, droite;

        Location chestLocation;

        Location dropLocation = joueur.getLocation();
        dropLocation.setY(dropLocation.getY() + hauteur);

        int positionX, positionZ;
        Random random = new Random();


        //positionX = (int) (random.nextInt((int) ((int) (((joueur.getLocation().getX() + distanceMax) - (joueur.getLocation().getX() - distanceMax) + 1)) + joueur.getLocation().getX())));
        //positionZ = (int) (random.nextInt((int) ((int) (((joueur.getLocation().getZ() + distanceMax) - (joueur.getLocation().getZ() - distanceMax) + 1)) + joueur.getLocation().getZ())));

        int minX, maxX;
        int minZ, maxZ;

        minX = (int) (joueur.getLocation().getX() - distanceMax);
        maxX = (int) (joueur.getLocation().getX() + distanceMax);

        minZ = (int) (joueur.getLocation().getZ() - distanceMax);
        maxZ = (int) (joueur.getLocation().getZ() + distanceMax);

        positionX = random.nextInt((maxX - minX) + 1) + minX;
        positionZ = random.nextInt((maxZ - minZ) + 1) + minZ;

        dropLocation.setX(positionX);
        dropLocation.setZ(positionZ);

        for (Player membre : mineralcontest.getPlayerGroupe(joueur).getPlayers())
            membre.sendTitle("Largage" + ChatColor.RED + " aérien", "Un largage est apparu en X:" + ChatColor.GREEN + positionX + ChatColor.WHITE + " - Z: " + ChatColor.GREEN + positionZ + ChatColor.WHITE, 20, 40, 20);
        chestLocation = dropLocation.clone();

        coffre = dropLocation.getBlock();
        coffre.setType(Material.CHEST);
        parachute.put("coffre", new Pair<>(coffre.getType(), coffre.getLocation()));
        //parachute.add(coffre);

        dropLocation.setY(dropLocation.getY() + 2);
        dessus2 = dropLocation.getBlock();
        dessus2.setType(Material.BLUE_CONCRETE);
        parachute.put("dessus2", new Pair<>(dessus2.getType(), dessus2.getLocation()));

        //parachute.add(dessus2);

        dropLocation.setX(dropLocation.getX() + 1);
        dessus1 = dropLocation.getBlock();
        dessus1.setType(Material.RED_CONCRETE);
        parachute.put("dessus1", new Pair<>(dessus1.getType(), dessus1.getLocation()));

        //parachute.add(dessus1);

        dropLocation.setX(dropLocation.getX() - 2);
        dessus3 = dropLocation.getBlock();
        dessus3.setType(Material.RED_CONCRETE);
        parachute.put("dessus3", new Pair<>(dessus3.getType(), dessus3.getLocation()));

        //parachute.add(dessus3);

        dropLocation.setX(dropLocation.getX() - 1);
        dropLocation.setY(dropLocation.getY() - 1);
        droite = dropLocation.getBlock();
        droite.setType(Material.BLUE_CONCRETE);
        parachute.put("droite", new Pair<>(droite.getType(), droite.getLocation()));

        //arachute.add(droite);

        dropLocation.setX(dropLocation.getX() + 4);
        gauche = dropLocation.getBlock();
        gauche.setType(Material.BLUE_CONCRETE);
        parachute.put("gauche", new Pair<>(gauche.getType(), gauche.getLocation()));

        //parachute.add(gauche);







        /*Block blockDepart = dropLocation.getBlock();
        blockDepart.setType(Material.CHEST);*/

        joueur.sendMessage("GO");


        new BukkitRunnable() {
            @Override
            public void run() {

                for (Map.Entry<String, Pair<Material, Location>> block : parachute.entrySet()) {
                    String nomBlock = block.getKey();
                    Pair<Material, Location> coupleMaterialLoc = block.getValue();
                    Location blockLocation = coupleMaterialLoc.getValue();
                    Material blockMaterial = coupleMaterialLoc.getKey();

                    Block blockEnDessous = blockLocation.getBlock().getRelative(BlockFace.DOWN);
                    Bukkit.getLogger().info("BLock: " + blockLocation.getBlock().getType().toString() + " - Dessous: " + blockEnDessous.getType().toString());

                    if (nomBlock.equals("coffre") && blockEnDessous.getType() != Material.AIR) {
                        for (Map.Entry<String, Pair<Material, Location>> blocks : parachute.entrySet()) {
                            if (!blocks.getKey().equals("coffre"))
                                blocks.getValue().getValue().getBlock().setType(Material.AIR);
                        }

                        blockLocation.getWorld().playEffect(blockLocation, Effect.END_GATEWAY_SPAWN, 20);
                        blockLocation.getWorld().playSound(blockLocation, Sound.ENTITY_GENERIC_EXPLODE, 5, 1);
                        this.cancel();
                        return;
                    }

                    if (nomBlock.equals("coffre")) {
                        dropLocation.getWorld().spawnParticle(Particle.REDSTONE, blockLocation, 0, 0.001, 0, 0, 0, new Particle.DustOptions(Color.BLACK, 10));
                    }

                    if (blockEnDessous.getType() == Material.AIR) {


                        blockLocation.getBlock().setType(Material.AIR);
                        blockLocation.setY(blockLocation.getY() - 1);

                        blockEnDessous.setType(blockMaterial);

                        Pair<Material, Location> newPair = new Pair<>(blockMaterial, blockLocation);
                        parachute.replace(nomBlock, newPair);
                    }
                }


                /*
                NE FONCTIONNE PAS
                for(Block block : parachute)  {
                    Block blockEnDessous = block.getRelative(BlockFace.DOWN, 1);
                    Bukkit.getLogger().info("block: " + block.getType().toString() + " - dessous: " + blockEnDessous.getType().toString());
                    if(blockEnDessous.getType() != Material.AIR && block.getType() == Material.CHEST) {
                        for(Block blocks : parachute)
                            blocks.setType(Material.AIR);
                        Bukkit.getLogger().info("CANCEL");
                        this.cancel();
                    }

                    if(block.getType() == Material.CHEST) {
                        dropLocation.getWorld().spawnParticle(Particle.REDSTONE, block.getLocation(), 0, 0.001, 1, 0, 1, new Particle.DustOptions(Color.RED, 1));
                    }

                    //if(blockEnDessous.getType() == Material.AIR) {
                        block.getLocation().setY(blockEnDessous.getLocation().getY());
                    //}
                }*/

                /*joueur.sendMessage("run");
                Block block = dropLocation.getBlock();
                joueur.sendMessage(dropLocation.toVector().toString());
                Block blockEnDessous = block.getRelative(BlockFace.DOWN);
                if(blockEnDessous.getType() != Material.AIR) {
                    block.setType(Material.AIR);
                    this.cancel();
                }

                dropLocation.getWorld().spawnParticle(Particle.REDSTONE, dropLocation, 0, 0.001, 1, 0, 1, new Particle.DustOptions(Color.RED, 1));

                //dropLocation.getWorld().playEffect(dropLocation, effet, nbParticle);


                if(blockEnDessous.getType() == Material.AIR) {
                    dropLocation.setY(dropLocation.getY()-1);
                    blockEnDessous.setType(Material.CHEST);
                    block.setType(Material.AIR);
                }*/


            }
        }.runTaskTimer(mineralcontest.plugin, 0, tempsEntick);


        return false;
    }
}
