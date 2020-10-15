package fr.synchroneyes.data_storage;

import fr.synchroneyes.file_manager.FileList;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.util.List;

/**
 * Classe permettant de créer les tables sql dans la base de donnée
 */
public class DatabaseInitialisation {

    public static void createDatabase() throws Exception {

        boolean sqlInstallationRequired = false;
        File SQLFile = new File(mineralcontest.plugin.getDataFolder(), FileList.MySQL_database_schema.toString());

        if(!SQLFile.exists()) throw new Exception("SQL Schema file doesnt exists: " + FileList.MySQL_database_schema.toString());


        // on vérifie si la table existe ou non
        // SHOW TABLES LIKE '%tablename%';

        for(DatabaseTablesName table : DatabaseTablesName.values()) {
            Bukkit.getLogger().severe("SHOW TABLES LIKE '" + table.toString() + "';");
            ResultSet resultSet = mineralcontest.plugin.getConnexion_database().query("SHOW TABLES LIKE '" + table.toString() + "';");

            // Si il n'y a pas de résultat, alors il manque une table
            if(!resultSet.next()) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + " Missing " + table.toString());
                sqlInstallationRequired = true;
                break;
            }
        }

        // SI on a pas besoin de faire une installation, on s'arrête
        if(!sqlInstallationRequired) return;

        StringBuilder sqlFileContent = new StringBuilder();
        List<String> lignes_fichiers = Files.readAllLines(SQLFile.toPath());
        for(String ligne : lignes_fichiers)
            if(ligne.length() > 1 ) {
                mineralcontest.plugin.getConnexion_database().query(ligne);

            }





    }
}
