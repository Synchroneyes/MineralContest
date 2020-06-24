package fr.synchroneyes.mineral.Core.Referee.Inventory;

import fr.synchroneyes.mineral.Core.Referee.Items.StopGameConfirm.CancelItem;
import fr.synchroneyes.mineral.Core.Referee.Items.StopGameConfirm.ConfirmItem;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class StopGameInventory extends InventoryTemplate {
    @Override
    public void setInventoryItems(Player arbitre) {
        registerItem(new ConfirmItem(null, this));
        registerItem(new CancelItem(null, this));
    }

    @Override
    public Material getItemMaterial() {
        return Material.RED_CONCRETE;
    }

    @Override
    public String getNomInventaire() {
        return Lang.referee_item_inventory_stopgame_title.toString();
    }

    @Override
    public String getDescriptionInventaire() {
        return Lang.referee_item_inventory_stopgame_description.toString();
    }
}
