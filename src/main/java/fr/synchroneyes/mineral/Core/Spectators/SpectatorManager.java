package fr.synchroneyes.mineral.Core.Spectators;


import fr.synchroneyes.custom_events.MCPlayerRespawnEvent;
import fr.synchroneyes.custom_events.PlayerDeathByPlayerEvent;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Utils.Player.PlayerUtils;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Classe permettant de gérer les spectateurs
 */
public class SpectatorManager implements Listener {

    // Liste des joueurs actuellement spectateurs
    private Queue<Spectator> spectateurs;

    // Variable stockant la boucle de gestion de spectateur
    private BukkitTask boucle;

    // Partie dans laquelle on doit gérer les spectateurs
    private Game partie;

    // Nombre de tick entre chaque tour de boucle
    private int delayBoucle = 1;

    public SpectatorManager(Game partie) {
        this.spectateurs = new LinkedBlockingQueue<>();

        // On enregistre nos evenements
        Bukkit.getPluginManager().registerEvents(this, mineralcontest.plugin);
    }

    /**
     * Méthode permettant de mettre un joueur comme spectateur
     *
     * @param joueur
     */
    public void ajouterSpectateur(Player joueur) {

        // On commence par récuperer sa partie
        Game partie = mineralcontest.getPlayerGame(joueur);

        // Si sa partie est nulle, on ne peut pas l'ajouter
        if (partie == null) return;


        // On crée l'instance de spectate
        Spectator spectator = new Spectator(joueur);

        // On vérifie si le joueur est déjà spectateur
        // Si c'est le cas, on ne l'ajoute pas à nouveau
        if (isPlayerSpectator(joueur)) return;


        // On récupère l'équipe du joueur si il en a une
        Equipe playerTeam = partie.getPlayerTeam(joueur);

        // Si il n'a pas d'équipe, on le fait spectate toute la game
        if (playerTeam == null) spectator.fillSpectatablePlayerList(partie);

            // Sinon, il peut spectate son équipe
        else spectator.fillSpectatablePlayerList(playerTeam);

        // On l'ajoute à notre liste de spectateurs
        this.spectateurs.add(spectator);

    }

    /**
     * On supprime notre joueur de la liste des spectateurs
     *
     * @param joueur
     */
    public void supprimerSpectateur(Player joueur) {


        // On récupère l'instance de spectateur du joueur passé en paramètre
        // Et on le supprime
        Spectator playerSpectator = null;
        for (Spectator spectator : spectateurs)
            if (spectator.getJoueur().equals(joueur)) playerSpectator = spectator;

        // On le supprime
        if (playerSpectator != null) spectateurs.remove(playerSpectator);

        if (spectateurs.isEmpty()) {
            if (boucle != null) boucle.cancel();
            if (boucle != null) boucle = null;
        }
    }

    /**
     * Permet de retourner si oui ou non un joueur est spectateur de la partie
     *
     * @param player
     * @return
     */
    public boolean isPlayerSpectator(Player player) {

        // On regarde chaque spectateur si c'est notre joueur ou non
        for (Spectator spectator : spectateurs)
            if (spectator.getJoueur().equals(player) && spectator.getCurrent_spectated_player() != null) return true;
        return false;
    }

    /**
     * Permet de récuperer l'instance de spectateur du joueur
     *
     * @param joueur
     * @return
     */
    public Spectator getSpectator(Player joueur) {
        if (!isPlayerSpectator(joueur)) return null;
        for (Spectator spectator : spectateurs)
            if (spectator.getJoueur().equals(joueur)) return spectator;
        return null;
    }


    /**
     * Fonction permettant de faire spectate les joueurs
     */
    public void startSpectateLoop() {
        if (boucle != null) {
            boucle.cancel();
            boucle = null;
        }

        // On démarre notre boucle
        boucle = Bukkit.getScheduler().runTaskTimer(mineralcontest.plugin, this::doSpectateTick, 0, delayBoucle);
    }

    /**
     * Fonction contenant toutes les verifs à faire lors d'un tour de boucle
     */
    private void doSpectateTick() {
        // Pour chaque spectateurs
        for (Spectator spectator : spectateurs)
            // On TP le spectateur au joueur qu'il regarde
            spectator.teleportPlayer();
    }


    /**
     * Evenement appelé lors de l'interaction d'un joueur
     *
     * @param event
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player joueur = event.getPlayer();

        // On vérifie que le joueur fait parti du plugin
        if (!mineralcontest.isInAMineralContestWorld(joueur)) return;

        // Si il n'est pas spectateur, on ne fait rien
        if (!isPlayerSpectator(joueur)) return;

        // On annule l'event
        event.setCancelled(true);

        // Et en fonction du type de clic, on effectue une action
        Spectator spectator = getSpectator(joueur);

        // Clic gauche => Joueur suivant
        // Clic droit => joueur précedent
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
            spectator.spectateNextPlayer();
        else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
            spectator.spectatePreviousPlayer();
    }

    /**
     * Evenement appelé lors du déplacement d'un joueur
     *
     * @param event
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        Player joueur = event.getPlayer();

        // On vérifie que le joueur fait parti du plugin
        if (!mineralcontest.isInAMineralContestWorld(joueur)) return;

        // Si il n'est pas spectateur, on ne fait rien
        if (!isPlayerSpectator(joueur)) return;

        // Si il est spectateur, on annule l'event
        event.setCancelled(true);


    }


    /**
     * Evenement appelé lors de la mort d'un joueur
     *
     * @param event
     */
    @EventHandler
    public void OnPlayerDeath(PlayerDeathByPlayerEvent event) {

        Player joueur = event.getPlayerDead();

        // On vérifie que le joueur fait parti du plugin
        if (!mineralcontest.isInAMineralContestWorld(joueur)) return;

        // Si il est déjà spectateur, on ne fait rien
        if (isPlayerSpectator(joueur)) return;

        if (boucle == null) startSpectateLoop();

        // SI il n'est pas dans une partie ou que la partie n'est pas en cours, on s'arrête
        Game playerGame = mineralcontest.getPlayerGame(joueur);

        if (playerGame == null || !playerGame.isGameStarted()) return;


        // On rend le jouuer invisible par les autres
        for (Player membre_partie : playerGame.groupe.getPlayers())
            membre_partie.hidePlayer(mineralcontest.plugin, joueur);

        // Sinon, on ajoute le joueur à la liste des spectateurs
        ajouterSpectateur(joueur);

    }

    /**
     * Evenemement appelé lors du respawn d'un joueur
     *
     * @param event
     */
    @EventHandler
    public void OnPlayerRespawn(MCPlayerRespawnEvent event) {
        Player joueur = event.getJoueur();

        // Sinon, on ajoute le joueur à la liste des spectateurs
        supprimerSpectateur(joueur);

        // On rend le joueur visible de tous si il n'est pas arbitre
        Game partie = mineralcontest.getPlayerGame(joueur);
        if (!partie.isReferee(joueur)) {
            for (Player membre_partie : partie.groupe.getPlayers()) {
                membre_partie.showPlayer(mineralcontest.plugin, joueur);
                joueur.showPlayer(mineralcontest.plugin, membre_partie);
            }

            Equipe playerTeam = partie.getPlayerTeam(joueur);

            if (playerTeam != null)
                PlayerUtils.teleportPlayer(joueur, playerTeam.getMaison().getHouseLocation().getWorld(), playerTeam.getMaison().getHouseLocation());
        }


        joueur.setGameMode(GameMode.SURVIVAL);

        // On regarde si il reste des spectateurs, si il n'y en a pas, on arrête la boucle
        if (spectateurs.isEmpty()) {
            if (boucle != null) boucle.cancel();
            boucle = null;
        }

    }

}
