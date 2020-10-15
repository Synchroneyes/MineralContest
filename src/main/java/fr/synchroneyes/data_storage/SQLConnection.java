package fr.synchroneyes.data_storage;


import fr.synchroneyes.data_storage.Config.FileReader;
import org.bukkit.Bukkit;

import java.sql.*;

/**
 * Classe permettant de gerer une connexion à une base de donnée
 */
public class SQLConnection {

    // Permet de stocker la connexion
    public Connection connection;

    public static SQLConnection instance;

    private SQLConnection() {
        instance = this;

        try {
            FileReader configFileReader = new FileReader();
            this.connection = configFileReader.getCredentials().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    public static SQLConnection getInstance() {
        if(instance == null) return new SQLConnection();
        return instance;
    }

    public ResultSet query(String query) throws SQLException {
        PreparedStatement requetePrepare = getInstance().connection.prepareStatement(query);
        requetePrepare.execute();
        return requetePrepare.getResultSet();
    }
}
