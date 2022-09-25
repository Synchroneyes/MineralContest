package fr.synchroneyes.world_downloader.Items;

import fr.synchroneyes.groups.Core.MapVote;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import fr.synchroneyes.world_downloader.MapInfo;
import fr.synchroneyes.world_downloader.WorldDownloader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AllMapDownloadItem extends ItemInterface{

    private List<MapDownloadItem> mapsInfo = new ArrayList<>();
    private List<MapDownloadItem> mapsToDownload = new ArrayList<>();

    private BossBar downloadStatusBar;

    @Override
    public Material getItemMaterial() {
        return Material.DIAMOND;
    }

    @Override
    public String getNomInventaire() {
        return "ALL MAPS";
    }

    @Override
    public String getDescriptionInventaire() {
        return "Permet de télécharger toutes les cartes disponibles";
    }

    @Override
    public synchronized void performClick(Player joueur) {
        LinkedList<MapInfo> maps_available = WorldDownloader.getMaps(false);

        MapVote mapVote = new MapVote();
        ArrayList<String> maps_telecharger = mapVote.getMaps();

        for (MapInfo map : maps_available)
            if (!maps_telecharger.contains(map.map_folder_name))
                mapsToDownload.add(MapDownloadItem.fromMapInfo(map));

        if(mapsToDownload.isEmpty()) return;

        mineralcontest.plugin.getServer().getScheduler().runTaskAsynchronously(mineralcontest.plugin, () -> {
            // Now download each item
            // download
            double currentMapIndex = 1;
            double maxMapIndex = mapsToDownload.size()+1;
            for(MapDownloadItem map : mapsToDownload){
                updateDownloadBar(joueur, currentMapIndex, maxMapIndex, map.getMapName());
                try {
                    WorldDownloader.download(map, joueur);
                    currentMapIndex++;
                } catch (Exception e) {
                    joueur.sendMessage(e.getMessage());
                    e.printStackTrace();
                }

            }

            downloadStatusBar.removeAll();
        });

    }



    public void updateDownloadBar(Player downloader, double currentMapIndex, double maxIndex, String currentMapName) {
        if (downloadStatusBar == null)
            downloadStatusBar = Bukkit.createBossBar(currentMapName, BarColor.BLUE, BarStyle.SOLID);

        downloadStatusBar.setTitle(currentMapName + " " + ((int)currentMapIndex) + "/" + ((int)maxIndex));
        double status = (currentMapIndex / maxIndex);
        downloadStatusBar.setProgress(status);
        downloadStatusBar.addPlayer(downloader);
    }
}
