package fr.synchroneyes.mineral.Core.Referee.Items.StopGameConfirm;

import fr.synchroneyes.mineral.Core.Referee.Inventory.InventoryTemplate;
import fr.synchroneyes.mineral.Core.Referee.Items.RefereeItemTemplate;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CancelItem extends RefereeItemTemplate {

    public CancelItem(Object target, InventoryTemplate inventaireSource) {
        super(target, inventaireSource);
    }

    @Override
    public void performClick(Player joueur) {
        joueur.closeInventory();
    }

    @Override
    public String getNomItem() {
        return Lang.referee_item_cancel_game_stop_title.toString();
    }

    @Override
    public String getDescriptionItem() {
        return Lang.referee_item_cancel_game_stop_title.toString();
    }

    @Override
    public Material getItemMaterial() {
        return Material.RED_CONCRETE;
    }
}
