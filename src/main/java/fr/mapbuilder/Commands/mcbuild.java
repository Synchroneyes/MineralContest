package fr.mapbuilder.Commands;

import fr.groups.Commands.CommandTemplate;
import fr.mapbuilder.Core.Monde;
import fr.mapbuilder.MapBuilder;
import fr.mineral.Core.House;
import fr.mineral.Utils.Door.DisplayBlock;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class mcbuild extends CommandTemplate {

    private LinkedList<String> actionsPossible;


    public mcbuild() {

        this.actionsPossible = new LinkedList<>();
        actionsPossible.add("save");
        actionsPossible.add("build");


        addArgument("action", true);
        addArgument("nom de la map", false);
        constructArguments();

        accessCommande.add(PLAYER_COMMAND);


    }

    @Override
    public String getCommand() {
        return "mcbuild";
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {

        try {
            canPlayerUseCommand(commandSender, args);
        } catch (Exception e) {
            commandSender.sendMessage(mineralcontest.prefixErreur + e.getMessage());
            return false;
        }

        Player joueur = (Player) commandSender;
        if (args[0].equalsIgnoreCase("save")) {
            if (args.length == 2) {
                String nomMap = args[1];
                joueur.sendMessage("Sauvegarde de la map: " + nomMap);
                sauvegarderMonde(nomMap);
                return false;
            } else {
                joueur.sendMessage(mineralcontest.prefixErreur + "Usage: /" + getCommand() + "save <nom de la map>");
                return false;
            }
        }

        return false;
    }

    private void sauvegarderMonde(String nom) {
        Monde monde = MapBuilder.monde;
        monde.setNom(nom);

        File fichierMonde = new File(mineralcontest.plugin.getDataFolder() + File.separator + "generated_maps" + File.separator + nom + ".yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fichierMonde);

        yamlConfiguration.set("map_name", nom);
        try {
            yamlConfiguration.set("arena.chest.x", monde.getArene().getCoffre().getPosition().getX());
            yamlConfiguration.set("arena.chest.y", monde.getArene().getCoffre().getPosition().getY());
            yamlConfiguration.set("arena.chest.z", monde.getArene().getCoffre().getPosition().getZ());
        } catch (Exception e) {
            Bukkit.getLogger().severe(e.getMessage());
            e.printStackTrace();
            Bukkit.broadcastMessage("Une erreur est survenue lors de la sauvegarde de la map, veuillez regarder la console!");
            return;
        }

        yamlConfiguration.set("arena.teleport.x", monde.getArene().getTeleportSpawn().getX());
        yamlConfiguration.set("arena.teleport.y", monde.getArene().getTeleportSpawn().getY());
        yamlConfiguration.set("arena.teleport.z", monde.getArene().getTeleportSpawn().getZ());

        try {
            for (House house : monde.getHouses()) {
                yamlConfiguration.set("house." + house.getTeam().getNomEquipe() + ".color", house.getTeam().getCouleur().toString());
                yamlConfiguration.set("house." + house.getTeam().getNomEquipe() + ".spawn.x", house.getHouseLocation().getX());
                yamlConfiguration.set("house." + house.getTeam().getNomEquipe() + ".spawn.y", house.getHouseLocation().getY());
                yamlConfiguration.set("house." + house.getTeam().getNomEquipe() + ".spawn.z", house.getHouseLocation().getZ());
                yamlConfiguration.set("house." + house.getTeam().getNomEquipe() + ".coffre.x", house.getCoffreEquipeLocation().getX());
                yamlConfiguration.set("house." + house.getTeam().getNomEquipe() + ".coffre.y", house.getCoffreEquipeLocation().getY());
                yamlConfiguration.set("house." + house.getTeam().getNomEquipe() + ".coffre.z", house.getCoffreEquipeLocation().getZ());

                int index = 0;
                for (DisplayBlock blockPorte : house.getPorte().getPorte()) {
                    yamlConfiguration.set("house." + house.getTeam().getNomEquipe() + ".porte." + index + ".x", blockPorte.getBlock().getLocation().getX());
                    yamlConfiguration.set("house." + house.getTeam().getNomEquipe() + ".porte." + index + ".y", blockPorte.getBlock().getLocation().getY());
                    yamlConfiguration.set("house." + house.getTeam().getNomEquipe() + ".porte." + index + ".z", blockPorte.getBlock().getLocation().getZ());
                    index++;
                }
            }
        } catch (Exception e) {
            Bukkit.getLogger().severe(e.getMessage());
            e.printStackTrace();
            Bukkit.broadcastMessage("Une erreur est survenue lors de la sauvegarde de la map, veuillez regarder la console!");
            return;
        }

        try {
            yamlConfiguration.save(fichierMonde);
            Bukkit.broadcastMessage("Le fichier a bien été enregistré ! Il se trouve dans " + fichierMonde.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public String getDescription() {
        return "Gestion du monde";
    }

    @Override
    public String getPermissionRequise() {
        return null;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (alias.equalsIgnoreCase("mcbuild")) {
            if (args.length == 0 || args.length == 1) return actionsPossible;
        }
        return new LinkedList<>();
    }
}