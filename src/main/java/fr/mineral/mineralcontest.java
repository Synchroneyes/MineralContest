package fr.mineral;

import fr.mineral.Commands.*;
import fr.mineral.Commands.CVAR.*;
import fr.mineral.Core.Game;
import fr.mineral.Events.*;

import fr.mineral.Utils.Save.FileToGame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class mineralcontest extends JavaPlugin implements CommandExecutor, Listener {

    /*
        TODO: CVAR
     */

    public String versionRequired = "1.14.4";
    public static boolean debug = false;
    public static String currentWorld = "world2";


    public static String prefix = ChatColor.BLUE + "[MINERALC] " + ChatColor.WHITE;
    public static String prefixErreur = ChatColor.BLUE + "[MINERALC] " + ChatColor.RED + "[ERREUR] " + ChatColor.WHITE;
    public static String prefixGlobal = ChatColor.BLUE + "[MINERALC] " + ChatColor.GREEN + "[GLOBAL] " + ChatColor.WHITE;
    public static String prefixPrive = ChatColor.BLUE + "[MINERALC] " + ChatColor.YELLOW + "[PRIVE] " + ChatColor.WHITE;
    public static String prefixAdmin = ChatColor.BLUE + "[MINERALC] " + ChatColor.RED + "[ADMIN] " + ChatColor.WHITE;

    public static String ERROR_GAME_ALREADY_STARTED = "La partie à déjà commence";
    public static String ERROR_ALL_TEAM_NOT_FULL = "Au moins une équipe n'est pas complète. Il faut " + mineralcontest.teamMaxPlayers + " joueur(s) par équipe.";
    public static String ERROR_NOT_ENOUGHT_PLAYER = "Il n'y a pas assez de joueur connecté";
    public static String ERROR_PLAYER_NOT_IN_TEAM = "Vous devez être dans une équipe";
    public static String ERROR_ARENA_NOT_DEFINED = "Le spawn pour l'arène n'a pas encore été ajouté";
    public static String ERROR_GAME_NOT_STARTED = "La partie n'a pas encore démarrer.";
    public static String ERROR_CHEST_NOT_DEFINED = "La position du coffre n'a pas été défini";

    public static String GAME_SUCCESSFULLY_STARTED = "La partie vient de commencer";
    public static String GAME_STARTING = "La partie va démarrer";
    public static String GAME_PAUSED = "La partie est en pause";

    public static String GAME_WAITING_START = "En attente du démarrage de la partie";

    public static String ARENA_SPAWN_ADDED = "Le spawn pour l'arène a bien été ajouté";
    public static String ARENA_TELEPORTING = "Téléportation vers l'arène.";
    public static String ARENA_TELEPORT_DISABLED = "La téléportation vers l'arène n'est pas encore autorisée.";

    public static String RANDOMIZE_TEAM_BEGIN = "Vous allez etre attribué à une equipe de manière aléatoire.";
    public static String RANDOMIZE_TEAM_END = "Les équipes ont été crée !";

    public static String GLOBAL_CHEST_SPAWNED = "Le coffre est apparu dans l'arène !";
    public static String CHEST_DEFINED = "La position du coffre a bien été enregistrée";

    public static String GAME_STARTING_CHECKS = "Démarage des vérifications ...";

    public static int playZoneRadius = 1000;



    public static mineralcontest plugin;
    public static int teamMaxPlayers = 1;
    private Game partie;

    // Constructeur, on initialise les variables
    public mineralcontest() {
        mineralcontest.plugin = this;
        this.partie = new Game();
        FileToGame fg = new FileToGame();

    }


    public boolean isVersionCompatible() {
        String version = Bukkit.getBukkitVersion();

        if(version.equalsIgnoreCase(versionRequired)) return true;

        String currentV[] = version.split(".");

        String requiredV[] = versionRequired.split(".");

        // 1.14.4
        // 1.14.5
        //
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
        // Plugin startup logic
        Bukkit.getServer().getPluginManager().registerEvents(new BlockDestroyed(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BlockPlaced(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BlockSpread(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ChestEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EntityDamage(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EntityInteract(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EntityTarget(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ExplosionEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDisconnect(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        //Bukkit.getServer().getPluginManager().registerEvents(new PlayerMort(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerMove(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerSpawn(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new SafeZoneEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryClick(), this);

        Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), "gamerule sendCommandFeedback false");

        this.getGame().init();


        // Register les commands
        getCommand("start").setExecutor(new StartGameCommand());
        getCommand("pause").setExecutor(new PauseGameCommand());
        getCommand("stopGame").setExecutor(new StopGameCommand());
        getCommand("vote").setExecutor(new VoteCommand());
        getCommand("arene").setExecutor(new AreneTeleportCommand());
        getCommand("switch").setExecutor(new SwitchCommand());
        getCommand("resume").setExecutor(new ResumeGameCommand());
        getCommand("loadWorld").setExecutor(new LoadWorldCommand());
        getCommand("mp_randomize_team").setExecutor(new mp_randomize_team());
        getCommand("mp_iron_score").setExecutor(new mp_iron_score());
        getCommand("mp_gold_score").setExecutor(new mp_gold_score());
        getCommand("mp_diamond_score").setExecutor(new mp_diamond_score());
        getCommand("mp_emerald_score").setExecutor(new mp_emerald_score());
        getCommand("mp_team_max_players").setExecutor(new mp_team_max_players());
        getCommand("join").setExecutor(new JoinCommand());






        if(mineralcontest.plugin.getServer().getOnlinePlayers().size() > 0){
            try {
                FileToGame fg = new FileToGame();
                fg.readFile(currentWorld);
            }catch(Exception e) {
                e.printStackTrace();
            }
        }


        if(!isVersionCompatible()) {
            ConsoleCommandSender console = mineralcontest.plugin.getServer().getConsoleSender();
            console.sendMessage(ChatColor.RED + "[MINERALC] [ERREUR] La version de bukkit n'est pas compatible avec ce plugin. Version demandée: " + versionRequired + ", version actuelle: " + Bukkit.getBukkitVersion());
            //getServer().getLogger().info("La version de bukkit n'est pas compatible avec ce plugin. Version demandée: " + versionRequired + ", version actuelle: " + Bukkit.getBukkitVersion());
            Bukkit.getPluginManager().disablePlugin(this);
        }



        //getCommand("set").setExecutor(new SetCommand());
        //getCommand("resume").setExecutor(new ResumeGameCommand());
        //getCommand("setup").setExecutor(new SetupCommand());
        //getCommand("valider").setExecutor(new ValiderCommand());

        //getCommand("tprouge").setExecutor(new TestSetupCommand());
        //getCommand("tpjaune").setExecutor(new TestSetupCommand());
        //getCommand("tpbleu").setExecutor(new TestSetupCommand());
        //getCommand("spawnarene").setExecutor(new TestSetupCommand());

        //getCommand("ouvrir").setExecutor(new OpenDoor());
        //getCommand("fermer").setExecutor(new OpenDoor());

        //getCommand("cooldownchest").setExecutor(new SetCooldownChestCommand());

        //getCommand("saveWorld").setExecutor(new SaveWorldCommand());








    }

    @Override
    public void onDisable() {

    }

}
