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


    public boolean isChestSpawned() { return this.spawned; }

    public CoffreAvecCooldown(Location loc) {
        this.position = loc;
        coffre = this;
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



        }catch (Exception e) {
            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixErreur + e.getMessage());

        }

    }

    public void close() {

        if(timeLeft == 0) {
            Block block = position.getBlock();
            block.breakNaturally();
        }

        timeLeft = time;
        opened = false;
        spawned = false;
    }

    public void open(Player joueur) {
        if(!opened) {
            opened = true;
            openingPlayer = joueur;

            new BukkitRunnable() {
                @Override
                public void run() {
                    joueur.sendMessage(timeLeft + " - " + time);
                    joueur.updateInventory();
                    // SI l'utilisateur a fermé le menu, on arrête le timer
                    if(isCancelled) {
                        this.cancel();
                        isCancelled = false;
                    }


                    if(opened && timeLeft >= 0) {
                        Block block = position.getBlock();
                        Chest chest = (Chest)block.getState();
                        Inventory inv = chest.getInventory();
                        chest.setCustomName(ChatColor.RED + Lang.get("arena_chest_title"));
                        inv.clear();
                        inv.setMaxStackSize(1);

                        for(int i = 0; i < 5; i++) {

                            ItemStack vert = new ItemStack(XMaterial.WOOL.parse(), 1);
                            ItemStack rouge = new ItemStack(XMaterial.WOOL.parse(), 1);

                            vert.setDurability(new Wool(DyeColor.GREEN).getData());
                            rouge.setDurability(new Wool(DyeColor.RED).getData());

                            if(i <= 5 - timeLeft) {
                                inv.setItem(i, vert);
                            } else {
                                inv.setItem(i, rouge);
                            }
                        }




                        if(--timeLeft > 0) {

                            joueur.playNote(joueur.getLocation(), Instrument.PIANO, new Note(1));
                        }else{
                            try {
                                joueur.playNote(joueur.getLocation(), Instrument.PIANO, new Note(24));
                                inv.clear();
                                inv.setMaxStackSize(64);
                                generateChestContent();

                                for(ItemStack item : inv.getContents()) {
                                    if(item != null) {
                                        joueur.getInventory().addItem(item);
                                        joueur.playSound(joueur.getLocation(), Sound.ENTITY_ITEM_PICKUP, 50, 1);
                                    }


                                }
                                inv.clear();
                                position.getBlock().breakNaturally();
                                joueur.closeInventory();

                                opened = false;
                                mineralcontest.plugin.getGame().getArene().disableTeleport();

                                this.cancel();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        // Si coffre non ouvert/fermé
                    } else {
                        this.cancel();
                    }


                }
            }.runTaskTimer(mineralcontest.plugin, 0, 20);
        } else {
            joueur.sendTitle(Lang.get("error"), Lang.get("arena_chest_being_opened"), 1, 5, 1);
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
