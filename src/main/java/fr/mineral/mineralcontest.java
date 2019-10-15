package fr.mineral;

import fr.mineral.Commands.*;
import fr.mineral.Core.Game;
import fr.mineral.Events.*;

import fr.mineral.Utils.Save.FileToGame;
import fr.mineral.Utils.Save.GameToFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class mineralcontest extends JavaPlugin implements CommandExecutor, Listener {



    public static String prefix = ChatColor.BLUE + "[MINERALC] " + ChatColor.WHITE;
    public static String prefixErreur = ChatColor.BLUE + "[MINERALC] " + ChatColor.RED + "[ERREUR] " + ChatColor.WHITE;
    public static String prefixGlobal = ChatColor.BLUE + "[MINERALC] " + ChatColor.GREEN + "[GLOBAL] " + ChatColor.WHITE;
    public static String prefixPrive = ChatColor.BLUE + "[MINERALC] " + ChatColor.YELLOW + "[PRIVE] " + ChatColor.WHITE;

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



    public static mineralcontest plugin;
    public static int teamMaxPlayers = 1;
    private Game partie;

    // Constructeur, on initialise les variables
    public mineralcontest() {
        mineralcontest.plugin = this;
        this.partie = new Game();
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
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDisconnect(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        //Bukkit.getServer().getPluginManager().registerEvents(new PlayerMort(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerMove(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerSpawn(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new SafeZoneEvent(), this);

        Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), "gamerule sendCommandFeedback false");

        this.getGame().init();


        // Register les commands
        getCommand("start").setExecutor(new StartGameCommand());
        getCommand("pause").setExecutor(new PauseGameCommand());
        getCommand("stopGame").setExecutor(new StopGameCommand());
        getCommand("set").setExecutor(new SetCommand());
        getCommand("resume").setExecutor(new ResumeGameCommand());
        getCommand("setup").setExecutor(new SetupCommand());
        getCommand("valider").setExecutor(new ValiderCommand());

        getCommand("tprouge").setExecutor(new TestSetupCommand());
        getCommand("tpjaune").setExecutor(new TestSetupCommand());
        getCommand("tpbleu").setExecutor(new TestSetupCommand());
        getCommand("spawnarene").setExecutor(new TestSetupCommand());

        getCommand("ouvrir").setExecutor(new OpenDoor());
        getCommand("fermer").setExecutor(new OpenDoor());







    }

    @Override
    public void onDisable() {

    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        return true;
    }
}
