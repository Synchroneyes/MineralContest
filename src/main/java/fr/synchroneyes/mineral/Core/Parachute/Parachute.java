package fr.synchroneyes.mineral.Core.Parachute;

import fr.synchroneyes.file_manager.FileList;
import fr.synchroneyes.mineral.Core.Coffre.AutomatedChestAnimation;
import fr.synchroneyes.mineral.Core.Coffre.Coffres.CoffreParachute;
import fr.synchroneyes.mineral.Settings.GameSettings;
import fr.synchroneyes.mineral.Utils.LocationRange;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Classe permettant de définir un parachute
 */
public class Parachute implements Listener {


    // Blocs représentant le parachute
    private Map<String, ParachuteBlock> blocksParachute;

    // Santé du parachute
    private double health;

    // Variable permettant de savoir si le parachute est au sol ou non
    private boolean isParachuteOnGround = false;

    private boolean isParachuteBroken = false;

    // Vitesse de chute de plannage (jsp si ça se dit comme ça)
    private int normalFallingSpeed = 40;

    // Vitesse en chute libre
    private int freeFallingSpeed = 2;

    // Interval d'items présent dans le coffre de base
    private int max_items_in_chest = 100;

    private int min_items_in_chest = 35;

    // Vitesse de chute du parachute
    private int currentFallingSpeed = normalFallingSpeed;

    // Position du coffre du parachute
    private Location chestLocation = null;

    // ID du coffre dans la liste
    private String chestId;

    private AutomatedChestAnimation coffre;

    private ParachuteManager parachuteManager;

    private BukkitTask parachuteLoop;

    // Falling start location, used to check if falling block is from the parachute (from event)
    private Location fallingStartLocation;

    // ArmorStand, used when falling
    private ArmorStand armorStand;

    private Vector armorStandVelocity;


    /**
     * Constructeur, prend en paramètre la santé que doit avoir le parachute
     *
     * @param health - santé du parachute
     */
    public Parachute(double health, ParachuteManager manager) {

        // Initialisation des variables
        this.health = health;
        this.blocksParachute = new LinkedHashMap<>();

        // On charge le parachute
        this.loadParachuteFromFile();

        this.parachuteManager = manager;

        this.coffre = new CoffreParachute(manager.getGroupe().getAutomatedChestManager());


        GameSettings parametres = manager.getGroupe().getParametresPartie();

        // ON récupère les valeurs présentes dans la config
        if (parametres != null) {
            this.normalFallingSpeed = manager.getGroupe().getParametresPartie().getCVAR("normal_falling_speed").getValeurNumerique();
            this.freeFallingSpeed = manager.getGroupe().getParametresPartie().getCVAR("free_falling_speed").getValeurNumerique();

            this.max_items_in_chest = manager.getGroupe().getParametresPartie().getCVAR("max_item_in_drop").getValeurNumerique();
            this.min_items_in_chest = manager.getGroupe().getParametresPartie().getCVAR("min_item_in_drop").getValeurNumerique();

            ((CoffreParachute) this.coffre).setMinItems(min_items_in_chest);
            ((CoffreParachute) this.coffre).setMaxItems(max_items_in_chest);

        }

        // On register l'event
        Bukkit.getPluginManager().registerEvents(this, mineralcontest.plugin);

    }


    /**
     * Retourne vrai si le parachute est touché par une flèche
     *
     * @param fleche La flèche tirée
     * @return
     */
    public boolean isParachuteHit(Projectile fleche) {
        if (isParachuteOnGround || isParachuteBroken) return false;
        return isParachuteHit(fleche.getLocation().getBlock().getLocation());
    }

    /**
     * Retourne vrai si la localisation donnée est une localisation d'un des blocs du parachute
     *
     * @param loc
     * @return
     */
    public boolean isParachuteHit(Location loc) {
        if (isParachuteOnGround || isParachuteBroken) return false;

        for (Map.Entry<String, ParachuteBlock> blockDeParachute : getParachute().entrySet()) {
            if (LocationRange.isLocationBetween(loc, blockDeParachute.getValue().getLocation(), 2, 2)) return true;
        }
        return false;
    }

    /**
     * Fonction appelée lorsque le parachute prend des dégats
     * On joue également un effet à l'endroit où il y a eu le hit
     *
     * @param damage
     */
    public void receiveDamage(Double damage, Location hitLocation) {

        if (health <= damage) {
            this.setFalling(true);
            return;
        }

        health -= damage;

        if (hitLocation != null) {
            World monde = hitLocation.getWorld();

            if (monde != null) {
                monde.playEffect(hitLocation, Effect.END_GATEWAY_SPAWN, 1);
            }

        } else {
            playEffectOnChest(Effect.END_GATEWAY_SPAWN);

        }
        playSoundOnChest(Sound.ENTITY_GENERIC_EXPLODE);
    }

    /**
     * Retourne le parachute
     *
     * @return HashMap parachute
     */
    public Map<String, ParachuteBlock> getParachute() {
        return blocksParachute;
    }


    /**
     * Fait descendre tout les blocs de parachute d'une hauteur
     *
     * @param checkUnderBefore - Si vrai, on vérifiera si le bloc du dessous sera de l'air ou non, et si c'est le cas, on casse le parachute
     */
    private void makeParachuteGoDown(boolean checkUnderBefore) {

        // Pour chaque bloc de parachute
        for (Map.Entry<String, ParachuteBlock> blocks : blocksParachute.entrySet()) {

            // On récupère le bloc
            ParachuteBlock parachuteBlock = blocks.getValue();
            Block block = parachuteBlock.getLocation().getBlock();

            // On vérifie d'abord si on doit vérifier en dessous ou pas
            if (checkUnderBefore) {

                // On regarde si le bloc en dessous de notre bloc actuel est de l'air ou non
                if (block.getRelative(BlockFace.DOWN, 1).getType() != Material.AIR && !isThisBlockAParachute(block.getRelative(BlockFace.DOWN, 1))) {
                    // Le bloc n'est pas de l'air ... On arrête donc la tombée du parachute, et on casse le parachute
                    this.breakParachute();
                    setFalling(true);
                    return;
                }
            }

            // Si on arrive ici, c'est soit que l'on ne vérifie pas en dessous, soit que la vérif s'est bien passée et qu'on peut descendre
            // On va donc faire descendre le bloc de 1 de hauteur
            // On initialise un nouveau block, que l'on va remplacer après
            ParachuteBlock nouveauBlock = null;
            Location nouvelleLocation = block.getLocation();
            nouvelleLocation.setY(nouvelleLocation.getY() - 1);

            nouveauBlock = new ParachuteBlock(nouvelleLocation, block.getType());

            // On supprime l'ancien bloc
            parachuteBlock.remove();

            // Et on le remplace
            blocksParachute.replace(blocks.getKey(), nouveauBlock);
            nouvelleLocation.getBlock().setType(parachuteBlock.getMaterial());

            // On update la position du coffre
            if (nouveauBlock.getMaterial() == Material.CHEST)
                this.chestLocation = nouvelleLocation;


        }
    }


    /**
     * Fonction permettant de vérifier si un bloc du parachute touche quelque chose
     *
     * @return
     */
    private boolean isParachuteHittingABlock() {
        for (Map.Entry<String, ParachuteBlock> blocks : blocksParachute.entrySet())
            if (blocks.getValue().getLocation().getBlock().getRelative(BlockFace.DOWN, 1).getType() != Material.AIR)
                return true;
        return false;
    }


    /**
     * Fonction permettant de vérifier si le bloc donné fait parti du parachute
     *
     * @param b block
     * @return
     */
    private boolean isThisBlockAParachute(Block b) {
        for (Map.Entry<String, ParachuteBlock> blocks : blocksParachute.entrySet())
            if (blocks.getValue().getLocation().equals(b.getLocation())) return true;
        return false;
    }


    /**
     * Si le parachute est marqué comme tombant, alors tout sera retiré du parachute sauf le coffre qui lui, continuera de tomber
     *
     * @param falling
     * @return
     */
    private void setFalling(boolean falling) {

        // Si on marque le parachute comme tombant
        if (falling) {
            currentFallingSpeed = freeFallingSpeed;
            breakParachute();
        }

    }


    /**
     * Permet de casser le parachute et de ne laisser que le coffre
     */
    private void breakParachute() {
        // Pour chaque bloc du parachute

        //isParachuteBroken = true;
        World currentWorld = null;
        Location dropLocation = null;
        BlockData chestData = null;

        for (Map.Entry<String, ParachuteBlock> block : blocksParachute.entrySet())
            if (block.getValue().getLocation().getBlock().getType() != Material.CHEST) {
                block.getValue().remove();
            } else {
                currentWorld = block.getValue().getLocation().getWorld();
                dropLocation = block.getValue().getLocation();
                chestData = dropLocation.getBlock().getBlockData();

            }

        // On récupère le monde actuel
        if(currentWorld != null) {
            dropLocation.getBlock().setType(Material.AIR);
            parachuteLoop.cancel();

            ArmorStand armorStand = currentWorld.spawn(dropLocation, ArmorStand.class);
            armorStand.getEquipment().setHelmet(new ItemStack(Material.CHEST));
            armorStand.setVelocity(new Vector(0, -1, 0));
            armorStand.setInvulnerable(true);
            //armorStand.setGlowing(true);
            armorStand.setVisible(false);
            armorStand.setBasePlate(false);
            armorStand.setSmall(true);
            armorStand.setArms(false);
            this.fallingStartLocation = dropLocation;
            this.armorStand = armorStand;

            this.isParachuteBroken = true;

            this.parachuteLoop = Bukkit.getScheduler().runTaskTimer(mineralcontest.plugin, this::doChestFallingTickWhenParachuteIsBroken, 0, 1);


        }
    }

    /**
     * Retourne le coffre du parachute
     *
     * @return
     */
    private ParachuteBlock getChest() {
        for (Map.Entry<String, ParachuteBlock> blocks : blocksParachute.entrySet())
            if (blocks.getValue().getMaterial() == Material.CHEST) return blocks.getValue();
        return null;
    }

    /**
     * Méthode appelée lorsque le parachute est "cassé" et que le parachute peut encore tomber
     */
    private void makeChestGoDown() {

        if (isParachuteBroken) {

            // On récupère le coffre
            ParachuteBlock coffre = getChest();

            // On récupère le block du coffre
            Block blockCoffre = coffre.getLocation().getBlock();


            Block blockEnDessous = blockCoffre.getRelative(BlockFace.DOWN, 1);

            // Si le block en dessous est de l'air, on descend
            if (blockEnDessous.getType() == Material.AIR) {


                Location positionActuelle = blockCoffre.getLocation();


                // On met une particule de redstone pour symboliser la chute
                positionActuelle.getWorld().spawnParticle(Particle.REDSTONE, positionActuelle, 10, 0.000, 0, 0, 0, new Particle.DustOptions(Color.GREEN, 10));

                Location nouvellePosition = blockEnDessous.getLocation();


                // On supprime le block actuel
                coffre.remove();

                // On applique le coffre en bas
                nouvellePosition.getBlock().setType(coffre.getMaterial());


                // On met à jour le block

                blocksParachute.replace(chestId + "", new ParachuteBlock(nouvellePosition, coffre.getMaterial()));

            } else {
                // Le parachute est au sol
                isParachuteOnGround = true;
                this.coffre.setChestLocation(blockCoffre.getLocation());
                playEffectOnChest(Effect.END_GATEWAY_SPAWN);
                playSoundOnChest(Sound.ENTITY_GENERIC_EXPLODE);




            }

        }
    }


    /**
     * Permet de charger le parachute depuis son fichier
     */
    private void loadParachuteFromFile() {

        File fichierParachute = new File(mineralcontest.plugin.getDataFolder(), FileList.AirDrop_model.toString());

        // Si le fichier n'existe pas, on affiche une erreur dans la console
        if (!fichierParachute.exists()) {
            Bukkit.getLogger().severe(mineralcontest.prefix + " Unable to load parachute file (" + fichierParachute.getAbsolutePath() + ")");
            return;
        }

        // Le fichier existe, on le charge
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fichierParachute);

        // On va maintenant lire le fichier contenant les infos sur les blocs du parachute
        for (String id : yamlConfiguration.getKeys(false)) {

            // On va récupérer les infos lié à l'id du bloc (=> id.x, id.y, id.z, id.material)
            ConfigurationSection infos = yamlConfiguration.getConfigurationSection(id);
            if (infos == null) continue;

            // Variables par défaut
            double posX, posY, posZ;
            Material blockMaterial = null;

            // Récupération des valeurs
            posX = Double.parseDouble(infos.get("x").toString());
            posY = Double.parseDouble(infos.get("y").toString());
            posZ = Double.parseDouble(infos.get("z").toString());

            blockMaterial = Material.valueOf(infos.get("material").toString());

            // On connait maintenant les coordonnées par défaut du block
            // On peut instancier la location
            Location blockLocation = new Location(null, posX, posY, posZ);

            if (blockMaterial == Material.CHEST) {
                chestLocation = blockLocation;
                chestId = id;
            }

            // Et on peut ajouter le block a la liste des blocs du parachute
            this.blocksParachute.put(id, new ParachuteBlock(blockLocation, blockMaterial));

        }


    }


    /**
     * Permet de faire apparaitre un parachute à une position donnée
     *
     * @param spawnLocation - La position où doit apparaitre le parachute
     */
    public void spawnParachute(Location spawnLocation) {

        // On doit changer la position de chaque bloc
        // Pour chaque block du parachute
        for (Map.Entry<String, ParachuteBlock> block : blocksParachute.entrySet()) {

            // On récupère le block
            ParachuteBlock parachuteBlock = block.getValue();

            Location blockLocation = parachuteBlock.getLocation();

            // On change le monde du block
            blockLocation.setWorld(spawnLocation.getWorld());

            // Ainsi que ses coordonnées
            blockLocation.setX(blockLocation.getX() + spawnLocation.getX());
            blockLocation.setY(blockLocation.getY() + spawnLocation.getY());
            blockLocation.setZ(blockLocation.getZ() + spawnLocation.getZ());

            // Et on le fait apparaitre
            blockLocation.getBlock().setType(parachuteBlock.getMaterial());

            // Puis, on remplace sa valeur dans notre liste
            this.blocksParachute.replace(block.getKey(), new ParachuteBlock(blockLocation, parachuteBlock.getMaterial()));
        }

        this.coffre.setChestLocation(spawnLocation);

        handleParachute();

    }

    /**
     * Boucle permettant de vérifier le parachute et de faire les actions requises
     */
    private void handleParachute() {

        // On crée un timer s'executant chaque tick, comme ça on peut réguler la vitesse de chute du parachute

        // On sauvegarde le tick actuel dans une variable afin de pouvoir gérer la vitesse de chute
        // AtomicInteger serait thread-safe (https://stackoverflow.com/questions/4818699/practical-uses-for-atomicinteger)
        AtomicInteger ticks = new AtomicInteger();


        if(parachuteLoop != null) parachuteLoop.cancel();

        parachuteLoop = new BukkitRunnable() {

            @Override
            public void run() {

                // Si le parachute est au sol
                if (isParachuteOnGround) {

                    // On arrète le timer
                    this.cancel();

                    // On supprime le coffre du parachute (le sans animation)
                    getChest().remove();

                    // On fait apparaitre le coffre d'animation
                    coffre.spawn();

                    // Et on oublie pas d'enregistrer !
                    parachuteManager.getGroupe().getAutomatedChestManager().replace(coffre.getClass(), coffre);


                    return;
                }



                int tickActuel = ticks.incrementAndGet();


                // Si on est sur un tick où il faut faire descendre le parachute
                if (tickActuel % currentFallingSpeed == 0) {

                    if (isParachuteBroken) {
                        makeChestGoDown();
                    } else makeParachuteGoDown(true);

                }
            }
        }.runTaskTimer(mineralcontest.plugin, 0, 1);

    }


    /**
     * Joue une animation sur le coffre
     *
     * @param effect
     */
    private void playEffectOnChest(Effect effect) {
        ParachuteBlock coffre = getChest();
        if (coffre != null) {
            World monde = coffre.getLocation().getWorld();
            if (monde != null) {
                monde.playEffect(coffre.getLocation(), effect, 1);
            }
        }
    }

    /**
     * Joue une animation sur le coffre
     *
     * @param effect
     */
    private void playSoundOnChest(Sound effect) {
        ParachuteBlock coffre = getChest();
        if (coffre != null) {
            World monde = coffre.getLocation().getWorld();
            if (monde != null) {
                monde.playSound(coffre.getLocation(), effect, 1, 0);
            }
        }
    }


    /**
     * Méthode appelée lorsque le parachute est cassé et que le coffre tomnre
     */
    private void doChestFallingTickWhenParachuteIsBroken() {


        if(armorStandVelocity == null) {
            this.armorStandVelocity = this.armorStand.getVelocity();
            return;
        }


        // Si l'armorstand est null, on s'arrête
        if(armorStand == null) {
            Bukkit.getLogger().info("ArmorStand is null!");
            parachuteLoop.cancel();
            parachuteLoop = null;
            return;
        }


        // On vérifie la vélocité de l'armorstand
        if(this.armorStandVelocity.equals(this.armorStand.getVelocity())) {
            parachuteLoop.cancel();
            parachuteLoop = null;

            // On récupère la position au sol du drop
            Location feltLocation = this.armorStand.getLocation();


            // On descend jusqu'au sol, tant que le bloc en dessous est de l'air
            while(feltLocation.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
                // On change la position pour prendre le bloc du dessous
                int y = feltLocation.getBlockY();
                y--;
                feltLocation.setY(y);
            }

            // On supprime l'armorStand
            this.armorStand.remove();

            // On spawn le coffre
            this.coffre.setChestLocation(feltLocation);
            // On fait apparaitre le coffre d'animation
            coffre.spawn();

            // Et on oublie pas d'enregistrer !
            parachuteManager.getGroupe().getAutomatedChestManager().addChest(coffre);

            return;
        }

        // On update la velocité
        this.armorStandVelocity = this.armorStand.getVelocity();

        Location particleLocation = this.armorStand.getLocation().clone();

        particleLocation.setY(particleLocation.getY() + 5);

        // On fait spawn des particules pour l'effet de chute
        //this.armorStand.getLocation().getWorld().spawnParticle(Particle.REDSTONE, this.armorStand.getLocation(), 10, 0.000, 0, 0, 0, new Particle.DustOptions(Color.BLACK, 10));
        this.armorStand.getLocation().getWorld().spawnParticle(Particle.SMOKE_LARGE, this.armorStand.getLocation(), 10, 0.000, 0, 0, 0);

    }


}
