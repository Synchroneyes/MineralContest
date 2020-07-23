package fr.synchroneyes.mineral.Shop.Items.Informations;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Shop.Items.Abstract.ConsumableItem;
import fr.synchroneyes.mineral.Shop.ShopManager;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Location;
import org.bukkit.Material;

public class ProchainLargageAerienPosition extends ConsumableItem {

    @Override
    public String getNomItem() {
        return Lang.shopitem_next_airdrop_location_title.toString();
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{Lang.shopitem_next_airdrop_description_1.toString(), Lang.shopitem_next_airdrop_description_2.toString(), Lang.shopitem_next_airdrop_description_3.toString()};
    }

    @Override
    public Material getItemMaterial() {
        return Material.CARTOGRAPHY_TABLE;
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

        Location positionDrop = playerGroup.getGame().getParachuteManager().getNextDropLocation();

        String prochainLargageTexte = Lang.shopitem_next_airdrop_onitemuse_location.toString();

        prochainLargageTexte = prochainLargageTexte.replace("%x", positionDrop.getBlockX() + "");
        prochainLargageTexte = prochainLargageTexte.replace("%z", positionDrop.getBlockZ() + "");

        joueur.sendMessage(mineralcontest.prefixPrive + prochainLargageTexte);

    }

    @Override
    public int getPrice() {
        return ShopManager.getBonusPriceFromName("next_airdrop_location");
    }

}
