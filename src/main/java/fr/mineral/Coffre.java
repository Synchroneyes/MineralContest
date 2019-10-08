package fr.mineral;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Coffre {
    private Location position;
    private boolean actif = false;

    public void setPosition(Location p) {
        this.position = p;
    }

    public Location getPosition() throws Exception {
        if(this.position == null)
            throw new Exception(mineralcontest.ERROR_CHEST_NOT_DEFINED);
        return this.position;
    }

    public void spawn() {
        Location loc;
        try {
            loc = this.getPosition();
            Block block = loc.getBlock();
            loc.getBlock().setType(Material.CHEST);
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
            for(int i = 0; i < nbItem; i++) {
                inv.addItem(new ItemStack(Range.getInsideRange(probabilites, r.nextInt(100)),1));
            }


        }catch (Exception e) {
            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixErreur + e.getMessage());

        }
    }
}
