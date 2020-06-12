package fr.mineral.Utils.VersionChecker;

import fr.mineral.Translation.Lang;
import fr.mineral.Utils.UrlFetcher.Urls;
import fr.mineral.mineralcontest;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Version {

    public static boolean isUpdating = false;
    public static boolean hasUpdated = false;

    public static void Check() {
        // $.post("http://localhost:8000/api/plugin/check-version", {version: "2"}).done(function(data){console.log(data)})
        // $.post("http://localhost:8000/api/plugin/get-messages", {version: "1.0"}).done(function(data){console.log(data)})


        String currentVersion = mineralcontest.plugin.getDescription().getVersion();
        HttpPost request = new HttpPost(Urls.API_URL_LAST_VERSION_CHECK);
        try {
            List<NameValuePair> parametres = new ArrayList<>();
            parametres.add(new BasicNameValuePair("version", currentVersion));

            request.setEntity(new UrlEncodedFormEntity(parametres));

            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = null;
            response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String entityContents = EntityUtils.toString(entity);


            // On récupère la réponse, plusieurs cas
            JSONObject reponse = new JSONObject(entityContents);
            if (reponse.getString("status").equals("update")) {
                Bukkit.getConsoleSender().sendMessage(mineralcontest.prefix + ChatColor.RED + " A new update is available, plugin will now auto-update to version " + reponse.getString("message"));
                isUpdating = true;
                DownloadNewVersion(reponse.getString("url"), reponse.getString("file_name"), reponse.get("file_size").toString());
            }

            if (reponse.getString("status").equals("same")) {
                Bukkit.getConsoleSender().sendMessage(mineralcontest.prefix + ChatColor.GREEN + " Plugin is up-to-date!");
            }
            // Réponse du site web
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Converti une version (ex: 1.0.2) en "nombre" (ex: 102). En gros retire les "." ou les ","
     *
     * @param chaine
     * @return
     */
    private static String toVersion(String chaine) {
        chaine = chaine.replace(",", "");
        return chaine.replace(".", "");

    }

    private static void DownloadNewVersion(String url, String fileName, String fileSize) throws InterruptedException {
        Bukkit.getConsoleSender().sendMessage(mineralcontest.prefix + "" + ChatColor.GOLD + " Downloading version " + url);

        try {

            File dossierTelechargement = new File("plugins");
            if (!dossierTelechargement.exists()) dossierTelechargement.mkdir();

            File fichierTelecharge = new File(dossierTelechargement, fileName);


            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            FileOutputStream fos = new FileOutputStream(fichierTelecharge);
            int inByte;

            // Taille d'un MO en byte
            int taille_mo = 1048576;
            int taille_fichier = Integer.parseInt(fileSize);
            double downloaded = 0;

            while ((inByte = is.read()) != -1) {
                fos.write(inByte);
                downloaded++;

                if (downloaded % (taille_mo / 10) == 0)
                    Bukkit.getConsoleSender().sendMessage(mineralcontest.prefix + ChatColor.GREEN + "Download progress: " + ((downloaded / taille_fichier) * 100) + "%");
            }

            is.close();
            fos.close();

            client.close();

            Bukkit.getConsoleSender().sendMessage(mineralcontest.prefix + ChatColor.GREEN + " Download complete!");
            //Bukkit.getConsoleSender().sendMessage(mineralcontest.prefix + ChatColor.RED + " Sending: " + "reload " + mineralcontest.plugin.getDescription().getName());
            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "reload " + mineralcontest.plugin.getDescription().getName());
            isUpdating = false;
            hasUpdated = true;


        } catch (FileNotFoundException fno) {
            fno.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /**
     * Mettre 2 chaines de caractère à la même "longueur" en ajoutant des "0" à la fin.
     * Ex: s1 = "10" et s2 = "111", s1 va devenir "100" et s2 restera pareil
     *
     * @param s1
     * @param s2
     * @return
     */
    private String[] setStringToSameSize(String s1, String s2) {
        StringBuilder s1Builder = new StringBuilder(s1);
        StringBuilder s2Builder = new StringBuilder(s2);

        while (s1Builder.length() > s2.length())
            s1Builder.append("0");
        s1 = s1Builder.toString();

        while (s2Builder.length() > s1.length())
            s2Builder.append("0");
        s2 = s2Builder.toString();

        return new String[]{s1, s2};
    }
}
