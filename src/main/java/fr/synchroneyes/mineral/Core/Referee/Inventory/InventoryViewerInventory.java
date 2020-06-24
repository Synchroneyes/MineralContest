package fr.synchroneyes.mineral.Core.Referee.Inventory;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Referee.Items.OpenPlayerInventory;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InventoryViewerInventory extends InventoryTemplate {
    @Override
    public void setInventoryItems(Player arbitre) {
        Groupe groupe = mineralcontest.getPlayerGroupe(arbitre);

        if (groupe == null) return;

        // On récupère tous les joueurs non arbitre
        for (Player joueur : groupe.getPlayers())
            if (!groupe.getGame().isReferee(joueur))
                // On crée un item avec leur nom
                registerItem(new OpenPlayerInventory(joueur, this));
    }

    @Override
    public Material getItemMaterial() {
        return Material.CHEST;
    }

    @Override
    public String getNomInventaire() {
        return Lang.referee_item_player_inventory_title.toString();
    }

    @Override
    public String getDescriptionInventaire() {
        return Lang.referee_item_player_inventory_description.toString();
    }
}
