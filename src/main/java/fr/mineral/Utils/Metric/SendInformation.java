package fr.mineral.Utils.Metric;

import fr.mineral.Settings.GameSettingsCvar;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;

public class SendInformation {

    private static boolean enabled = true;
    private static String ApiServerURL = "http://mineral.synchroneyes.fr/api/metrics";
    public static String start = "start";
    public static String ended = "ended";

    public static void sendGameData(String state) {
        // On utilise des threads pour ne pas avoir à se soucier du temps de réponse

        boolean useThread = state.equals(start);
        if(useThread) {
            Thread thread = new Thread(() -> {
                send(state);
            });
            thread.start();
        } else {
            send(state);
        }

    }

    private static void send(String state) {
        if((int) GameSettingsCvar.mp_enable_metrics.getValue() == 1) {

            try {
                // On crée un nouvel objet request
                URLRequest request = new URLRequest("POST");
                request.setUrl(ApiServerURL);

                // On lui passe les parametres
                request.addParameters("serverPort", Bukkit.getServer().getPort());
                request.addParameters("numberOfPlayers", mineralcontest.plugin.pluginWorld.getPlayers().size());
                request.addParameters("biomePlayed", "0");
                request.addParameters("state", state);
                request.addParameters("killCounter", mineralcontest.plugin.getGame().killCounter);

                String result = request.getQueryResult();
                Bukkit.getLogger().info(mineralcontest.prefix + "Resultat appel API: " + result);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

}
