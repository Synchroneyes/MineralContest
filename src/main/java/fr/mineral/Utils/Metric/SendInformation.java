package fr.mineral.Utils.Metric;

import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.net.InetAddress;

public class SendInformation {

    private static boolean enabled = true;
    private static String ApiServerURL = "http://mineral.synchroneyes.fr/api/metrics";

    public static void enable() {
        enabled = true;
        Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "Activation de l'envoie de statistique");
    }
    public static void disable() {
        enabled = false;
        Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "Désactivation de l'envoie de statistique");

    }

    public static void sendGameStartedData() throws Exception {

        if(enabled) {
            // On crée un nouvel objet request
            URLRequest request = new URLRequest("POST");
            request.setUrl(ApiServerURL);

            // On lui passe les paramètres
            try {
                request.addParameters("ServerIP", InetAddress.getLocalHost().getAddress());
                request.addParameters("ServerPort", Bukkit.getServer().getPort());
                request.addParameters("numberOfPlayers", Bukkit.getServer().getOnlinePlayers().size());
                request.addParameters("biomePlayed", mineralcontest.plugin.getGame().votemap.getWinnerBiome());
                request.addParameters("state", "started");

                String result = request.getQueryResult();
                Bukkit.getLogger().info(mineralcontest.prefix + "Resultat appel API: " + result);
            }catch (Exception e) {
                Bukkit.getLogger().info(mineralcontest.prefixErreur + "Erreur appel API: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void sendGameEndedData() throws Exception {

        if(enabled) {
            // On crée un nouvel objet request
            URLRequest request = new URLRequest("POST");
            request.setUrl(ApiServerURL);

            // On lui passe les paramètres
            try {
                request.addParameters("ServerIP", InetAddress.getLocalHost().getAddress());
                request.addParameters("ServerPort", Bukkit.getServer().getPort());
                request.addParameters("numberOfPlayers", Bukkit.getServer().getOnlinePlayers().size());
                request.addParameters("biomePlayed", mineralcontest.plugin.getGame().votemap.getWinnerBiome());
                request.addParameters("state", "ended");

                String result = request.getQueryResult();
                Bukkit.getLogger().info(mineralcontest.prefix + "Resultat appel API: " + result);
            }catch (Exception e) {
                Bukkit.getLogger().info(mineralcontest.prefixErreur + "Erreur appel API: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
