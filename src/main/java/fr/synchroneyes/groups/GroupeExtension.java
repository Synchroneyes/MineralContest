package fr.synchroneyes.groups;

import fr.synchroneyes.groups.Commands.Admin.AjouterAdmin;
import fr.synchroneyes.groups.Commands.Admin.RetirerAdmin;
import fr.synchroneyes.groups.Commands.Groupe.*;
import fr.synchroneyes.groups.Commands.Vote.StartVote;
import fr.synchroneyes.groups.Commands.Vote.Vote;
import fr.synchroneyes.mineral.Utils.ErrorReporting.Error;
import fr.synchroneyes.mineral.Utils.Log.GameLogger;
import fr.synchroneyes.mineral.Utils.Log.Log;
import fr.synchroneyes.mineral.Utils.Player.PlayerUtils;
import fr.synchroneyes.mineral.mineralcontest;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;

import java.io.File;
import java.io.IOException;
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
            Error.Report(e, null);
        }

        registerCommands();
        supprimerMapsExistantes();

        Bukkit.getLogger().info("GroupeExtension loaded");

    }

    /**
     * Supprime toute les maps crées auparavant
     */
    private void supprimerMapsExistantes() {
        File dossierServer = new File(System.getProperty("user.dir"));
        File[] fichiers = dossierServer.listFiles((dir, name) -> name.toLowerCase().startsWith("mc_"));

        for (File fichier : fichiers) {
            try {
                Bukkit.getServer().unloadWorld(fichier.getName(), false);
                FileUtils.deleteDirectory(fichier);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                Error.Report(ioe, null);
            }
        }


        if (mineralcontest.plugin.pluginWorld == null) {
            mineralcontest.plugin.pluginWorld = PlayerUtils.getPluginWorld();
        }

        for (World world : Bukkit.getWorlds()) {
            if (world.getName().contains("mc_")) {
                world.setAutoSave(false);
                for (Player p : world.getPlayers()) {
                    if (mineralcontest.plugin.defaultSpawn == null) {
                        p.setHealth(0);
                    } else {
                        p.sendMessage("Teleporting you to plugin hub");
                        p.teleport(mineralcontest.plugin.defaultSpawn);
                        PlayerUtils.clearPlayer(p, true);
                    }
                }

                Bukkit.unloadWorld(world, false);
                Bukkit.getLogger().info("Successfully unloaded world " + world.getName());
                GameLogger.addLog(new Log("world_unload", "Successfully unloaded world " + world.getName(), "GroupeExtension: supprimerMapsExistantes"));

            }
        }


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
