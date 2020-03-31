package fr.mineral.Core.Referee;

import fr.mineral.Translation.Lang;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RefereeItem {
    private ItemStack item;
    public static String nomItem = Lang.referee_item_name.toString();

    public RefereeItem() {
        this.item = new ItemStack(Material.BOOK, 1);

        ItemMeta itemMeta = this.item.getItemMeta();
        itemMeta.setDisplayName(nomItem);
        this.item.setItemMeta(itemMeta);
    }

    public ItemStack getItem() {
        return this.item;
    }

}
