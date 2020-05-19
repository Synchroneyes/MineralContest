/**
 * TODO: - Charger les differents spawns de la map
 */
package fr.groups;

import fr.groups.Commands.Admin.AjouterAdmin;
import fr.groups.Commands.Admin.RetirerAdmin;
import fr.groups.Commands.Groupe.*;
import fr.groups.Commands.Vote.StartVote;
import fr.groups.Commands.Vote.Vote;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;

public class GroupeExtension {

    private static GroupeExtension instance;
    private CommandMap bukkitCommandMap;
    public static boolean enabled = true;

    private GroupeExtension() {
        if (!enabled) return;
        instance = this;
        Bukkit.getLogger().info("Loading GroupeExtension ...");
        try {
            getPluginCommandMap();
        } catch (Exception e) {
            e.printStackTrace();
        }

        registerCommands();
    }

    public static GroupeExtension getInstance() {
        if (instance == null) return new GroupeExtension();
        return instance;
    }

    private void registerCommands() {
        if (!enabled) return;
        this.bukkitCommandMap.register("", new CreerGroupe());
        this.bukkitCommandMap.register("", new StartVote());
        this.bukkitCommandMap.register("", new InviterGroupe());
        this.bukkitCommandMap.register("", new JoinGroupe());
        this.bukkitCommandMap.register("", new KickPlayerFromGroup());
        this.bukkitCommandMap.register("", new QuitterGroupe());
        this.bukkitCommandMap.register("", new AjouterAdmin());
        this.bukkitCommandMap.register("", new RetirerAdmin());

        this.bukkitCommandMap.register("", new Vote());


    }

    private void getPluginCommandMap() throws NoSuchFieldException, IllegalAccessException {
        Field cmdMapField = SimplePluginManager.class.getDeclaredField("commandMap");
        cmdMapField.setAccessible(true);
        this.bukkitCommandMap = (CommandMap) cmdMapField.get(Bukkit.getPluginManager());
    }
}
