package fr.mineral.Core.Referee.Inventory;

import fr.mineral.Core.Referee.Items.RefereeItemTemplate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class InventoryTemplate {


    protected LinkedList<RefereeItemTemplate> items;
    protected Inventory inventaire;

    public InventoryTemplate() {
        this.items = new LinkedList<>();
        this.inventaire = Bukkit.createInventory(null, 27, getNomInventaire());
        inventaire.setMaxStackSize(1);
    }

    public void registerItem(RefereeItemTemplate itemTemplate) {
        this.items.add(itemTemplate);
    }


    public abstract void setInventoryItems(Player arbitre);

    public void openInventory(Player arbitre) {
        setInventoryItems(arbitre);
        for (RefereeItemTemplate item : items) {
            inventaire.addItem(item.toItemStack());
        }

        arbitre.closeInventory();
        arbitre.openInventory(inventaire);
    }


    public LinkedList<RefereeItemTemplate> getItems() {
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

    public LinkedList<RefereeItemTemplate> getObjets() {
        return items;
    }


    /**
     * Ajoute des espaces dans l'inventaire
     *
     * @param number
     */
    public void addSpaces(int number) {
        for (int i = 0; i < number; ++i)
            inventaire.addItem(new ItemStack(Material.AIR, 1));
    }

}
