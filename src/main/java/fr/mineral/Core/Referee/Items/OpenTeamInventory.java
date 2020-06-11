package fr.mineral.Core.Referee.Items;

import fr.mineral.Core.House;
import fr.mineral.Core.Referee.Inventory.InventoryTemplate;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class OpenTeamInventory extends RefereeItemTemplate {

    public OpenTeamInventory(Object target, InventoryTemplate inventaireSource) {
        super(target, inventaireSource);
    }

    @Override
    public void performClick(Player joueur) {
        if (target instanceof House) {
            House maison = (House) target;
            joueur.closeInventory();

            Inventory coffreMaison = null;
            try {
                Block block_coffre = maison.getCoffreEquipeLocation().getBlock();
                Chest coffre = ((Chest) block_coffre.getState());
                coffreMaison = coffre.getInventory();
                joueur.openInventory(coffreMaison);
                joueur.sendMessage(mineralcontest.prefixPrive + Lang.translate(Lang.referee_team_current_score.toString(), maison.getTeam()));
            } catch (Exception e) {
                Error.Report(e, mineralcontest.getPlayerGame(joueur));
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getNomItem() {
        return Lang.translate(Lang.referee_item_team_chest_item_title.toString(), ((House) target).getTeam());
    }

    @Override
    public String getDescriptionItem() {
        return Lang.translate(Lang.referee_item_team_chest_item_description.toString(), ((House) target).getTeam());
    }

    @Override
    public Material getItemMaterial() {
        return Material.CHEST;
    }
}
