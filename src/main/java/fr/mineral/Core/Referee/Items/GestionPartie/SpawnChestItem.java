package fr.mineral.Core.Referee.Items.GestionPartie;

import fr.groups.Core.Groupe;
import fr.mineral.Core.Referee.Inventory.InventoryTemplate;
import fr.mineral.Core.Referee.Items.RefereeItemTemplate;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SpawnChestItem extends RefereeItemTemplate {

    public SpawnChestItem(Object target, InventoryTemplate inventaireSource) {
        super(target, inventaireSource);
    }

    @Override
    public void performClick(Player joueur) {
        Groupe groupe = mineralcontest.getPlayerGroupe(joueur);
        if (groupe == null) return;
        if (groupe.getGame() == null) return;

        if (groupe.getGame().isGameStarted()) {
            try {
                groupe.getGame().getArene().getCoffre().spawn();
            } catch (Exception e) {
                e.printStackTrace();
                Error.Report(e, groupe.getGame());
            }
        } else {
            joueur.sendMessage(mineralcontest.prefixErreur + Lang.game_not_started.toString());
        }
    }

    @Override
    public String getNomItem() {
        return Lang.referee_item_spawn_arena_chest.toString();
    }

    @Override
    public String getDescriptionItem() {
        return "";
    }

    @Override
    public Material getItemMaterial() {
        return Material.CHEST;
    }
}
