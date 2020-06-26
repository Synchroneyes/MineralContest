package fr.synchroneyes.mapbuilder.Menus.Objets;

import fr.synchroneyes.mapbuilder.Menus.SousMenu.MenuTemplate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public abstract class ObjetTemplate {

    // Dans le cas où on doit effectuer une action sur un objet
    private Object target = null;

    private MenuTemplate parent;

    public ObjetTemplate(MenuTemplate parent, Object target) {
        this.parent = parent;
        this.target = target;
    }

    public ObjetTemplate(MenuTemplate parent) {
        this.parent = parent;
    }

    public abstract String getNomItem();

    public abstract List<String> getDescription();

    public abstract Material getItemMaterial();

    public MenuTemplate getParent() {
        return parent;
    }


    /**
     * Méthode appelée lors du clic de l'item dans un inventaire
     *
     * @param joueur
     */
    public abstract void onClick(Player joueur);


    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(getItemMaterial(), 1);

        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta != null) {
            itemMeta.setDisplayName(getNomItem());
            itemMeta.setLore(getDescription());
            item.setItemMeta(itemMeta);
        }

        return item;
    }

}
