package fr.mineral.Utils.ErrorReporting;

import fr.mineral.Core.Game.Game;
import fr.mineral.Utils.UrlFetcher.Urls;
import fr.mineral.mineralcontest;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.bukkit.Bukkit;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public class Error {
    public static void Report(java.lang.Exception exception, Game partie) {
        JSONObject gameInfo = new JSONObject();
        if (partie != null) gameInfo = Configuration.export(partie);

        JSONObject _exception = Exception.toJson(exception);

        JSONObject report = new JSONObject();

        report.put("server_port", Bukkit.getServer().getPort());
        report.put("report", new JSONObject()
                .put("game", gameInfo)
                .put("exception", _exception)
        );


        Bukkit.getLogger().severe("[MINERAL CONTEST] Envoie d'une erreur au serveur en cours ... L'erreur est la suivante:");
        exception.printStackTrace();

        try {
            Send(report);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        //Bukkit.getLogger().info(report.toString());
    }

    private static void Send(JSONObject json) throws IOException {
        // String url_post = "http://mineral.synchroneyes.fr/api/error/send";
        //String url_post = "http://localhost:8000/api/error/send";
        /*
        HttpPost post = new HttpPost(url);

        // add request parameter, form parameters
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("report", json.toString()));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            System.out.println("Response => " + EntityUtils.toString(response.getEntity()));
        }*/

        URL url = new URL(Urls.API_URL_SEND_ERROR);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("report", json.toString());
        //params.put("email", "fishie@seamail.example.com");
        //params.put("reply_to_thread", 10394);
        //params.put("message", "Shark attacks in Botany Bay have gotten out of control. We need more defensive dolphins to protect the schools here, but Mayor Porpoise is too busy stuffing his snout with lobsters. He's so shellfish.");

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

        StringBuilder reponse = new StringBuilder();
        for (int c; (c = in.read()) >= 0; )
            reponse.append((char) c);

        Bukkit.getLogger().info(reponse.toString());
    }

}
