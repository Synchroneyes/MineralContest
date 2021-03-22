package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.custom_events.MCGameStartedEvent;
import fr.synchroneyes.custom_events.MCPlayerJoinEvent;
import fr.synchroneyes.custom_events.MCPlayerJoinTeamEvent;
import fr.synchroneyes.custom_events.MCWorldLoadedEvent;
import fr.synchroneyes.mineral.Scoreboard.newapi.ScoreboardAPI;
import fr.synchroneyes.mineral.Scoreboard.newapi.ScoreboardFields;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Score;

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
        ScoreboardAPI.createScoreboard(event.getPlayer());
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

        // Pour chaque joueur
        for(Player joueur : event.getGame().groupe.getPlayers()){
            ChatColor teamColor;
            ChatColor resetColor = ChatColor.RESET;
            int position = 16;
            boolean isPlayerReferee = event.getGame().isReferee(joueur);
            Equipe playerTeam = event.getGame().getPlayerTeam(joueur);

            if(event.getGame().isReferee(joueur)) {
                teamColor = ChatColor.GOLD;
            } else {
                teamColor = playerTeam.getCouleur();
            }

            // On remet à zéro les HUD
            ScoreboardAPI.clearScoreboard(joueur);

            // On ajoute la version du plugin
            ScoreboardAPI.addScoreboardText(joueur, ChatColor.GREEN + "v" + mineralcontest.plugin.getDescription().getVersion(), position--);

            ScoreboardAPI.addEmptyLine(joueur, position--);

            // On ajoute le temps restant
            ScoreboardAPI.registerNewObjective(joueur, ScoreboardFields.SCOREBOARD_TIMELEFT_TEXT, teamColor + Lang.hud_timeleft_text.toString(), position--);
            ScoreboardAPI.registerNewObjective(joueur, ScoreboardFields.SCOREBOARD_TIMELEFT_VALUE, event.getGame().getTempsRestant(), position--);

            ScoreboardAPI.addEmptyLine(joueur, position--);

            // On ajoute le nom de l'équipe
            ScoreboardAPI.registerNewObjective(joueur, ScoreboardFields.SCOREBOARD_TEAMNAME_TEXT, teamColor + Lang.hud_team_text.toString(), position--);
            ScoreboardAPI.registerNewObjective(joueur, ScoreboardFields.SCOREBOARD_TEAMNAME_VALUE, playerTeam.getNomEquipe(), position--);

            ScoreboardAPI.addEmptyLine(joueur, position--);

            // On ajoute le score de l'équipe
            String score = (playerTeam.getScore() >= 0) ? ChatColor.GREEN + "" + playerTeam.getScore() : ChatColor.RED + "" + playerTeam.getScore();
            ScoreboardAPI.registerNewObjective(joueur, ScoreboardFields.SCOREBOARD_TEAMSCORE_TEXT, teamColor + Lang.hud_score_text.toString(), position--);
            ScoreboardAPI.registerNewObjective(joueur, ScoreboardFields.SCOREBOARD_TEAMSCORE_VALUE, score, position--);


            ScoreboardAPI.addEmptyLine(joueur, position--);
            // ON affiche la position du joueur
            String position_joueur = teamColor + "X: " + resetColor + joueur.getLocation().getBlockX() + " " + teamColor + "Y:" + resetColor + joueur.getLocation().getBlockY() + teamColor + " Z:" + resetColor + joueur.getLocation().getBlockZ();
            ScoreboardAPI.registerNewObjective(joueur, ScoreboardFields.SCOREBOARD_PLAYERLOCATION_VALUE, position_joueur, position--);
        }
    }

}
