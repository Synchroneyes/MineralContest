package fr.synchroneyes.mineral.Utils.UrlFetcher;

import fr.synchroneyes.mineral.mineralcontest;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.bukkit.Bukkit;
import org.json.JSONObject;

import java.util.logging.Logger;

public class Urls {


    public static boolean isWebsiteDown = false;

    public static String API_URL_WORKSHOP_LIST = "";
    public static String API_URL_VERSIONS = "";
    public static String API_URL_MESSAGES = "";


    public static String WEBSITE_URL = "https://api.mc.monvoisin-kevin.fr";
    public static String GET_ALL_URL_ROUTE = "/index.json";

    public static boolean areAllUrlFetched = false;

    /**
     * Récupères toutes les URL de l'api
     */
    public static void FetchAllUrls() {

        boolean displayInConsole = mineralcontest.debug;

        Logger logger = mineralcontest.plugin.getLogger();
        if (displayInConsole) logger.info(mineralcontest.prefix + "Fetching all URLs");

        HttpGet request = new HttpGet(Urls.WEBSITE_URL + GET_ALL_URL_ROUTE);
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response;
        try {
            response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String entityContents = EntityUtils.toString(entity);

            // Réponse du site web
            JSONObject jsonResponse = new JSONObject(entityContents);
            String prefixURL = mineralcontest.prefix + "[URL] ";

            API_URL_WORKSHOP_LIST = WEBSITE_URL + jsonResponse.getString("api_workshop_list");
            if (displayInConsole) logger.info(prefixURL + "API_URL_WORKSHOP_LIST => " + API_URL_WORKSHOP_LIST);

            API_URL_VERSIONS = WEBSITE_URL + jsonResponse.getString("api_files_list");
            if (displayInConsole) logger.info(prefixURL + "API_URL_VERSIONS => " + API_URL_VERSIONS);

            API_URL_MESSAGES = WEBSITE_URL + jsonResponse.getString("api_messages_list");
            if (displayInConsole) logger.info(prefixURL + "API_URL_MESSAGES => " + API_URL_MESSAGES);

            areAllUrlFetched = true;


        } catch (Exception e) {
            isWebsiteDown = true;
            e.printStackTrace();
        }
    }
}
