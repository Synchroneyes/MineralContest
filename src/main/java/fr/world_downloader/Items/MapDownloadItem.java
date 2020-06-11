package fr.world_downloader.Items;

import fr.world_downloader.WorldDownloader;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MapDownloadItem extends ItemInterface {

    private String mapUrl = "";
    private String shortDescription = "";
    private String mapName = "";

    public MapDownloadItem(String mapName, String mapUrl, String shortDescription) {
        super();
        this.mapName = mapName;
        this.mapUrl = mapUrl;
        this.shortDescription = shortDescription;
    }

    @Override
    public Material getItemMaterial() {
        return Material.PAPER;
    }

    @Override
    public String getNomInventaire() {
        return mapName;
    }

    @Override
    public String getDescriptionInventaire() {
        return shortDescription;
    }

    @Override
    public void performClick(Player joueur) {
        joueur.sendMessage("Downloading " + mapName + " @ " + mapUrl);
        try {
            WorldDownloader.download(this);
        } catch (Exception e) {
            e.printStackTrace();
            joueur.sendMessage(e.getMessage());
        }
    }
}
