package fr.mineral.Core.Referee.Items.GestionPartie;

import fr.groups.Core.Groupe;
import fr.mineral.Core.Referee.Inventory.InventoryTemplate;
import fr.mineral.Core.Referee.Items.RefereeItemTemplate;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class StartGameItem extends RefereeItemTemplate {

    public StartGameItem(Object target, InventoryTemplate inventaireSource) {
        super(target, inventaireSource);
    }

    @Override
    public void performClick(Player joueur) {
        Groupe groupe = mineralcontest.getPlayerGroupe(joueur);
        if (groupe == null) return;
        if (groupe.getGame() == null) return;

        if (!groupe.getGame().isGameStarted()) {
            try {
                groupe.getGame().demarrerPartie(true);
            } catch (Exception e) {
                Error.Report(e, groupe.getGame());
            }
        } else {
            joueur.sendMessage(mineralcontest.prefixErreur + Lang.game_already_started.toString());
        }
    }

    @Override
    public String getNomItem() {
        return Lang.referee_item_start.toString();
    }

    @Override
    public String getDescriptionItem() {
        return "";
    }

    @Override
    public Material getItemMaterial() {
        return Material.GREEN_CONCRETE;
    }
}
