package fr.synchroneyes.mineral.Shop.Items.Informations;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Shop.Items.Abstract.ConsumableItem;
import fr.synchroneyes.mineral.Shop.ShopManager;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.TimeConverter;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Material;

public class ProchainCoffreAreneItem extends ConsumableItem {
    @Override
    public String getNomItem() {
        return Lang.shopitem_next_arenachest_time_title.toString();
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{Lang.shopitem_next_arenachest_time_desc.toString()};
    }

    @Override
    public Material getItemMaterial() {
        return Material.BOOK;
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

        int tempsRestant = playerGroup.getGame().getArene().getTIME_BEFORE_CHEST();

        String tempsRestantText = Lang.shopitem_next_arenachest_onitemuse.toString();
        tempsRestantText = tempsRestantText.replace("%time", TimeConverter.intToString(tempsRestant));

        joueur.sendMessage(mineralcontest.prefixPrive + tempsRestantText);
    }


    @Override
    public int getPrice() {
        return ShopManager.getBonusPriceFromName("next_arenachest_time");
    }

    @Override
    public void onPlayerBonusAdded() {
        onItemUse();
    }
}
