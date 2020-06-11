package fr.mineral.Core.Referee.Items;

import fr.groups.Core.Groupe;
import fr.groups.Utils.Etats;
import fr.mineral.Core.Referee.Inventory.InventoryTemplate;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class LoadMapItem extends RefereeItemTemplate {


    public LoadMapItem(Object target, InventoryTemplate inventaireSource) {
        super(target, inventaireSource);
    }

    public LoadMapItem(String nomMap, Object target, InventoryTemplate inventaireSource) {
        super(nomMap, target, inventaireSource);
    }

    @Override
    public void performClick(Player joueur) {
        Groupe groupe = mineralcontest.getPlayerGroupe(joueur);
        if (groupe == null || !groupe.getEtatPartie().equals(Etats.EN_ATTENTE)) {
            joueur.sendMessage(mineralcontest.prefixErreur + Lang.referee_error_map_selector_only_hub.toString());
            return;
        }

        if (customName.length() > 1) {
            groupe.chargerMonde(customName);
        }
    }

    @Override
    public String getNomItem() {
        return customName;
    }

    @Override
    public String getDescriptionItem() {
        return Lang.referee_item_map_selector_description.toString() + customName;
    }

    @Override
    public Material getItemMaterial() {
        return Material.PAPER;
    }
}
