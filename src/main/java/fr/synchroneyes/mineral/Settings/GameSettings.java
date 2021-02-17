package fr.synchroneyes.mineral.Settings;

import fr.synchroneyes.file_manager.FileList;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Utils.ErrorReporting.Error;
import fr.synchroneyes.mineral.Utils.Log.GameLogger;
import fr.synchroneyes.mineral.Utils.Log.Log;
import fr.synchroneyes.mineral.mineralcontest;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class GameSettings {

    // La liste des paramètres
    private LinkedList<GameCVAR> parametres;

    private static LinkedList<GameCVAR> parametresParDefaut;
    private File fichierConfiguration;

    // Les paramètres du fichier de configuration à ne pas tenir compte
    private static LinkedList<GameCVAR> parametresExclu;

    private Groupe groupe;

    public GameSettings(boolean loadDefaultSettings, Groupe g) {
        if (parametresExclu == null) {
            parametresExclu = new LinkedList<>();
            parametresExclu.add(new GameCVAR("chest_content", "", "", "arena", false, false));
        }

        parametres = new LinkedList<>();
        if (loadDefaultSettings) {
            for (GameCVAR parametre : getParametresParDefaut())
                parametres.add(new GameCVAR(parametre.getCommand(), parametre.getValeur(), parametre.getDescription(), parametre.getType(), parametre.canBeReloaded(), parametre.isNumber()));
        }



        fichierConfiguration = new File(mineralcontest.plugin.getDataFolder(), FileList.Config_default_game.toString());

        // On regarde si le fichier de configuration contient tout
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(fichierConfiguration);
        if(configuration != null) {
            for(GameCVAR parametre : parametres){
                if(!configuration.contains("config." + parametre.getType() + "." + parametre.getCommand())) {
                    saveCVAR(parametre);
                }
            }
        }

        this.groupe = g;
    }


    /**
     * Retourne si un parametre passé en argument doit être exclu
     *
     * @param cvar
     * @return
     */
    private static boolean isCvarExcluded(GameCVAR cvar) {
        for (GameCVAR paramtreExclu : parametresExclu)
            if (paramtreExclu.getCommand().equalsIgnoreCase(cvar.getCommand())) return true;
        return false;
    }

    public LinkedList<GameCVAR> getParametres() {
        return parametres;
    }

    /**
     * Retourne la valeur d'un paramètre
     *
     * @param cvar
     * @return null, int, string
     */
    public GameCVAR getCVAR(String cvar) {
        for (GameCVAR parametre : parametres) {
            // Si la commande passée en argument est égale au paramtre actuel
            if (parametre.getCommand().equalsIgnoreCase(cvar)) {
                return parametre;
            }
        }

        Bukkit.getLogger().severe(mineralcontest.prefixErreur + "Invalid cvar name: \"" + cvar + "\"");
        return null;
    }


    /**
     * Permet d'attribuer une valeur à un paramètre
     *
     * @param cvar   - Le paramètre
     * @param valeur - LA valeur
     * @throws Exception
     */
    public void setCVARValeur(String cvar, String valeur) throws Exception {
        GameCVAR gameCVAR = null;
        for (GameCVAR parametre : parametres) {
            if (parametre.getCommand().equalsIgnoreCase(cvar)) {


                gameCVAR = parametre;

                gameCVAR.setValeur(valeur);

                if (parametre.getCommand().equalsIgnoreCase("mp_enable_old_pvp")) {
                    for (Player player : groupe.getPlayers()) {
                        if (parametre.getValeurNumerique() == 1) {
                            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(1024d);
                        } else {
                            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);

                        }
                    }

                }

                if (parametre.getCommand().equalsIgnoreCase("enable_kits")) {
                    if (parametre.getValeurNumerique() == 1) {
                        groupe.getKitManager().setKitsEnabled(true);
                    } else {
                        groupe.getKitManager().setKitsEnabled(false);
                    }
                }


                // On vérifie le paramètre du shop
                if (parametre.getCommand().equalsIgnoreCase("enable_shop")) {
                    // Si la valeur est à 1, on active le shop
                    if (parametre.getValeurNumerique() == 1) {
                        groupe.getGame().getShopManager().enableShop();
                    } else {
                        groupe.getGame().getShopManager().disableShop();
                    }
                }

                break;
            }
        }


        if (gameCVAR == null)
            throw new Exception("Impossible d'appliquer la valeur " + valeur + " à " + cvar + " car ce paramètre est inconnu");

    }

    /**
     * Vérifie dans tous les paramètres existants si celui passé en argument existe
     *
     * @param cvar
     * @return bool
     */
    public boolean doesCvarExists(GameCVAR cvar) {
        for (GameCVAR parametre : parametres) {
            if (parametre.getCommand().equalsIgnoreCase(cvar.getCommand())) return true;
        }
        return false;
    }


    /**
     * Permet de sauvegarder la configuration actuelle vers un fichier
     * @param saveDefaultPluginSetting - True si on sauvegarde le fichier par défaut du plugin, false si un fichier custom
     */
    public void saveToFile(String nomDeFichier, boolean saveDefaultPluginSetting) {

        GameLogger.addLog(new Log("game_cvar", "About to save current configuration into a file named " + nomDeFichier + ".yml", "GameSettings: saveToFile"));

        File dossierConfiguration = null;
        if (saveDefaultPluginSetting) dossierConfiguration = mineralcontest.plugin.getDataFolder();
        else dossierConfiguration = new File(mineralcontest.plugin.getDataFolder() + File.separator + "saved-configs");

        // Si le dossier n'existe pas, on le crée
        if (!dossierConfiguration.exists()) dossierConfiguration.mkdir();

        // On crée le nouveau fichier
        File nouveauFichierConfig = new File(dossierConfiguration, nomDeFichier + ".yml");
        GameLogger.addLog(new Log("game_cvar", "Created file " + nomDeFichier + ".yml in folder " + dossierConfiguration.getAbsolutePath(), "GameSettings: saveToFile"));


        // On enregiste notre configuration dans ce fichier
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(nouveauFichierConfig);
        ConfigurationSection sectionConfig = yamlConfiguration.createSection("config");

        for (GameCVAR parametre : parametres) {
            sectionConfig.set(parametre.getType() + "." + parametre.getCommand(), parametre.getValeur());
        }

        try {
            yamlConfiguration.save(nouveauFichierConfig);
        } catch (IOException e) {
            e.printStackTrace();
            Error.Report(e, null);
        }


    }

    /**
     * Permet d'écrire la configuration actuelle du plugin dans le fichier de config
     * Permet en gros de sauvegarder la configuration actuelle
     */
    public void saveCVAR(GameCVAR cvar) {
        File fichierConfig = new File(mineralcontest.plugin.getDataFolder(), FileList.Config_default_game.toString());
        if (!fichierConfig.exists()) return;

        // On charge la configuration
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fichierConfig);

        // On regarde si le cvar existe ou non
        String file_cvar = "config." + cvar.getType() + "." + cvar.getCommand();
        yamlConfiguration.set(file_cvar, cvar.getValeur());
        //Bukkit.getLogger().warning(file_cvar + " => " + cvar.getValeur());

        try {
            yamlConfiguration.save(fichierConfig);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }


    }


    /**
     * Permet de charger la configuration de la partie à partir d'un fichier
     *
     * @param fichierDeConfiguration - Le fichier de configuration
     */
    public void loadFromFile(File fichierDeConfiguration) {
        // On charge le fichier de configuration
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fichierDeConfiguration);
        // On récupère les configurations
        ConfigurationSection config = yamlConfiguration.createSection("config");

        Bukkit.getLogger().info(mineralcontest.prefix + "Loading CVARS from file " + fichierDeConfiguration.getAbsolutePath());

        // On vide les paramètres actuels
        parametres.clear();

        for (String typeParametre : config.getKeys(false)) {
            for (String nomParamtre : config.getConfigurationSection(typeParametre).getKeys(false)) {
                boolean isNumber = false;
                String valeur = (String) config.get(typeParametre + "." + nomParamtre);
                isNumber = StringUtils.isNumeric(valeur);
                GameCVAR cvar = new GameCVAR(nomParamtre, valeur, "", typeParametre, true, isNumber);
                if (!doesCvarExists(cvar) && !isCvarExcluded(cvar)) {
                    parametres.add(cvar);
                    GameLogger.addLog(new Log("game_cvar", "Loaded " + typeParametre + "." + nomParamtre + " (value: " + valeur + ") from configuration file " + fichierDeConfiguration.getAbsolutePath(), "GameSettings: loadConfigFromFile"));
                    Bukkit.getLogger().info(mineralcontest.prefix + "Loaded CVAR " + typeParametre + "." + nomParamtre + " with value: " + valeur);
                }
            }
        }
        GameLogger.addLog(new Log("game_cvar", "Loading game cvar ended with success", "GameSettings: loadConfigFromFile"));
        Bukkit.getLogger().info(mineralcontest.prefix + "Loading CVARS from file " + fichierDeConfiguration.getAbsolutePath() + " done !");


    }


    public static LinkedList<GameCVAR> getParametresParDefaut() {
        if (parametresParDefaut == null) parametresParDefaut = new LinkedList<>();
        if (parametresParDefaut.isEmpty()) {
            GameLogger.addLog(new Log("game_cvar", "Adding default game cvars ...", "GameSettings: getParametresParDefaut"));
            parametresParDefaut.add(new GameCVAR("mp_randomize_team", "0", "Permet d'activer ou non les équipes aléatoires", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("mp_enable_item_drop", "2", "Permet d'activer ou non le drop d'item à la mort. 0 pour aucun, 1 pour les minerais uniquement, 2 pour tout", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("SCORE_IRON", "10", "Permet de définir le score pour un lingot de fer", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("SCORE_GOLD", "50", "Permet de définir le score pour un lingot d'or", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("SCORE_DIAMOND", "150", "Permet de définir le score pour un diamant", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("SCORE_EMERALD", "300", "Permet de définir le score pour un émeraude", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("SCORE_REDSTONE", "-3", "Permet de définir le score pour une redstone", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("mp_set_playzone_radius", "1000", "Permet de définir le rayon de la zone jouable en nombre de bloc", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("mp_enable_friendly_fire", "0", "Permet d'activer ou non les dégats entre alliés", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("mp_enable_old_pvp", "1", "Permet d'activer ou non l'ancien système de pvp", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("mp_enable_block_adding", "0", "Permet d'activer ou non la pose de bloc autour de l'arène", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("game_time", "60", "Permet de définir le temps d'une partie", "settings", true, true));
            parametresParDefaut.add(new GameCVAR("pre_game_timer", "10", "Permet de définir le temps d'attente avant de démarrer la partie", "settings", true, true));
            parametresParDefaut.add(new GameCVAR("end_game_timer", "60", "Permet de définir le temps avant de quitter le monde à la fin d'une partie", "settings", true, true));

            parametresParDefaut.add(new GameCVAR("chest_opening_cooldown", "5", "Permet de définir le temps d'attente avant d'ouvrir un coffre d'arène", "arena", true, true));
            parametresParDefaut.add(new GameCVAR("max_time_between_chests", "15", "Permet de définir le temps maximum avant l'apparition d'un coffre d'arène", "arena", true, true));
            parametresParDefaut.add(new GameCVAR("min_time_between_chests", "10", "Permet de définir le temps minimum avant l'apparition d'un coffre d'arène", "arena", true, true));
            parametresParDefaut.add(new GameCVAR("max_teleport_time", "15", "Permet de définir le temps maximum afin de pouvoir se téléporter après l'apparition d'un coffre", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("max_item_in_chest", "20", "Permet de définir le nombre maximum d'objet dans un coffre d'équipe", "arena", true, true));
            parametresParDefaut.add(new GameCVAR("min_item_in_chest", "10", "Permet de définir le nombre minimum d'objet dans un coffre d'équipe", "arena", true, true));
            parametresParDefaut.add(new GameCVAR("death_time", "10", "Permet de définir le temps de réapparition", "settings", true, true));
            parametresParDefaut.add(new GameCVAR("chicken_spawn_time", "60", "Permet de définir le temps restant necessaire avant de faire apparaitre les poulets dans l'arène", "arena", true, true));
            parametresParDefaut.add(new GameCVAR("chicken_spawn_interval", "15", "Permet de définir le temps en seconde necessaire avant de pouvoir faire apparaitre une vague de poulet", "arena", true, true));
            parametresParDefaut.add(new GameCVAR("chicken_spawn_min_count", "2", "Permet de définir le nombre minimum de poulet dans une vague d'apparition", "arena", true, true));
            parametresParDefaut.add(new GameCVAR("chicken_spawn_max_count", "5", "Permet de définir le nombre minimum de poulet dans une vague d'apparition", "arena", true, true));
            parametresParDefaut.add(new GameCVAR("chicken_spawn_min_item_count", "1", "Permet de définir le nombre minimum de d'item qu'un poulet va drop dans une vague d'apparition", "arena", true, true));
            parametresParDefaut.add(new GameCVAR("chicken_spawn_max_item_count", "3", "Permet de définir le nombre maximum de d'item qu'un poulet va drop dans une vague d'apparition", "arena", true, true));
            parametresParDefaut.add(new GameCVAR("protected_zone_area_radius", "55", "Permet de définir le rayon en bloc de la zone protégé, où les blocs ne peuvent pas être cassé", "settings", false, true));
            parametresParDefaut.add(new GameCVAR("enable_monster_in_protected_zone", "0", "Permet d'activer ou non l'apparition de monstre dans la zone protégée", "settings", true, true));
            parametresParDefaut.add(new GameCVAR("arena_safezone_radius", "5", "Permet de modifier le rayon de safezone de la zone de téléportation de l'arène", "arena", true, true));
            parametresParDefaut.add(new GameCVAR("arena_warn_chest_time", "10", "Permet de définir le temps restant en seconde avant de mettre un message dans le chat annonçant l'arrivée du coffre d'arène", "arena", true, true));


            // paramètres par défaut du système de largage
            parametresParDefaut.add(new GameCVAR("max_time_between_drop", "25", "Permet de définir le temps maximum entre chaque largage", "airdrop", true, true));
            parametresParDefaut.add(new GameCVAR("min_time_between_drop", "20", "Permet de définir le temps minimum entre chaque largage", "airdrop", true, true));
            parametresParDefaut.add(new GameCVAR("max_distance_from_arena", "300", "Permet de définir la distance maximale entre la génération de position du largage et le centre de l'arène", "airdrop", true, true));
            parametresParDefaut.add(new GameCVAR("min_distance_from_arena", "150", "Permet de définir la distance minimale entre la génération de position du largage et le centre de l'arène", "airdrop", true, true));
            parametresParDefaut.add(new GameCVAR("normal_falling_speed", "40", "Permet de définir la vitesse de chute lorsque le parachute est présent (en nombre de ticks, 20 ticks environ égale à 1 sec)", "airdrop", true, true));
            parametresParDefaut.add(new GameCVAR("free_falling_speed", "2", "Permet de définir la vitesse de chute lorsque le parachute est cassé (en nombre de ticks, 20 ticks environ égale à 1 sec)", "airdrop", true, true));
            parametresParDefaut.add(new GameCVAR("max_item_in_drop", "40", "Permet de définir le nombre d'item minimum présent dans le coffre", "airdrop", true, true));
            parametresParDefaut.add(new GameCVAR("min_item_in_drop", "30", "Permet de définir le nombre d'item minimum présent dans le coffre", "airdrop", true, true));
            parametresParDefaut.add(new GameCVAR("drop_opening_time", "10", "Permet de définir le nombre définir le temps d'ouverture du coffre du largage", "airdrop", true, true));
            parametresParDefaut.add(new GameCVAR("drop_display_time", "5", "Permet de définir combien de temps en seconde le message doit s'afficher", "airdrop", true, true));


            parametresParDefaut.add(new GameCVAR("enable_shop", "1", "Permet d'activer ou non le shop", "shop", true, false));
            parametresParDefaut.add(new GameCVAR("enable_kits", "1", "Permet d'activer ou non les kits", "kits", true, false));

            // Système de coffre à la mort
            parametresParDefaut.add(new GameCVAR("drop_chest_on_death", "1", "Permet d'activer ou non l'apparition de coffre à la mort d'un joueur contenant tout son inventaire", "settings", true, false));
            parametresParDefaut.add(new GameCVAR("drop_chest_on_death_time", "1", "Permet de définir le temps de vie d'un coffre, combien de temps il doit rester après sa première ouverture", "settings", true, true));


            // Evenement Halloween
            parametresParDefaut.add(new GameCVAR("enable_halloween_event", "0", "Permet d'activer ou non le mode halloween", "event", true, false));


            // Paramètre permettant de donner des points lors d'un kill d'un joueur
            parametresParDefaut.add(new GameCVAR("points_per_kill", "0", "Permet de définir le nombre de points offert lors d'un kill de joueur", "cvar", true, true));

            parametresParDefaut.add(new GameCVAR("enable_chat_from_other_worlds", "1", "Permet d'activer ou non l'isolement du chat dans une partie. Par exemple, si l'option est activé, tous les messages envoyé par des joueurs seront affiché. Sinon, seuls les messages provenant de la partie seront affiché", "settings", true, true));

            GameLogger.addLog(new Log("game_cvar", "Successfully added default cvar", "GameSettings: getParametresParDefaut"));



        }

        return parametresParDefaut;

    }

    /**
     * Retourne une valeur par défaut
     *
     * @param commande
     * @return null ou Object
     */
    public static GameCVAR getValeurParDefaut(String commande) {
        for (GameCVAR cvar : getParametresParDefaut())
            if (cvar.getCommand().equalsIgnoreCase(commande)) return cvar;
        return null;
    }


}
