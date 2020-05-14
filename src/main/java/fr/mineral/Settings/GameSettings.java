package fr.mineral.Settings;

import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class GameSettings {

    // CVAR

    public static int PLUGIN_START = 1;
    public static int CONFIG_RELOAD = 2;

    private String configFileName = "config.yml";

    private static GameSettings instance;

    private File configFile;
    private FileConfiguration pluginConfiguration;
    private mineralcontest pluginInstance;
    protected boolean isConfigLoaded = false;

    private GameSettings() {
        this.configFile = new File(mineralcontest.plugin.getDataFolder(), configFileName);

        if(!this.configFile.exists()) createGameSettings();
        this.pluginInstance = mineralcontest.plugin;
        GameSettings.instance = this;
    }

    public YamlConfiguration getYamlConfiguration() throws Exception{
        if(!configFile.exists()) throw new Exception("[GameSettings] Config file doesnt exists");
        return YamlConfiguration.loadConfiguration(configFile);
    }

    public static GameSettings getInstance() {
        if(instance == null) return new GameSettings();
        return instance;
    }

    public void createGameSettings() {
        // On vérifie si le ficheir config existe déjà sur le serveur
        // Sinon, on applique notre fichier config
        if(!configFile.exists()) {
            mineralcontest.plugin.getLogger().info(mineralcontest.prefix + "Creating default config file");
            mineralcontest.plugin.saveResource(configFileName, false);
        }

        this.pluginConfiguration = YamlConfiguration.loadConfiguration(configFile);
        addMissingCVARsToConfigFile();

    }

    public void addMissingCVARsToConfigFile() {


        for(GameSettingsCvar cvar : GameSettingsCvar.values()) {
            if (pluginConfiguration.get(buildConfigKeyValue(cvar)) == null) {
                Bukkit.getLogger().info(cvar.getName() + " WAS MISSING, ADDING THE CVAR WITH VALUE => " + cvar.getValue());
                pluginConfiguration.set(buildConfigKeyValue(cvar), cvar.getValue());
            }
        }

        try {
            saveConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGameSettings(int value) {

        getInstance();

        if(isConfigLoaded) return;

        if(value != CONFIG_RELOAD && value != PLUGIN_START) {
            Bukkit.getLogger().severe("RELOADING");
            loadGameSettings(PLUGIN_START);
            return;
        }

        Bukkit.getLogger().info("[MINERALC] Loading game settings");
        try {
            // for each cvar
            for(GameSettingsCvar cvar : GameSettingsCvar.values()) {
                String configCvar = buildConfigKeyValue(cvar);
                Bukkit.getLogger().info("[MINERALC] " + configCvar + " => " + getConfigValue(configCvar));

                if(getConfigValue(configCvar) == null) {
                    addMissingCVARsToConfigFile();
                    continue;
                }

                if (value == CONFIG_RELOAD) {
                    if(cvar.canBeReloaded())
                        setCvarValue(cvar, configCvar);
                    else Bukkit.getLogger().severe("CVAR " + cvar.getName() + " can't be reloaded in game");
                } else {
                    setCvarValue(cvar, configCvar);
                }

            }

            isConfigLoaded = true;
            Bukkit.getLogger().info("[MINERALC] Configuration loaded: " + isConfigLoaded);




        }catch(NumberFormatException nfe) {
            mineralcontest.plugin.getLogger().severe(mineralcontest.prefixErreur + " An error happened while parsing the config file. A number was expected but got something else");
            nfe.printStackTrace();
            resetConfig();

        }catch (Exception e) {
            mineralcontest.plugin.getLogger().severe(mineralcontest.prefixErreur + " An error happened while parsing the config file");
            e.printStackTrace();
            resetConfig();

        }
    }

    private void setCvarValue(GameSettingsCvar cvar, String configCvar) {

        if(configCvar == null) {
            Bukkit.getLogger().info(cvar.getName() + "=> configCvar IS NULL");
            addMissingCVARsToConfigFile();
            return;
        }


        if(cvar.getExpectedValue().equals("int")) {
            int valueToSet = Integer.parseInt((String) getConfigValue(configCvar));
            cvar.setValue(valueToSet);
        }

        if(cvar.getExpectedValue().equals("string")) {
            cvar.setValue(getConfigValue(configCvar));
        }
    }

    public String buildConfigKeyValue(GameSettingsCvar gameSettingsCvar) {
        return "config." + gameSettingsCvar.getType() + "." + gameSettingsCvar.getName();
    }

    private Object getConfigValue(String key) {
        if(this.pluginConfiguration == null) this.pluginConfiguration = YamlConfiguration.loadConfiguration(configFile);
        return this.pluginConfiguration.getString(key);
    }


    private void resetConfig() {
        if(configFile.exists()) {
            mineralcontest.plugin.getLogger().info(mineralcontest.prefix + "Replacing default config file");
            pluginInstance.saveResource(configFileName, true);
            createGameSettings();
        }
    }

    public void updatePluginConfig(String key, Object value) {
        this.pluginConfiguration.set(key, value);
    }

    public void saveConfig() throws IOException {

        this.pluginConfiguration.save(configFile);
    }
}
