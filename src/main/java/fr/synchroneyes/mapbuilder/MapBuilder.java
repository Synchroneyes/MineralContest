package fr.synchroneyes.mapbuilder;

import fr.synchroneyes.mapbuilder.Commands.mcarena;
import fr.synchroneyes.mapbuilder.Commands.mcbuild;
import fr.synchroneyes.mapbuilder.Commands.mcrevert;
import fr.synchroneyes.mapbuilder.Commands.mcteam;
import fr.synchroneyes.mapbuilder.Core.Monde;
import fr.synchroneyes.mapbuilder.Events.BlockPlaced;
import fr.synchroneyes.mapbuilder.Events.PlayerInteract;
import fr.synchroneyes.mineral.Scoreboard.ScoreboardUtil;
import fr.synchroneyes.mineral.Utils.BlockSaver;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Stack;

public class MapBuilder {

    private mineralcontest plugin = mineralcontest.plugin;
    private static MapBuilder instance;
    public boolean isBuilderModeEnabled = false;
    private CommandMap bukkitCommandMap;

    public static Stack<Stack<BlockSaver>> modifications;


    public static Monde monde;

    private MapBuilder() {
        instance = this;
        //mineralcontest.debug = isBuilderModeEnabled;
        modifications = new Stack<>();


        if (isBuilderModeEnabled) enableMapBuilder();

        try {
            getPluginCommandMap();
        } catch (Exception e) {
            e.printStackTrace();
        }


        printToConsole("Loading custom maps module ...");
        registerEvents();
        registerCommands();
    }


    public static void enableMapBuilder() {
        instance.isBuilderModeEnabled = true;
        instance.enableMapBuilding();
        monde = new Monde();
        Bukkit.broadcastMessage("MapBuilder mode enabled!");
    }

    public static void disableMapBuilder() {
        instance.isBuilderModeEnabled = false;
        instance.disableMapBuilding();
    }


    private void getPluginCommandMap() throws NoSuchFieldException, IllegalAccessException {
        Field cmdMapField = SimplePluginManager.class.getDeclaredField("commandMap");
        cmdMapField.setAccessible(true);
        this.bukkitCommandMap = (CommandMap) cmdMapField.get(Bukkit.getPluginManager());
    }

    public static MapBuilder getInstance() {
        if (instance == null) return new MapBuilder();
        return instance;
    }


    private void registerEvents() {
        printToConsole("Registering events");
        this.plugin.getServer().getPluginManager().registerEvents(new BlockPlaced(), plugin);
        this.plugin.getServer().getPluginManager().registerEvents(new PlayerInteract(), plugin);


    }

    private void registerCommands() {
        printToConsole("Registering commands");

        this.bukkitCommandMap.register("", new mcteam());
        this.bukkitCommandMap.register("", new mcarena());
        this.bukkitCommandMap.register("", new mcbuild());
        this.bukkitCommandMap.register("", new mcrevert());

    }


    private void enableMapBuilding() {
        isBuilderModeEnabled = true;
        World game_world = mineralcontest.plugin.pluginWorld;
        int size = 1000000;

        if (game_world != null) {
            game_world.getWorldBorder().setCenter(mineralcontest.plugin.defaultSpawn);
            game_world.getWorldBorder().setSize(size);
            game_world.setDifficulty(Difficulty.PEACEFUL);

            for (Player p : game_world.getPlayers())
                p.setGameMode(GameMode.CREATIVE);
        }
    }

    private void disableMapBuilding() {
        isBuilderModeEnabled = false;
        World game_world = mineralcontest.plugin.pluginWorld;
        int size = 1000000;

        if (game_world != null) {
            game_world.getWorldBorder().setCenter(mineralcontest.plugin.defaultSpawn);
            game_world.getWorldBorder().setSize(size);
            game_world.setDifficulty(Difficulty.NORMAL);

            for (Player p : game_world.getPlayers())
                p.setGameMode(GameMode.SURVIVAL);
        }
    }

    private void printToConsole(String text) {
        String prefix = "[MINERALC] [CUSTOM-MAPS] ";
        Bukkit.getLogger().info(prefix + text);
    }

    public void sendPlayersHUD() {

        if (!isBuilderModeEnabled) return;

        ArrayList<String> playerHudContents = new ArrayList<>();
        playerHudContents.add("MapBuiler mode enabled");
        playerHudContents.add(" ");
        playerHudContents.add("Type /build to get the build item");

        String[] hudContentAsArray = new String[playerHudContents.size()];
        int index = 0;
        for (String hudcontent : playerHudContents) {
            hudContentAsArray[index] = hudcontent;
            index++;
        }

        for (Player player : plugin.pluginWorld.getPlayers())
            ScoreboardUtil.unrankedSidebarDisplay(player, hudContentAsArray);
    }
}
