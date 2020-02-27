package fr.mineral.Core.MapBuilder;

import com.sun.istack.internal.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class MapBuilderInventory {
    public static MapBuilderInventory instance;
    private Inventory inventory;
    public static String inventoryName = "InventoryName";

    private int inventoryLines = 3;

    private int getInventorySize() { return this.inventoryLines * 9;}

    private MapBuilderInventory(int numberOfLines) {
        this.inventoryLines = numberOfLines;
        this.inventory = Bukkit.createInventory(null, getInventorySize(), inventoryName);
        MapBuilderInventory.instance = this;
    }

    private void fillInventory() {

    }

    public static void initializeMapBuilderInventory() {
        MapBuilderInventory mapBuilderInventory = new MapBuilderInventory(3);
    }

}
