package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.custom_events.*;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.House;
import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.Kits.KitAbstract;
import fr.synchroneyes.mineral.Scoreboard.newapi.ScoreboardAPI;
import fr.synchroneyes.mineral.Scoreboard.newapi.ScoreboardFields;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.*;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Classe gérant les HUD des joueurs
 */
public class PlayerHUDEvents implements Listener {

    /**
     * Création du HUD du joueur lorsqu'il rejoint le plugin
     * @param event
     */
    @EventHandler
    public void onPlayerJoinPlugin(MCPlayerJoinEvent event) {

        // On lui ajoute son HUD
        Game partie = event.getMcPlayer().getPartie();
        if(partie == null || partie.isGameStarted()) {


            if(event.getMcPlayer().getEquipe() != null) {
                setPlayerInGameHUD(event.getMcPlayer());
                return;
            }

            Player joueur = event.getPlayer();
            int position = 16;

            ScoreboardAPI.createScoreboard(event.getPlayer(), false);
            ScoreboardAPI.clearScoreboard(event.getPlayer());
            ScoreboardAPI.addScoreboardText(joueur, ChatColor.GREEN + "v" + mineralcontest.plugin.getDescription().getVersion(), position--);
            ScoreboardAPI.addEmptyLine(joueur, position--);
            ScoreboardAPI.addScoreboardText(joueur, "Temps restant", position--);
            ScoreboardAPI.registerNewObjective(joueur, ScoreboardFields.SCOREBOARD_TIMELEFT_VALUE, "", position--);
            ScoreboardAPI.addEmptyLine(joueur, position--);
            ScoreboardAPI.addScoreboardText(joueur, "Spectateur", position--);



            return;
        }

        ScoreboardAPI.createScoreboard(event.getPlayer(), false);
    }

    /**
     * Event appelé lorsqu'un joueur se reconnecte au plugin. On lui remet son HUD
     * @param event
     */
    @EventHandler
    public void onPlayerReconnect(MCPlayerReconnectEvent event) {
        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> setPlayerInGameHUD(event.getPlayer()), 2);
    }

    /**
     * Mise à jour du HUD des joueurs lorsque le monde sur lequel ils jouent est chargé
     * @param event
     */
    @EventHandler
    public void onGroupWorldLoaded(MCWorldLoadedEvent event) {
        // On clear le HUD de tous les joueurs
        for(Player membre_groupe : event.getGroupe().getPlayers()) {
            ScoreboardAPI.clearScoreboard(membre_groupe);

            int position = 16;
            // Ajout de l'état du joueur
            ScoreboardAPI.addScoreboardText(membre_groupe, ChatColor.GOLD + Lang.hud_current_game_state.toString(), position--);
            ScoreboardAPI.registerNewObjective(membre_groupe, ScoreboardFields.SCOREBOARD_PLAYER_READY, ScoreboardAPI.prefix + ChatColor.RED + Lang.not_ready_tag, position--);

            ScoreboardAPI.addEmptyLine(membre_groupe, position--);

            // Ajout du nom de l'équipe
            ScoreboardAPI.registerNewObjective(membre_groupe, ScoreboardFields.SCOREBOARD_TEAMNAME_TEXT, ChatColor.GOLD + Lang.hud_team_text.toString(), position--);

            // Gestion du arbitre ou non
            if(event.getGroupe().getGame().isReferee(membre_groupe)) ScoreboardAPI.registerNewObjective(membre_groupe, ScoreboardFields.SCOREBOARD_TEAMNAME_VALUE,ScoreboardAPI.prefix + Lang.hud_referee_text.toString(), position--);
            else ScoreboardAPI.registerNewObjective(membre_groupe, ScoreboardFields.SCOREBOARD_TEAMNAME_VALUE,ScoreboardAPI.prefix + Lang.hud_you_are_not_in_team.toString(), position--);



        }
    }


    /**
     * Mise à jour du HUD du joueur lorsqu'i rejoint une équipe
     * @param event
     */
    @EventHandler
    public void onPlayerJoinTeam(MCPlayerJoinTeamEvent event){
        Player player = event.getMcPlayer().getJoueur();
        Equipe equipe = event.getJoinedTeam();

        ChatColor teamColor = event.getJoinedTeam().getCouleur();
        ChatColor resetColor = ChatColor.RESET;


        // Mise à jour du nom
        ScoreboardAPI.updateField(player, ScoreboardFields.SCOREBOARD_TEAMNAME_TEXT, teamColor + Lang.hud_team_text.toString());
        ScoreboardAPI.updateField(player, ScoreboardFields.SCOREBOARD_TEAMNAME_VALUE, equipe.getNomEquipe());

        // Mise à jour du score
        String score = (equipe.getScore() >= 0) ? ChatColor.GREEN + "" + equipe.getScore() : ChatColor.RED + "" + equipe.getScore();
        ScoreboardAPI.updateField(player, ScoreboardFields.SCOREBOARD_TEAMSCORE_TEXT, teamColor + Lang.hud_score_text.toString());
        ScoreboardAPI.updateField(player, ScoreboardFields.SCOREBOARD_TEAMSCORE_VALUE, score);

        // Mise à jour du temps
        ScoreboardAPI.updateField(player, ScoreboardFields.SCOREBOARD_TIMELEFT_TEXT, teamColor + Lang.hud_timeleft_text.toString());


        // Mise à jour de la position
        String position = teamColor + "X: " + resetColor + player.getLocation().getBlockX() + " " + teamColor + "Y:" + resetColor + player.getLocation().getBlockY() + teamColor + " Z:" + resetColor + player.getLocation().getBlockZ();
        ScoreboardAPI.updateField(player, ScoreboardFields.SCOREBOARD_PLAYERLOCATION_VALUE, position);
    }


    /**
     * Mise à jour du HUD lorsqu'une game démarre
     * @param event
     */
    @EventHandler
    public void onGameStart(MCGameStartedEvent event) {


        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
            for(Player joueur : event.getGame().groupe.getPlayers()){
                // On envoie le HUD normal si le joueur n'est pas arbitre
                if(!event.getGame().isReferee(joueur)) setPlayerInGameHUD(mineralcontest.plugin.getMCPlayer(joueur));
                    // Sinon, on envoie le HUD arbitre
                else setPlayerRefereeHUD(mineralcontest.plugin.getMCPlayer(joueur));
            }
        }, 1);


    }


    /**
     * Mise à jour du score quand le score change (merci la logique de ce commentaire)
     * @param mcTeamScoreUpdated
     */
    @EventHandler
    public void onScoreUpdated(MCTeamScoreUpdated mcTeamScoreUpdated) {



        // On récupère les membres de l'équipe
        for(Player player : mcTeamScoreUpdated.getEquipe().getJoueurs()){
            // Pour chaque joueur, on update le score
            ScoreboardAPI.updateField(player, ScoreboardFields.SCOREBOARD_TEAMSCORE_VALUE, mcTeamScoreUpdated.getEquipe().getFormattedScore(mcTeamScoreUpdated.getNewScore()));
        }


        // On retarde l'execution de cette méthode
        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
            // On update le HUD des arbitres
            for(Player joueur : mcTeamScoreUpdated.getEquipe().getPartie().getReferees()) {
                setPlayerRefereeHUD(mineralcontest.plugin.getMCPlayer(joueur));
                joueur.sendMessage("HUD arbitre updated");
            }
        }, 1);

    }

    /**
     * Mise à jour du temps restant lors d'un tick d'un game
     * @param event
     */
    @EventHandler
    public void onGameTick(MCGameTickEvent event) {
        // Pour chaque joueur de la game
        for(Player joueur : event.getGame().groupe.getPlayers()){
            // On met à jour le temps
            ScoreboardAPI.updateField(joueur, ScoreboardFields.SCOREBOARD_TIMELEFT_VALUE, event.getGame().getTempsRestant());
        }
    }

    /**
     * Evenemnt appelé lorsqu'un joueur change de monde
     * @param event
     */
    @EventHandler
    public void onWorldChangeEvent(MCPlayerWorldChangeEvent event){
        // Si la destination n'est pas un monde mineralcntest, on vire son hud
        if(!mineralcontest.isAMineralContestWorld(event.getToWorld())){ event.getPlayer().setScoreboard(null); return;}

        // Si il rejoint le lobby, on lui remet son HUD par défaut
        if(mineralcontest.plugin.pluginWorld.equals(event.getToWorld())) {
            ScoreboardAPI.createScoreboard(event.getPlayer(), true);
            return;
        }

        // Si il rejoint un monde, on lui applique le HUD de jeu
        MCPlayer mcPlayer = mineralcontest.plugin.getMCPlayer(event.getPlayer());
        if(mcPlayer.getPartie().isPlayerReady(event.getPlayer())) setPlayerRefereeHUD(mcPlayer);
        else setPlayerInGameHUD(mcPlayer);


    }


    /**
     * Evenemnt appelé lorsqu'un joueur devient arbitre. ON lui applique le HUD Arbitre
     * @param event
     */
    @EventHandler
    public void onPlayerBecomesReferee(MCPlayerBecomeRefereeEvent event) {
        if(event.getPlayer().getPartie().isGameStarted()) setPlayerRefereeHUD(event.getPlayer());


    }

    /**
     * Evenemnt appelé lorsqu'un joueur quitte l'arbitrage. ON lui applique le HUD de jeu
     * @param event
     */
    @EventHandler
    public void onPlayerQuitReferee(MCPlayerQuitRefereeEvent event) {
        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> setPlayerInGameHUD(event.getPlayer()), 1);
    }

    /**
     * Event appelé lorsqu'un joueur selectionne un kit
     * @param event
     */
    @EventHandler
    public void onPlayerKitSelected(PlayerKitSelectedEvent event){
        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
            Player joueur = event.getPlayer();
            ScoreboardAPI.updateField(joueur, ScoreboardFields.SCOREBOARD_KIT_VALUE, event.getSelectedKit().getNom());
        }, 1);
    }


    /**
     * Méthode utilisée pour appliquer le HUD de jeu à un joueur
     * @param player
     */
    public static void setPlayerInGameHUD(MCPlayer player) {

        // Pour chaque joueur
        Game game = player.getPartie();
        Player joueur = player.getJoueur();
        ChatColor teamColor = ChatColor.GOLD;
        ChatColor resetColor = ChatColor.RESET;
        int position = 16;
        String playerTeamName = "Arbitre";
        boolean isPlayerReferee = game.isReferee(player.getJoueur());
        Equipe playerTeam = game.getPlayerTeam(player.getJoueur());

        if(!game.isGameStarted()) {
            ScoreboardAPI.createScoreboard(player.getJoueur(), true);
            return;
        }


        if(isPlayerReferee) {
            teamColor = ChatColor.GOLD;
        } else {
            if(playerTeam != null){
                teamColor = playerTeam.getCouleur();
                playerTeamName = playerTeam.getNomEquipe();
            }
        }

        // On remet à zéro les HUD
        ScoreboardAPI.clearScoreboard(joueur);

        // On ajoute la version du plugin
        ScoreboardAPI.addScoreboardText(joueur, ChatColor.GREEN + "v" + mineralcontest.plugin.getDescription().getVersion(), position--);

        ScoreboardAPI.addEmptyLine(joueur, position--);

        // On ajoute le temps restant
        ScoreboardAPI.registerNewObjective(joueur, ScoreboardFields.SCOREBOARD_TIMELEFT_TEXT, teamColor + Lang.hud_timeleft_text.toString(), position--);
        ScoreboardAPI.registerNewObjective(joueur, ScoreboardFields.SCOREBOARD_TIMELEFT_VALUE, game.getTempsRestant(), position--);

        ScoreboardAPI.addEmptyLine(joueur, position--);

        // On ajoute le nom de l'équipe
        ScoreboardAPI.registerNewObjective(joueur, ScoreboardFields.SCOREBOARD_TEAMNAME_TEXT, teamColor + Lang.hud_team_text.toString(), position--);
        ScoreboardAPI.registerNewObjective(joueur, ScoreboardFields.SCOREBOARD_TEAMNAME_VALUE, playerTeamName, position--);

        ScoreboardAPI.addEmptyLine(joueur, position--);

        if(!isPlayerReferee){
            // On ajoute le score de l'équipe
            String score = playerTeam.getFormattedScore();
            ScoreboardAPI.registerNewObjective(joueur, ScoreboardFields.SCOREBOARD_TEAMSCORE_TEXT, teamColor + Lang.hud_score_text.toString(), position--);
            ScoreboardAPI.registerNewObjective(joueur, ScoreboardFields.SCOREBOARD_TEAMSCORE_VALUE, score, position--);
            ScoreboardAPI.addEmptyLine(joueur, position--);

        }

        // On affiche le kit du joueur
        if(game.groupe.getKitManager().isKitsEnabled()) {
            KitAbstract playerKit = game.groupe.getKitManager().getPlayerKit(joueur);
            if(playerKit != null) {
                ScoreboardAPI.registerNewObjective(joueur, ScoreboardFields.SCOREBOARD_KIT_NAME, teamColor + "Kit", position--);
                ScoreboardAPI.registerNewObjective(joueur, ScoreboardFields.SCOREBOARD_KIT_VALUE, playerKit.getNom(), position--);

                ScoreboardAPI.addEmptyLine(joueur, position--);
            }

        }



        // ON affiche la position du joueur
        String position_joueur = teamColor + "X: " + resetColor + joueur.getLocation().getBlockX() + " " + teamColor + "Y: " + resetColor + joueur.getLocation().getBlockY() + teamColor + " Z: " + resetColor + joueur.getLocation().getBlockZ();
        ScoreboardAPI.registerNewObjective(joueur, ScoreboardFields.SCOREBOARD_PLAYERLOCATION_VALUE, position_joueur, position--);

    }

    /**
     * Applique le HUD d'arbitre à un joueur
     * @param player
     */
    public static void setPlayerRefereeHUD(MCPlayer player) {

        Player joueur = player.getJoueur();

        int position = 16;

        // ON commence par vider son HUD
        ScoreboardAPI.clearScoreboard(player.getJoueur());

        // On ajoute la version du plugin
        ScoreboardAPI.addScoreboardText(joueur, ChatColor.GREEN + "v" + mineralcontest.plugin.getDescription().getVersion(), position--);

        ScoreboardAPI.addEmptyLine(joueur, position--);

        // On ajoute le temps restant
        ScoreboardAPI.registerNewObjective(joueur, ScoreboardFields.SCOREBOARD_TIMELEFT_TEXT, ChatColor.GOLD + Lang.hud_timeleft_text.toString(), position--);
        ScoreboardAPI.registerNewObjective(joueur, ScoreboardFields.SCOREBOARD_TIMELEFT_VALUE, player.getPartie().getTempsRestant(), position--);


        // On ajoute le score de chaque équipe
        for(House maisons : player.getPartie().getHouses()){
            if(maisons.getTeam().getJoueurs().isEmpty()) continue;

            ScoreboardAPI.addEmptyLine(joueur, position--);
            String teamScore = maisons.getTeam().getFormattedScore();
            ScoreboardAPI.addScoreboardText(joueur,maisons.getTeam().getCouleur() + maisons.getTeam().getNomEquipe(), position--);
            ScoreboardAPI.addScoreboardText(joueur, teamScore, position--);

        }
    }



}
