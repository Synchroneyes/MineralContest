package fr.mineral.Utils;

import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Metrics {
    private int numberOfPlayers;
    private String serverIP;
    private String serverPort;

    private String apiUrl = "http://mineral.synchroneyes.fr/api/sendStatistic.php";


    public Metrics() {
        numberOfPlayers = mineralcontest.plugin.getServer().getOnlinePlayers().size();
        serverIP = Bukkit.getIp();
        serverPort = "" + Bukkit.getPort();
    }

    public void sendData() {
        try {

            String url = "?numberOfPlayer=" + numberOfPlayers + "&serverIP=" + serverIP + "&serverPort=" + serverPort;
            URL myUrl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) myUrl.openConnection();





            Map<String, String> parameters = new HashMap<>();
            parameters.put("numberOfPlayer", numberOfPlayers + "");
            parameters.put("serverIP", serverIP);
            parameters.put("serverPort", serverPort);



        }catch(MalformedURLException mue) {
            mue.printStackTrace();
        }catch(ProtocolException pe) {
            pe.printStackTrace();
        }catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
