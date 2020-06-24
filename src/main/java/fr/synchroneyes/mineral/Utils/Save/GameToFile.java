package fr.synchroneyes.mineral.Utils.Save;

import fr.synchroneyes.mineral.Core.Arena.Arene;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;


public class GameToFile {
    public String worldName;
    public Arene arene;

    public GameToFile(String nomMonde) {
        this.worldName = nomMonde;
        // On récupère les valeurs du monde actuel
        /*this.teamBleu = mineralcontest.getPlayerGame(joueur).getBlueHouse().getTeam();
        this.teamRouge = mineralcontest.getPlayerGame(joueur).getRedHouse().getTeam();
        this.teamJaune = mineralcontest.getPlayerGame(joueur).getYellowHouse().getTeam();*/
        //this.arene = mineralcontest.getPlayerGame(joueur).getArene();
    }

    public boolean saveToFile() throws Exception {

        JSONObject data = new JSONObject();
        JSONArray mondes = new JSONArray();
        JSONObject monde_actuel = new JSONObject();

        JSONArray porteRouge = new JSONArray();


        String biomefolder = mineralcontest.plugin.getDataFolder() + File.separator + "biome" + File.separator;
        FileWriter nouveau_biome = new FileWriter(biomefolder + "nouveau_biome.json");
        //nouveau_biome.write(json.toString());
        //nouveau_biome.close();
        Bukkit.getLogger().info("======================================");
        //mineralcontest.broadcastMessage("Un nouveau biome a été ajouté. Pour pouvoir l'utiliser, il faut remplacer un biome existant par celui ci." + ChatColor.RED + ChatColor.BOLD + " MERCI DE LIRE LA CONSOLE !!!!!!!!!");
        Bukkit.getLogger().info("Le nouveau biome est dispo dans le dossier /plugins/mineralcontest/biome/nouveau_biome.json");
        Bukkit.getLogger().info("Il faut renommer le fichier \"nouveau_biome.json\" en <numero>.json, exemple: 0.json");
        Bukkit.getLogger().info("Ce numéro ne doit pas dépasser 5");
        Bukkit.getLogger().info("Il faut également ouvrir le fichier avec un éditeur de texte (notepad++, ou blocnote par exemple) et au tout début du fichier; vous avez \"{\"nouveau_biome\":\". Il faut remplacer \"nouveau_biome\" par le numéro choisit plus haut.");
        Bukkit.getLogger().info("Vous allez également devoir supprimer l'ancien fichier. Pour jouer sur ce nouveau biome, voici les ID en fonction des noms de biome:");
        Bukkit.getLogger().info("0 = neige\n" +
                "1 = desert\n" +
                "2 = foret\n" +
                "3 = plaine\n" +
                "4 = montagne\n" +
                "5 = marécage");
        Bukkit.getLogger().info("======================================");


        return true;
    }


}
