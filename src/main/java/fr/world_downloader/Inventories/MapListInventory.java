package fr.world_downloader.Inventories;

import fr.mineral.Translation.Lang;
import fr.world_downloader.Items.MapDownloadItem;
import fr.world_downloader.MapInfo;
import fr.world_downloader.WorldDownloader;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class MapListInventory extends InventoryInterface {


    public MapListInventory(boolean displayInMainMenu) {
        super(displayInMainMenu);
    }

    @Override
    public void setInventoryItems(Player arbitre) {
        LinkedList<MapInfo> maps = WorldDownloader.getMaps(false);
        for (MapInfo map : maps)
            registerItem(MapDownloadItem.fromMapInfo(map));
    }

    @Override
    public Material getItemMaterial() {
        return Material.MAP;
    }

    @Override
    public String getNomInventaire() {
        return Lang.map_downloader_inventory_name.toString();
    }

    @Override
    public String getDescriptionInventaire() {
        return Lang.map_downloader_inventory_name.toString();
    }
}
