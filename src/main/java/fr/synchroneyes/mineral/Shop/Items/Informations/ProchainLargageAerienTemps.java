package fr.synchroneyes.mineral.Shop.Items.Informations;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Shop.Items.Abstract.ConsumableItem;
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
        return "Temps restant avant largage";
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{"Vous voulez vous préparer pour le prochain largage?", "Ce bonus est fait pour vous.", "Vous aurez le temps restant avant le prochain largage !"};
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

        joueur.sendMessage(mineralcontest.prefixPrive + " Le prochain largage va apparaitre dans " + (TimeConverter.intToString(tempsRestant).replace(":", " minutes ")));

    }

    @Override
    public int getPrice() {
        return 1;
    }

}
