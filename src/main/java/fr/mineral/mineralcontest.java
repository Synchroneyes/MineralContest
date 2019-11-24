package fr.mineral;

import fr.mineral.Commands.*;
import fr.mineral.Commands.CVAR.*;
import fr.mineral.Core.Game;
import fr.mineral.Translation.Lang;
import fr.mineral.Events.*;

import fr.mineral.Translation.Language;
import fr.mineral.Utils.Metric.SendInformation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class mineralcontest extends JavaPlugin implements CommandExecutor, Listener {


    public String versionRequired = "1.14.4";
    public static boolean debug = false;


    public static String prefix;
    public static String prefixErreur;
    public static String prefixGlobal;
    public static String prefixPrive;
    public static String prefixAdmin;


    public static int playZoneRadius = 1000;
    public static boolean isGameInitialized = false;

    public static YamlConfiguration LANG;
    public static File LANG_FILE;

    public static Logger log = Bukkit.getLogger();


    public static mineralcontest plugin;
    public static int teamMaxPlayers = 2;
    private Game partie;

    // Constructeur, on initialise les variables
    public mineralcontest() {
        mineralcontest.plugin = this;
        this.partie = new Game();

    }

    public void createLangFiles() throws IOException {


        // Create a lang folder
        InputStream defaultLang = getClass().getResourceAsStream("/lang/english.yml");

        File folder = new File(getDataFolder() + File.separator + "lang");
        if(! folder.exists())folder.mkdirs();

        File langFile = null;
        OutputStream outputStream = null;


        for(Language item: Language.values()) {
            if(item.getLanguageName() != null && !item.getLanguageName().contains("default")) {
                InputStream is = getClass().getResourceAsStream("/lang/" + item.getLanguageName() + ".yml");
                log.info(item.getLanguageName());
                langFile = new File(folder + File.separator + item.getLanguageName() + ".yml");
                try {
                    outputStream = new FileOutputStream(langFile);
                    int read = 0;
                    byte[] bytes = new byte[1024];
                    while ((read = is.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, read);
                    }
                }
                finally{
                    if(outputStream != null) outputStream.close();
                }
            }
        }

    }

    public void LoadLangFile(String lang) {
        Bukkit.getLogger().info("Loading " + lang + " language");
        Bukkit.broadcastMessage("Loading " + lang + " language");
        File langFile = new File(getDataFolder() + File.separator + "lang" + File.separator + lang + ".yml");

        if(!langFile.exists()) {
            Bukkit.getLogger().severe(lang + ".yml lang file doesnt exists or could not be loaded.");
            Bukkit.getLogger().severe("Loading english language file");

            LoadLangFile("english");
            return;
        }

        YamlConfiguration conf = YamlConfiguration.loadConfiguration(langFile);
        for(Lang item:Lang.values()) {
            if (conf.getString(item.getPath()) == null) {
                conf.set(item.getPath(), item.getDefault());
            }
        }
        Lang.setFile(conf);

        try {
            conf.save(langFile);
            Bukkit.getLogger().info("Loaded " + lang + " language");
            Bukkit.broadcastMessage("Loaded " + lang + " language");
            prefix = Lang.title.toString() + ChatColor.WHITE;
            prefixErreur = Lang.title.toString() +  ChatColor.RED + Lang.error.toString() + ChatColor.WHITE;
            prefixGlobal = Lang.title.toString() + ChatColor.GREEN + Lang.global.toString() + ChatColor.WHITE;
            prefixPrive = Lang.title.toString() + ChatColor.YELLOW + Lang._private.toString() + ChatColor.WHITE;
            prefixAdmin = Lang.title.toString() + ChatColor.RED + Lang.admin.toString() + ChatColor.WHITE;

            getGame().getTeamRouge().setNomEquipe(Lang.red_team.toString());
            getGame().getTeamJaune().setNomEquipe(Lang.yellow_team.toString());
            getGame().getTeamBleu().setNomEquipe(Lang.blue_team.toString());

            mineralcontest.plugin.getConfig().set("config.lang.language", lang);
            mineralcontest.plugin.saveConfig();

        } catch(IOException e) {
            log.log(Level.WARNING, "MineralContest: Failed to save lang.yml.");
            e.printStackTrace();
        }



    }



    private void loadConfig() {
        getConfig().options().copyDefaults(true); // Reset le fichier de config à chaque fois
        initConfig();
        Bukkit.getLogger().info(mineralcontest.prefix + "LoadingCOnfig");

    }

    private void initConfig() {
        // Si on a pas encore save le allowSharing
        if(!getConfig().isSet("config.metrics.allowSharing"))
            SendInformation.enable();
        else {
            boolean allowSharing = (boolean) getConfig().get("config.metrics.allowSharing");

            if(allowSharing)
                SendInformation.enable();
            else
                SendInformation.disable();
        }

        if(!getConfig().isSet("config.lang.language"))
            LoadLangFile("english");
        else
            LoadLangFile((String) getConfig().get("config.lang.language"));
    }

    public boolean isVersionCompatible() {
        String version = Bukkit.getBukkitVersion();

        if(version.equalsIgnoreCase(versionRequired)) return true;

        String currentV[] = version.split(".");

        String requiredV[] = versionRequired.split(".");

        for(int i = 0; i < currentV.length; i++) {

            if(Integer.parseInt(currentV[i]) < Integer.parseInt(requiredV[i]))
                return false;

            if(Integer.parseInt(currentV[i]) > Integer.parseInt(requiredV[i]))
                return true;
        }

        return true;
    }

    public Game getGame() {
        return this.partie;
    }

    @Override
    public void onEnable() {

        try {
            createLangFiles();
            LoadLangFile("english");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Plugin startup logic
        Bukkit.getServer().getPluginManager().registerEvents(new BlockDestroyed(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BlockPlaced(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BlockSpread(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ChestEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EntityDamage(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EntityInteract(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EntityTarget(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ExplosionEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDisconnect(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerMove(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerSpawn(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new SafeZoneEvent(), this);


        Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), "gamerule sendCommandFeedback false");

        this.getGame().init();

        // On lit la config
        loadConfig();


        // Register les commands
        getCommand("start").setExecutor(new StartGameCommand());
        getCommand("pause").setExecutor(new PauseGameCommand());
        getCommand("stopGame").setExecutor(new StopGameCommand());
        getCommand("vote").setExecutor(new VoteCommand());
        getCommand("arene").setExecutor(new AreneTeleportCommand());
        getCommand("arena").setExecutor(new AreneTeleportCommand());

        getCommand("switch").setExecutor(new SwitchCommand());
        getCommand("resume").setExecutor(new ResumeGameCommand());
        getCommand("mp_randomize_team").setExecutor(new mp_randomize_team());
        getCommand("mp_iron_score").setExecutor(new mp_iron_score());
        getCommand("mp_gold_score").setExecutor(new mp_gold_score());
        getCommand("mp_diamond_score").setExecutor(new mp_diamond_score());
        getCommand("mp_emerald_score").setExecutor(new mp_emerald_score());
        getCommand("mp_team_max_players").setExecutor(new mp_team_max_players());
        getCommand("mp_enable_metrics").setExecutor(new mp_enable_metrics());
        getCommand("join").setExecutor(new JoinCommand());
        getCommand("mp_set_language").setExecutor(new mp_set_language());





        if(mineralcontest.plugin.getServer().getOnlinePlayers().size() > 0){
            try {

                Bukkit.getWorld("world").setAutoSave(false);
            }catch(Exception e) {
                e.printStackTrace();
            }
        }


        if(!isVersionCompatible()) {
            ConsoleCommandSender console = mineralcontest.plugin.getServer().getConsoleSender();
            console.sendMessage(ChatColor.RED + "[MINERALC] [ERREUR] Incompatible bukkit version, Version asked: " + versionRequired + ", current version: " + Bukkit.getBukkitVersion());
            console.sendMessage(Lang.plugin_shutdown.toString());
            //getServer().getLogger().info("La version de bukkit n'est pas compatible avec ce plugin. Version demandée: " + versionRequired + ", version actuelle: " + Bukkit.getBukkitVersion());
            Bukkit.getPluginManager().disablePlugin(this);
        }

    }

    @Override
    public void onDisable() {

    }

}
