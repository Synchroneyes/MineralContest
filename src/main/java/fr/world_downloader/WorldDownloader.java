package fr.world_downloader;

import fr.mapbuilder.Core.Monde;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;

public class WorldDownloader {
    public static String WEBSITE_URL = "http://localhost:8000/api/";
    public static String API_LIST_FILE_URL = WEBSITE_URL + "workshop/list-files";

    private mineralcontest plugin = mineralcontest.plugin;
    private static WorldDownloader instance;
    private CommandMap bukkitCommandMap;

    public static Monde monde;

    private WorldDownloader() {
        instance = this;

        printToConsole("Loading world downloader module ...");
        registerEvents();
        registerCommands();
        try {
            getPluginCommandMap();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
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
        //this.plugin.getServer().getPluginManager().registerEvents(new BlockPlaced(), plugin);
    }

    private void registerCommands() {
        printToConsole("Registering commands");

        //this.bukkitCommandMap.register("", new SaveArena());

    }


    protected void printToConsole(String text) {
        String prefix = "[MINERALC] [WORLD-DOWNLOADER] ";
        Bukkit.getLogger().info(prefix + text);
    }

}
