package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.custom_events.MCPlayerJoinEvent;
import fr.synchroneyes.custom_events.MCWorldLoadedEvent;
import fr.synchroneyes.mineral.Scoreboard.newapi.ScoreboardAPI;
import fr.synchroneyes.mineral.Scoreboard.newapi.ScoreboardFields;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Classe gérant les HUD des joueurs
 */
public class PlayerHUDEvents implements Listener {

    @EventHandler
    public void onPlayerJoinPlugin(MCPlayerJoinEvent event) {
        // On lui ajoute son HUD
        ScoreboardAPI.createScoreboard(event.getPlayer());
    }

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



}
