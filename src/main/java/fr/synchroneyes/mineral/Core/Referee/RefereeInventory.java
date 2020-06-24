package fr.synchroneyes.mineral.Core.Referee;


import fr.synchroneyes.mineral.Core.Referee.Inventory.*;
import fr.synchroneyes.mineral.Core.Referee.Items.RefereeItemTemplate;
import fr.synchroneyes.mineral.Core.Referee.Items.SetInvisibleItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.LinkedList;

public class RefereeInventory {
    private Inventory inventory;
    protected static LinkedList<InventoryTemplate> inventaires;
    protected static LinkedList<RefereeItemTemplate> items;


    public static RefereeInventory instance;

    private RefereeInventory() {
        if (inventaires == null) {
            inventaires = new LinkedList<>();
            items = new LinkedList<>();
            registerInventories();
            registerItems();
        }
        this.inventory = Bukkit.createInventory(null, 9, "Menu Arbitrage");
        RefereeInventory.instance = this;
        fillInventory();
    }

    protected static LinkedList<RefereeItemTemplate> getItems() {
        return items;
    }


    private void registerItems() {
        //items.add(new TeleportMenuInventory());
        items.add(new SetInvisibleItem(null, null));
    }

    private void registerInventories() {
        inventaires.add(new TeleportMenuInventory());
        inventaires.add(new InventoryViewerInventory());
        inventaires.add(new TeamChestInventory());
        inventaires.add(new MapSelectorInventory());
        inventaires.add(new GestionPartieInventory());
        inventaires.add(new StopGameInventory());
    }

    private void fillInventory() {


        inventory.clear();

        for (InventoryTemplate inventaire : inventaires) {
            inventory.addItem(inventaire.toItemStack());
        }

        for (RefereeItemTemplate item : items) {
            inventory.addItem(item.toItemStack());
        }

    }



    public static Inventory getInventory() {
        if (instance == null) {
            new RefereeInventory();
        }

        RefereeInventory refereeInventory = RefereeInventory.instance;
        refereeInventory.fillInventory();

        return refereeInventory.inventory;
    }
}
