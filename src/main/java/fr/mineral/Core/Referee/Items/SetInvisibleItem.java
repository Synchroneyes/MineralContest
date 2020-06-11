package fr.mineral.Core.Referee.Items;

import fr.groups.Core.Groupe;
import fr.mineral.Core.Referee.Inventory.InventoryTemplate;
import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SetInvisibleItem extends RefereeItemTemplate {

    public SetInvisibleItem(Object target, InventoryTemplate inventaireSource) {
        super(target, inventaireSource);
    }

    @Override
    public void performClick(Player joueur) {
        Groupe playerGroupe = mineralcontest.getPlayerGroupe(joueur);
        if (playerGroupe == null) return;

        boolean playerHidden = PlayerUtils.isPlayerHidden(joueur);

        if (playerHidden) joueur.sendMessage(mineralcontest.prefixPrive + "Vous êtes maintenant visible");

        else joueur.sendMessage(mineralcontest.prefixPrive + "Vous êtes maintenant invisible");

        for (Player membre_groupe : playerGroupe.getPlayers()) {

            if (!playerGroupe.getGame().isReferee(membre_groupe)) {

                if (playerHidden) {

                    membre_groupe.showPlayer(mineralcontest.plugin, joueur);

                } else {

                    membre_groupe.hidePlayer(mineralcontest.plugin, joueur);
                }
            }

        }
    }

    @Override
    public String getNomItem() {
        return "Se rendre visible/invisible";
    }

    @Override
    public String getDescriptionItem() {
        return "Permet de se rendre visible ou non par les autres joueurs";
    }

    @Override
    public Material getItemMaterial() {
        return Material.RED_BANNER;
    }
}
