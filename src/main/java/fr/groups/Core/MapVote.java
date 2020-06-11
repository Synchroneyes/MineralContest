package fr.groups.Core;

import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapVote {

    private static String folder_name = mineralcontest.plugin.getDataFolder() + File.separator + "worlds" + File.separator;
    private ArrayList<String> maps;
    //              (nom_map, joueur)
    private HashMap<Player, String> votes;
    protected boolean voteEnabled = false;

    public MapVote() {
        this.maps = new ArrayList<>();
        this.votes = new HashMap<>();
        chargerNomMaps();
        voteEnabled = true;
    }

    public void clearVotes() {
        this.votes.clear();
    }

    /**
     * Charge les noms de maps depuis le dossier plugins/mineralcontest/worlds
     */
    private void chargerNomMaps() {
        File dossierMaps = new File(folder_name);

        if (!dossierMaps.exists()) dossierMaps.mkdir();

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
     * Retourne si oui ou non un joueur a voté
     *
     * @param joueur - Le joueur
     * @return true si le joueur a voté, false sinon
     */
    public boolean havePlayerVoted(Player joueur) {
        for (Map.Entry<Player, String> couple : votes.entrySet()) {
            if (couple.getKey().equals(joueur)) return true;
        }
        return false;
    }

    public void enregistrerVoteJoueur(int idMap, Player joueur) {

        if (!voteEnabled) {
            joueur.sendMessage(mineralcontest.prefixErreur + Lang.vote_not_enabled.toString());
            return;
        }

        if (havePlayerVoted(joueur)) {
            joueur.sendMessage(mineralcontest.prefixErreur + Lang.vote_already_voted.toString());
            return;
        }

        String nom_map = getNomMapFromID(idMap);


        this.votes.put(joueur, nom_map);
        joueur.sendMessage(mineralcontest.prefixPrive + "Vous avez voté pour la map " + nom_map);
        // On veut vérifier si on a tout les votes
        Groupe playerGroupe = mineralcontest.getPlayerGroupe(joueur);
        if (votes.size() == playerGroupe.getPlayerCount()) {
            // On arrête le vote
            setVoteEnabled(false);
            // On récupère la map gagnante
            String mapAcharger = getMapGagnante();
            // On demande à charger la map

            playerGroupe.chargerMonde(mapAcharger);

        }
    }

    /**
     * Retourn le nom de la map à partir de son identifiant
     *
     * @param idMap - Nom de la map
     * @return nom de la map
     */
    public String getNomMapFromID(int idMap) {
        int index = 0;
        for (String nom : maps) {
            if (index == idMap) return nom;
            index++;
        }
        return "";
    }

    /**
     * Retourne le nombre de vote pour une map
     *
     * @param nomMap - ID de la map
     * @return
     */
    public int getMapVoteCount(String nomMap) {
        int nbVote = 0;
        for (Map.Entry<Player, String> couple : votes.entrySet()) {
            if (couple.getValue().equalsIgnoreCase(nomMap)) nbVote++;
        }

        return nbVote;
    }

    /**
     * Retourne la map avec le plus de vote
     *
     * @return nomMapGagnante
     */
    public String getMapGagnante() {
        int maxVote = -1;
        int currentMapVote;
        String mapGagnante = "";
        for (String map : maps) {
            currentMapVote = getMapVoteCount(map);
            if (currentMapVote > maxVote) {
                maxVote = currentMapVote;
                mapGagnante = map;
            }
        }

        return mapGagnante;
    }


    public boolean isVoteEnabled() {
        return voteEnabled;
    }

    public void setVoteEnabled(boolean voteEnabled) {
        this.voteEnabled = voteEnabled;
    }
}
