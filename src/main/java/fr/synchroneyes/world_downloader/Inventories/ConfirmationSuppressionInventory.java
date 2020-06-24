package fr.synchroneyes.world_downloader.Inventories;

import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.world_downloader.Items.CancelMapDeletionItem;
import fr.synchroneyes.world_downloader.Items.ConfirmMapDeletionItem;
import fr.synchroneyes.world_downloader.MapInfo;
import fr.synchroneyes.world_downloader.WorldDownloader;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class ConfirmationSuppressionInventory extends InventoryInterface {

    private String nom_dossier;

    public ConfirmationSuppressionInventory(String nom_dossier) {
        super(false);
        this.nom_dossier = nom_dossier;
    }


    public void setNomDossier(String map) {
        this.nom_dossier = map;
    }

    @Override
    public void setInventoryItems(Player arbitre) {
        LinkedList<MapInfo> maps = WorldDownloader.getMaps(false);
        registerItem(new ConfirmMapDeletionItem(nom_dossier));
        registerItem(new CancelMapDeletionItem(nom_dossier));
    }

    @Override
    public Material getItemMaterial() {
        return Material.RED_CONCRETE;
    }

    @Override
    public String getNomInventaire() {
        return Lang.map_downloader_delete_title.toString();
    }

    @Override
    public String getDescriptionInventaire() {
        return Lang.map_downloader_delete_title.toString();
    }
}
