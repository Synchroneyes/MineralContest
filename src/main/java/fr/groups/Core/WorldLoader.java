package fr.groups.Core;

import fr.groups.Utils.FileManager.FileCopy;
import fr.mineral.Core.Game.Game;
import fr.mineral.Core.House;
import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.mineralcontest;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class WorldLoader {

    private Groupe groupe;
    private String nomMonde;

    public WorldLoader(Groupe g) {
        this.groupe = g;
    }

    private static String folder_name = mineralcontest.plugin.getDataFolder() + File.separator + "worlds" + File.separator;

    /**
     * Fonction appelé à l'exterieur permettant de demander à charger le monde
     *
     * @param nomMap      - Nom de la map a charger
     * @param identifiant - Identifiant unique du groupe qui demande le chargement
     * @return monde - Le monde chargé
     */
    public World chargerMonde(String nomMap, String identifiant) throws Exception {
        File dossierMaps = new File(folder_name);

        File[] maps = dossierMaps.listFiles();

        for (File map : maps)
            if (map.isDirectory())
                if (nomMap.equalsIgnoreCase(map.getName())) {
                    return doChargerMonde(nomMap, identifiant);
                }
        return null;
    }

    /**
     * Fonction permettant de charger le monde
     *
     * @param nomMap      - Nom de la map à charger
     * @param identifiant - Identifiant unique du groupe qui demande le chargement
     * @return monde - Le monde chargé
     */
    private World doChargerMonde(String nomMap, String identifiant) throws Exception {
        String server_executable_path = System.getProperty("user.dir") + File.separator;
        File dossierMondeACopier = new File(folder_name + nomMap);
        this.nomMonde = nomMap;

        String nomMondeDossier = server_executable_path + nomMap + "_" + identifiant;

        File repertoireServer = new File(nomMondeDossier);

        try {
            FileCopy.copyDirectoryContent(dossierMondeACopier, repertoireServer);
            File uidDat = new File(dossierMondeACopier, "uid.dat");
            File level_dat_new = new File(dossierMondeACopier, "level.dat_new");
            if (!level_dat_new.exists()) level_dat_new.createNewFile();

            uidDat.delete();


            WorldCreator wc = new WorldCreator(nomMap + "_" + identifiant);


            World createdWorld = Bukkit.getServer().createWorld(wc);

            lireFichierMonde(nomMondeDossier, createdWorld);

            createdWorld.setSpawnLocation(groupe.getGame().getArene().getCoffre().getPosition());

            return createdWorld;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Error.Report(ioe);
        }


        return null;

    }

    public void supprimerMonde(World world) {
        String nomMonde = world.getName();
        try {
            Bukkit.getLogger().severe("Suppression: " + System.getProperty("user.dir") + File.separator + nomMonde);
            FileUtils.deleteDirectory(new File(System.getProperty("user.dir") + File.separator + nomMonde));

        } catch (IOException ioe) {
            ioe.printStackTrace();
            Error.Report(ioe);
        }

    }

    private void lireFichierMonde(String nomDossier, World monde) throws Exception {
        String nomFichierConfig = "mc_world_settings.yml";

        File fichierConfigMonde = new File(nomDossier + File.separator + nomFichierConfig);

        if (!fichierConfigMonde.exists()) {
            throw new Exception(nomFichierConfig + " doesnt exists in world folder. Can't load world settings");
        }

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fichierConfigMonde);


        ConfigurationSection arene, houses;
        arene = yamlConfiguration.getConfigurationSection("arena");
        houses = yamlConfiguration.getConfigurationSection("house");

        if (arene == null)
            throw new Exception("Unable to load \"arena\" section from " + nomFichierConfig + ". World file settings is not correct.");
        if (houses == null)
            throw new Exception("Unable to load \"house\" section from " + nomFichierConfig + ". World file settings is not correct.");


        ConfigurationSection arena_chest = yamlConfiguration.getConfigurationSection("arena.chest");
        Location chestLocation = new Location(monde, Double.parseDouble(arena_chest.get("x").toString()),
                Double.parseDouble(arena_chest.get("y").toString()),
                Double.parseDouble(arena_chest.get("z").toString()));

        ConfigurationSection arena_teleport = yamlConfiguration.getConfigurationSection("arena.teleport");
        Location teleportLocation = new Location(monde, Double.parseDouble(arena_teleport.get("x").toString()),
                Double.parseDouble(arena_teleport.get("y").toString()),
                Double.parseDouble(arena_teleport.get("z").toString()));

        Game partie = groupe.getGame();
        partie.getArene().setCoffre(chestLocation);
        partie.getArene().setTeleportSpawn(teleportLocation);

        // chargements des équipes
        for (String nomEquipe : houses.getKeys(false)) {
            String teamColorString = houses.get(nomEquipe + ".color").toString().replace("§", "");
            ChatColor couleur = ChatColor.getByChar(teamColorString);

            House nouvelleEquipe = new House(nomEquipe, couleur);


            Location chestLoc = new Location(monde,
                    Double.parseDouble(houses.get(nomEquipe + ".coffre.x").toString()),
                    Double.parseDouble(houses.get(nomEquipe + ".coffre.y").toString()),
                    Double.parseDouble(houses.get(nomEquipe + ".coffre.z").toString())
            );

            Location spawnLoc = new Location(monde,
                    Double.parseDouble(houses.get(nomEquipe + ".spawn.x").toString()),
                    Double.parseDouble(houses.get(nomEquipe + ".spawn.y").toString()),
                    Double.parseDouble(houses.get(nomEquipe + ".spawn.z").toString())
            );

            for (String idPorte : houses.getConfigurationSection(nomEquipe + ".porte").getKeys(false)) {
                ConfigurationSection configPorte = houses.getConfigurationSection(nomEquipe + ".porte");
                Location locPorte = new Location(monde,
                        Double.parseDouble(configPorte.get(idPorte + ".x").toString()),
                        Double.parseDouble(configPorte.get(idPorte + ".y").toString()),
                        Double.parseDouble(configPorte.get(idPorte + ".z").toString())
                );

                nouvelleEquipe.getPorte().addToDoor(locPorte.getBlock());
            }


            nouvelleEquipe.setCoffreEquipe(chestLoc);
            nouvelleEquipe.setHouseLocation(spawnLoc);
            groupe.sendToadmin(mineralcontest.prefixPrive + "L'équipe " + couleur + nomEquipe + ChatColor.WHITE + " a bien été crée");
        }

    }

    private static ChatColor toChatColor(String v) {
        for (ChatColor couleur : ChatColor.values())
            Bukkit.getLogger().info(couleur.getChar() + " == v: " + v);
        return null;
    }
}
