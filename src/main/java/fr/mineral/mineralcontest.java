package fr.mineral;

import fr.mineral.Core.Game;
import fr.mineral.Events.*;
import fr.mineral.Exception.FullTeamException;

import fr.mineral.Scoreboard.ScoreboardUtil;
import fr.mineral.Teams.Equipe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public final class mineralcontest extends JavaPlugin implements CommandExecutor, Listener {



    public static String prefix = ChatColor.BLUE + "[MINERALC] " + ChatColor.WHITE;
    public static String prefixErreur = ChatColor.BLUE + "[MINERALC] " + ChatColor.RED + "[ERREUR] " + ChatColor.WHITE;
    public static String prefixGlobal = ChatColor.BLUE + "[MINERALC] " + ChatColor.GREEN + "[GLOBAL] " + ChatColor.WHITE;
    public static String prefixPrive = ChatColor.BLUE + "[MINERALC] " + ChatColor.YELLOW + "[PRIVE] " + ChatColor.WHITE;


    // 60*60 car dans 60min il y a 60*60 sec
    public static int timeLeft = 60*60-1;
    public static int teamMaxPlayers = 2;
    private static int gameStarted = 0;
    private static boolean gamePaused = false;

    private Game partie;



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

    // Constructeur, on initialise les variables
    public mineralcontest() {
        this.partie = new Game();
        mineralcontest.plugin = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerSpawn(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new SafeZoneEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDisconnect(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerMove(), this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        return true;
    }
}
