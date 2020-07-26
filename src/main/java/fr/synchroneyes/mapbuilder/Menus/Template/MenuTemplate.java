package fr.synchroneyes.mapbuilder.Menus.Template;

import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

/**
 * Classe représentant un menu à ouvrir
 */
public class MenuTemplate implements Listener {

    public MenuTemplate() {
        // On enregistre
        Bukkit.getPluginManager().registerEvents(this, mineralcontest.plugin);
    }


}
