package fr.synchroneyes.mineral.Shop.Items.Informations;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Shop.Items.Abstract.ConsumableItem;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Location;
import org.bukkit.Material;

public class ProchainLargageAerienPosition extends ConsumableItem {

    @Override
    public String getNomItem() {
        return "Position du prochain largage";
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{"Vous voulez vous préparer pour le prochain largage?", "Ce bonus est fait pour vous.", "Vous aurez la position du prochain largage !"};
    }

    @Override
    public Material getItemMaterial() {
        return Material.MAP;
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

        joueur.sendMessage(mineralcontest.prefixPrive + " Le prochain largage va apparaitre en X: " + positionDrop.getBlockX() + " Z: " + positionDrop.getBlockZ());

    }

    @Override
    public int getPrice() {
        return 2;
    }

    @Override
    public Material getCurrency() {
        return Material.EMERALD;
    }
}
