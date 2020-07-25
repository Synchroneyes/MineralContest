package fr.synchroneyes.mineral.Shop.Items.Permanent;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Shop.Items.Abstract.PermanentItem;
import fr.synchroneyes.mineral.Shop.ShopManager;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class EpeeDiamant extends PermanentItem {


    // Nom de l'item
    public static String itemNameColored = ChatColor.AQUA + Lang.shopitem_diamond_sword_title.toString();


    @Override
    public String getNomItem() {
        return Lang.shopitem_diamond_sword_title.toString();
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{Lang.shopitem_diamond_sword_desc.toString()};
    }

    @Override
    public Material getItemMaterial() {
        return Material.DIAMOND_SWORD;
    }

    @Override
    public void onItemUse() {

        Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);

        if (playerGroup == null) return;


        // On supprime l'épée de base
        List<ItemStack> items_de_base = playerGroup.getPlayerBaseItem().getItems();
        for (ItemStack item : items_de_base)
            if (item != null && item.getType().toString().toLowerCase().contains("sword"))
                joueur.getInventory().remove(item);


        // On prépare l'item
        ItemStack epee_diamant = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = epee_diamant.getItemMeta();

        if (meta != null) meta.setDisplayName(itemNameColored);

        epee_diamant.setItemMeta(meta);


        joueur.getInventory().addItem(epee_diamant);
    }


    @Override
    public int getPrice() {
        return ShopManager.getBonusPriceFromName("permanent_diamond_sword");
    }

}
