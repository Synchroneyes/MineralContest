package fr.groups;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;

public class GroupeExtension {

    private static GroupeExtension instance;
    private CommandMap bukkitCommandMap;

    private GroupeExtension() {
        this.instance = this;
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

        this.bukkitCommandMap.register("", new fr.groups.Commands.CreerGroupe());
        this.bukkitCommandMap.register("", new fr.groups.Commands.StartVote());
        this.bukkitCommandMap.register("", new fr.groups.Commands.InviterGroupe());
        this.bukkitCommandMap.register("", new fr.groups.Commands.JoinGroupe());
        this.bukkitCommandMap.register("", new fr.groups.Commands.KickPlayerFromGroup());
        this.bukkitCommandMap.register("", new fr.groups.Commands.QuitterGroupe());
        this.bukkitCommandMap.register("", new fr.groups.Commands.AjouterAdmin());
        this.bukkitCommandMap.register("", new fr.groups.Commands.RetirerAdmin());
    }

    private void getPluginCommandMap() throws NoSuchFieldException, IllegalAccessException {
        Field cmdMapField = SimplePluginManager.class.getDeclaredField("commandMap");
        cmdMapField.setAccessible(true);
        this.bukkitCommandMap = (CommandMap) cmdMapField.get(Bukkit.getPluginManager());
    }
}
