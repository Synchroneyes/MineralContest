package fr.synchroneyes.mineral.Shop.Items.Informations;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Shop.Items.Abstract.ConsumableItem;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.TimeConverter;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Material;

public class ProchainLargageAerienTemps extends ConsumableItem {
    @Override
    public void onPlayerBonusAdded() {
        onItemUse();
    }

    @Override
    public String getNomItem() {
        return Lang.shopitem_next_airdrop_time_title.toString();
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{Lang.shopitem_next_airdrop_description_1.toString(), Lang.shopitem_next_airdrop_description_2.toString(), Lang.shopitem_next_airdrop_time_desc.toString()};
    }

    @Override
    public Material getItemMaterial() {
        return Material.FLETCHING_TABLE;
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

        int tempsRestant = playerGroup.getGame().getParachuteManager().getTimeleft_before_next_drop();

        String tempsRestantText = Lang.shopitem_next_airdrop_onitemuse_timeleft.toString();
        tempsRestantText = tempsRestantText.replace("%time", TimeConverter.intToString(tempsRestant));
        joueur.sendMessage(mineralcontest.prefixPrive + tempsRestantText);

    }

    @Override
    public int getPrice() {
        return 1000;
    }

}
