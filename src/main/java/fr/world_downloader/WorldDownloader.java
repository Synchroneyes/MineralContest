package fr.world_downloader;

import fr.mapbuilder.Core.Monde;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import fr.world_downloader.Commands.mcdownload;
import fr.world_downloader.Inventories.InventoryInterface;
import fr.world_downloader.Inventories.MapListInventory;
import fr.world_downloader.Items.ItemInterface;
import fr.world_downloader.Items.MapDownloadItem;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.LinkedList;

public class WorldDownloader {
    public static String WEBSITE_URL = "http://localhost:8000/api/";
    public static String API_LIST_FILE_URL = WEBSITE_URL + "workshop/list-files";

    private mineralcontest plugin = mineralcontest.plugin;
    private static WorldDownloader instance;
    private CommandMap bukkitCommandMap;


    private Inventory inventaire;
    public LinkedList<InventoryInterface> inventaires;
    public LinkedList<ItemInterface> items;

    public boolean downloading = false;

    public static Monde monde;

    public WorldDownloader() {
        instance = this;

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

    private void registerInventories() {
        this.inventaires.add(new MapListInventory());
    }

    private void registerItems() {

    }

    public Inventory getInventory() {
        inventaire.clear();
        for (InventoryInterface inventory : inventaires)
            inventaire.addItem(inventory.toItemStack());

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


    private void registerEvents() {
        printToConsole("Registering events");
        plugin.getServer().getPluginManager().registerEvents(new InventoryEvent(), plugin);
        //this.plugin.getServer().getPluginManager().registerEvents(new BlockPlaced(), plugin);
    }

    private void registerCommands() {
        printToConsole("Registering commands");
        bukkitCommandMap.register("", new mcdownload());
        //this.bukkitCommandMap.register("", new SaveArena());

    }

    public static void download(MapDownloadItem map) throws Exception {
        WorldDownloader worldDownloader = getInstance();
        if (worldDownloader.downloading) throw new Exception("Already downloading a map, please wait");
        worldDownloader.downloading = true;
    }


    protected void printToConsole(String text) {
        String prefix = "[MINERALC] [WORLD-DOWNLOADER] ";
        Bukkit.getLogger().info(prefix + text);
    }

}
