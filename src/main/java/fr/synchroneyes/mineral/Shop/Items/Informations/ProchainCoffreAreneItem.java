package fr.synchroneyes.mineral.Shop.Items.Informations;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Shop.Items.Abstract.ConsumableItem;
import fr.synchroneyes.mineral.Utils.TimeConverter;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Material;

public class ProchainCoffreAreneItem extends ConsumableItem {
    @Override
    public String getNomItem() {
        return "Prochaine apparition coffre arène";
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{"Vous affiche à quel moment le coffre d'arène va apparaitre"};
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

        joueur.sendMessage(mineralcontest.prefixPrive + " Le coffre d'arène va apparaitre dans " + (TimeConverter.intToString(tempsRestant).replace(":", " minutes ")));
    }


    @Override
    public int getPrice() {
        return 1;
    }

    @Override
    public void onPlayerBonusAdded() {
        onItemUse();
    }
}
