package fr.mineral.Core.MapBuilder;

import fr.mineral.Core.MapBuilder.Item.HouseEgg;
import fr.mineral.Translation.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MapBuilderInventory {
    public static boolean initialized = false;
    private static MapBuilderInventory instance;
    private Inventory inventory;
    public static String inventoryName = "InventoryName";

    private int inventoryLines = 3;

    private int getInventorySize() { return this.inventoryLines * 9;}

    private MapBuilderInventory(int numberOfLines) {
        this.inventoryLines = numberOfLines;
        this.inventory = Bukkit.createInventory(null, getInventorySize(), inventoryName);
        MapBuilderInventory.instance = this;
        initialized = true;

    }

    public Inventory getInventory() {
        return inventory;
    }

    private void fillInventory() {
        /*
            9 - 17
         */

        inventory.clear();
        ItemStack houseEgg = new ItemStack(Material.EGG);
        ItemMeta houseEggMeta = houseEgg.getItemMeta();
        houseEggMeta.setDisplayName(Lang.map_builder_item_name.toString());
        houseEgg.setItemMeta(houseEggMeta);



        inventory.setItem(9, houseEgg);

    }

    public static MapBuilderInventory getInstance() {
        if(!initialized) return initializeMapBuilderInventory();
        return instance;
    }

    public void openInventory(Player player) throws Exception{
        if(initialized) {
            this.fillInventory();
            player.openInventory(inventory);
        }
        else throw new Exception("InventoryBuilder not initialized");
    }

    public static MapBuilderInventory initializeMapBuilderInventory() {
        if(!initialized) {
            MapBuilderInventory mapBuilderInventory = new MapBuilderInventory(3);
            return mapBuilderInventory;
        }

        return instance;

    }

}
