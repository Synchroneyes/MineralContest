package fr.synchroneyes.mapbuilder.Menus;

import fr.synchroneyes.mapbuilder.Menus.Objets.ObjetTemplate;
import fr.synchroneyes.mapbuilder.Menus.SousMenu.GestionEquipe.GestionEquipeMenu;
import fr.synchroneyes.mapbuilder.Menus.SousMenu.MenuTemplate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class MenuManager {
    // Liste de menus
    private List<MenuTemplate> menus;

    // Liste des objets présent dans le menu principal
    private List<ObjetTemplate> objets;

    private Inventory inventaire;

    public MenuManager() {
        this.menus = new ArrayList<>();
        this.objets = new ArrayList<>();
        this.inventaire = Bukkit.createInventory(null, 3 * 9, "MenuParDefaut");

        registerMenus();
        registerItems();

    }

    public void registerMenus() {
        menus.add(new GestionEquipeMenu());
    }

    public void registerItems() {

    }

    public List<MenuTemplate> getMenus() {
        return menus;
    }

    public List<ObjetTemplate> getObjets() {
        return objets;
    }

    public Inventory getInventaire() {
        return inventaire;
    }

    public void openInventory(Player p) {
        p.closeInventory();
        inventaire.clear();
        for (MenuTemplate menu : menus)
            if (menu.isDisplayInventoryInMainMenu())
                inventaire.addItem(menu.toItemStack());

        for (ObjetTemplate objet : objets)
            inventaire.addItem(objet.toItemStack());

        p.openInventory(inventaire);
    }


}
