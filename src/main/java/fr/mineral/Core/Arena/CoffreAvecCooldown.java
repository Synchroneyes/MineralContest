package fr.mineral.Core.Arena;

import fr.mineral.Translation.Lang;
import fr.mineral.Utils.Range;
import fr.mineral.Utils.XMaterial;
import fr.mineral.mineralcontest;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class CoffreAvecCooldown {
    private Location position;
    public boolean opened = false;
    public boolean spawned = false;
    public boolean isCancelled = false;
    private int time = 5;
    private int timeLeft = time;
    public static CoffreAvecCooldown coffre;
    public Player openingPlayer;

    private BukkitRunnable chestTimer;
    public CoffreAvecCooldown(Location loc) {
        this.position = loc;
        coffre = this;
        fillChestTimer();
    }


    public boolean isChestSpawned() { return this.spawned; }

    public void clear() {
        this.position.getBlock().setType(Material.AIR);
        if(this.position.getBlock().getState() instanceof Chest) ((Chest)this.position.getBlock().getState()).getInventory().clear();
        opened = false;
        spawned = false;
        isCancelled = false;
        time = 5;
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
                    for(int i = 0; i < 5; i++) {
                        ItemStack vert = new ItemStack(Material.GREEN_CONCRETE, 1);
                        ItemStack rouge = new ItemStack(Material.RED_CONCRETE, 1);
                        if(i <= 5 - timeLeft) {
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
                        generateChestContent();

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

    private void generateChestContent() {


        Block block = position.getBlock();
        position.getBlock().setType(Material.CHEST);
        Chest chest = (Chest)block.getState();
        Inventory inv = chest.getInventory();

        Range[] probabilites = new Range[4];
        probabilites[0] = new Range(Material.IRON_INGOT, 0, 75);
        probabilites[1] = new Range(Material.GOLD_INGOT, 75, 90);
        probabilites[2] = new Range(Material.DIAMOND, 90, 97);
        probabilites[3] = new Range(Material.EMERALD, 97, 101);

        int maxItem, minItem;
        maxItem = 30;
        minItem = 10;
        Random r = new Random();
        // Formule pour generer un nombre entre [X .... Y]
        // ((Y - X) + 1) + X
        int nbItem = r.nextInt((maxItem - minItem) + 1) + minItem;

        try {
            for(int i = 0; i < nbItem; i++) {
                inv.addItem(new ItemStack(Range.getInsideRange(probabilites, r.nextInt(100)),1));
            }
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

}
