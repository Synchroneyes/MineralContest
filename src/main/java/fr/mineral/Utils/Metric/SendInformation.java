package fr.mineral.Utils.Metric;

import fr.mineral.Core.Game.Game;
import fr.mineral.Settings.GameSettings;
import fr.mineral.Settings.GameSettingsCvarOLD;
import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.Utils.UrlFetcher.Urls;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;

public class SendInformation {

    private static boolean enabled = true;
    public static String start = "start";
    public static String ended = "ended";

    public static void sendGameData(String state, Game partie) {
        // On utilise des threads pour ne pas avoir à se soucier du temps de réponse

        boolean useThread = state.equals(start);
        if(useThread) {
            Thread thread = new Thread(() -> {
                send(state, partie);
            });
            thread.start();
        } else {
            send(state, partie);
        }

    }

    private static void send(String state, Game partie) {
        GameSettings settings = partie.groupe.getParametresPartie();

        try {
            if (((boolean)mineralcontest.getPluginConfigValue("enable_metrics"))) {

                try {
                    // On crée un nouvel objet request
                    URLRequest request = new URLRequest("POST");
                    request.setUrl(Urls.API_URL_SEND_METRIC);

                    // On lui passe les parametres
                    request.addParameters("serverPort", Bukkit.getServer().getPort());
                    request.addParameters("numberOfPlayers", partie.groupe.getPlayers().size());
                    request.addParameters("biomePlayed", "0");
                    request.addParameters("state", state);
                    request.addParameters("killCounter", partie.killCounter);

                    String result = request.getQueryResult();
                    Bukkit.getLogger().info(mineralcontest.prefix + "Resultat appel API: " + result);
                } catch (Exception e) {
                    //e.printStackTrace();
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
            Error.Report(e, partie);
        }
    }

}
