package fr.synchroneyes.mineral.Core.Referee.Inventory;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.House;
import fr.synchroneyes.mineral.Core.Referee.Items.OpenTeamInventory;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TeamChestInventory extends InventoryTemplate {
    @Override
    public void setInventoryItems(Player arbitre) {
        Groupe groupe = mineralcontest.getPlayerGroupe(arbitre);
        if (groupe == null) return;

        // On va créer un objet pour chaque équipe non vide
        for (House maison : groupe.getGame().getHouses())
            if (!maison.getTeam().getJoueurs().isEmpty())
                registerItem(new OpenTeamInventory(maison, this));

    }

    @Override
    public Material getItemMaterial() {
        return Material.DIAMOND;
    }

    @Override
    public String getNomInventaire() {
        return Lang.referee_item_team_chest_inventory_title.toString();
    }

    @Override
    public String getDescriptionInventaire() {
        return Lang.referee_item_team_chest_inventory_description.toString();
    }
}
