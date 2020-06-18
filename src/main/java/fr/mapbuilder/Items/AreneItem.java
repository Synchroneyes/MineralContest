package fr.mapbuilder.Items;

import fr.mapbuilder.Blocks.BlocksDataColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AreneItem {

    private ItemStack item;
    public static String itemPrefix = "Arene";

    public AreneItem() {
        this.item = new ItemStack(Material.CHEST, 1);
        ItemMeta itemMeta = this.item.getItemMeta();
        itemMeta.setDisplayName(itemPrefix);
        this.item.setItemMeta(itemMeta);
    }

    public void giveItemToPlayer(Player p) {
        p.getInventory().addItem(item);
    }

}
