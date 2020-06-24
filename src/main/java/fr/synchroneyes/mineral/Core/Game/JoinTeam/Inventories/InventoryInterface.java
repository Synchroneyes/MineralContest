package fr.synchroneyes.mineral.Core.Game.JoinTeam.Inventories;

import fr.synchroneyes.mineral.Core.Game.JoinTeam.Items.ItemInterface;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class InventoryInterface {


    protected LinkedList<ItemInterface> items;
    protected Inventory inventaire;

    // Si oui, on l'affiche dans le menu principal
    protected boolean displayInMainMenu = true;

    public InventoryInterface(boolean displayInMainMenu) {
        this.items = new LinkedList<>();
        this.inventaire = Bukkit.createInventory(null, 54, getNomInventaire());
        inventaire.setMaxStackSize(1);
        this.displayInMainMenu = displayInMainMenu;
    }

    public InventoryInterface(boolean displayInMainMenu, int inventorySize) {
        this.items = new LinkedList<>();
        this.inventaire = Bukkit.createInventory(null, inventorySize, getNomInventaire());
        inventaire.setMaxStackSize(1);
        this.displayInMainMenu = displayInMainMenu;
    }

    public void registerItem(ItemInterface itemTemplate) {
        this.items.add(itemTemplate);
    }

    public void clearItems() {
        this.items.clear();
    }

    public boolean isDisplayInMainMenu() {
        return displayInMainMenu;
    }

    public abstract void setInventoryItems();

    public void openInventory(Player admin) {
        inventaire.clear();
        clearItems();
        setInventoryItems();


        admin.closeInventory();
        admin.openInventory(inventaire);
    }

    public Inventory getInventory() {
        inventaire.clear();
        clearItems();
        setInventoryItems();

        return inventaire;
    }


    public LinkedList<ItemInterface> getItems() {
        setInventoryItems();
        return items;
    }


    public abstract Material getItemMaterial();

    public abstract String getNomInventaire();

    public abstract String getDescriptionInventaire();

    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(getItemMaterial(), 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(getNomInventaire());

        List<String> description = new ArrayList<>();
        description.add(getDescriptionInventaire());

        itemMeta.setLore(description);

        item.setItemMeta(itemMeta);
        return item;
    }

    /**
     * Retourne vrai si l'item passé en paramètre est le même que l'inventaire (toItemStack)
     *
     * @param item
     * @return
     */
    public boolean isRepresentedItemStack(ItemStack item) {
        return item.equals(toItemStack());
    }

    /**
     * Retourne vrai si cet inventaire est égal à celui passé en paramètre
     *
     * @param i
     * @return
     */
    public boolean isEqualsToInventory(Inventory i) {
        return i.equals(inventaire);
    }

    public LinkedList<ItemInterface> getObjets() {
        return items;
    }


}
