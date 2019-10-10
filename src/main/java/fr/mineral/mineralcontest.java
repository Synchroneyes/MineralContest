package fr.mineral;

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

    // liste chainee de joueurs
    private Equipe teamJaune;
    private Equipe teamRouge;
    private Equipe teamBleu;
    private LinkedList<Equipe> teamList = new LinkedList<Equipe>();

    private Location positionSpawnArene;
    private static Coffre coffre;
    private static boolean allowAreneTeleport = false;
    private static int areneTimer = 15;
    // Temps en minute
    private static int TIME_BETWEEN_ARENA_CHEST = 15;


    public static String prefix = ChatColor.BLUE + "[MINERALC] " + ChatColor.WHITE;
    public static String prefixErreur = ChatColor.BLUE + "[MINERALC] " + ChatColor.RED + "[ERREUR] " + ChatColor.WHITE;
    public static String prefixGlobal = ChatColor.BLUE + "[MINERALC] " + ChatColor.GREEN + "[GLOBAL] " + ChatColor.WHITE;
    public static String prefixPrive = ChatColor.BLUE + "[MINERALC] " + ChatColor.YELLOW + "[PRIVE] " + ChatColor.WHITE;


    // 60*60 car dans 60min il y a 60*60 sec
    public static int timeLeft = 60*60-1;
    public static int teamMaxPlayers = 2;
    private static int gameStarted = 0;
    private static boolean gamePaused = false;

    public static int SCORE_IRON = 10;
    public static int SCORE_GOLD = 50;
    public static int SCORE_DIAMOND = 150;
    public static int SCORE_EMERALD = 300;


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

    public static String RANDOMIZE_TEAM_BEGIN = "Vous allez etre attribué à une equipe de manière aléatoire.";
    public static String RANDOMIZE_TEAM_END = "Les équipes ont été crée !";

    public static String GLOBAL_CHEST_SPAWNED = "Le coffre est apparu dans l'arène !";
    public static String CHEST_DEFINED = "La position du coffre a bien été enregistrée";

    public static String GAME_STARTING_CHECKS = "Démarage des vérifications ...";

    public static mineralcontest plugin;

    // Constructeur, on initialise les variables
    public mineralcontest() {

        this.teamJaune = new Equipe("Jaune", ChatColor.YELLOW);
        this.teamRouge = new Equipe("Rouge", ChatColor.RED);
        this.teamBleu = new Equipe("Bleu", ChatColor.BLUE);

        this.coffre = new Coffre();

        mineralcontest.plugin = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerMort(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerSpawn(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new SafeZoneEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDisconnect(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerMove(), this);




        new BukkitRunnable() {
            public void run() {

                if(isGamePaused()) {
                    for(Player online : Bukkit.getOnlinePlayers()) {
                        Equipe equipe = mineralcontest.plugin.getPlayerTeam(online);
                        if(equipe == null)
                            ScoreboardUtil.unrankedSidebarDisplay(online, "   MineralContest   ", " ", mineralcontest.GAME_PAUSED, "", "Vous n'êtes pas dans une " + ChatColor.RED + "équipe");
                        else
                            ScoreboardUtil.unrankedSidebarDisplay(online, "   MineralContest   ", " ", mineralcontest.GAME_PAUSED, "", equipe.getCouleur() + "Equipe " + equipe.getNomEquipe(), " ", "Score: " + equipe.getScore() + " points");
                      }
                }

                if(mineralcontest.isGameStarted() && !isGamePaused()) {
                    if(timeLeft > 0) timeLeft--;

                    if(timeLeft <= 0) {
                        afficherScore();
                        afficherGagnant();
                    }

                        for(Player online : Bukkit.getOnlinePlayers()) {
                            Equipe equipe = mineralcontest.plugin.getPlayerTeam(online);
                            ScoreboardUtil.unrankedSidebarDisplay(online, "   MineralContest   ", " ", "Temps restant", getTempsRestant(), equipe.getCouleur() + "Equipe " + equipe.getNomEquipe(), " ", "Score: " + equipe.getScore() + " points");
                        }

                        if(timeLeft % (mineralcontest.TIME_BETWEEN_ARENA_CHEST*60) == 0 ){ // on fait spawn un chest
                            mineralcontest.allowAreneTeleport = true;
                            mineralcontest.areneTimer = 0;
                            getServer().broadcastMessage(prefixGlobal + "Le /arene est désormais disponible pendant 15 secondes !");
                        }

                        if(allowAreneTeleport) {
                            if(areneTimer >= 15) {
                                allowAreneTeleport = false;
                                getServer().broadcastMessage(prefixGlobal + "Le /arene n'est plus disponible!");
                            }
                            areneTimer++;
                        }

                    } else {
                        for(Player online : Bukkit.getOnlinePlayers()) {
                            Equipe equipe = mineralcontest.plugin.getPlayerTeam(online);
                            if(equipe == null)
                                ScoreboardUtil.unrankedSidebarDisplay(online, "   MineralContest   ", " ", mineralcontest.GAME_WAITING_START, "", "Vous n'êtes pas dans une " + ChatColor.RED + "équipe");
                            else
                                ScoreboardUtil.unrankedSidebarDisplay(online, "   MineralContest   ", " ", mineralcontest.GAME_WAITING_START, "", equipe.getCouleur() + "Equipe " + equipe.getNomEquipe());
                        }
                    }
                }

        }.runTaskTimer(plugin, 0, 20);



    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){


        if(cmd.getName().equalsIgnoreCase("join")){
            if(sender instanceof Player) {
                Player joueur = (Player) sender;
                if(args.length == 1) {
                    try {
                        this.addPlayerToTeam(joueur, args[0]);
                    }catch (Exception e) {
                        joueur.sendMessage(this.prefixErreur + e.getMessage());
                    }
                } else {
                    joueur.sendMessage(this.prefixErreur + "Utilisation de la commande: /join <team>");
                }
            }
            return true;
        }

        if(cmd.getName().equalsIgnoreCase("switch")){
            if(sender instanceof Player) {
                Player joueur = (Player) sender;
                if(args.length == 2) {
                    try {
                        this.switchPlayer(Bukkit.getPlayer(args[0]), args[1]);
                    }catch (Exception e) {
                        joueur.sendMessage(this.prefixErreur + e.getMessage());
                    }
                } else {
                    joueur.sendMessage(this.prefixErreur + "Utilisation de la commande: /switch <player> <team>");
                }
            }
            return true;
        }

        if(cmd.getName().equalsIgnoreCase("leave")){
            if(sender instanceof Player) {
                Player joueur = (Player) sender;
                try {
                    this.playerLeaveTeam(joueur);
                }catch (Exception e) {
                        joueur.sendMessage(this.prefixErreur + e.getMessage());
                }
            }
            return true;

        }

        if(cmd.getName().equalsIgnoreCase("start")){
            if(sender instanceof Player) {
                Player joueur = (Player) sender;
                try {
                    demarrerPartie();
                }catch (Exception e) {
                    joueur.sendMessage(this.prefixErreur + e.getMessage());
                }
            }
            return true;

        }

        if(cmd.getName().equalsIgnoreCase("randomizeTeam")){
            if(sender instanceof Player) {
                Player joueur = (Player) sender;
                try {
                    randomizeTeam();
                    this.teamList.add(teamBleu);
                    this.teamList.add(teamJaune);
                    this.teamList.add(teamRouge);


                }catch (Exception e) {
                    joueur.sendMessage(this.prefixErreur + e.getMessage());
                }
            }

            return true;


        }

        if(cmd.getName().equalsIgnoreCase("setSpawn")){
            if(sender instanceof Player) {
                Player joueur = (Player) sender;
                try {
                    // TODO only person with certain permissions can do this cmd
                    Location loc = joueur.getLocation();
                    this.setAreneLocation(loc);

                }catch (Exception e) {
                    joueur.sendMessage(this.prefixErreur + e.getMessage());
                }
            }

            return true;
        }

        if(cmd.getName().equalsIgnoreCase("setCoffre")){
            if(sender instanceof Player) {
                Player joueur = (Player) sender;
                try {
                    // TODO only person with certain permissions can do this cmd
                    Location loc = joueur.getLocation();
                    this.coffre.setPosition(loc);
                    joueur.sendMessage(mineralcontest.prefixPrive + mineralcontest.CHEST_DEFINED);

                }catch (Exception e) {
                    joueur.sendMessage(this.prefixErreur + e.getMessage());
                }
            }

            return true;
        }

        if(cmd.getName().equalsIgnoreCase("spawnCoffre")){
            if(sender instanceof Player) {
                Player joueur = (Player) sender;
                try {
                    // TODO only person with certain permissions can do this cmd

                    this.coffre.spawn();
                    getServer().broadcastMessage(mineralcontest.prefixGlobal + mineralcontest.GLOBAL_CHEST_SPAWNED);
                }catch (Exception e) {
                    joueur.sendMessage(this.prefixErreur + e.getMessage());
                }
            }

            return true;
        }

        if(cmd.getName().equalsIgnoreCase("setCoffreTeam")){
            if(sender instanceof Player) {
                Player joueur = (Player) sender;
                if(args.length == 1) {
                    try {
                        String[] equipes = {"rouge", "red", "bleu", "blue", "yellow", "jaune"};
                        // On fait un foreach pour parcourir le tableau d'équipe
                        for(String equipe : equipes) {
                            // Le nom de l'équipe passé en commentaire existe
                            if (args[1].toLowerCase().equalsIgnoreCase(equipe)) {
                                switch (equipe) {
                                    // On va vérifier si l'équipe est pleine ou non
                                    // Si elle l'est, on retourne FALSE
                                    // Sinon, on l'ajoute et on le supprime de son équipe initiale
                                    case "red":
                                    case "rouge":
                                        this.teamRouge.setCoffreEquipe(joueur.getLocation());
                                        break;

                                    case "jaune":
                                    case "yellow":
                                        this.teamJaune.setCoffreEquipe(joueur.getLocation());
                                        break;

                                    case "blue":
                                    case "bleu":
                                        this.teamBleu.setCoffreEquipe(joueur.getLocation());
                                        break;
                                }
                            }
                        }
                    }catch (Exception e) {
                        joueur.sendMessage(this.prefixErreur + e.getMessage());
                    }
                } else {
                    joueur.sendMessage(this.prefixErreur + "Utilisation de la commande: /setCoffreTeam  <team>");
                }
            }
            return true;
        }


        if(cmd.getName().equalsIgnoreCase("arene")){
            if(sender instanceof Player && allowAreneTeleport){
                Player joueur = (Player) sender;

                try{
                    if(this.gameStarted != 1) {
                        throw new Exception(this.ERROR_GAME_NOT_STARTED);
                    }

                    if(this.getPlayerTeam(joueur) == null)
                        throw new Exception(this.ERROR_PLAYER_NOT_IN_TEAM);


                    try {
                        Equipe team = getPlayerTeam(joueur);

                        for (Player P : team.getJoueurs()) {
                            P.teleport(this.positionSpawnArene);
                            P.sendMessage(this.prefixPrive + this.ARENA_TELEPORTING);
                        }
                    }catch(Exception e) {
                        joueur.sendMessage(this.prefixErreur + e.getMessage());
                    }


                }catch (Exception e){
                    joueur.sendMessage(this.prefixErreur + e.getMessage());
                }
            }

            return true;
        }

        if(cmd.getName().equalsIgnoreCase("teams")){
            if(sender instanceof Player){
                Player joueur = (Player) sender;

                try{
                    try {
                        getServer().broadcastMessage("================");
                        getServer().broadcastMessage(this.teamBleu.toString());
                        getServer().broadcastMessage(this.teamJaune.toString());
                        getServer().broadcastMessage(this.teamRouge.toString());
                        getServer().broadcastMessage("================");


                    }catch(Exception e) {
                        joueur.sendMessage(this.prefixErreur + e.getMessage());
                    }


                }catch (Exception e){
                    joueur.sendMessage(this.prefixErreur + e.getMessage());
                }
            }

            return true;
        }

        if(cmd.getName().equalsIgnoreCase("resume")){
            if(sender instanceof Player){
                Player joueur = (Player) sender;
                resumeGame();
            }

            return true;
        }

        if(cmd.getName().equalsIgnoreCase("setHouse")){
            if(sender instanceof Player) {
                Player joueur = (Player) sender;
                try {
                    // TODO only person with certain permissions can do this cmd
                    Location loc = joueur.getLocation();

                    if(args.length == 1){
                        if(args[0].equals("rouge")){
                            teamRouge.setHouseLocation(loc);
                            for ( Player P : teamRouge.getJoueurs()) {
                                P.setBedSpawnLocation(teamRouge.getHouseLocation(), true);
                            }
                        }else if(args[0].equals("jaune")){
                            teamJaune.setHouseLocation(loc);
                            for ( Player P : teamJaune.getJoueurs()) {
                                P.setBedSpawnLocation(teamJaune.getHouseLocation(), true);
                            }
                        }else if(args[0].equals("bleu")){
                            teamBleu.setHouseLocation(loc);
                            for ( Player P : teamBleu.getJoueurs()) {
                                P.setBedSpawnLocation(teamBleu.getHouseLocation(), true);
                            }
                        }
                    }

                }catch (Exception e) {
                    joueur.sendMessage(this.prefixErreur + e.getMessage());
                }
            }

            return true;
        }

        return true;
    }

    public static void afficherGagnant() {
        Equipe[] equipes = new Equipe[3];
        equipes[0] = plugin.teamBleu;
        equipes[1] = plugin.teamRouge;
        equipes[2] = plugin.teamJaune;

        int[] resultats = new int[3];
        int max = 0;
        int index = 0;
        for(int i = 0; i < 3; i++) {
            resultats[i] = equipes[i].getScore();
            if (resultats[i] >= max) {
                max = resultats[i];
                index = i;
            }
        }

        plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + "L'équipe " + equipes[index].getCouleur() + equipes[index].getNomEquipe() + ChatColor.WHITE + " remporte la partie avec " + equipes[index].getScore());

    }

    public static void afficherScore() {
        Equipe[] equipes = new Equipe[3];
        equipes[0] = plugin.teamBleu;
        equipes[1] = plugin.teamRouge;
        equipes[2] = plugin.teamJaune;

        for(int i = 0; i < 3; i++) {
            plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + "Equipe " + equipes[i].getCouleur() + equipes[i].getNomEquipe() + ChatColor.WHITE + ": " + equipes[i].getScore() + " point(s)");
        }
    }

    public void switchPlayer(Player joueur, String teamName) throws Exception {
        Equipe team = getPlayerTeam(joueur);

        if(team != null)
            team.removePlayer(joueur);
        String[] equipes = {"rouge", "red", "bleu", "blue", "yellow", "jaune"};
        // On fait un foreach pour parcourir le tableau d'équipe
        for(String equipe : equipes) {
            // Le nom de l'équipe passé en commentaire existe
            if (teamName.toLowerCase().equalsIgnoreCase(equipe)) {
                switch (equipe) {
                    // On va vérifier si l'équipe est pleine ou non
                    // Si elle l'est, on retourne FALSE
                    // Sinon, on l'ajoute et on le supprime de son équipe initiale
                    case "red":
                    case "rouge":
                        this.teamRouge.addPlayerToTeam(joueur);
                        break;

                    case "jaune":
                    case "yellow":
                        this.teamJaune.addPlayerToTeam(joueur);
                        break;

                    case "blue":
                    case "bleu":
                        this.teamBleu.addPlayerToTeam(joueur);
                        break;
                }
            }
        }

    }

    public static boolean isGamePaused() {
        return gamePaused;
    }
    public static void pauseGame() {
        gamePaused = true;
        for(Player online : mineralcontest.plugin.getServer().getOnlinePlayers()) {
            online.setGameMode(GameMode.SPECTATOR);
            online.sendMessage(mineralcontest.prefixPrive + "La partie à été mise en pause ! Vous ne pouvez plus bouger.");
            if(online.isOp()) {
                online.sendMessage(mineralcontest.prefixPrive + "Pour redemarrer la partie, veuillez utiliser /resume");
                online.sendMessage(mineralcontest.prefixPrive + "Pour switch un utilisateur qui s'est reconnecer, veuillez utiliser /switch <player> <team>");

            }
        }
    }

    public static boolean resumeGame() {
        // EQUIPES PLEINE
        if(!mineralcontest.plugin.teamRouge.isTeamFull()) {
            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + "[Verification] Equipe rouge pleine: " + ChatColor.RED + "X");
            return false;
        }
        if(!mineralcontest.plugin.teamBleu.isTeamFull()) {
            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + "[Verification] Equipe bleu pleine: " + ChatColor.RED + "X");
            return false;
        }
        if(!mineralcontest.plugin.teamJaune.isTeamFull()) {
            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + "[Verification] Equipe jaune pleine: " + ChatColor.RED + "X");
            return false;
        }

        for(Player online : mineralcontest.plugin.getServer().getOnlinePlayers()) {
            online.setGameMode(GameMode.SURVIVAL);
        }
        gamePaused = true;

        return true;
    }

    public void setAreneLocation(Location areneLocation) {
        Bukkit.getServer().broadcastMessage(this.prefixGlobal + this.ARENA_SPAWN_ADDED);
        this.positionSpawnArene = areneLocation;
    }

    public Location getAreneLocation() throws Exception {
        if(this.positionSpawnArene == null)
            throw new Exception(this.ERROR_ARENA_NOT_DEFINED);
        return positionSpawnArene;
    }

    public void afficherEquipe() {
        getServer().broadcastMessage(this.prefixGlobal + this.teamBleu.toString());
        getServer().broadcastMessage(this.prefixGlobal + this.teamRouge.toString());
        getServer().broadcastMessage(this.prefixGlobal + this.teamJaune.toString());

    }

    private boolean demarrerPartie() throws Exception {

        if(this.gameStarted != 0) {
            throw new Exception(this.ERROR_GAME_ALREADY_STARTED);
        }

        getServer().broadcastMessage(this.prefixGlobal + this.GAME_STARTING);
        getServer().broadcastMessage("=============================");
        getServer().broadcastMessage(this.prefixGlobal + this.GAME_STARTING_CHECKS);
        // Pour démarrer la partie, il faut:
        // Tous les spawn maison défini
        // Spawn coffre arene définit
        // Spawn arene définit
        // Toutes les equipes soient pleine

        // SPAWN MAISON
        if(this.teamBleu.getHouseLocation() == null) {
            getServer().broadcastMessage(this.prefixGlobal + "[Verification] Spawn maison bleu: " + ChatColor.RED + "X");
            return false;
        }

        if(this.teamRouge.getHouseLocation() == null) {
            getServer().broadcastMessage(this.prefixGlobal + "[Verification] Spawn maison rouge: " + ChatColor.RED + "X");
            return false;
        }
        if(this.teamJaune.getHouseLocation() == null) {
            getServer().broadcastMessage(this.prefixGlobal + "[Verification] Spawn maison jaune: " + ChatColor.RED + "X");
            return false;
        }

        getServer().broadcastMessage(this.prefixGlobal + "[Verification] Spawn maison: " + ChatColor.GREEN + "OK");

        // SPAWN COFFRE MAISON
        if(this.teamJaune.getCoffreEquipeLocation() == null) {
            getServer().broadcastMessage(this.prefixGlobal + "[Verification] spawn coffre maison equipe " + this.teamJaune.getNomEquipe() + ": " + ChatColor.RED + "X");
            return false;
        }

        if(this.teamRouge.getCoffreEquipeLocation() == null) {
            getServer().broadcastMessage(this.prefixGlobal + "[Verification] spawn coffre maison equipe " + this.teamRouge.getNomEquipe() + ": " + ChatColor.RED + "X");
            return false;
        }

        if(this.teamBleu.getCoffreEquipeLocation() == null) {
            getServer().broadcastMessage(this.prefixGlobal + "[Verification] spawn coffre maison equipe " + this.teamBleu.getNomEquipe() + ": " + ChatColor.RED + "X");
            return false;
        }
        getServer().broadcastMessage(this.prefixGlobal + "[Verification] Spawn coffre arene: " + ChatColor.GREEN + "OK");

        // SPAWN COFFRE ARENE
        if(this.coffre.getPosition() == null) {
            getServer().broadcastMessage(this.prefixGlobal + "[Verification] spawn coffre arene: " + ChatColor.RED + "X");
            return false;
        }
        getServer().broadcastMessage(this.prefixGlobal + "[Verification] Spawn coffre arene: " + ChatColor.GREEN + "OK");



        // SPAWN ARENE
        if(this.positionSpawnArene == null) {
            getServer().broadcastMessage(this.prefixGlobal + "[Verification] spawn arene: " + ChatColor.RED + "X");
            return false;
        }
        getServer().broadcastMessage(this.prefixGlobal + "[Verification] Spawn arene: " + ChatColor.GREEN + "OK");


        randomizeTeam();

        // EQUIPES PLEINE
        if(!this.teamRouge.isTeamFull()) {
            getServer().broadcastMessage(this.prefixGlobal + "[Verification] Equipe rouge pleine: " + ChatColor.RED + "X");
            return false;
        }
        if(!this.teamBleu.isTeamFull()) {
            getServer().broadcastMessage(this.prefixGlobal + "[Verification] Equipe bleu pleine: " + ChatColor.RED + "X");
            return false;
        }
        if(!this.teamJaune.isTeamFull()) {
            getServer().broadcastMessage(this.prefixGlobal + "[Verification] Equipe jaune pleine: " + ChatColor.RED + "X");
            return false;
        }
        getServer().broadcastMessage(this.prefixGlobal + "[Verification] Equipes pleines: " + ChatColor.GREEN + "OK");


        getServer().broadcastMessage(this.prefixGlobal + this.GAME_SUCCESSFULLY_STARTED);
        getServer().broadcastMessage("=============================");


        this.gameStarted = 1;
        return true;

    }

    // Créer les equipes aléatoirement
    public void randomizeTeam() throws Exception {
        if(this.gameStarted != 0) {
            throw new Exception(this.ERROR_GAME_ALREADY_STARTED);
        }

        if(mineralcontest.teamMaxPlayers*3 != getServer().getOnlinePlayers().size())
           throw new Exception(this.ERROR_NOT_ENOUGHT_PLAYER);

        ArrayList<String> team = new ArrayList<String>();


        getServer().broadcastMessage("================");


        getServer().broadcastMessage(this.prefixGlobal + this.RANDOMIZE_TEAM_BEGIN);
        for(int i = 0; i < mineralcontest.teamMaxPlayers; i++){
            team.add("jaune");
            team.add("rouge");
            team.add("bleu");
        }

        for(Player joueur : this.teamRouge.getJoueurs()) {
            this.teamRouge.removePlayer(joueur);
        }

        for(Player joueur : this.teamJaune.getJoueurs()) {
            this.teamJaune.removePlayer(joueur);
        }

        for(Player joueur : this.teamBleu.getJoueurs()) {
            this.teamBleu.removePlayer(joueur);
        }

        int random;
        Random r = new Random();
        String result;
        int indexJoueur = 0;
        Object[] joueurs = Bukkit.getServer().getOnlinePlayers().toArray();

        while(team.size() > 0 && indexJoueur != joueurs.length) {
            random = r.nextInt(team.size());
            result = team.get(random);

            if(result.equals("jaune")) {
                this.teamJaune.addPlayerToTeam((Player) joueurs[indexJoueur]);
                team.remove(random);
            }

            if(result.equals("rouge")) {
                this.teamRouge.addPlayerToTeam((Player) joueurs[indexJoueur]);
                team.remove(random);
            }

            if(result.equals("bleu")) {
                this.teamBleu.addPlayerToTeam((Player) joueurs[indexJoueur]);
                team.remove(random);
            }

            indexJoueur++;
        }

        getServer().broadcastMessage(this.prefixGlobal + this.RANDOMIZE_TEAM_END);
        getServer().broadcastMessage("================");

    }


    // Ajoute un joueur a une equipe
    private void addPlayerToTeam(Player p, String teamName) throws Exception {
        if(this.gameStarted != 0) {
            throw new Exception(this.ERROR_GAME_ALREADY_STARTED);
        }

        String[] equipes = {"rouge", "red", "bleu", "blue", "yellow", "jaune"};
        // On fait un foreach pour parcourir le tableau d'équipe
        for(String equipe : equipes) {
            // Le nom de l'équipe passé en commentaire existe
            if (teamName.toLowerCase().equalsIgnoreCase(equipe)) {
                switch (equipe) {
                    // On va vérifier si l'équipe est pleine ou non
                    // Si elle l'est, on retourne FALSE
                    // Sinon, on l'ajoute et on le supprime de son équipe initiale
                    case "red":
                    case "rouge":
                        if(this.teamRouge.isTeamFull()) {
                            throw new FullTeamException("rouge");
                        }
                        this.teamRouge.addPlayerToTeam(p);
                        break;

                    case "jaune":
                    case "yellow":
                        if(this.teamJaune.isTeamFull()) {
                            throw new FullTeamException("rouge");
                        }
                        this.teamJaune.addPlayerToTeam(p);
                        break;

                    case "blue":
                    case "bleu":
                        if(this.teamBleu.isTeamFull()) {
                            throw new FullTeamException("rouge");
                        }
                        this.teamBleu.addPlayerToTeam(p);
                        break;
                }
            }
        }
    }


    // Retourne vrai si le joueur est dans une equipe
    public boolean isPlayerInATeam(Player P) {
        return (this.teamBleu.isPlayerInTeam(P) || this.teamRouge.isPlayerInTeam(P) || this.teamJaune.isPlayerInTeam(P));
    }


    // Retourne l'equipe d'un joueur, NULL si sans team
    public Equipe getPlayerTeam(Player x) {
        if(this.teamBleu.isPlayerInTeam(x)) return this.teamBleu;
        if(this.teamRouge.isPlayerInTeam(x)) return this.teamRouge;
        if(this.teamJaune.isPlayerInTeam(x)) return this.teamJaune;
        return null;
    }


    // Retire un joueur de son equipe
    public boolean playerLeaveTeam(Player x) throws Exception {
        if(this.gameStarted != 0) {
            throw new Exception(this.ERROR_GAME_ALREADY_STARTED);
        }

        this.getPlayerTeam(x).removePlayer(x);
        return true;
    }

    public static String getTempsRestant() {
        int minutes, secondes;
        minutes = (timeLeft % 3600) / 60;
        secondes = (timeLeft % 60);
        return String.format("%02d:%02d", minutes, secondes);
    }

    public static boolean isGameStarted() {
        if(gameStarted == 0)
            return false;
        return true;
    }

    public Location getAreneSpawnLoc() {
        return this.positionSpawnArene;
    }

    public Coffre getCoffre() {
        return this.coffre;
    }
}
