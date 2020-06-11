package fr.mineral.Core.Referee.Items;

import fr.mineral.Core.Referee.Inventory.InventoryTemplate;
import fr.mineral.Translation.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class OpenPlayerInventory extends RefereeItemTemplate {

    public OpenPlayerInventory(Object target, InventoryTemplate inventaireSource) {
        super(target, inventaireSource);
    }

    @Override
    public void performClick(Player joueur) {
        if (target instanceof Player) {
            Inventory copiedInventory = Bukkit.createInventory(joueur, 54, Lang.referee_item_inventory_of_player_title + ((Player) target).getDisplayName());
            Inventory targetInventory = ((Player) target).getInventory();

            for (ItemStack item : targetInventory.getContents())
                if (item != null && !item.getType().equals(Material.AIR))
                    copiedInventory.addItem(item);

            for (int i = 0; i < 10; ++i)
                copiedInventory.addItem(new ItemStack(Material.AIR, 1));

            for (ItemStack armure : targetInventory.getStorageContents())
                if (armure != null && !armure.getType().equals(Material.AIR))
                    copiedInventory.addItem(armure);


            joueur.closeInventory();
            joueur.openInventory(copiedInventory);

        }
    }

    @Override
    public String getNomItem() {
        return Lang.referee_item_inventory_of_player_title.toString() + ((Player) target).getDisplayName();
    }

    @Override
    public String getDescriptionItem() {
        return Lang.referee_item_inventory_of_player_description.toString() + ((Player) target).getDisplayName();
    }

    @Override
    public Material getItemMaterial() {
        return Material.CHEST;
    }
}
