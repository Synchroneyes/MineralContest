package fr.synchroneyes.mineral.Shop.Items.Items;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.House;
import fr.synchroneyes.mineral.Shop.Items.Abstract.PermanentItem;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Boussole extends PermanentItem {
    @Override
    public String getNomItem() {
        return Lang.shopitem_compass_title.toString();
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{Lang.shopitem_compass_desc.toString()};
    }

    @Override
    public Material getItemMaterial() {
        return Material.COMPASS;
    }

    @Override
    public boolean isEnabledOnRespawn() {
        return true;
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
        ItemStack boussole = new ItemStack(Material.COMPASS);
        ItemMeta meta = boussole.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + Lang.translate(getNomItem()));

        boussole.setItemMeta(meta);


        if (mineralcontest.getPlayerGame(joueur) != null) {
            Game partie = mineralcontest.getPlayerGame(joueur);
            House equipeJoueur = partie.getPlayerHouse(joueur);

            if (equipeJoueur != null) joueur.setCompassTarget(equipeJoueur.getHouseLocation());
        }

        joueur.getInventory().addItem(boussole);
    }

    @Override
    public int getPrice() {
        return 200;
    }

}
