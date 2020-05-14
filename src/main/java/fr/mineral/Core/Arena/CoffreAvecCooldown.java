package fr.mineral.Core.Arena;

import fr.mineral.Settings.GameSettings;
import fr.mineral.Settings.GameSettingsCvar;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.Range;
import fr.mineral.mineralcontest;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.graalvm.compiler.hotspot.GraalHotSpotVMConfig;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

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

    private BukkitRunnable chestTimer;
    public CoffreAvecCooldown(Location loc) {
        this.position = loc;
        coffre = this;
        fillChestTimer();

        Arene arena = mineralcontest.plugin.getGame().getArene();
        try {
            if(arena != null & arena.getCoffre() != null) {
                if(arena.getCoffre().equals(this)) {
                    time = (int) GameSettingsCvar.getValueFromCVARName("chest_opening_cooldown");
                    timeLeft = time;
                }
            }
        }catch (Exception e) {

        }

    }


    public boolean isChestSpawned() { return this.spawned; }

    public void clear() {
        this.position.getBlock().setType(Material.AIR);
        if(this.position.getBlock().getState() instanceof Chest) ((Chest)this.position.getBlock().getState()).getInventory().clear();
        opened = false;
        spawned = false;
        isCancelled = false;
        time = (int) GameSettingsCvar.getValueFromCVARName("chest_opening_cooldown");;
        timeLeft = time;
        openingPlayer = null;
    }


    public void setPosition(Location p) {
        this.position = p;
    }

    public Location getPosition() throws Exception {
        if(this.position == null)
            throw new Exception("ArenaChestNotDefined");
        return this.position;
    }

    public void spawn() {
        try {
            mineralcontest.plugin.getGame().getArene().enableTeleport();
            mineralcontest.plugin.getGame().getArene().generateTimeBetweenChest();

            position = this.getPosition();
            Block block = position.getBlock();
            position.getBlock().setType(Material.CHEST);
            Chest chest = (Chest)block.getState();

            Inventory inv = chest.getInventory();
            inv.clear();
            this.timeLeft = time;
            this.isCancelled = false;
            this.opened = false;
            this.openingPlayer = null;
            this.spawned = true;

            mineralcontest.plugin.getGame().addAChest(position.getBlock());



        }catch (Exception e) {
            mineralcontest.broadcastMessage(mineralcontest.prefixErreur + e.getMessage());

        }

    }

    public void close() {

        if(timeLeft <= 0) {
            Block block = position.getBlock();
            block.breakNaturally();
        }

        if(!this.chestTimer.isCancelled()) this.chestTimer.cancel();

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
                if(!position.getBlock().getType().equals(Material.CHEST)) {
                    this.cancel();
                    return;
                }
                Chest chest = (Chest)block.getState();
                Inventory inv = chest.getInventory();

                if(!opened || openingPlayer == null) {
                    this.cancel();
                    close();
                }
                if(timeLeft >= 0) {

                    chest.setCustomName(ChatColor.RED + Lang.arena_chest_title.toString());
                    chest.update();
                    inv.clear();
                    inv.setMaxStackSize(1);
                    // Fill inventory with coloured concrete
                    for(int i = 0; i < time; i++) {
                        ItemStack vert = new ItemStack(Material.GREEN_CONCRETE, 1);
                        ItemStack rouge = new ItemStack(Material.RED_CONCRETE, 1);
                        if(i <= time - timeLeft) {
                            inv.setItem(i, vert);
                        } else {
                            inv.setItem(i, rouge);
                        }
                    }
                }

                if(openingPlayer != null) {
                    if(--timeLeft > 0) openingPlayer.playNote(openingPlayer.getLocation(), Instrument.PIANO, new Note(1));
                    else {
                        joueur.playNote(joueur.getLocation(), Instrument.PIANO, new Note(24));
                        inv.clear();
                        inv.setMaxStackSize(64);
                        try {
                            generateChestContent();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // Give chest items to user
                        for(ItemStack item : inv.getContents()) {
                            if(item != null) {
                                joueur.getInventory().addItem(item);
                                joueur.playSound(joueur.getLocation(), Sound.ENTITY_ITEM_PICKUP, 50, 1);
                            }
                        }
                        inv.clear();
                        position.getBlock().breakNaturally();
                        joueur.closeInventory();
                        close();
                        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.arena_chest_opened.toString());
                        mineralcontest.plugin.getGame().getArene().disableTeleport();
                        this.cancel();

                    }
                }


            }
        };
    }

    public void openChest(Player joueur) {

        if(openingPlayer == null) {
            openingPlayer = joueur;
            opened = true;
            fillChestTimer();
            startChestTimer();
        } else {
            // prevent player from cheating
            if(openingPlayer.equals(joueur)) {
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
        Chest chest = (Chest)block.getState();
        Inventory inv = chest.getInventory();

        LinkedList<Range> items = new LinkedList<>();
        GameSettings gameSettings = GameSettings.getInstance();
        YamlConfiguration config = gameSettings.getYamlConfiguration();
        ConfigurationSection chest_items = config.getConfigurationSection("config.arena.chest_content");
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
                    mineralcontest.broadcastMessage(item_name + " is missing some attributes.");
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
        maxItem = (int) GameSettingsCvar.getValueFromCVARName("max_item_in_chest");
        minItem = (int) GameSettingsCvar.getValueFromCVARName("min_item_in_chest");

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
        }

    }

    private boolean checkIfAttributeExists(ConfigurationSection configSection, String attributeName) {
        if(configSection.get(attributeName) == null) {
            Bukkit.getLogger().severe(mineralcontest.prefixErreur + configSection.getCurrentPath() + " doesnt have an \"" + attributeName + "\" attribute" );
            return false;
        }
        return true;
    }

}
