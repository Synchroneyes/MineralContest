package fr.mapbuilder;

import fr.mapbuilder.Commands.SpawnHouse;
import fr.mapbuilder.Events.BlockPlaced;
import fr.mineral.Scoreboard.ScoreboardUtil;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MapBuilder {

    private mineralcontest plugin = mineralcontest.plugin;
    private static MapBuilder instance;
    public boolean isBuilderModeEnabled = false;
    private CommandMap bukkitCommandMap;

    private MapBuilder() {
        instance = this;
        mineralcontest.debug = isBuilderModeEnabled;

        try {
            getPluginCommandMap();
        }catch (Exception e) {
            e.printStackTrace();
        }


        printToConsole("Loading custom maps module ...");
        registerEvents();
        registerCommands();
        if(isBuilderModeEnabled) enableMapBuilding();

        RessourceFilesManager.copyFilesToPluginFolder();
    }


    private void getPluginCommandMap() throws NoSuchFieldException, IllegalAccessException {
        Field cmdMapField = SimplePluginManager.class.getDeclaredField("commandMap");
        cmdMapField.setAccessible(true);
        this.bukkitCommandMap = (CommandMap) cmdMapField.get(Bukkit.getPluginManager());
    }

    public static MapBuilder getInstance() {
        if(instance == null) return new MapBuilder();
        return instance;
    }

    private void registerEvents() {
        printToConsole("Registering events");
        this.plugin.getServer().getPluginManager().registerEvents(new BlockPlaced(), plugin);
    }

    private void registerCommands() {
        printToConsole("Registering commands");

        this.bukkitCommandMap.register(SpawnHouse.pluginCommand, new SpawnHouse());
    }


    private void enableMapBuilding() {
        isBuilderModeEnabled = true;
        World game_world = mineralcontest.plugin.pluginWorld;
        int size = 1000000;

        if(game_world != null) {
            game_world.getWorldBorder().setCenter(mineralcontest.plugin.defaultSpawn);
            game_world.getWorldBorder().setSize(size);
            game_world.setDifficulty(Difficulty.PEACEFUL);

            for(Player p : game_world.getPlayers())
                p.setGameMode(GameMode.CREATIVE);
        }


    }

    protected void printToConsole(String text) {
        String prefix = "[MINERALC] [CUSTOM-MAPS] ";
        Bukkit.getLogger().info(prefix + text);
    }

    public void sendPlayersHUD() {
        ArrayList<String> playerHudContents = new ArrayList<>();
        playerHudContents.add("MapBuiler mode enabled");
        playerHudContents.add(" ");
        playerHudContents.add("Type /build to get the build item");

        String[] hudContentAsArray = new String[playerHudContents.size()];
        int index = 0;
        for(String hudcontent : playerHudContents){
            hudContentAsArray[index] = hudcontent;
            index++;
        }

        for(Player player : plugin.pluginWorld.getPlayers())
            ScoreboardUtil.unrankedSidebarDisplay(player, hudContentAsArray);
    }
}
