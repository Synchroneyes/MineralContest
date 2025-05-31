package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.custom_events.MCGameStartedEvent;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GameStartedEvent implements Listener {

    @EventHandler
    public void onGameStartEvent(MCGameStartedEvent event) {

        if (!(boolean) mineralcontest.getPluginConfigValue("enable_metrics")) return;

        String file_url = "https://github.com/Synchroneyes/mineralcontest-static-backend/releases/download/stats-gamestarted/game_started";

        try {
            // Create a URL object
            URL url = new URL(file_url);

            // Open a connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to GET
            connection.setRequestMethod("GET");

            // Check if the response code is 200 (HTTP OK)
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Log the response (or handle it as needed)
                Bukkit.getLogger().info(mineralcontest.prefix + "Sent game started event to backend.");
            } else {
                Bukkit.getLogger().warning(mineralcontest.prefix + "Failed to send game started event to backend. Response code: " + responseCode);
            }

            // Disconnect the connection
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}