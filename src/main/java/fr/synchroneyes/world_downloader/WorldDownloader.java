package fr.synchroneyes.world_downloader;

import fr.synchroneyes.groups.Core.MapVote;
import fr.synchroneyes.mapbuilder.Core.Monde;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.UrlFetcher.Urls;
import fr.synchroneyes.mineral.mineralcontest;
import fr.synchroneyes.world_downloader.Commands.mcdownload;
import fr.synchroneyes.world_downloader.Inventories.ConfirmationSuppressionInventory;
import fr.synchroneyes.world_downloader.Inventories.GestionMapsInventory;
import fr.synchroneyes.world_downloader.Inventories.InventoryInterface;
import fr.synchroneyes.world_downloader.Inventories.MapListInventory;
import fr.synchroneyes.world_downloader.Items.ItemInterface;
import fr.synchroneyes.world_downloader.Items.MapDownloadItem;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.SimplePluginManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class WorldDownloader {

    private mineralcontest plugin = mineralcontest.plugin;
    private static WorldDownloader instance;
    private CommandMap bukkitCommandMap;


    public LinkedList<MapInfo> maps;

    private Inventory inventaire;
    public LinkedList<InventoryInterface> inventaires;
    public LinkedList<ItemInterface> items;

    public boolean downloading = false;
    public static boolean areMapsLoaded = false;

    public static Monde monde;
    private BossBar status_telechargement;

    public WorldDownloader() {
        instance = this;
        maps = new LinkedList<>();
        printToConsole("Loading world downloader module ...");

        try {
            getPluginCommandMap();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }


        inventaire = Bukkit.createInventory(null, 9, Lang.map_downloader_inventory_name.toString());
        inventaires = new LinkedList<>();
        items = new LinkedList<>();


        registerInventories();
        registerItems();

        registerEvents();
        registerCommands();
    }

    public void initMapLists() {
        printToConsole("Loading all map from workshop ...");

        Thread thead = new Thread(() -> {
            int tentatives = 0;

            try {
                while (!Urls.areAllUrlFetched && tentatives <= 5) {
                    // On ne veut pas faire plus de 5 tentatives, si c'est le cas on affiche un message d'erreur


                    Bukkit.getLogger().info(mineralcontest.prefix + " All urls are not fetched yet.... Retrying in 2 sec");
                    Thread.sleep(2 * 1000);
                    tentatives++;
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Si la tentative est >= à 5, ça veut dire qu'on a pas pu récuperer l'URL de la liste des maps
            if (tentatives >= 5) areMapsLoaded = true;
            else getMaps(true);
        });
        thead.start();

    }

    private void registerInventories() {
        this.inventaires.add(new MapListInventory(true));
        this.inventaires.add(new GestionMapsInventory(true));
        this.inventaires.add(new ConfirmationSuppressionInventory(null));
    }

    private void registerItems() {

    }

    public Inventory getInventory() {
        inventaire.clear();
        for (InventoryInterface inventory : inventaires)
            if (inventory.isDisplayInMainMenu()) inventaire.addItem(inventory.toItemStack());

        for (ItemInterface item : items)
            inventaire.addItem(item.toItemStack());

        return inventaire;
    }


    private void getPluginCommandMap() throws NoSuchFieldException, IllegalAccessException {
        Field cmdMapField = SimplePluginManager.class.getDeclaredField("commandMap");
        cmdMapField.setAccessible(true);
        this.bukkitCommandMap = (CommandMap) cmdMapField.get(Bukkit.getPluginManager());
    }

    public static WorldDownloader getInstance() {
        if (instance == null) return new WorldDownloader();
        return instance;
    }

    public static LinkedList<MapInfo> getMaps(boolean download) {
        WorldDownloader worldDownloader = getInstance();
        MapVote mapVote = new MapVote();
        List<String> maps_existing = mapVote.getMaps();


        if (download) {
            areMapsLoaded = false;

            HttpGet request = new HttpGet(Urls.API_URL_WORKSHOP_LIST);
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = null;
            try {
                response = httpClient.execute(request);
                HttpEntity entity = response.getEntity();
                String entityContents = EntityUtils.toString(entity);

                // Réponse du site web
                JSONObject jsonResponse = new JSONObject(entityContents);
                JSONArray maps = jsonResponse.getJSONArray("maps");

                for (int i = 0; i < maps.length(); ++i) {
                    JSONObject map = maps.getJSONObject(i);
                    MapInfo mapInfo = MapInfo.fromJsonObject(map);

                    // Si la map existe déjà, on ne la stock pas ;)
                    if (!maps_existing.contains(mapInfo.map_folder_name)) worldDownloader.maps.add(mapInfo);
                }

                // We remove the one already existing

                WorldDownloader.getInstance().printToConsole(worldDownloader.maps.size() + " maps are available to download from website");
                areMapsLoaded = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return instance.maps;
    }


    private void registerEvents() {
        printToConsole("Registering events");
        plugin.getServer().getPluginManager().registerEvents(new InventoryEvent(), plugin);
    }

    private void registerCommands() {
        printToConsole("Registering commands");
        bukkitCommandMap.register("", new mcdownload());
        //this.bukkitCommandMap.register("", new SaveArena());

    }

    public static synchronized void download(MapDownloadItem map, Player joueur) throws Exception {
        WorldDownloader worldDownloader = getInstance();
        if (worldDownloader.downloading) {
            joueur.sendMessage(mineralcontest.prefixErreur + Lang.error_already_downloading_a_map.toString());
            return;
        }

        doDownload(map, joueur);

    }

    private static synchronized void doDownload(MapDownloadItem map, Player joueur) {
        joueur.closeInventory();
        WorldDownloader worldDownloader = getInstance();
        Thread thread = new Thread(() -> {
            try {

                // On crée la progress bar de téléchargement
                //instance.createProgressBar(map, joueur);

                // On marque qu'on est en train de télécharger
                worldDownloader.downloading = true;

                // On prépare le fichier qu'on télécharge
                File dossierTelechargement = new File(mineralcontest.plugin.getDataFolder() + File.separator + "map_download");
                if (!dossierTelechargement.exists()) dossierTelechargement.mkdir();

                File fichierTelecharge = new File(dossierTelechargement, map.getMapFileName());

                FileUtils.copyURLToFile(new URL(map.getMapUrl()), fichierTelecharge);
                joueur.sendMessage(mineralcontest.prefixPrive + Lang.downloading_map_done_now_extracting.toString());
                extraireMapTelechargee(map, fichierTelecharge, joueur);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }

    /*private static synchronized void doDownload(MapDownloadItem map, Player joueur) {
        WorldDownloader worldDownloader = getInstance();
        Thread thread = new Thread(() -> {
            try {
                instance.createProgressBar(map, joueur);
                worldDownloader.downloading = true;

                File dossierTelechargement = new File(mineralcontest.plugin.getDataFolder() + File.separator + "map_download");
                if (!dossierTelechargement.exists()) dossierTelechargement.mkdir();

                File fichierTelecharge = new File(dossierTelechargement, map.getMapFileName());


                CloseableHttpClient client = HttpClientBuilder.create().build();
                HttpGet request = new HttpGet(
                        map.getMapUrl());

                HttpResponse response = client.execute(request);
                HttpEntity entity = response.getEntity();


                InputStream is = entity.getContent();

                FileOutputStream fos = new FileOutputStream(fichierTelecharge);
                int inByte;
                double downloaded = 0;

                // Taille d'un MO en byte
                int taille_mo = 1048576;


                while ((inByte = is.read()) != -1) {
                    fos.write(inByte);
                    downloaded++;

                    if ((downloaded % (taille_mo / 2) == 0)) instance.updateTeleportBar(downloaded, map, joueur);

                }

                is.close();
                fos.close();

                client.close();
                instance.removePlayerDownloadBar();
                joueur.sendMessage(mineralcontest.prefixPrive + Lang.downloading_map_done_now_extracting.toString());
                extraireMapTelechargee(map, fichierTelecharge, joueur);

                fichierTelecharge.delete();


            } catch (FileNotFoundException ignored) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }*/

    private static void extraireMapTelechargee(MapDownloadItem map, File fichierTelecharge, Player joueur) throws IOException {
        ZipFile fichierZip = new ZipFile(fichierTelecharge.getAbsoluteFile());

        Enumeration<?> enu = fichierZip.entries();

        Enumeration<?> enu_copy = fichierZip.entries();

        File dossierCustomMaps = new File(mineralcontest.plugin.getDataFolder() + File.separator + "worlds");
        if (!dossierCustomMaps.exists()) dossierCustomMaps.mkdir();

        while (enu.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) enu.nextElement();

            String name = zipEntry.getName();


            File file = new File(dossierCustomMaps, name);
            if (name.endsWith("/")) {
                file.mkdirs();
                continue;
            }

            File parent = file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }


            InputStream is = fichierZip.getInputStream(zipEntry);
            FileOutputStream fos = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = is.read(bytes)) >= 0) {
                fos.write(bytes, 0, length);
            }
            is.close();
            fos.close();
        }
        fichierZip.close();
        instance.downloading = false;
        joueur.sendMessage(mineralcontest.prefixPrive + Lang.downloading_map_extracted.toString());
        fichierTelecharge.delete();

    }


    private void createProgressBar(MapDownloadItem map, Player joueur) {

        String downloadTitle = Lang.downloading_map_progress.getDefault();
        downloadTitle = downloadTitle.replace("%mapName%", map.getMapName());
        downloadTitle = downloadTitle.replace("%percentage%", "0");
        if (status_telechargement == null)
            status_telechargement = Bukkit.createBossBar(downloadTitle, BarColor.BLUE, BarStyle.SOLID);
        status_telechargement.setProgress(0);
        status_telechargement.addPlayer(joueur);
    }

    public void removePlayerDownloadBar() {
        if (status_telechargement != null) status_telechargement.removeAll();
    }

    public void updateTeleportBar(double downloaded, MapDownloadItem map, Player joueur) {


        DecimalFormat decimalFormat = new DecimalFormat("###.##");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        decimalFormat.setDecimalFormatSymbols(dfs);

        int mapSize = Integer.parseInt(map.getMapSize());

        double status = ((downloaded / (double) mapSize));

        String _status = decimalFormat.format(status);
        status = Double.parseDouble(_status);

        int pourcentage = (int) (Math.round(status * 100));

        status_telechargement.setProgress(clamp(status, 0, 1));

        String downloadTitle = Lang.downloading_map_progress.getDefault();
        downloadTitle = downloadTitle.replace("%mapName%", map.getMapName());
        downloadTitle = downloadTitle.replace("%percentage%", pourcentage + "");

        status_telechargement.setTitle(downloadTitle);

        status_telechargement.removePlayer(joueur);
        status_telechargement.addPlayer(joueur);

    }

    protected void printToConsole(String text) {
        String prefix = "[MINERALC] [WORLD-DOWNLOADER] ";
        Bukkit.getLogger().info(prefix + text);
    }

    private double clamp(double valeur, double min, double max) {
        return (valeur > max) ? max : Math.max(valeur, min);
    }

}
