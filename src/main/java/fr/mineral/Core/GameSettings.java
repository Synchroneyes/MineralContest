package fr.mineral.Core;

import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class GameSettings {

    // CVAR

    private String configFileName = "config.yml";

    private static GameSettings instance;

    private File configFile;
    private FileConfiguration pluginConfiguration;
    private mineralcontest pluginInstance;

    private GameSettings() {
        this.configFile = new File(mineralcontest.plugin.getDataFolder(), configFileName);
        this.pluginInstance = mineralcontest.plugin;
        GameSettings.instance = this;
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
            if (pluginConfiguration.get(buildConfigKeyValue(cvar)) == null)
                pluginConfiguration.set(buildConfigKeyValue(cvar), cvar.getValue());
        }
        try {
            saveConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGameSettings() {
        try {
            // for each cvar
            for(GameSettingsCvar cvar : GameSettingsCvar.values()) {
                String configCvar = buildConfigKeyValue(cvar);
                if(cvar.getExpectedValue().equals("int")) {
                    int valueToSet = Integer.parseInt((String) getConfigValue(configCvar));
                    cvar.setValue(valueToSet);
                }

                if(cvar.getExpectedValue().equals("string")) {
                    cvar.setValue(getConfigValue(configCvar));
                }
            }


            Lang.loadLang((String) GameSettingsCvar.getValueFromCVARName("mp_set_language"));

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
