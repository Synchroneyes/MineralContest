package fr.mineral.Core.Referee.Items.StopGameConfirm;

import fr.groups.Core.Groupe;
import fr.mineral.Core.Referee.Inventory.InventoryTemplate;
import fr.mineral.Core.Referee.Items.RefereeItemTemplate;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ConfirmItem extends RefereeItemTemplate {
    public ConfirmItem(Object target, InventoryTemplate inventaireSource) {
        super(target, inventaireSource);
    }

    @Override
    public void performClick(Player joueur) {
        Groupe groupe = mineralcontest.getPlayerGroupe(joueur);
        if (groupe == null || !groupe.getGame().isGameStarted()) return;
        try {
            groupe.getGame().terminerPartie();
        } catch (Exception e) {
            e.printStackTrace();
            Error.Report(e, groupe.getGame());
        }
    }

    @Override
    public String getNomItem() {
        return Lang.referee_item_confirm_game_stop_title.toString();
    }

    @Override
    public String getDescriptionItem() {
        return Lang.referee_item_confirm_game_stop_title.toString();
    }

    @Override
    public Material getItemMaterial() {
        return Material.GREEN_CONCRETE;
    }
}
