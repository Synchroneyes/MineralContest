package fr.synchroneyes.groups.Core;

import fr.synchroneyes.file_manager.FileList;
import fr.synchroneyes.groups.Utils.Etats;
import fr.synchroneyes.groups.Utils.FileManager.FileCopy;
import fr.synchroneyes.mineral.Core.Coffre.Coffres.CoffreArene;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.House;
import fr.synchroneyes.mineral.Settings.GameSettings;
import fr.synchroneyes.mineral.Shop.NPCs.BonusSeller;
import fr.synchroneyes.mineral.Shop.ShopManager;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.ErrorReporting.Error;
import fr.synchroneyes.mineral.mineralcontest;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WorldLoader {

    private Groupe groupe;
    private String nomMonde;
    private Location spawnLocation;

    // default spawn location if none set
    protected static int defaultX = 999999, defaultY = 150, defaultZ = 999999;

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
            if (!level_dat_new.exists()) {
                level_dat_new.createNewFile();
            }

            uidDat.delete();


            WorldCreator wc = new WorldCreator(nomMap + "_" + identifiant);


            World createdWorld = Bukkit.getServer().createWorld(wc);

            lireConfigurationPartie();
            lireFichierMonde(nomMondeDossier, createdWorld);
            lireFichierConfigurationContenuCoffreArene(nomMondeDossier, createdWorld);


            if (this.spawnLocation != null) {
                this.spawnLocation.setWorld(createdWorld);
            } else {
                this.spawnLocation = new Location(createdWorld, defaultX, defaultY, defaultZ);
            }


            createdWorld.setSpawnLocation(this.spawnLocation);
            createdWorld.setAutoSave(false);

            return createdWorld;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Error.Report(ioe, null);
        }


        return null;

    }

    public void supprimerMonde(World world) {
        String nomMonde = world.getName();
        try {
            FileUtils.deleteDirectory(new File(System.getProperty("user.dir") + File.separator + nomMonde));

        } catch (IOException ioe) {
            ioe.printStackTrace();
            Error.Report(ioe, null);
        }

    }

    private void lireFichierMonde(String nomDossier, World monde) throws Exception {
        String nomFichierConfig = "mc_world_settings.yml";

        boolean loadNPC = true;

        // Variable utilisée pour vérifier les coffres présent dans un rayon de X bloc autour du spawn
        int rayonDeBloc = 20;

        // Liste contenant tous les blocks étant des coffres, utilisé pour autoriser leur ouverture
        List<Block> chestToAdd = new ArrayList<>();


        File fichierConfigMonde = new File(nomDossier + File.separator + nomFichierConfig);

        if (!fichierConfigMonde.exists()) {
            throw new Exception(nomFichierConfig + " doesnt exists in world folder. Can't load world settings");
        }

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fichierConfigMonde);


        ConfigurationSection arene, houses, npcs, settings;
        arene = yamlConfiguration.getConfigurationSection("arena");
        houses = yamlConfiguration.getConfigurationSection("house");
        npcs = yamlConfiguration.getConfigurationSection("npcs");
        settings = yamlConfiguration.getConfigurationSection("settings");



        if (arene == null)
            throw new Exception("Unable to load \"arena\" section from " + nomFichierConfig + ". World file settings is not correct.");
        if (houses == null)
            throw new Exception("Unable to load \"house\" section from " + nomFichierConfig + ". World file settings is not correct.");

        if (npcs == null)
            loadNPC = false;

        if (settings == null)
            throw new Exception("Unable to load \"npcs\" section from " + nomFichierConfig + ". World file settings is not correct.");


        if (yamlConfiguration.getConfigurationSection("default_spawn") == null) {
            spawnLocation = null;
        } else {
            ConfigurationSection spawn_loc = yamlConfiguration.getConfigurationSection("default_spawn");
            Location loc = null;
            if (spawn_loc.get("x") != null) {
                this.spawnLocation = new Location(null, Double.parseDouble(spawn_loc.get("x").toString()), Double.parseDouble(spawn_loc.get("y").toString()), Double.parseDouble(spawn_loc.get("z").toString()));
            }
        }

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

            House nouvelleEquipe = new House(nomEquipe, couleur, this.groupe);

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

            // On vérifie partout autour du spawn si il y a des coffres dans un rayon de x blocs
            chestToAdd.addAll(getNearbyBlocksByMaterial(Material.CHEST, spawnLoc, rayonDeBloc));

            nouvelleEquipe.setCoffreEquipe(chestLoc);
            nouvelleEquipe.setHouseLocation(spawnLoc);
            if (mineralcontest.debug)
                groupe.sendToadmin(mineralcontest.prefixPrive + "L'équipe " + couleur + nomEquipe + ChatColor.WHITE + " a bien été crée");
            groupe.getGame().addEquipe(nouvelleEquipe);
        }

        // Pour chaque bloc récupéré
        for (Block block : chestToAdd)
            // Si le bloc n'est pas déjà sauvegardé
            if (!partie.isThisChestAlreadySaved(block))
                // On autorise son ouverture
                partie.addAChest(block);

        if (!chestToAdd.isEmpty())
            Bukkit.getLogger().info(mineralcontest.prefix + " Allowed " + chestToAdd.size() + " chests to be opened");


        ShopManager shopManager = groupe.getGame().getShopManager();
        // On charge les NPCS

        if (loadNPC) {
            for (String idNpc : npcs.getKeys(false)) {
                ConfigurationSection npc = npcs.getConfigurationSection(idNpc);
                Location npcLocation = new Location(monde, 0, 0, 0);

                npcLocation.setX(Float.parseFloat(npc.get("x").toString()));
                npcLocation.setY(Float.parseFloat(npc.get("y").toString()));
                npcLocation.setZ(Float.parseFloat(npc.get("z").toString()));

                npcLocation.setYaw(Float.parseFloat(npc.get("yaw").toString()));
                npcLocation.setPitch(Float.parseFloat(npc.get("pitch").toString()));

                BonusSeller vendeur = ShopManager.creerVendeur(npcLocation);

                shopManager.ajouterVendeur(vendeur);
                vendeur.spawn();

            }
        }


        // Chargement des paramètres de la carte
        groupe.getParametresPartie().setCVARValeur("mp_set_playzone_radius", settings.get("mp_set_playzone_radius").toString());
        groupe.getParametresPartie().setCVARValeur("protected_zone_area_radius", settings.get("protected_zone_area_radius").toString());


        groupe.getGame().isGameInitialized = true;
        groupe.setEtat(Etats.ATTENTE_DEBUT_PARTIE);

    }

    private void lireConfigurationPartie() {
        GameSettings parametres = groupe.getParametresPartie();
        String nomFichierConfig = "mc_game_settings.yml";
        //File fichierConfigPartie = new File(nomDossier + File.separator + nomFichierConfig);
        File fichierConfigPartie = new File(mineralcontest.plugin.getDataFolder(), FileList.Config_default_game.toString());

        // Si le fichier de la map n'existe pas, on charge le fichier par défaut
        if (!fichierConfigPartie.exists()) {
            groupe.sendToadmin(mineralcontest.prefixAdmin + Lang.error_cant_load_game_settings_file.toString());
            fichierConfigPartie = new File(mineralcontest.plugin.getDataFolder(), FileList.Config_default_game.toString());
        }

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fichierConfigPartie);
        ConfigurationSection config = yamlConfiguration.getConfigurationSection("config");
        if (config == null) {
            groupe.sendToadmin(mineralcontest.prefixAdmin + Lang.error_cant_load_game_settings_file.toString());
            return;
        }

        for (String section : config.getKeys(false)) {
            for (String variable : config.getConfigurationSection(section).getKeys(false)) {
                try {
                    parametres.setCVARValeur(variable, (String) config.get(section + "." + variable));
                } catch (Exception e) {
                    //groupe.sendToadmin(mineralcontest.prefixErreur + "Setting " + variable + " doesnt exists");
                }
            }
        }

        groupe.sendToadmin(mineralcontest.prefixGroupe + "Les paramètres de la carte ont bien été chargé!");


    }

    private void lireFichierConfigurationContenuCoffreArene(String nomDossier, World monde) {
        // mc_arena_chest_content
        GameSettings parametres = groupe.getParametresPartie();
        String nomFichierConfig = "mc_arena_chest_content.yml";
        File fichierConfigPartie = new File(nomDossier + File.separator + nomFichierConfig);

        // Si le fichier de config n'existe pas, on charge celui par défaut
        if (!fichierConfigPartie.exists())
            fichierConfigPartie = new File(mineralcontest.plugin.getDataFolder(), FileList.Config_default_arena_chest.toString());

        try {
            CoffreArene coffreArene = (CoffreArene) groupe.getGame().getArene().getCoffre();
            coffreArene.getArenaChestContentGenerator().initialize(fichierConfigPartie);
        } catch (Exception e) {
            Error.Report(e, groupe.getGame());
        }


    }

    private static ChatColor toChatColor(String v) {
        for (ChatColor couleur : ChatColor.values())
            Bukkit.getLogger().info(couleur.getChar() + " == v: " + v);
        return null;
    }

    private static List<Block> getNearbyBlocksByMaterial(Material itemMaterial, Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    Block block = location.getWorld().getBlockAt(x, y, z);
                    if (block.getType().equals(itemMaterial))
                        blocks.add(block);
                }
            }
        }
        return blocks;
    }
}
