package fr.synchroneyes.mineral.Utils.UrlFetcher;

import fr.synchroneyes.mineral.mineralcontest;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.logging.Logger;

public class Urls {


    public static boolean isWebsiteDown = false;

    public static String API_URL_WORKSHOP_LIST = "";
    public static String API_URL_LAST_VERSION_CHECK = "";
    public static String API_URL_GET_CURRENT_VERSION_MESSAGES = "";
    public static String API_URL_SEND_ERROR = "";
    public static String API_URL_SEND_METRIC = "";


    public static String WEBSITE_URL = "http://beta.synchroneyes.fr";
    public static String GET_ALL_URL_ROUTE = "/api/getAllUrls";

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

            API_URL_WORKSHOP_LIST = jsonResponse.getString("api_workshop_list");
            if (displayInConsole) logger.info(prefixURL + "API_URL_WORKSHOP_LIST => " + API_URL_WORKSHOP_LIST);

            API_URL_LAST_VERSION_CHECK = jsonResponse.getString("api_last_version_check");
            if (displayInConsole)
                logger.info(prefixURL + "API_URL_LAST_VERSION_CHECK => " + API_URL_LAST_VERSION_CHECK);

            API_URL_GET_CURRENT_VERSION_MESSAGES = jsonResponse.getString("api_get_current_version_messages");
            if (displayInConsole)
                logger.info(prefixURL + "API_URL_GET_CURRENT_VERSION_MESSAGES => " + API_URL_GET_CURRENT_VERSION_MESSAGES);


            API_URL_SEND_ERROR = jsonResponse.getString("api_send_error");
            if (displayInConsole) logger.info(prefixURL + "API_URL_SEND_ERROR => " + API_URL_SEND_ERROR);


            API_URL_SEND_METRIC = jsonResponse.getString("api_send_metric");
            if (displayInConsole) logger.info(prefixURL + "API_URL_SEND_METRIC => " + API_URL_SEND_METRIC);

            areAllUrlFetched = true;


        } catch (Exception e) {
            isWebsiteDown = true;
            e.printStackTrace();
        }
    }
}
