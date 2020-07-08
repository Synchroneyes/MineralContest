package fr.synchroneyes.mineral.Shop.Categories;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Shop.Items.Abstract.ShopItem;
import fr.synchroneyes.mineral.Shop.Players.PlayerBonus;
import fr.synchroneyes.mineral.mineralcontest;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public abstract class Category {

    // Table de hachage, contenant l'item ainsi que sa position dans l'inventaire
    private HashMap<ShopItem, Integer> items;

    @Getter
    private Inventory inventory;

    public Category() {
        this.items = new HashMap<>();
        this.inventory = Bukkit.createInventory(null, 9 * 6, getNomCategorie());
    }

    /**
     * Retourne le nom de la categorie
     *
     * @return
     */
    public abstract String getNomCategorie();


    /**
     * Définit l'item qui doit être affiché
     *
     * @return
     */
    public abstract Material getItemMaterial();


    /**
     * Retourne la description de la catégorie
     *
     * @return
     */
    public abstract String[] getDescription();

    /**
     * Retourne un itemStack
     *
     * @return
     */
    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(getItemMaterial(), 1);
        if (item.getItemMeta() != null) {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(getNomCategorie());
            List<String> description = new LinkedList<>();

            description.addAll(Arrays.asList(getDescription()));

            meta.setLore(description);

            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Permet d'ouvrir le menu d'inventaire au joueur
     *
     * @param joueur
     */
    public void openMenuToPlayer(Player joueur) {
        this.inventory.clear();

        // On récupère les items de la catégorie
        for (Map.Entry<ShopItem, Integer> item : items.entrySet())
            inventory.setItem(item.getValue(), item.getKey().toItemStack());

        joueur.closeInventory();
        joueur.openInventory(inventory);
    }

    /**
     * Ajoute un item dans l'inventaire à une position donnée
     *
     * @param item
     * @param position
     */
    public void addItemToInventory(ShopItem item, int position) {
        if (items.containsKey(item)) items.replace(item, position);
        else items.put(item, position);
    }

    /**
     * Retourne si l'ItemStack passé en paramètre est un item de la catégorie
     *
     * @param item
     * @return
     */
    public boolean isItemInInventory(ItemStack item) {
        if (item == null) return false;

        for (Map.Entry<ShopItem, Integer> infoItem : items.entrySet())
            if (infoItem.getKey().toItemStack().equals(item)) return true;
        return false;
    }

    /**
     * Evenement appelé lors d'un click sur un item de la catégorie
     *
     * @param event
     */
    public void onCategoryItemClick(InventoryClickEvent event) {
        Player joueur = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);
        if (playerGroup == null) return;

        PlayerBonus playerBonusManager = playerGroup.getGame().getPlayerBonusManager();


        if (clickedItem == null) return;

        // Pour chaque item de la catégorie
        for (Map.Entry<ShopItem, Integer> item : items.entrySet()) {
            ItemStack _inventoryItem = item.getKey().toItemStack();

            // Si on a cliqué sur un item de la catégorie

            // TODO: retirer la monnaie lors de l'achat

            if (clickedItem.equals(_inventoryItem)) {
                ShopItem _item = item.getKey();
                if (playerBonusManager.canPlayerAffordItem(item.getKey(), joueur))
                    playerBonusManager.purchaseItem(joueur, item.getKey());
                else
                    joueur.sendMessage("Vous n'avez pas assez de sous, requis: " + _item.getPrice() + " " + _item.getCurrency().toString());

                return;
            }
        }
    }

}
