package fr.synchroneyes.mineral.Utils.VersionChecker;

import fr.synchroneyes.mapbuilder.MapBuilder;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.UrlFetcher.Urls;
import fr.synchroneyes.mineral.mineralcontest;
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
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Version {

    public static boolean isUpdating = false;
    public static boolean hasUpdated = false;
    public static boolean isCheckingStarted = false;

    /**
     * Récupère tous les messages à partir du site web pour cette version du plugin
     *
     * @paramNonUsed threadedFetch - Utiliser un thread ou non pour l'utilisation de cette fonction
     * @param listToFill    - Une liste à remplir avec les messages du site
     */
    public static void fetchAllMessages(List<String> listToFill) {

        listToFill.clear();

        // On récupère la verison du plugin
        String currentVersion = mineralcontest.plugin.getDescription().getVersion();

        // On crée la nouvelle requete
        HttpGet request = new HttpGet(Urls.API_URL_MESSAGES);
        HttpClient httpClient = HttpClientBuilder.create()
                .setRedirectStrategy(new LaxRedirectStrategy()).build();
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String entityContents = EntityUtils.toString(entity);

            // Réponse du site web
            JSONObject messages = new JSONObject(entityContents);

            // Check si message pour notre version
            if (!messages.has(currentVersion)) return;

            JSONArray messagesArray = messages.getJSONArray(currentVersion);

            for(int i = 0; i < messagesArray.length(); i++) {
                listToFill.add(Lang.translate(messagesArray.getString(i)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static void doCheck() {

        String currentVersion = mineralcontest.plugin.getDescription().getVersion();

        HttpGet request = new HttpGet(Urls.API_URL_VERSIONS);
        HttpClient httpClient = HttpClientBuilder.create()
                .setRedirectStrategy(new LaxRedirectStrategy()).build();
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String entityContents = EntityUtils.toString(entity);

            // Réponse du site web
            JSONObject files = new JSONObject(entityContents);
            JSONObject versions = files.getJSONObject("plugins");

            List<String> available_versions = new ArrayList<>(versions.keySet());

            available_versions.sort((v1, v2) -> {
                String[] parts1 = v1.split("\\.");
                String[] parts2 = v2.split("\\.");
                int length = Math.max(parts1.length, parts2.length);

                for (int i = 0; i < length; i++) {
                    int num1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
                    int num2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;
                    if (num1 != num2) {
                        return Integer.compare(num2, num1);  // Reverse order for DESC
                    }
                }
                return 0;
            });

            String latestVersion = available_versions.get(0);

            if (isCurrentVersionLast(latestVersion)) {
                Bukkit.getConsoleSender().sendMessage(mineralcontest.prefix + ChatColor.GREEN + " Plugin is up-to-date! Current Version: " + currentVersion + " - Latest Version: " + latestVersion);
            } else {
                Bukkit.getConsoleSender().sendMessage(mineralcontest.prefix + ChatColor.RED + " A new update is available, plugin will now auto-update to version " + latestVersion);
                isUpdating = true;
                DownloadNewVersion(versions.getJSONObject(latestVersion).getString("file_url"), versions.getJSONObject(latestVersion).getString("file_name"), versions.getJSONObject(latestVersion).getString("file_size"), latestVersion);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void Check(boolean theadedCheck) {
        if (MapBuilder.getInstance().isBuilderModeEnabled) return;
        if (!isCheckingStarted) isCheckingStarted = true;

        if (theadedCheck) {
            Thread thread = new Thread(Version::doCheck);
            thread.start();
        } else {
            doCheck();
        }


    }

    private static boolean isCurrentVersionLast(String version) {
        // Fetch the current version of the plugin
        String currentVersion = mineralcontest.plugin.getDescription().getVersion();

        // Compare the current version with the passed version string
        return compareVersions(currentVersion, version) >= 0;
    }

    // Helper method to compare two version strings
    private static int compareVersions(String version1, String version2) {
        String[] parts1 = version1.split("\\.");
        String[] parts2 = version2.split("\\.");

        int length = Math.max(parts1.length, parts2.length);

        for (int i = 0; i < length; i++) {
            int num1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
            int num2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;
            if (num1 != num2) {
                return Integer.compare(num1, num2);
            }
        }

        return 0; // The versions are equal
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

    private static void DownloadNewVersion(String url, String fileName, String fileSize, String version) throws InterruptedException {
        Bukkit.getConsoleSender().sendMessage(mineralcontest.prefix + "" + ChatColor.GOLD + " Downloading version " + version);
        Bukkit.broadcastMessage(mineralcontest.prefix + ChatColor.GOLD + " Downloading a new version of the plugin ...");

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
                    Bukkit.getConsoleSender().sendMessage(mineralcontest.prefix + ChatColor.GREEN + " Download progress: " + ((downloaded / taille_fichier) * 100) + "%");
            }

            is.close();
            fos.close();

            client.close();

            Bukkit.getConsoleSender().sendMessage(mineralcontest.prefix + ChatColor.GREEN + " Download complete! Now reloading ...");
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
