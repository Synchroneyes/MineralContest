package fr.synchroneyes.groups.Core;

import fr.synchroneyes.groups.Menus.MenuVote;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapVote {

    private static String folder_name = mineralcontest.plugin.getDataFolder() + File.separator + "worlds" + File.separator;
    private ArrayList<String> maps;
    //              (nom_map, joueur)
    private HashMap<Player, String> votes;
    protected boolean voteEnabled;

    private MenuVote menuVote;

    public MapVote() {
        this.maps = new ArrayList<>();
        this.votes = new HashMap<>();
        chargerNomMaps();
        voteEnabled = true;
        this.menuVote = new MenuVote();
    }

    public MenuVote getMenuVote() {
        return menuVote;
    }


    /**
     * Permet de retirer un joueur des votes
     *
     * @param p
     */
    public void removePlayerVote(Player p) {
        if (votes.containsKey(p)) votes.remove(p);
    }

    /**
     * Retourne une hashmap avec les maps et leur nombre de vote!
     *
     * @return
     */
    public Map<String, Integer> getMapVotes(boolean orderByMostVoted) {
        Map<String, Integer> mapsVote = new HashMap<>();

        // On regarde pour chaque vote enregistré
        for (Map.Entry<Player, String> infoVoteJoueur : votes.entrySet())
            // Si cette map n'a pas encore de vote, on l'ajoute
            if (!mapsVote.containsKey(infoVoteJoueur.getValue())) mapsVote.put(infoVoteJoueur.getValue(), 1);
                // Sinon, on lui ajoute un vote
            else mapsVote.replace(infoVoteJoueur.getValue(), mapsVote.get(infoVoteJoueur.getValue()) + 1);

        if (orderByMostVoted) {
            Map<String, Integer> mapsVoteOrdered = new HashMap<>();

            // On récupère à chaque tour de boucle la meilleure map
            int maxVotes = -1;
            String nomMap = "";

            HashMap<Player, String> _votes = (HashMap<Player, String>) votes.clone();
            // Tant que la liste de vote n'est pas vide
            while (!mapsVote.isEmpty()) {

                // Pour chaque votes
                for (Map.Entry<String, Integer> vote : mapsVote.entrySet()) {

                    // On récupère la map avec le plus de vote
                    if (vote.getValue() >= maxVotes) {
                        maxVotes = vote.getValue();
                        nomMap = vote.getKey();
                    }
                }

                // Et on ajoute la plus grande valeur à notre liste ordonnée
                mapsVoteOrdered.put(nomMap, maxVotes);
                mapsVote.remove(nomMap);
                maxVotes = Integer.MIN_VALUE;
            }

            // On retourne notre liste!
            return mapsVoteOrdered;

        } else return mapsVote;
    }


    public void disableVote() {
        voteEnabled = false;
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

    public void enregistrerVoteJoueur(String idMap, Player joueur) {

        if (!voteEnabled) {
            joueur.sendMessage(mineralcontest.prefixErreur + Lang.vote_not_enabled.toString());
            return;
        }

        if (havePlayerVoted(joueur)) this.votes.replace(joueur, idMap);
        else this.votes.put(joueur, idMap);


        joueur.sendMessage(mineralcontest.prefixPrive + Lang.vote_you_voted_for_map.toString().replace("%map%", idMap));
        //joueur.sendMessage(mineralcontest.prefixPrive + "Vous avez voté pour la map " + nom_map);
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


    /**
     * Retourne la liste des joueurs n'ayent pas voté
     *
     * @return
     */
    public List<Player> joueurAyantNonVote() {
        if (votes.isEmpty()) return new ArrayList<>();

        List<Player> joueurSansVote = new ArrayList<>();

        // On récupère le premier joueur de la liste
        Groupe groupe = mineralcontest.getPlayerGroupe(votes.entrySet().iterator().next().getKey());

        if (groupe == null) return joueurSansVote;

        // Maintenant, on va ajouter tous les joueurs du groupe dans les votants;
        joueurSansVote.addAll(groupe.getPlayers());

        // Et on va supprimer chaque joueur ayant déjà voté
        for (Map.Entry<Player, String> infoVote : votes.entrySet())
            joueurSansVote.remove(infoVote.getKey());

        return joueurSansVote;
    }
}
