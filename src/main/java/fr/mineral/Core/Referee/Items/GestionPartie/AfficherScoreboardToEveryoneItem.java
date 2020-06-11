package fr.mineral.Core.Referee.Items.GestionPartie;

import fr.groups.Core.Groupe;
import fr.mineral.Core.House;
import fr.mineral.Core.Referee.Inventory.InventoryTemplate;
import fr.mineral.Core.Referee.Items.RefereeItemTemplate;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class AfficherScoreboardToEveryoneItem extends RefereeItemTemplate {


    public AfficherScoreboardToEveryoneItem(Object target, InventoryTemplate inventaireSource) {
        super(target, inventaireSource);
    }

    @Override
    public void performClick(Player joueur) {
        Groupe groupe = mineralcontest.getPlayerGroupe(joueur);
        if (groupe == null || !groupe.getGame().isGameStarted()) return;

        for (House maison : groupe.getGame().getHouses())
            if (!maison.getTeam().getJoueurs().isEmpty())
                groupe.sendToEveryone(Lang.translate(Lang.team_score.toString(), maison.getTeam()));
    }

    @Override
    public String getNomItem() {
        return Lang.referee_item_show_score_to_everyone_title.toString();
    }

    @Override
    public String getDescriptionItem() {
        return Lang.referee_item_show_score_to_everyone_description.toString();
    }

    @Override
    public Material getItemMaterial() {
        return Material.EMERALD;
    }
}
