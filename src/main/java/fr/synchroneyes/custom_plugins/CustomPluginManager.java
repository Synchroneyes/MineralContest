package fr.synchroneyes.custom_plugins;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.HashMap;

/**
 * Classe permettant de gérer les plugins du mode jeu
 */
public class CustomPluginManager {

    // Tableau permettant de définir si un plugin est actif ou non
    private HashMap<CustomPlugin, Boolean> enabled_plugins;

    public CustomPluginManager() {
        this.enabled_plugins = new HashMap<>();
    }


    /**
     * Méthode permettant d'enregistrer un plugin
     * @param plugin
     */
    public void registerPlugin(CustomPlugin plugin) {
        this.enabled_plugins.put(plugin, true);
    }


}
