package fr.synchroneyes.mapbuilder.Menus.SousMenu;

import fr.synchroneyes.mapbuilder.Menus.Objets.ObjetTemplate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe 'template' pour les sous menus
 */
public abstract class MenuTemplate {

    // Où les items sont stocké
    private Inventory inventaire;

    // Variable permettant d'afficher cet inventaire dans le menu principal
    private boolean displayInventoryInMainMenu;

    // Liste contenant les objets à afficher avec leur position
    private HashMap<ObjetTemplate, Integer> objets;

    public boolean isDisplayInventoryInMainMenu() {
        return displayInventoryInMainMenu;
    }

    public List<ObjetTemplate> getObjets() {
        List<ObjetTemplate> objet_list = new ArrayList<>();
        for (Map.Entry<ObjetTemplate, Integer> couple : objets.entrySet()) {
            objet_list.add(couple.getKey());
        }
        return objet_list;
    }

    /**
     * Constructeurs , permet d'initialiser l'inventaire avec un certain nombre de ligne et d'activer ou non l'affichage
     *
     * @param displayInventoryInMainMenu - Boolean permettant d'afficher ou non l'inventaire
     * @param nombreDeLigne              - Nombre de ligne voulu
     */
    public MenuTemplate(boolean displayInventoryInMainMenu, int nombreDeLigne) {
        this.objets = new HashMap<>();
        this.inventaire = Bukkit.createInventory(null, nombreDeLigne * 9, getNomInventaire());
        this.displayInventoryInMainMenu = displayInventoryInMainMenu;
    }

    /**
     * Constructeur, sautant l'étape du nombre de ligne en en mettant 3 par défaut
     *
     * @param displayInventoryInMainMenu - Boolean permettant d'afficher ou non l'inventaire
     */
    public MenuTemplate(boolean displayInventoryInMainMenu) {
        int nombreDeLigne = 3;
        this.objets = new HashMap<>();
        this.inventaire = Bukkit.createInventory(null, nombreDeLigne * 9, getNomInventaire());
        this.displayInventoryInMainMenu = displayInventoryInMainMenu;
    }

    /**
     * Constructeur créeant un menu non affichable avec 3 lignes
     */
    public MenuTemplate() {
        int nombreDeLigne = 3;
        this.objets = new HashMap<>();
        this.inventaire = Bukkit.createInventory(null, nombreDeLigne * 9, getNomInventaire());
        this.displayInventoryInMainMenu = false;
    }


    public abstract String getNomInventaire();

    public abstract List<String> getDescription();

    public abstract Material getItemMaterial();


    /**
     * Ouvre l'inventaire à un joueur
     *
     * @param p
     */
    public void openInventory(Player p) {
        inventaire.clear();

        // Pour chaque objet, on l'ajoute à notre inventaire
        objets.forEach((item, slot) -> {

            ItemStack itemStack = item.toItemStack();

            // On a ajouté un item sans préciser de position
            if (slot == -1) {
                inventaire.addItem(itemStack);
            } else {
                // On connait la position de l'item
                inventaire.setItem(slot, itemStack);
            }
        });

        // On ferme l'inventaire actuel du joueur
        p.closeInventory();

        // On l'ouvre
        p.openInventory(inventaire);
    }

    public Inventory getInventaire() {
        return inventaire;
    }

    /**
     * Permet d'enregistrer l'item pour l'afficher dans l'inventaire
     *
     * @param item
     */
    public void ajouterItem(ObjetTemplate item) {
        this.objets.put(item, -1);
    }

    /**
     * Permet d'enregister l'item pour l'afficher dans l'inventaire à une position donnée
     *
     * @param item
     * @param slot
     */
    public void ajouterItem(ObjetTemplate item, int slot) {
        this.objets.put(item, slot);
    }


    /**
     * Permet de convertir le menu en un item afin de l'afficher
     *
     * @return
     */
    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(getItemMaterial(), 1);

        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta != null) {
            itemMeta.setDisplayName(getNomInventaire());
            itemMeta.setLore(getDescription());
            item.setItemMeta(itemMeta);
        }

        return item;
    }
}
