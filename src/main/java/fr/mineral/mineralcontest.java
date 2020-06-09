package fr.mineral;

import fr.groups.Core.Groupe;
import fr.groups.GroupeExtension;
import fr.mineral.Commands.*;
import fr.mineral.Commands.Developper.SaveWorldCommand;
import fr.mineral.Commands.Developper.SetupCommand;
import fr.mineral.Commands.Developper.ValiderCommand;
import fr.mineral.Core.Game.Game;
import fr.mineral.Settings.GameSettings;
import fr.mineral.Settings.GameSettingsOLD;
import fr.mineral.Settings.GameSettingsCvarOLD;
import fr.mapbuilder.MapBuilder;
import fr.mineral.Core.Referee.RefereeEvent;
import fr.mineral.Events.*;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.Log.GameLogger;
import fr.mineral.Utils.Log.Log;
import fr.mineral.Utils.Metric.SendInformation;
import fr.mineral.Utils.Player.PlayerBaseItem;
import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.Utils.Save.MapFileHandler;
import jdk.internal.jline.internal.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.command.CommandMap;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.logging.Logger;

public final class mineralcontest extends JavaPlugin {

    public static boolean debug = false;

    /**
     * Community version is a version ran by server that wants to have multiple games running at the same time.
     * If set to true, then players will have to create a groupe, invite players, start the vote.
     * If set to false, one group will automatically be created, OP players will be set as admin, and every players joining the server
     * will automatically be added to the group.
     */
    public static boolean communityVersion = true;

    public static String prefix = "[MineralContest]";
    public static String prefixErreur;
    public static String prefixGlobal;
    public static String prefixPrive;
    public static String prefixAdmin;
    public static String prefixTeamChat;
    public static String prefixGroupe;
    public static Logger log = Bukkit.getLogger();
    public static mineralcontest plugin;
    private Game partie;
    public World pluginWorld;

    public Location defaultSpawn;
    public MapBuilder mapBuilderInstance;


    public GroupeExtension groupeExtension;
    public LinkedList<Groupe> groupes;

    // Constructeur, on initialise les variables
    public mineralcontest() {
        mineralcontest.plugin = this;
        //this.partie = new Game();
        Lang.loadLang(getPluginConfigValue("mp_set_language"));

        this.groupeExtension = GroupeExtension.getInstance();
        this.groupes = new LinkedList<>();
    }

    public static String getPluginConfigValue(String configName) {
        // TODO
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(new File(mineralcontest.plugin.getDataFolder(), "plugin_config.yml"));
        return (String) configuration.get(configName);
    }

    public static void supprimerGroupe(Groupe g) {
        plugin.groupes.remove(g);
    }

    public static Groupe getPlayerGroupe(Player p) {
        mineralcontest instance = plugin;
        for (Groupe groupe : instance.groupes) {
            if (groupe.containsPlayer(p)) return groupe;
        }
        return null;
    }

    public void creerNouveauGroupe(Groupe nouveauGroupe) {
        Bukkit.getLogger().info("Creating new groupe ...");
        for (Groupe groupe : groupes) {
            if (groupe.getNom().equals(nouveauGroupe.getNom())) {
                groupe.sendToadmin(prefixErreur + Lang.error_group_with_this_name_already_exists.toString());
                return;
            }
        }

        while (!isGroupeIdentifiantUnique(nouveauGroupe)) {
            nouveauGroupe.genererIdentifiant();
        }


        this.groupes.add(nouveauGroupe);
        nouveauGroupe.sendToEveryone(prefixPrive + Lang.success_group_successfully_created.toString());
    }

    private boolean isGroupeIdentifiantUnique(Groupe groupe) {
        for (Groupe g : groupes)
            if (g.getIdentifiant().equalsIgnoreCase(groupe.getIdentifiant())) return false;
        return true;
    }

    public void setDefaultSpawn(Location defaultSpawn) {
        this.defaultSpawn = defaultSpawn;
    }


    @Override
    public void onEnable() {

        pluginWorld = PlayerUtils.getPluginWorld();
        defaultSpawn = (pluginWorld != null) ? pluginWorld.getSpawnLocation() : null;

        this.mapBuilderInstance = MapBuilder.getInstance();


        Lang.copyLangFilesFromRessources();
        registerCommands();
        registerEvents();
        MapFileHandler.copyMapFileToPluginRessourceFolder();
        PlayerBaseItem.copyDefaultFileToPluginDataFolder();


        if(!debug)
            if(pluginWorld != null)
                for(Player online : pluginWorld.getPlayers())
                    PlayerUtils.teleportPlayer(online, defaultSpawn);

        PlayerUtils.runScoreboardManager();
        GameLogger.addLog(new Log("server_event", "OnEnable", "plugin_startup"));


        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerUtils.drawPlayersHUD();
            }
        }.runTaskTimer(this, 0, 20);
    }

    @Override
    public void onDisable() {

        for (Groupe groupe : groupes) {
            Game game = groupe.getGame();
            SendInformation.sendGameData(SendInformation.ended, game);
            // todo GETGAME
            if (pluginWorld != null && !debug) {
                for (Player player : pluginWorld.getPlayers()) {
                    game.teleportToLobby(player);
                    PlayerUtils.clearPlayer(player);
                }
            }
        }


        //getGame().resetMap();
        Bukkit.getScheduler().cancelTasks(this);
        GameLogger.addLog(new Log("server_event", "OnDisable", "plugin_shutdown"));

    }

    private void registerEvents() {

        if(mapBuilderInstance != null && !mapBuilderInstance.isBuilderModeEnabled) {
            Bukkit.getServer().getPluginManager().registerEvents(new BlockDestroyed(), this);
            Bukkit.getServer().getPluginManager().registerEvents(new BlockPlaced(), this);
            Bukkit.getServer().getPluginManager().registerEvents(new EntityInteract(), this);
            Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        }


        Bukkit.getServer().getPluginManager().registerEvents(new BucketEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ChestEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EntityDamage(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EntityTarget(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EntitySpawn(), this);

        Bukkit.getServer().getPluginManager().registerEvents(new ExplosionEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDisconnect(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerMove(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerSpawn(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new SafeZoneEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerChat(), this);


        Bukkit.getServer().getPluginManager().registerEvents(new RefereeEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerPick(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerWorldChange(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new WorldLoaded(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EntityDeathEvent(), this);


    }

    private void registerCommands() {
        // Register les commands
        getCommand("start").setExecutor(new StartGameCommand());
        getCommand("pause").setExecutor(new PauseGameCommand());
        getCommand("stopGame").setExecutor(new StopGameCommand());
        if (!groupeExtension.enabled) getCommand("vote").setExecutor(new VoteCommand());
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

        Field cmdMapField = null;
        try {
            cmdMapField = SimplePluginManager.class.getDeclaredField("commandMap");
            cmdMapField.setAccessible(true);
            CommandMap bukkitCommandMap = (CommandMap) cmdMapField.get(Bukkit.getPluginManager());

            bukkitCommandMap.register("", new MCCvarCommand());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }


        /*getCommand("mp_randomize_team").setExecutor(new mp_randomize_team());
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
        getCommand("mp_enable_old_pvp").setExecutor(new mp_enable_old_pvp());
        getCommand("mp_enable_block_adding").setExecutor(new mp_enable_block_adding());

        getCommand("mp_set_playzone_radius").setExecutor(new mp_set_playzone_radius());
        getCommand("mp_enable_friendly_fire").setExecutor(new mp_enable_friendly_fire());*/
        getCommand("spawnchest").setExecutor(new SpawnChestCommand());
        getCommand("allow").setExecutor(new AllowCommand());
        getCommand("leaveteam").setExecutor(new LeaveTeamCommand());

        /*getCommand("setup").setExecutor(new SetupCommand());
        getCommand("saveworld").setExecutor(new SaveWorldCommand());
        getCommand("valider").setExecutor(new ValiderCommand());*/

    }

    // Called when the game start
    public void setWorldBorder() throws Exception {
        if(pluginWorld == null) return;
        int playZoneRadius = GameSettings.getValeurParDefaut("mp_set_playzone_radius").getValeurNumerique();
        int marge = 100; // Use to prevent server.properties spawn protection

        WorldBorder world = pluginWorld.getWorldBorder();

        // TODO getGame
        //Location arenaCenterLocation = getGame().getArene().getCoffre().getPosition().clone();

        //arenaCenterLocation.setX(arenaCenterLocation.getX()-marge);
        //world.setCenter(arenaCenterLocation);
        //world.setSize(playZoneRadius + marge);
    }

    public void setDefaultWorldBorder() {

        if(mapBuilderInstance.isBuilderModeEnabled) return;

        World game_world = mineralcontest.plugin.pluginWorld;
        int size = 30000000;

        if(game_world != null) {
            game_world.getWorldBorder().setCenter(mineralcontest.plugin.defaultSpawn);
            game_world.getWorldBorder().setSize(size);
        }
    }


    public static void checkIfMapIsCorrect() {
        if(mineralcontest.plugin.pluginWorld == null) {
            mineralcontest.plugin.pluginWorld = PlayerUtils.getPluginWorld();
        }
        if(mineralcontest.debug) return;
        if (mineralcontest.plugin.pluginWorld == null) {
            ConsoleCommandSender console = mineralcontest.plugin.getServer().getConsoleSender();
            log.severe("NULL");
            log.severe(mineralcontest.prefixErreur + Lang.bad_map_loaded.toString());
            log.severe(mineralcontest.prefixErreur + Lang.github_link.toString());
            log.severe(mineralcontest.prefixErreur + Lang.plugin_shutdown.toString());
            Bukkit.getPluginManager().disablePlugin(mineralcontest.plugin);

        }
    }

    public static void broadcastMessage(String message, Groupe groupe) {

        groupe.sendToEveryone(message);
        Bukkit.getLogger().info(message);
        GameLogger.addLog(new Log("broadcast-group", message, "server"));
    }

    public static void broadcastMessageToAdmins(String message) {
        for(Player player : mineralcontest.plugin.pluginWorld.getPlayers())
            if(player.isOp()) player.sendMessage(message);
        Bukkit.getLogger().info(message);
        GameLogger.addLog(new Log("broadcast_admin", message, "server"));
    }


    public static boolean isInAMineralContestWorld(Player p) {
        for (Groupe groupe : plugin.groupes)
            if (groupe.getMonde() != null && p.getWorld().equals(groupe.getMonde())) return true;

        return p.getWorld().equals(mineralcontest.plugin.pluginWorld);
    }

    public static boolean isAMineralContestWorld(World w) {
        for (Groupe groupe : plugin.groupes)
            if (groupe.getMonde() == null) return false;
            else if (w.equals(groupe.getMonde())) return true;
        return w.equals(plugin.pluginWorld);
    }

    @Nullable
    public static Game getPlayerGame(Player p) {
        Groupe g = getPlayerGroupe(p);
        if (g != null) return g.getGame();
        return null;
    }

    @Nullable
    public static Game getWorldGame(World world) {
        for (Groupe groupe : plugin.groupes)
            if (groupe.getMonde() == null) return null;
            else if (groupe.getMonde().equals(world))
                return groupe.getGame();
        return null;
    }



}
