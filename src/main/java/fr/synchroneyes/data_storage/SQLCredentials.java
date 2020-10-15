package fr.synchroneyes.data_storage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe permettant de stocker les informations de connexion à une base de donnée
 */
public class SQLCredentials {

    private String hostname;
    private String database;
    private String username;
    private String password;
    private String port;



    public String getHostname() {
        return hostname;
    }

    public SQLCredentials setHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public String getDatabase() {
        return database;
    }

    public SQLCredentials setDatabase(String database) {
        this.database = database;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public SQLCredentials setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public SQLCredentials setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getPort() {
        return port;
    }

    public SQLCredentials setPort(String port) {
        this.port = port;
        return this;
    }

    public Connection getConnection() throws SQLException {
        /*return "jdbc:mysql://" + this.hostname + ":" + Integer.parseInt(this.port) + ";"+
                "database=" + this.database + ";" +
                "user=" + this.username + ";" +
                "password=" + this.password + ";";*/

        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database + ";" + this.username + ";" + this.password);
        return DriverManager.getConnection( "jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database, this.username, this.password);
    }
}
