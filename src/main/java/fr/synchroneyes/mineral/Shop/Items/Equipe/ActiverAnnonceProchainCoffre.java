package fr.synchroneyes.mineral.Shop.Items.Equipe;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Shop.Items.Abstract.ConsumableItem;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Material;

public class ActiverAnnonceProchainCoffre extends ConsumableItem {


    @Override
    public String getNomItem() {
        return Lang.shopitem_enable_next_arenachest_announce_title.toString();
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{Lang.shopitem_enable_next_arenachest_announce_desc1.toString(), Lang.shopitem_enable_next_arenachest_announce_desc2.toString()};
    }

    @Override
    public Material getItemMaterial() {
        return Material.CHEST;
    }

    @Override
    public boolean isEnabledOnRespawn() {
        return false;
    }

    @Override
    public boolean isEnabledOnPurchase() {
        return true;
    }

    @Override
    public int getNombreUtilisations() {
        return 1;
    }

    @Override
    public void onItemUse() {
        Groupe playerGroup = mineralcontest.getPlayerGroupe(this.joueur);

        if (playerGroup == null) return;

        Equipe playerTeam = playerGroup.getPlayerTeam(this.joueur);
        if (playerTeam == null) return;

        playerGroup.getGame().getArene().addTeamToNotify(playerTeam);
    }

    @Override
    public int getPrice() {
        return 350;
    }

}
