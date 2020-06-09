package fr.mineral.Settings;

import fr.mineral.Utils.ErrorReporting.Configuration;
import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.Utils.Log.GameLogger;
import fr.mineral.Utils.Log.Log;
import fr.mineral.mineralcontest;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class GameSettings {

    // La liste des paramètres
    private LinkedList<GameCVAR> parametres;

    private static LinkedList<GameCVAR> parametresParDefaut;

    // Les paramètres du fichier de configuration à ne pas tenir compte
    private static LinkedList<GameCVAR> parametresExclu;

    public GameSettings(boolean loadDefaultSettings) {
        if (parametresExclu == null) {
            parametresExclu = new LinkedList<>();
            parametresExclu.add(new GameCVAR("chest_content", "", "", "arena", false, false));
        }

        if (loadDefaultSettings) parametres = getParametresParDefaut();
        else parametres = new LinkedList<>();
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
    public Object getCVARValeur(String cvar) throws Exception {
        for (GameCVAR parametre : parametres) {
            // Si la commande passée en argument est égale au paramtre actuel
            if (parametre.getCommand().equalsIgnoreCase(cvar)) {
                if (parametre.isNumber()) return parametre.getValeurNumerique();
            }

            return parametre.getValeur();
        }

        throw new Exception("Paramètre inconnu: " + cvar);
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
                break;
            }
        }

        if (gameCVAR == null)
            throw new Exception("Impossible d'appliquer la valeur " + valeur + " à " + cvar + " car ce paramètre est inconnu");
        gameCVAR.setValeur(valeur);
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
     */
    public void saveToFile(String nomDeFichier) {

        GameLogger.addLog(new Log("game_cvar", "About to save current configuration into a file named " + nomDeFichier + ".yml", "GameSettings: saveToFile"));

        File dossierConfiguration = new File(mineralcontest.plugin.getDataFolder() + File.separator + "saved-configs");
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
            parametresParDefaut.add(new GameCVAR("mp_enable_metrics", "1", "Permet d'activer ou non l'envoie de stats", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("mp_randomize_team", "0", "Permet d'activer ou non les équipes aléatoires", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("mp_enable_item_drop", "2", "Permet d'activer ou non le drop d'item à la mort. 0 pour aucun, 1 pour les minerais uniquement, 2 pour tout", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("SCORE_IRON", "10", "Permet de définir le score pour un lingot de fer", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("SCORE_GOLD", "50", "Permet de définir le score pour un lingot d'or", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("SCORE_DIAMOND", "150", "Permet de définir le score pour un diamant", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("SCORE_EMERALD", "300", "Permet de définir le score pour un émeraude", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("mp_team_max_player", "2", "Permet de définir le nombre maximum de joueur par équipe", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("mp_set_language", "french", "Permet de définir le langage par défaut", "cvar", true, false));
            parametresParDefaut.add(new GameCVAR("world_name", "1", "Permet de définir le monde dans le quel le plugin doit être activé", "settings", true, false));
            parametresParDefaut.add(new GameCVAR("mp_set_playzone_radius", "1000", "Permet de définir le rayon de la zone jouable en nombre de bloc", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("mp_enable_friendly_fire", "1", "Permet d'activer ou non les dégats entre alliés", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("mp_enable_old_pvp", "1", "Permet d'activer ou non l'ancien système de pvp", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("mp_enable_block_adding", "1", "Permet d'activer ou non la pose de bloc autour de l'arène", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("game_time", "60", "Permet de définir le temps d'une partie", "settings", true, true));
            parametresParDefaut.add(new GameCVAR("pre_game_timer", "10", "Permet de définir le temps d'attente avant de démarrer la partie", "settings", true, true));
            parametresParDefaut.add(new GameCVAR("chest_opening_cooldown", "5", "Permet de définir le temps d'attente avant d'ouvrir un coffre d'arène", "arena", true, true));
            parametresParDefaut.add(new GameCVAR("max_time_between_chests", "15", "Permet de définir le temps maximum avant l'apparition d'un coffre d'arène", "arena", true, true));
            parametresParDefaut.add(new GameCVAR("min_time_between_chests", "10", "Permet de définir le temps minimum avant l'apparition d'un coffre d'arène", "arena", true, true));
            parametresParDefaut.add(new GameCVAR("max_teleport_time", "15", "Permet de définir le temps maximum afin de pouvoir se téléporter après l'apparition d'un coffre", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("max_item_in_chest", "20", "Permet de définir le nombre maximum d'objet dans un coffre d'équipe", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("min_item_in_chest", "10", "Permet de définir le nombre minimum d'objet dans un coffre d'équipe", "cvar", true, true));
            parametresParDefaut.add(new GameCVAR("death_time", "10", "Permet de définir le temps de réapparition", "cvar", true, true));
        }

        return parametresParDefaut;

    }


}
