package fr.synchroneyes.groups.Menus;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.groups.Core.MapVote;
import fr.synchroneyes.mineral.Core.Referee.Inventory.InventoryTemplate;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

/**
 * Pour le menu de vote on utilise la classe Inventory Template
 */
public class MenuVote extends InventoryTemplate {

    public MenuVote() {
        this.inventaire = Bukkit.createInventory(null, 9 * 6, getNomInventaire());

    }

    @Override
    public void setInventoryItems(Player arbitre) {
        Groupe playerGroup = mineralcontest.getPlayerGroupe(arbitre);

        if (playerGroup == null) return;
        MapVote mapVote = playerGroup.getMapVote();
        if (mapVote == null) {
            mapVote = new MapVote();
        }

        List<String> liste_maps = mapVote.getMaps();
        for (String nomMap : liste_maps)
            registerItem(new MapItem(nomMap, playerGroup, this));
    }

    @Override
    public Material getItemMaterial() {
        return Material.PAPER;
    }

    @Override
    public String getNomInventaire() {
        return Lang.map_downloader_inventory_maps_list_name.toString();
    }

    @Override
    public String getDescriptionInventaire() {
        return "Vote pour un item !";
    }

    public Inventory getInventory() {
        return this.inventaire;
    }


}
