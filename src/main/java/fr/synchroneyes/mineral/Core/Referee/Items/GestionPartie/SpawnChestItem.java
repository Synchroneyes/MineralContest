package fr.synchroneyes.mineral.Core.Referee.Items.GestionPartie;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Referee.Inventory.InventoryTemplate;
import fr.synchroneyes.mineral.Core.Referee.Items.RefereeItemTemplate;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.ErrorReporting.Error;
import fr.synchroneyes.mineral.mineralcontest;
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
