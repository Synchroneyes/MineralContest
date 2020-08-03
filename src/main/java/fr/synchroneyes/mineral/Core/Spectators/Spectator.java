package fr.synchroneyes.mineral.Core.Spectators;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Utils.CircularList;
import fr.synchroneyes.mineral.mineralcontest;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Classe représentant un spectateur
 * Il est représenté par un joueur,
 * Il contient une liste de joueur à spectate
 * <p>
 * Il peut remplir la liste de joueurs à spectate en passant une équipe en paramètre
 * Il peut spectate un joueur en passant un joueur en paramètre
 */
public class Spectator {

    // Joueur spectateur
    @Getter
    private Player joueur;

    // Liste des joueurs que notre joueur peut regarder
    @Getter
    private CircularList<Player> joueurs_a_spectate;

    // Joueur actuellement regardé
    @Getter
    @Setter
    private Player current_spectated_player;


    // Permet de sauvegarder l'index du joueur actuellement regardé depuis notre liste circulaire
    @Getter
    @Setter
    private int current_spectated_player_index = 0;

    /**
     * Constructeur de la classe
     *
     * @param joueur le joueur a passer en spectateur
     */
    public Spectator(Player joueur) {
        this.joueur = joueur;
        this.joueurs_a_spectate = new CircularList<>();

        // Initialement, le joueur ne regarde personne
        this.current_spectated_player = null;
    }

    /**
     * Méthode permettant de remplir la liste des joueurs "spectatable" à partir d'une équipe donnée en argument
     *
     * @param equipe - L'équipe où il faut récuperer les joueurs
     */
    public void fillSpectatablePlayerList(Equipe equipe) {

        // On vide la liste actuelle
        this.clear();

        // Pour chaque joueur, si le joueur est différent du notre, on l'ajoute à la liste à spectate
        for (Player membre_equipe : equipe.getJoueurs())
            if (!membre_equipe.equals(this.joueur))
                joueurs_a_spectate.add(membre_equipe);
    }

    /**
     * Méthode permettant de remplir la liste des joueurs "spectatable" à partir d'une partie donnée en argument
     *
     * @param partie
     */
    public void fillSpectatablePlayerList(Game partie) {
        // On vide la liste actuelle
        this.clear();

        // Pour chaque joueur, si le joueur est différent du notre, on l'ajoute à la liste à spectate
        for (Player membre_partie : partie.groupe.getPlayers())
            if (!membre_partie.equals(this.joueur))
                joueurs_a_spectate.add(membre_partie);
    }

    /**
     * Méthode permettant de remplir la liste des joueurs "spectatable" à partir d'un groupe donnée en argument
     *
     * @param groupe
     */
    public void fillSpectatablePlayerList(Groupe groupe) {
        // On vide la liste actuelle
        this.clear();

        // Pour chaque joueur, si le joueur est différent du notre, on l'ajoute à la liste à spectate
        for (Player membre_partie : groupe.getPlayers())
            if (!membre_partie.equals(this.joueur))
                joueurs_a_spectate.add(membre_partie);
    }


    /**
     * Méthode permettant de vider la liste de joueur à spectate
     */
    public void clear() {
        this.joueurs_a_spectate.clear();
    }

    /**
     * Permet de spectate le prochain joueur
     */
    public void spectateNextPlayer() {


        if (joueurs_a_spectate.isEmpty()) return;

        if (current_spectated_player != null) {
            joueur.showPlayer(mineralcontest.plugin, current_spectated_player);
            current_spectated_player.showPlayer(mineralcontest.plugin, joueur);
        }


        joueur.setGameMode(GameMode.SPECTATOR);


        // On récupère l'index actuel et on l'incrémente
        int _current_index = this.current_spectated_player_index + 1;

        // On récupère le nouvel index
        this.current_spectated_player_index = mod(_current_index, joueurs_a_spectate.size());

        // On récupère le nouveau joueur à spectate
        this.current_spectated_player = joueurs_a_spectate.get(current_spectated_player_index);
    }

    /**
     * Permet de téléporter le joueur au joueur qu'il regarde
     */
    public void teleportPlayer() {
        // On vérifie si le joueur spectate quelqu'un
        // Si il spectate personne, On regarde si sa liste de spectate est vide ou non
        // Si la liste est vide, on ne fait rien
        // Sinon, on spectate le premier joueur de la liste
        if (current_spectated_player == null) {
            if (joueurs_a_spectate.isEmpty()) {
                joueur.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 2, 1));
                joueur.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 2, 1));
                return;
            }
            spectateNextPlayer();
        }


        current_spectated_player.hidePlayer(mineralcontest.plugin, joueur);
        joueur.hidePlayer(mineralcontest.plugin, current_spectated_player);

        Location teleportLocation = new Location(joueur.getWorld(), 0, 0, 0, current_spectated_player.getLocation().getYaw(), current_spectated_player.getLocation().getPitch());

        teleportLocation.setX(current_spectated_player.getLocation().getX());
        teleportLocation.setY(current_spectated_player.getLocation().getY());
        teleportLocation.setZ(current_spectated_player.getLocation().getZ());


        joueur.teleport(teleportLocation);
    }

    /**
     * Retourne le nombre de joueur où il est possible de spectate
     *
     * @return
     */
    public int getSpectatablePlayerCount() {
        return joueurs_a_spectate.size();
    }

    /**
     * Permet de spectate le joueur précedent
     */
    public void spectatePreviousPlayer() {

        if (joueurs_a_spectate.isEmpty()) return;

        if (current_spectated_player != null) {
            joueur.showPlayer(mineralcontest.plugin, current_spectated_player);
            current_spectated_player.showPlayer(mineralcontest.plugin, joueur);
        }

        joueur.setGameMode(GameMode.SPECTATOR);


        // On récupère l'index actuel et on l'incrémente
        int _current_index = this.current_spectated_player_index - 1;

        // On récupère le nouvel index
        this.current_spectated_player_index = mod(_current_index, joueurs_a_spectate.size());

        // On récupère le nouveau joueur à spectate
        this.current_spectated_player = joueurs_a_spectate.get(current_spectated_player_index);
    }


    /**
     * Permet d'effectuer un modulo avec une valeur négative
     *
     * @param valeur   - Le nombre à diviser
     * @param diviseur - Le diviseur
     * @return - Résultat
     */
    private int mod(int valeur, int diviseur) {
        int resultat = 0;
        if (valeur < 0)
            if (Math.abs(valeur) % diviseur != 0) resultat = diviseur - Math.abs(valeur) % diviseur;
            else resultat = Math.abs(valeur) % diviseur;
        else resultat = valeur % diviseur;

        return resultat;
    }


}
