package fr.synchroneyes.mineral.Core.Arena;

import fr.synchroneyes.mineral.Core.Arena.ArenaChestContent.ArenaChestContentGenerator;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.ErrorReporting.Error;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class CoffreAvecCooldown {
    private Location position;
    public boolean opened = false;
    public boolean spawned = false;
    public boolean isCancelled = false;
    // default values
    private int time = 5;
    private int timeLeft = time;
    public static CoffreAvecCooldown coffre;
    public Player openingPlayer;
    private Arene arene;

    private ArenaChestContentGenerator arenaChestContent;

    private BukkitRunnable chestTimer;


    public CoffreAvecCooldown(Location loc, Arene arene) {
        this.arene = arene;
        this.position = loc;
        coffre = this;
        fillChestTimer();
        this.arenaChestContent = new ArenaChestContentGenerator(arene.groupe);

        try {
            if (arene != null & arene.getCoffre() != null) {
                if (arene.getCoffre().equals(this)) {
                    time = arene.groupe.getParametresPartie().getCVAR("chest_opening_cooldown").getValeurNumerique();
                    timeLeft = time;
                }
            }
        } catch (Exception e) {
            Error.Report(e, arene.groupe.getGame());
            e.printStackTrace();
        }

    }

    public void initializeChestContent(File fichier) {
        try {
            this.arenaChestContent.initialize(fichier);
        } catch (Exception e) {
            Error.Report(e, arene.groupe.getGame());
        }
    }


    public boolean isChestSpawned() {
        return arene.isChestSpawned();
    }

    public void clear() {
        this.position.getBlock().setType(Material.AIR);
        if (this.position.getBlock().getState() instanceof Chest)
            ((Chest) this.position.getBlock().getState()).getInventory().clear();
        opened = false;
        spawned = false;
        isCancelled = false;

        try {
            time = arene.groupe.getParametresPartie().getCVAR("chest_opening_cooldown").getValeurNumerique();
        } catch (Exception e) {
            e.printStackTrace();
            Error.Report(e, arene.groupe.getGame());
        }

        timeLeft = time;
        openingPlayer = null;
    }


    public Location getPosition() throws Exception {
        if (this.position == null)
            throw new Exception("ArenaChestNotDefined");
        return this.position;
    }

    public void spawn() {
        try {
            arene.enableTeleport();
            arene.generateTimeBetweenChest();

            position = this.getPosition();
            Block block = position.getBlock();
            position.getBlock().setType(Material.CHEST);
            Chest chest = (Chest) block.getState();

            Inventory inv = chest.getInventory();
            inv.clear();


            this.timeLeft = time;
            this.isCancelled = false;
            this.opened = false;
            this.openingPlayer = null;
            this.spawned = true;
            arene.setChestSpawned(true);

            arene.groupe.getGame().addAChest(position.getBlock());


        } catch (Exception e) {
            mineralcontest.broadcastMessage(mineralcontest.prefixErreur + e.getMessage(), arene.groupe);

        }

    }

    public void close() {

        if (timeLeft <= 0) {
            Block block = position.getBlock();
            block.breakNaturally();
        }

        if (!this.chestTimer.isCancelled()) this.chestTimer.cancel();

        timeLeft = time;
        opened = false;
        spawned = false;
        openingPlayer = null;
    }

    private void startChestTimer() {
        this.chestTimer.runTaskTimer(mineralcontest.plugin, 0, 20);
    }

    private void fillChestTimer() {
        this.chestTimer = new BukkitRunnable() {
            @Override
            public void run() {
                Player joueur = openingPlayer;
                Block block = position.getBlock();
                if (!position.getBlock().getType().equals(Material.CHEST)) {
                    this.cancel();
                    return;
                }
                Chest chest = (Chest) block.getState();
                Inventory inv = chest.getInventory();
                inv.clear();

                if (!opened || openingPlayer == null) {
                    this.cancel();
                    close();
                }
                if (timeLeft >= 0) {

                    chest.setCustomName(ChatColor.RED + Lang.arena_chest_title.toString());
                    chest.update();
                    inv.clear();
                    inv.setMaxStackSize(1);
                    // Fill inventory with coloured concrete
                    for (int i = 0; i < time; i++) {
                        ItemStack vert = new ItemStack(Material.GREEN_CONCRETE, 1);
                        ItemStack rouge = new ItemStack(Material.RED_CONCRETE, 1);
                        if (i <= time - timeLeft) {
                            inv.setItem(i, vert);
                        } else {
                            inv.setItem(i, rouge);
                        }
                    }
                }

                if (openingPlayer != null) {
                    if (--timeLeft > 0)
                        openingPlayer.playNote(openingPlayer.getLocation(), Instrument.PIANO, new Note(1));
                    else {
                        joueur.playNote(joueur.getLocation(), Instrument.PIANO, new Note(24));
                        inv.clear();
                        inv.setMaxStackSize(64);
                        try {
                            generateChestContent();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Error.Report(e, arene.groupe.getGame());
                        }

                        // Give chest items to user
                        for (ItemStack item : inv.getContents()) {
                            if (item != null) {
                                joueur.getInventory().addItem(item);
                                joueur.playSound(joueur.getLocation(), Sound.ENTITY_ITEM_PICKUP, 50, 1);
                            }
                        }
                        inv.clear();
                        position.getBlock().breakNaturally();
                        joueur.closeInventory();
                        close();
                        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.arena_chest_opened.toString(), arene.groupe);
                        mineralcontest.getPlayerGame(joueur).getArene().disableTeleport();
                        arene.setChestSpawned(false);
                        this.cancel();

                    }
                }


            }
        };
    }

    public void openChest(Player joueur) {

        if (openingPlayer == null) {
            openingPlayer = joueur;
            opened = true;
            fillChestTimer();
            startChestTimer();
        } else {
            // prevent player from cheating
            if (openingPlayer.equals(joueur)) {
                close();
                return;
            }
            joueur.sendMessage(mineralcontest.prefixErreur + Lang.arena_chest_being_opened.toString());
            return;
        }
    }

    private void generateChestContent() throws Exception {


        Block block = position.getBlock();
        position.getBlock().setType(Material.CHEST);
        Chest chest = (Chest) block.getState();
        Inventory inv = chest.getInventory();

        inv.clear();
        inv.setContents(arenaChestContent.generateInventory().getContents());

        /*LinkedList<Range> items = new LinkedList<>();

        GameSettings settings = arene.groupe.getParametresPartie();

        YamlConfiguration config = settings.getYamlConfiguration();
        ConfigurationSection chest_items = config.getConfigurationSection("chest_content");

        String[] attributes = {"name", "probability"};
        int currentMinRange = 0;
        int tmpNextMinRange = currentMinRange;

        if(chest_items != null) {
            for(String item_name : chest_items.getKeys(false)) {
                ConfigurationSection item_config = chest_items.getConfigurationSection(item_name);
                Range itemRange = new Range();
                for(String attribute : attributes) {
                    if (!checkIfAttributeExists(item_config, attribute)) break;
                    switch(attribute) {
                        case "name":
                            Material itemMaterial = null;
                            try {
                                itemMaterial = Material.valueOf(item_config.get(attribute).toString());
                            }catch (Exception e) {
                                Bukkit.getLogger().severe(mineralcontest.prefixErreur + "Attribute " + attribute + " is invalid.");
                                continue;
                            }
                            itemRange.setMaterial(itemMaterial);
                            break;
                        case "probability":
                            int min, max;
                            min = currentMinRange;
                            max = min + (int) item_config.get(attribute);
                            itemRange.setMin(min);
                            itemRange.setMax(max);
                            tmpNextMinRange = max;
                            break;

                    }
                }

                if(!itemRange.isFilled()) {
                    mineralcontest.broadcastMessage(item_name + " is missing some attributes.", arene.groupe);
                    continue;
                }

                items.add(itemRange);
                currentMinRange = tmpNextMinRange;
            }
        }

        // If there is no item in file, we add default one
        if(items.size() == 0) {
            items.add(new Range(Material.IRON_INGOT, 0, 75));
            items.add(new Range(Material.GOLD_INGOT, 75, 90));
            items.add(new Range(Material.DIAMOND, 90, 97));
            items.add(new Range(Material.EMERALD, 97, 101));
            currentMinRange = 101;
        }

        int maxItem, minItem;
        maxItem = arene.groupe.getParametresPartie().getCVAR("max_item_in_chest").getValeurNumerique();
        minItem = arene.groupe.getParametresPartie().getCVAR("min_item_in_chest").getValeurNumerique();

        //maxItem = 30;
        //minItem = 20;

        Random r = new Random();
        // Formule pour generer un nombre entre [X .... Y]
        // ((Y - X) + 1) + X
        int nbItem = r.nextInt((maxItem - minItem) + 1) + minItem;

        try {
            for(int i = 0; i < nbItem; i++) {
                int randomRange = r.nextInt(currentMinRange);
                ItemStack randomItem = Range.getRandomItemFromLinkedList(items, randomRange);
                inv.addItem(randomItem);
            }
        }catch(Exception e) {
            e.printStackTrace();
            Error.Report(e, arene.groupe.getGame());

        }*/

    }

    private boolean checkIfAttributeExists(ConfigurationSection configSection, String attributeName) {
        if (configSection.get(attributeName) == null) {
            Bukkit.getLogger().severe(mineralcontest.prefixErreur + configSection.getCurrentPath() + " doesnt have an \"" + attributeName + "\" attribute");
            return false;
        }
        return true;
    }

}
