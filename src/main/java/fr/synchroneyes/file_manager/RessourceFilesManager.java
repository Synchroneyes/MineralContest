package fr.synchroneyes.file_manager;

import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;

import java.io.File;

/**
 * Cette classe permet de copier les fichiers présent dans le fichier .jar du plugin vers le dossier du serveur
 */
public class RessourceFilesManager {

    /**
     * Cette méthode permet de copier les fichiers par défaults si ils n'existent pas
     */
    public static void createDefaultFiles() {

        for (FileList fichier : FileList.values()) {
            // On converti le chemin du fichier en un dossier
            File _dossier = pathToFile(fichier.getPath());

            // Si le dossier n'existe pas, on le crée
            if (!_dossier.exists()) _dossier.mkdir();

            // On copie le fichier dans le dossier
            File _fichier = new File(_dossier, fichier.getFileName());

            // Si le fichier n'existe pas, on le copie depuis les fichiers du plugin
            if (!_fichier.exists()) {
                // On copie le fichier depuis le .jar!
                mineralcontest.plugin.saveResource(fichier.toString(), true);

                Bukkit.getLogger().info(mineralcontest.prefix + " Created " + fichier.toString());
            }

        }
    }

    private static File pathToFile(String path) {
        File defaultPluginFolder = mineralcontest.plugin.getDataFolder();

        // Si le dossier par défaut n'existe pas, on le crée
        if (!defaultPluginFolder.exists()) defaultPluginFolder.mkdir();

        // On récupère un tableau de dossiers
        String[] dossiers = path.split("/");

        // Niveau du dossier, utile pour le premier tour de boucle
        int folderLevel = 0;


        File _tmpFolder = null;

        // On va créer les sous dossiers
        for (String sous_dossier : dossiers) {
            // Dans le cas où le nom du sous dossier est vide, on passe au prochain tour de boucle
            if (sous_dossier.length() == 0) continue;

            // Si on est au premier tour de boucle, alors le parent du sous dossier est le dossier du plugin, sinon, c'est le précédent dossier créer
            if (folderLevel == 0) _tmpFolder = new File(defaultPluginFolder, sous_dossier);
            else _tmpFolder = new File(_tmpFolder, sous_dossier);

            // Si le dossier n'existe pas, on le crée
            if (!_tmpFolder.exists()) _tmpFolder.mkdir();

            folderLevel++;
        }

        return _tmpFolder;
    }

}
