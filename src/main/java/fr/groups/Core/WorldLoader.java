package fr.groups.Core;

import fr.groups.Utils.FileManager.FileCopy;
import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.mineralcontest;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;

public class WorldLoader {

    private static String folder_name = mineralcontest.plugin.getDataFolder() + File.separator + "worlds" + File.separator;

    /**
     * Fonction appelé à l'exterieur permettant de demander à charger le monde
     *
     * @param nomMap      - Nom de la map a charger
     * @param identifiant - Identifiant unique du groupe qui demande le chargement
     * @return monde - Le monde chargé
     */
    public World chargerMonde(String nomMap, String identifiant) {
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
    private World doChargerMonde(String nomMap, String identifiant) {
        String server_executable_path = System.getProperty("user.dir") + File.separator;
        File dossierMondeACopier = new File(folder_name + nomMap);

        File repertoireServer = new File(server_executable_path + nomMap + "_" + identifiant);
        Bukkit.getLogger().severe(repertoireServer.getName());

        try {
            FileCopy.copyDirectoryContent(dossierMondeACopier, repertoireServer);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Error.Report(ioe);
        }

        WorldCreator wc = new WorldCreator(nomMap + "_" + identifiant);
        return Bukkit.getServer().createWorld(wc);

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
}
