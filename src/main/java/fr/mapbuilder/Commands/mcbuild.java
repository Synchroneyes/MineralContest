package fr.mapbuilder.Commands;

import fr.groups.Commands.CommandTemplate;
import fr.mapbuilder.Blocks.BlocksDataColor;
import fr.mapbuilder.Core.Monde;
import fr.mapbuilder.Items.AreneItem;
import fr.mapbuilder.Items.ColoredHouseItem;
import fr.mapbuilder.MapBuilder;
import fr.mapbuilder.Spawner.Arene;
import fr.mineral.Core.House;
import fr.mineral.Utils.Door.DisplayBlock;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
    public static Monde monde = MapBuilder.monde;



    public mcbuild() {

        this.actionsPossible = new LinkedList<>();
        actionsPossible.add("save");
        actionsPossible.add("menu");
        actionsPossible.add("setSpawn");


        addArgument("action", true);
        addArgument("nom de la map", false);
        constructArguments();

        accessCommande.add(PLAYER_COMMAND);


    }

    @Override
    public boolean performCommand(CommandSender commandSender, String command, String[] args) {
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

        if (args[0].equalsIgnoreCase("menu")) {
            for (BlocksDataColor color : BlocksDataColor.values()) {
                ColoredHouseItem house = new ColoredHouseItem(color);
                house.giveItemToPlayer((Player) commandSender);
            }
            commandSender.sendMessage(mineralcontest.prefixPrive + "Vous avez reçu les blocs de création de maison. Vous n'avez plus qu'a les poser");
            AreneItem item = new AreneItem();
            item.giveItemToPlayer((Player) commandSender);
            commandSender.sendMessage(mineralcontest.prefixPrive + "Vous avez reçu le bloc de création d'arène Vous n'avez plus qu'a le poser");
            return false;
        }

        if (args[0].equalsIgnoreCase("setSpawn")) {
            monde.setSpawnDepart(joueur.getLocation());
            joueur.sendMessage(mineralcontest.prefixPrive + "Le spawn de départ pour ce monde a bien été enregistré !");
            return false;
        }



        return false;
    }

    @Override
    public String getCommand() {
        return "mcbuild";
    }


    private void sauvegarderMonde(String nom) {
        Monde monde = MapBuilder.monde;
        monde.setNom(nom);

        File fichierMonde = new File(mineralcontest.plugin.getDataFolder() + File.separator + "generated_maps" + File.separator + nom + ".yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fichierMonde);

        yamlConfiguration.set("map_name", nom);

        Location spawnLocation = monde.getSpawnDepart();

        if (spawnLocation == null) {
            yamlConfiguration.set("default_spawn", "null");
        } else {
            yamlConfiguration.set("default_spawn.x", spawnLocation.getBlockX());
            yamlConfiguration.set("default_spawn.y", spawnLocation.getBlockY());
            yamlConfiguration.set("default_spawn.z", spawnLocation.getBlockZ());

        }

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