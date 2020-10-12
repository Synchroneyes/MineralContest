package fr.synchroneyes.mineral;

import fr.synchroneyes.custom_events.PermissionCheckerLoop;
import fr.synchroneyes.file_manager.FileList;
import fr.synchroneyes.file_manager.RessourceFilesManager;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.groups.GroupeExtension;
import fr.synchroneyes.groups.Utils.Etats;
import fr.synchroneyes.mapbuilder.MapBuilder;
import fr.synchroneyes.mineral.Commands.*;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.Game.JoinTeam.JoinTeamInventoryEvent;
import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.Core.Parachute.Events.ParachuteHitDetection;
import fr.synchroneyes.mineral.Core.Player.BaseItem.Commands.SetDefaultItems;
import fr.synchroneyes.mineral.Core.Player.BaseItem.Events.InventoryClick;
import fr.synchroneyes.mineral.Core.Referee.Referee;
import fr.synchroneyes.mineral.Core.Referee.RefereeEvent;
import fr.synchroneyes.mineral.Events.*;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.Log.GameLogger;
import fr.synchroneyes.mineral.Utils.Log.Log;
import fr.synchroneyes.mineral.Utils.Player.PlayerUtils;
import fr.synchroneyes.mineral.Utils.UrlFetcher.Urls;
import fr.synchroneyes.mineral.Utils.VersionChecker.Version;
import fr.synchroneyes.world_downloader.WorldDownloader;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.*;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public final class mineralcontest extends JavaPlugin {

    public static boolean debug = false;

    // TODO: kits onGameStart

    /**
     * Community version is a version ran by server that wants to have multiple games running at the same time.
     * If set to true, then players will have to create a groupe, invite players, start the vote.
     * If set to false, one group will automatically be created, OP players will be set as admin, and every players joining the server
     * will automatically be added to the group.
     */
    public static boolean communityVersion = false;

    public static String prefix = "[MineralContest]";
    public static String prefixErreur;
    public static String prefixGlobal;
    public static String prefixPrive;
    public static String prefixAdmin;
    public static String prefixTeamChat;
    public static String prefixGroupe;
    public static String prefixWeb;

    public static Logger log = Bukkit.getLogger();
    public static mineralcontest plugin;
    public World pluginWorld;

    public Location defaultSpawn;
    public MapBuilder mapBuilderInstance;


    /**
     * Array of all the messages we can fetch from synchroneyes's plugin website.
     */
    private ArrayList<String> messagesFromWebsite;
    public GroupeExtension groupeExtension;

    @Getter
    public LinkedList<Groupe> groupes;
    public WorldDownloader worldDownloader;

    /**
     * List of every players of the plugin
     */
    private List<MCPlayer> joueurs;

    // Constructeur, on initialise les variables
    public mineralcontest() {
        mineralcontest.plugin = this;
        this.joueurs = new ArrayList<>();
    }


    /**
     * Affiche dans le chat les messages récupéré depuis le site web, lié à cette version du plugin
     */
    public static void afficherMessageVersion() {
        // Pour chaque message
        for (String message : plugin.messagesFromWebsite) {
            broadcastMessage(mineralcontest.prefixWeb + message);
        }
    }

    public static void afficherMessageVersionToPlayer(Player joueur) {
        for (String message : plugin.messagesFromWebsite) {
            joueur.sendMessage(mineralcontest.prefixWeb + message);
        }

    }


    public static Object getPluginConfigValue(String configName) {
        File fichierConfigurationPlugin = new File(plugin.getDataFolder(), FileList.Config_default_plugin.toString());
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(fichierConfigurationPlugin);
        return configuration.get(configName);
    }

    public static void supprimerGroupe(Groupe g) {
        plugin.groupes.remove(g);
    }

    public static Groupe getPlayerGroupe(Player p) {
        mineralcontest instance = plugin;
        MCPlayer joueur = instance.getMCPlayer(p);

        if(joueur == null) return null;
        return joueur.getGroupe();

    }

    public Groupe getNonCommunityGroup() {
        return groupes.getFirst();
    }

    public void creerNouveauGroupe(Groupe nouveauGroupe) {

        if (!communityVersion) return;

        GameLogger.addLog(new Log("group_create", "Creating a new group with name " + nouveauGroupe.getNom(), "mineralcontest: creerNouveauGroupe"));
        for (Groupe groupe : groupes) {
            if (groupe.getNom().equals(nouveauGroupe.getNom())) {
                groupe.sendToadmin(prefixErreur + Lang.error_group_with_this_name_already_exists.toString());
                GameLogger.addLog(new Log("error_group_create", Lang.error_group_with_this_name_already_exists.getDefault(), "mineralcontest: creerNouveauGroupe"));
                return;
            }
        }

        while (!isGroupeIdentifiantUnique(nouveauGroupe)) {
            nouveauGroupe.genererIdentifiant();
        }


        this.groupes.add(nouveauGroupe);
        nouveauGroupe.sendToEveryone(prefixPrive + Lang.success_group_successfully_created.toString());
        GameLogger.addLog(new Log("group_created", "created a new group with name " + nouveauGroupe.getNom() + " and ID: " + nouveauGroupe.getIdentifiant(), "mineralcontest: creerNouveauGroupe"));

    }

    private boolean isGroupeIdentifiantUnique(Groupe groupe) {
        for (Groupe g : groupes)
            if (g.getIdentifiant().equalsIgnoreCase(groupe.getIdentifiant())) return false;
        return true;
    }

    public void initCommunityVersion() {
        if (!communityVersion) {
            Groupe defaut = new Groupe();
            defaut.setEtat(Etats.EN_ATTENTE);
            defaut.setNom("MineralContest");
            groupes.add(defaut);

            if (pluginWorld == null) pluginWorld = PlayerUtils.getPluginWorld();
            if (pluginWorld == null) return;
            for (Player joueur : mineralcontest.plugin.pluginWorld.getPlayers())
                if (joueur.isOp()) getNonCommunityGroup().addAdmin(joueur);
                else getNonCommunityGroup().addJoueur(joueur);
        }
    }

    @Override
    public void onEnable() {


        // On copie les fichiers par défaut
        RessourceFilesManager.createDefaultFiles();


        // On charge le fichier de langue
        Lang.loadLang(getPluginConfigValue("language").toString());

        this.groupes = new LinkedList<>();
        this.groupeExtension = GroupeExtension.getInstance();

        initCommunityVersion();

        // Initialisation des variables du plugin
        this.mapBuilderInstance = MapBuilder.getInstance();
        this.messagesFromWebsite = new ArrayList<>();
        this.worldDownloader = WorldDownloader.getInstance();

        // Initialisation des events customs
        PermissionCheckerLoop permissionCheckerLoop = new PermissionCheckerLoop(this, 1);
        permissionCheckerLoop.run();


        pluginWorld = PlayerUtils.getPluginWorld();
        defaultSpawn = (pluginWorld != null) ? pluginWorld.getSpawnLocation() : null;
        if (pluginWorld != null) pluginWorld.setDifficulty(Difficulty.PEACEFUL);


        registerCommands();
        registerEvents();


        if (!debug)
            if (pluginWorld != null)
                for (Player online : pluginWorld.getPlayers())
                    PlayerUtils.teleportPlayer(online, defaultSpawn.getWorld(), defaultSpawn);

        PlayerUtils.runScoreboardManager();
        GameLogger.addLog(new Log("server_event", "OnEnable", "plugin_startup"));

        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerUtils.drawPlayersHUD();
            }
        }.runTaskTimer(this, 0, 10);



        /*
            Verification de mise à jour et téléchargement automatique
            Seulement si la config l'autorise
         */
        if ((boolean) getPluginConfigValue("enable_auto_update")) {

            // On lance la procédure de vérification de version une fois que le plugin est totalement chargé
            getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {


                Version.isCheckingStarted = true;
                Thread operationsThreade = new Thread(() -> {
                    // On récupère toutes les URL du plugin
                    Urls.FetchAllUrls();

                    if(Urls.isWebsiteDown) {
                        Bukkit.broadcastMessage(ChatColor.RED + Urls.WEBSITE_URL + " is down. Please check on our discord to get the latest plugin version & maps mirrors link");
                        return;
                    }

                    worldDownloader.initMapLists();

                    Version.fetchAllMessages(messagesFromWebsite);

                    afficherMessageVersion();

                    Version.Check(true);

                });

                operationsThreade.start();

                // On lance un timer qui vérifie, à chaque seconde, si le téléchargement est terminé
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // La procédure à commencer
                        if (Version.isCheckingStarted && !Urls.isWebsiteDown) {

                            // Si le plugin a été mis à jour, on reload le plugin
                            if (Version.hasUpdated) {
                                Bukkit.reload();
                            }

                        } else {
                            // On arrête le timer
                            this.cancel();
                        }
                    }
                }.runTaskTimer(this, 20, 20);

            });



        }

    }

    @Override
    public void onDisable() {

        for (Groupe groupe : groupes) {
            Game game = groupe.getGame();
            //SendInformation.sendGameData(SendInformation.ended, game);
            // todo GETGAME
            if (pluginWorld != null && !debug) {
                for (Player player : pluginWorld.getPlayers()) {
                    game.teleportToLobby(player);
                    PlayerUtils.clearPlayer(player, true);
                }
            }
        }


        //getGame().resetMap();
        Bukkit.getScheduler().cancelTasks(this);
        GameLogger.addLog(new Log("server_event", "OnDisable", "plugin_shutdown"));

    }

    private void registerEvents() {

        Bukkit.getServer().getPluginManager().registerEvents(new ArmorStandPickup(), this);

        Bukkit.getServer().getPluginManager().registerEvents(new BlockDestroyed(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BlockPlaced(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EntityInteract(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteract(), this);


        Bukkit.getServer().getPluginManager().registerEvents(new BucketEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ChestEvent(), this);

        //
        Bukkit.getServer().getPluginManager().registerEvents(new EntityDamage(), this);
        //Bukkit.getServer().getPluginManager().registerEvents(new PlayerDeathEvent(), this);


        Bukkit.getServer().getPluginManager().registerEvents(new EntityTarget(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EntitySpawn(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EntityDeathEvent(), this);




        Bukkit.getServer().getPluginManager().registerEvents(new ExplosionEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ItemDropped(), this);


        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDisconnect(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerKilledByPlayer(), this);

        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);

        Bukkit.getServer().getPluginManager().registerEvents(new PlayerMove(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerSpawn(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDeathEvent(), this);

        Bukkit.getServer().getPluginManager().registerEvents(new SafeZoneEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerChat(), this);


        Bukkit.getServer().getPluginManager().registerEvents(new RefereeEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerPick(), this);
        //Bukkit.getServer().getPluginManager().registerEvents(new PlayerWorldChange(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new WorldLoaded(), this);

        Bukkit.getServer().getPluginManager().registerEvents(new PlayerPermissionChange(), this);


        // PlayerBaseItem
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryClick(), this);

        // JoinMenu
        Bukkit.getServer().getPluginManager().registerEvents(new JoinTeamInventoryEvent(), this);

        // Drop
        Bukkit.getServer().getPluginManager().registerEvents(new ParachuteHitDetection(), this);

        // Metric
        Bukkit.getServer().getPluginManager().registerEvents(new GameStart(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new GameEnd(), this);


        // AutomatedChest
        //Bukkit.getServer().getPluginManager().registerEvents(new ChestOpenEvent(), this);



    }

    private void registerCommands() {

        // Register les commands
        getCommand("start").setExecutor(new StartGameCommand());
        getCommand("pause").setExecutor(new PauseGameCommand());
        getCommand("stopGame").setExecutor(new StopGameCommand());
        getCommand("arene").setExecutor(new AreneTeleportCommand());
        getCommand("arena").setExecutor(new AreneTeleportCommand());
        getCommand("join").setExecutor(new JoinCommand());
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
            bukkitCommandMap.register("", new SetDefaultItems());
            bukkitCommandMap.register("", new SpawnDrop());
            bukkitCommandMap.register("", new RefereeCommand());

            //bukkitCommandMap.register("", new SaveDropToFile());


            //bukkitCommandMap.register("", new SpawnShop());

            //bukkitCommandMap.register("", new SpawnDrop());
            bukkitCommandMap.register("", new McStats());
            //bukkitCommandMap.register("", new GetPlayerDistance());


            //bukkitCommandMap.register("", new OuvrirMenuShop());

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }



        getCommand("spawnchest").setExecutor(new SpawnChestCommand());
        getCommand("allow").setExecutor(new AllowCommand());
        getCommand("leaveteam").setExecutor(new LeaveTeamCommand());

    }


    public void setDefaultWorldBorder() {

        if (mapBuilderInstance.isBuilderModeEnabled) return;

        World game_world = mineralcontest.plugin.pluginWorld;
        int size = 30000000;

        if (game_world != null) {
            game_world.getWorldBorder().setCenter(mineralcontest.plugin.defaultSpawn);
            game_world.getWorldBorder().setSize(size);
        }
    }


    public static void broadcastMessage(String message, Groupe groupe) {
        groupe.sendToEveryone(message);
        Bukkit.getConsoleSender().sendMessage(message);
        GameLogger.addLog(new Log("broadcast-group", message, "server"));
    }

    public static void broadcastMessage(String message) {
        for (Player joueur : Bukkit.getOnlinePlayers())
            if (isInAMineralContestWorld(joueur))
                joueur.sendMessage(message);

        Bukkit.getConsoleSender().sendMessage(message);
        GameLogger.addLog(new Log("broadcast-plugin", message, "server"));
    }

    public static void broadcastMessageToAdmins(String message) {
        for (Player player : mineralcontest.plugin.pluginWorld.getPlayers())
            if (player.isOp()) player.sendMessage(message);
        Bukkit.getLogger().info(message);
        GameLogger.addLog(new Log("broadcast_admin", message, "server"));
    }


    public static boolean isInAMineralContestWorld(Player p) {
        MCPlayer joueur = plugin.getMCPlayer(p);
        if(joueur == null) return false;
        return (p.getWorld().equals(plugin.pluginWorld) || joueur.getGroupe().getMonde() != null);
    }

    public static boolean isInMineralContestHub(Player p) {
        return p.getWorld().equals(plugin.pluginWorld);
    }

    public static boolean isAMineralContestWorld(World w) {

        if (w.equals(plugin.pluginWorld)) return true;

        for (Groupe groupe : plugin.groupes)
            if (groupe.getMonde() == null) return false;
            else if (w.equals(groupe.getMonde())) return true;
        return w.equals(plugin.pluginWorld);
    }

    public static Game getPlayerGame(Player p) {
        Groupe g = getPlayerGroupe(p);
        if (g != null) return g.getGame();
        return null;
    }

    public static Game getWorldGame(World world) {
        for (Groupe groupe : plugin.groupes) {
            if (groupe.getMonde() == null && world != mineralcontest.plugin.pluginWorld) return null;
            if (groupe.getMonde() == null && world == mineralcontest.plugin.pluginWorld) return groupe.getGame();

            else if (groupe.getMonde().equals(world))
                return groupe.getGame();
        }
        return null;
    }

    /**
     * Méthode permettant d'ajouter un nouveau joueur au plugin
     * @param nouveauJoueur
     */
    public void addNewPlayer(Player nouveauJoueur) {

        // On vérifie si il existe déjà
        for(MCPlayer joueur: joueurs)
            // Si il existe déjà, on ne l'ajoute pas
            if(joueur.getJoueur().equals(nouveauJoueur)) return;

        MCPlayer joueur = new MCPlayer(nouveauJoueur);

        this.joueurs.add(joueur);



    }

    /**
     * Méthode permettant de supprimer un joueur de la partie
     * @param joueur
     */
    public void removePlayer(Player joueur) {
        // On vérifie si il existe déjà
        for(MCPlayer _joueur: joueurs)
            // Si il existe déjà, on ne l'ajoute pas
            if(_joueur.getJoueur().equals(joueur)) {
                this.joueurs.remove(_joueur);
                return;
            }
    }

    /**
     * Méthode permettant de récuperer l'instance du joueur
     * @param joueur
     * @return
     */
    public MCPlayer getMCPlayer(Player joueur) {
        for(MCPlayer _joueur: joueurs)
            // Si il existe déjà, on ne l'ajoute pas
            if(_joueur.getJoueur().equals(joueur)) return _joueur;
        return null;
    }

    public List<MCPlayer> getMCPlayers() { return joueurs; }



}
