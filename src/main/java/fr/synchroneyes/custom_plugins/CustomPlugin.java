package fr.synchroneyes.custom_plugins;

import fr.synchroneyes.custom_events.MCPluginLoaded;
import fr.synchroneyes.custom_events.MCWorldLoadedEvent;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe utilisée pour créer un plugin mineral contest
 */
public abstract class CustomPlugin extends JavaPlugin implements PluginInterface, Listener {



    private List<Listener> evenements;

    public CustomPlugin() {
        this.evenements = new LinkedList<>();
    }

    /**
     * Méthode permettant d'enregistrer un listener
     * @param listener
     */
    public void registerEvent(Listener listener) {
        this.evenements.add(listener);
    }


    /**
     * Méthode utilisée pour décharger le plugin
     */
    public void unloadPlugin() {

        Bukkit.getLogger().info("Unloading plugin: " + this.getPluginName());
        HandlerList.unregisterAll((JavaPlugin)this);
        Bukkit.getPluginManager().disablePlugin(this);


        ClassLoader classLoader = this.getClassLoader();

        if (classLoader instanceof URLClassLoader) {
            try {
                ((URLClassLoader) classLoader).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.gc();
    }


    /**
     * Dans cette méthode, on vérifie si le plugin doit fonctionner sur une map défini ou non,
     * si c'est le cas, on vérifie le nom de la map; si la map est dans la liste des maps autorisé, on ne fait rien
     * sinon, on décharge le plugin
     * @param worldLoadedEvent
     */
    @EventHandler
    public void onPluginLoaded(MCWorldLoadedEvent worldLoadedEvent) {
        boolean shoudDisablePlugin = true;
        if(this.shouldPluginRunOnDefinedMap()) {
            for(String map : getAllowedMaps())
                if(map.equals(worldLoadedEvent.getWorld_name())) {
                    shoudDisablePlugin = false;
                    break;
                }
        } else {
            shoudDisablePlugin = false;
        }


        if(shoudDisablePlugin) {
            this.unloadPlugin();
            return;
        }

    }


    @Override
    public void onEnable() {
        mineralcontest.plugin.registerNewPlugin(this);
        Bukkit.getPluginManager().registerEvents(this, this);

        onPluginEnabled();
    }


    /**
     * Méthode contenant les instructions a effectuer au démarrage du plugin
     */
    public abstract void onPluginEnabled();
}
