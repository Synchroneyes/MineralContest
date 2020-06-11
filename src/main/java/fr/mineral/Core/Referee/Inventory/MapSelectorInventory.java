package fr.mineral.Core.Referee.Inventory;

import fr.groups.Core.Groupe;
import fr.groups.Core.MapVote;
import fr.groups.Utils.Etats;
import fr.mineral.Core.Referee.Items.LoadMapItem;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class MapSelectorInventory extends InventoryTemplate {

    @Override
    public void setInventoryItems(Player arbitre) {
        Groupe groupe = mineralcontest.getPlayerGroupe(arbitre);
        if (groupe == null) return;

        // On va utiliser mapVote pour récuperer la liste des maps installées
        MapVote mapVote = new MapVote();
        List<String> maps = mapVote.getMaps();

        // On ajoute les maps à l'inventaire
        for (String map : maps) {
            registerItem(new LoadMapItem(map, null, this));
        }

    }

    @Override
    public Material getItemMaterial() {
        return Material.COMPASS;
    }

    @Override
    public String getNomInventaire() {
        return Lang.referee_item_inventory_map_selector_title.toString();
    }

    @Override
    public String getDescriptionInventaire() {
        return Lang.referee_item_inventory_map_selector_description.toString();
    }
}
