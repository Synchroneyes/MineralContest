package fr.synchroneyes.world_downloader.Items;

import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CancelMapDeletionItem extends ItemInterface {



    @Override
    public Material getItemMaterial() {
        return Material.GREEN_CONCRETE;
    }

    @Override
    public String getNomInventaire() {
        return Lang.referee_item_cancel_game_stop_title.toString();
    }

    @Override
    public String getDescriptionInventaire() {
        return "";
    }

    @Override
    public void performClick(Player joueur) {
        joueur.closeInventory();
    }
}
