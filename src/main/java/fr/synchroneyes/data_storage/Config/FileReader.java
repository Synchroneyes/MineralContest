package fr.synchroneyes.data_storage.Config;

import fr.synchroneyes.data_storage.SQLCredentials;
import fr.synchroneyes.file_manager.FileList;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Objects;


/**
 * Classe permettant de lire un fichier de configuration SQL
 */
public class FileReader {

    private YamlConfiguration yamlConfiguration;
    private File fichierConfiguration;

    public FileReader() throws Exception {

        this.fichierConfiguration = new File(mineralcontest.plugin.getDataFolder(), FileList.MySQL_Config_File.toString());

        if(!fichierConfiguration.exists()) {
            throw new Exception("Unable to load " + FileList.MySQL_Config_File.toString());
        }

        this.yamlConfiguration = YamlConfiguration.loadConfiguration(this.fichierConfiguration);

    }

    /**
     * Méthode permettant de construire la connexion au serveur à partir du fichier
     * @return
     */
    public SQLCredentials getCredentials() {
        SQLCredentials infos_connexion = new SQLCredentials();

        /*
        host:
        port:
        username:
        password:
        database:
        */

        infos_connexion.setHostname(Objects.requireNonNull(this.yamlConfiguration.get("host")).toString());
        infos_connexion.setPort(Objects.requireNonNull(this.yamlConfiguration.get("port")).toString());
        infos_connexion.setUsername(Objects.requireNonNull(this.yamlConfiguration.get("username")).toString());
        infos_connexion.setPassword(Objects.requireNonNull(this.yamlConfiguration.get("password")).toString());
        infos_connexion.setDatabase(Objects.requireNonNull(this.yamlConfiguration.get("database")).toString());

        return infos_connexion;
    }
}
