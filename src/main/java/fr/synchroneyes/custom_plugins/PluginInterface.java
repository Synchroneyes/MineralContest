package fr.synchroneyes.custom_plugins;

/**
 * Interface modélisant les plugins du mode de jeu mineral contest
 */
public interface PluginInterface {

    /**
     * Méthode permettant de définir le nom du plugin
     * @return
     */
    String getPluginName();

    /**
     * Méthode permettant de définir la description du plugin
     * @return
     */
    String getPluginDescription();
}
