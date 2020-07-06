package fr.synchroneyes.groups.Menus;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Referee.Inventory.InventoryTemplate;
import fr.synchroneyes.mineral.Core.Referee.Items.RefereeItemTemplate;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MapItem extends RefereeItemTemplate {

    private Groupe groupe;

    public MapItem(String customName, Groupe playerGroup, InventoryTemplate inventaireSource) {
        super(customName, null, inventaireSource);
        this.groupe = playerGroup;
    }

    @Override
    public void performClick(Player joueur) {

        groupe.getMapVote().enregistrerVoteJoueur(customName, joueur);
    }

    @Override
    public String getNomItem() {
        return customName;
    }

    @Override
    public String getDescriptionItem() {
        return Lang.vote_map.toString().replace("%map%", customName);
    }

    @Override
    public Material getItemMaterial() {
        return Material.WRITABLE_BOOK;
    }
}
