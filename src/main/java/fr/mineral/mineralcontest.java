package fr.mineral;

import fr.mineral.Commands.*;
import fr.mineral.Commands.CVAR.*;
import fr.mineral.Core.Game;
import fr.mineral.Core.GameSettings;
import fr.mineral.Core.GameSettingsCvar;
import fr.mineral.Core.Referee.RefereeEvent;
import fr.mineral.Events.*;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.Metric.SendInformation;
import fr.mineral.Utils.Player.PlayerBaseItem;
import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.Utils.Save.MapFileHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class mineralcontest extends JavaPlugin {

    public static boolean debug = false;
    private GameSettings gameSettings;

    public static String prefix;
    public static String prefixErreur;
    public static String prefixGlobal;
    public static String prefixPrive;
    public static String prefixAdmin;
    public static String prefixTeamChat;
    public static Logger log = Bukkit.getLogger();
    public static mineralcontest plugin;
    private Game partie;
    public World pluginWorld;

    public Location defaultSpawn;

    // Constructeur, on initialise les variables
    public mineralcontest() {
        mineralcontest.plugin = this;
        this.partie = new Game();
        this.gameSettings = GameSettings.getInstance();
    }

    // Called when the game start
    public void setWorldBorder() throws Exception {
        if(pluginWorld == null) return;
        int playZoneRadius = (int) GameSettingsCvar.getValueFromCVARName("mp_set_playzone_radius");
        WorldBorder world = pluginWorld.getWorldBorder();
        world.setCenter(getGame().getArene().getCoffre().getPosition());
        world.setSize(playZoneRadius);
    }

    public void setDefaultWorldBorder() {
        World game_world = mineralcontest.plugin.pluginWorld;
        if(game_world != null) {
            game_world.getWorldBorder().setCenter(mineralcontest.plugin.defaultSpawn);
            game_world.getWorldBorder().setSize(6);
        }
    }

    public void setDefaultSpawn(Location defaultSpawn) {
        this.defaultSpawn = defaultSpawn;
    }

    public Game getGame() {
        return this.partie;
    }

    @Override
    public void onEnable() {

        Lang.copyLangFilesFromRessources();
        Lang.loadLang("french");
        Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), "gamerule sendCommandFeedback false");
        this.getGame().init();
        this.gameSettings.createGameSettings();
        this.gameSettings.loadGameSettings();
        registerCommands();
        registerEvents();
        MapFileHandler.copyMapFileToPluginRessourceFolder();
        PlayerBaseItem.copyDefaultFileToPluginDataFolder();

        pluginWorld = Bukkit.getWorld((String) GameSettingsCvar.getValueFromCVARName("world_name"));
        defaultSpawn = (pluginWorld != null) ? pluginWorld.getSpawnLocation() : null;

        if(!debug)
            if(pluginWorld != null)
                for(Player online : pluginWorld.getPlayers())
                    online.teleport(defaultSpawn);

        defaultSpawn = (pluginWorld != null) ? pluginWorld.getSpawnLocation() : null;
        PlayerUtils.runScoreboardManager();

    }

    @Override
    public void onDisable() {

        if(pluginWorld != null && !debug) {
            for(Player player : pluginWorld.getPlayers()) {
                getGame().teleportToLobby(player);
                PlayerUtils.clearPlayer(player);
            }
        }

        getGame().resetMap();
        SendInformation.sendGameData(SendInformation.ended);
        Bukkit.getScheduler().cancelTasks(this);
    }

    private void registerEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(new BlockDestroyed(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BlockPlaced(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BlockSpread(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ChestEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EntityDamage(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EntityInteract(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EntityTarget(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EntitySpawn(), this);

        Bukkit.getServer().getPluginManager().registerEvents(new ExplosionEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDisconnect(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerMove(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerSpawn(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new SafeZoneEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerChat(), this);


        Bukkit.getServer().getPluginManager().registerEvents(new RefereeEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerWorldChange(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new WorldLoaded(), this);
    }

    private void registerCommands() {
        // Register les commands
        getCommand("start").setExecutor(new StartGameCommand());
        getCommand("pause").setExecutor(new PauseGameCommand());
        getCommand("stopGame").setExecutor(new StopGameCommand());
        getCommand("vote").setExecutor(new VoteCommand());
        getCommand("arene").setExecutor(new AreneTeleportCommand());
        getCommand("arena").setExecutor(new AreneTeleportCommand());
        getCommand("join").setExecutor(new JoinCommand());
        getCommand("referee").setExecutor(new RefereeCommand());
        getCommand("arbitre").setExecutor(new RefereeCommand());
        getCommand("ready").setExecutor(new ReadyCommand());
        getCommand("t").setExecutor(new TeamChat());
        getCommand("team").setExecutor(new TeamChat());



        getCommand("switch").setExecutor(new SwitchCommand());
        getCommand("resume").setExecutor(new ResumeGameCommand());
        getCommand("mp_randomize_team").setExecutor(new mp_randomize_team());
        getCommand("mp_iron_score").setExecutor(new mp_iron_score());
        getCommand("mp_gold_score").setExecutor(new mp_gold_score());
        getCommand("mp_diamond_score").setExecutor(new mp_diamond_score());
        getCommand("mp_emerald_score").setExecutor(new mp_emerald_score());
        getCommand("mp_team_max_players").setExecutor(new mp_team_max_players());
        getCommand("mp_enable_metrics").setExecutor(new mp_enable_metrics());
        getCommand("mp_add_team_penality").setExecutor(new mp_add_team_penality());
        getCommand("mp_reset_team_penality").setExecutor(new mp_reset_team_penality());
        getCommand("mp_start_vote").setExecutor(new mp_start_vote());
        getCommand("mp_enable_item_drop").setExecutor(new mp_enable_item_drop());
        getCommand("mp_set_language").setExecutor(new mp_set_language());
        getCommand("allow").setExecutor(new AllowCommand());
        getCommand("leaveteam").setExecutor(new LeaveTeamCommand());
        getCommand("mp_set_playzone_radius").setExecutor(new mp_set_playzone_radius());
        getCommand("mp_enable_friendly_fire").setExecutor(new mp_enable_friendly_fire());



    }


    public static void checkIfMapIsCorrect() {
        if (mineralcontest.plugin.pluginWorld == null) {
            ConsoleCommandSender console = mineralcontest.plugin.getServer().getConsoleSender();
            log.severe("NULL");
            log.severe(mineralcontest.prefixErreur + Lang.bad_map_loaded.toString());
            log.severe(mineralcontest.prefixErreur + Lang.github_link.toString());
            log.severe(mineralcontest.prefixErreur + Lang.plugin_shutdown.toString());
            Bukkit.getPluginManager().disablePlugin(mineralcontest.plugin);
        }
    }

    public static void broadcastMessage(String message) {
        for(Player player : mineralcontest.plugin.pluginWorld.getPlayers())
            player.sendMessage(message);
        Bukkit.getLogger().info(message);
    }

    public static void broadcastMessageToAdmins(String message) {
        for(Player player : mineralcontest.plugin.pluginWorld.getPlayers())
            if(player.isOp()) player.sendMessage(message);
        Bukkit.getLogger().info(message);
    }

}
