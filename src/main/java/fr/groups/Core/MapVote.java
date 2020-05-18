package fr.groups.Core;

import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MapVote {

    private static String folder_name = mineralcontest.plugin.getDataFolder() + File.separator + "worlds" + File.separator;
    private ArrayList<String> maps;
    //              (nom_map, joueur)
    private HashMap<String, Player> votes;

    public MapVote() {
        this.maps = new ArrayList<>();
        this.votes = new HashMap<>();
        chargerNomMaps();
    }

    /**
     * Charge les noms de maps depuis le dossier plugins/mineralcontest/worlds
     */
    private void chargerNomMaps() {
        File dossierMaps = new File(folder_name);

        File[] maps = dossierMaps.listFiles();

        for (File map : maps)
            if (map.isDirectory())
                if (map.getName().startsWith("mc_"))
                    this.maps.add(map.getName());
    }

    public ArrayList<String> getMaps() {
        return this.maps;
    }

    /**
     * Retourne le nombre de vote pour une map
     *
     * @param nomMap - ID de la map
     * @return
     */
    public int getMapVoteCount(String nomMap) {
        return 1;
    }


}
