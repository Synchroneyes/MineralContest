/**
 * TODO:
 * - Votemap
 * - Démarrer partie
 * - Creer teams (max3)
 */
package fr.groups.Core;

import fr.groups.Utils.Etats;
import fr.mineral.Core.Game.Game;
import fr.mineral.Core.House;
import fr.mineral.Settings.GameSettings;
import fr.mineral.Teams.Equipe;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.Random;

public class Groupe {
    private int tailleIdentifiant = 10;
    private String identifiant;
    private LinkedList<Equipe> equipes;
    private LinkedList<Player> admins;
    private LinkedList<Player> joueurs;
    private LinkedList<Player> joueursInvites;
    private World gameWorld;
    private MapVote mapVote;

    private Game partie;
    private String nom;
    private WorldLoader worldLoader;

    private Etats etat;

    private boolean groupLocked = false;
    private String mapName = "";

    private GameSettings parametresPartie;


    public Groupe() {
        this.equipes = new LinkedList<>();
        this.admins = new LinkedList<>();
        this.joueurs = new LinkedList<>();
        this.joueursInvites = new LinkedList<>();

        parametresPartie = new GameSettings(true);

        this.partie = new Game(this);


        this.partie.init();
        partie.setGroupe(this);
        this.etat = Etats.EN_ATTENTE;
        this.worldLoader = new WorldLoader(this);
        genererIdentifiant();
    }


    public GameSettings getParametresPartie() {
        return parametresPartie;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public LinkedList<Player> getPlayers() {
        return joueurs;
    }

    public boolean isPlayerIngroupe(Player p) {
        return (this.joueurs.contains(p));
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public World getMonde() {
        return gameWorld;
    }

    public House getPlayerHouse(Player p) {
        for (House maison : getGame().getHouses())
            if (maison.getTeam().isPlayerInTeam(p)) return maison;
        return null;

    }

    public Equipe getPlayerTeam(Player p) {
        for (House maison : getGame().getHouses())
            if (maison.getTeam().isPlayerInTeam(p)) return maison.getTeam();
        return null;

    }

    public void genererIdentifiant() {
        String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        StringBuilder id_generer = new StringBuilder();
        Random random = new Random();
        int numero_aleatoire = 0;
        for (int i = 0; i < tailleIdentifiant; ++i) {
            numero_aleatoire = random.nextInt(alphabet.length);
            id_generer.append(alphabet[numero_aleatoire]);
        }

        this.identifiant = id_generer.toString();

    }

    /**
     * @param nomMonde - Nom du monde à charger
     * @return true si chargement réussi, false sinon
     */
    public boolean chargerMonde(String nomMonde) {

        try {
            this.gameWorld = worldLoader.chargerMonde(nomMonde, getIdentifiant());
            this.gameWorld.setAutoSave(false);
        } catch (Exception e) {
            sendToadmin(mineralcontest.prefixErreur + " Impossible de charger le monde. Erreur: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        if (gameWorld == null) {
            sendToadmin(mineralcontest.prefixErreur + " Impossible de charger le monde.");
            return false;
        }

        Location worldSpawnLocation = gameWorld.getSpawnLocation();
        for (Player joueur : joueurs)
            joueur.teleport(worldSpawnLocation);

        setMapName(nomMonde);

        this.mapVote.clearVotes();
        return true;
    }

    /**
     * Décharge un monde
     *
     * @return
     */
    public boolean dechargerMonde() {
        if (gameWorld == null) return false;

        for (Player joueur : joueurs) {
            joueur.teleport(mineralcontest.plugin.defaultSpawn);
        }


        mineralcontest.plugin.getServer().unloadWorld(gameWorld, false);

        worldLoader.supprimerMonde(gameWorld);
        return true;
    }

    public boolean isGroupLocked() {
        return groupLocked;
    }

    public void setGroupLocked(boolean groupLocked) {
        sendToadmin(mineralcontest.prefixPrive + ((groupLocked) ? Lang.group_is_now_locked.toString() : Lang.group_is_now_unlocked.toString()));
        this.groupLocked = groupLocked;
    }

    public MapVote getMapVote() {
        return this.mapVote;
    }

    public void initVoteMap() {
        if (this.mapVote != null) return;
        this.mapVote = new MapVote();
    }

    public void enableVote() {
        if (this.mapVote != null) mapVote.voteEnabled = true;
    }

    public Etats getEtatPartie() {
        return this.etat;
    }

    public void setEtat(Etats etat) {
        this.etat = etat;
    }

    public boolean isPlayerInvited(Player p) {
        return this.joueursInvites.contains(p);
    }

    public void removeAdmin(Player joueur) {
        sendToadmin(mineralcontest.prefixPrive + Lang.translate(Lang.player_is_no_longer_a_group_admin.toString(), joueur));
        this.admins.remove(joueur);

    }


    public void inviterJoueur(Player p) {
        if (joueursInvites.contains(p)) {
            sendToadmin("ERREUR DEJA INVITE");
            return;
        }

        if (joueurs.contains(p)) {
            sendToadmin(mineralcontest.prefixErreur + Lang.translate(Lang.error_player_already_in_this_group.toString(), p));
            return;
        }

        if (mineralcontest.getPlayerGroupe(p) != null) {
            sendToadmin(mineralcontest.prefixErreur + Lang.translate(Lang.error_player_already_have_a_group.toString(), p));
            return;
        }

        p.sendMessage(mineralcontest.prefixPrive + Lang.translate(Lang.you_got_invited_to_a_group.toString(), this));
        sendToadmin(mineralcontest.prefixPrive + Lang.translate(Lang.player_successfully_invited_to_group.toString(), p));
        this.joueursInvites.add(p);
    }

    public Game getGame() {
        return partie;
    }

    public String getNom() {
        return this.nom;
    }

    public String setNom(String nom) {
        this.nom = nom;
        return this.nom;
    }

    public boolean containsPlayer(Player p) {
        return joueurs.contains(p);
    }

    public boolean isGroupeCreateur(Player p) {
        return this.admins.getFirst().equals(p);
    }

    public boolean isAdmin(Player p) {
        return this.admins.contains(p);
    }

    public void kickPlayer(Player p) {
        this.joueurs.remove(p);
        this.admins.remove(p);
        this.joueursInvites.remove(p);
        p.sendMessage(mineralcontest.prefixPrive + Lang.translate(Lang.you_were_kicked_from_a_group.toString(), this));
        sendToEveryone(mineralcontest.prefixPrive + Lang.translate(Lang.player_got_kicked_from_group.toString(), p));
    }

    /**
     * Envoie un message aux admins
     * @param message: Message à envoyer
     */
    public void sendToadmin(String message) {
        for (Player player : admins)
            player.sendMessage(message);
    }

    public void sendToEveryone(String message) {
        for (Player p : joueurs) {
            p.sendMessage(message);
        }
    }

    /**
     * Permet de créer une équipe
     * @param nom - Nom de l'équipe
     * @param couleur - Couleur de l'équipe
     */
    public void addEquipe(String nom, ChatColor couleur) {

    }

    /**
     * Permet de supprimer une équipe
     * @param nom
     */
    public void removeEquipe(String nom) {

    }


    public void addJoueur(Player p) {
        if (this.joueurs.contains(p)) return;

        this.joueursInvites.remove(p);
        this.joueurs.add(p);
        p.sendMessage(mineralcontest.prefixPrive + Lang.translate(Lang.successfully_joined_a_group.toString(), this));
        sendToadmin(mineralcontest.prefixAdmin + p.getDisplayName() + " a rejoin le groupe");
    }

    public void addAdmin(Player p) {
        if (!this.joueurs.contains(p)) addJoueur(p);
        if (!this.admins.contains(p)) this.admins.add(p);
        sendToadmin(mineralcontest.prefixPrive + Lang.translate(Lang.player_is_now_group_admin.toString(), p));
    }

    public int getPlayerCount() {
        return this.joueurs.size();
    }

    public void retirerJoueur(Player joueur) {
        if (isGroupeCreateur(joueur)) {

            sendToEveryone(mineralcontest.prefixPrive + Lang.group_got_deleted.toString());
            this.joueurs.clear();
            this.admins.clear();
            this.joueursInvites.clear();
            mineralcontest.supprimerGroupe(this);
            return;
        }

        this.joueurs.remove(joueur);
        this.admins.remove(joueur);
        this.joueursInvites.remove(joueur);
        joueur.sendMessage(mineralcontest.prefixPrive + Lang.translate(Lang.you_left_the_group.toString(), this));
    }

    public int getPlayerCountRequired() {
        return 5;
    }

    public LinkedList<Player> getAdmins() {
        return admins;
    }


}

