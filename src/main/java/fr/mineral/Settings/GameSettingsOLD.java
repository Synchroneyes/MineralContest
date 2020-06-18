package fr.mineral.Settings;

import fr.mineral.mineralcontest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;

public interface GameSettingsOLD {

    // CVAR

    public static int PLUGIN_START = 1;
    public static int CONFIG_RELOAD = 2;

    String configFileName = "config.yml";

    GameSettingsOLD instance = null;

    File configFile = null;
    FileConfiguration pluginConfiguration = null;
    mineralcontest pluginInstance = null;
    boolean isConfigLoaded = false;


    ArrayList<GameSettingsCvarOLD> cvars = null;


    public YamlConfiguration getYamlConfiguration();

    public GameSettingsOLD getInstance();

    public void createGameSettings();

    public void addMissingCVARsToConfigFile();

    public void loadGameSettings(int value);

    void setCvarValue(GameSettingsCvarOLD cvar, String configCvar);

    String buildConfigKeyValue(GameSettingsCvarOLD gameSettingsCvar);

    Object getConfigValue(String key);


    void resetConfig();

    void updatePluginConfig(String key, Object value);

    public void saveConfig();
}
