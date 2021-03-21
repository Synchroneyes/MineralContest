package fr.synchroneyes.mineral.Scoreboard.newapi;

import org.bukkit.ChatColor;

/**
 * Classe listant les champs disponible pour un scoreboard
 */
public enum ScoreboardFields {
    SCOREBOARD_TITLE("title", ChatColor.WHITE + "" + ChatColor.YELLOW),
    SCOREBOARD_PLUGIN_VERSION("plugin_version", ChatColor.WHITE + "" + ChatColor.RED),
    SCOREBOARD_PLAYER_COUNT("player_count", ChatColor.WHITE + "" + ChatColor.GOLD),
    SCOREBOARD_GROUP_STATE("group_state", ChatColor.WHITE + "" + ChatColor.BLACK),
    SCOREBOARD_NON_READY("non_ready", ChatColor.WHITE + "" + ChatColor.BLUE),
    SCOREBOARD_ADMINS("admins", ChatColor.WHITE + "" + ChatColor.DARK_AQUA),
    SCOREBOARD_TEAMNAME("team_name", ChatColor.WHITE + "" + ChatColor.DARK_BLUE),
    SCOREBOARD_TIMELEFT("timeleft", ChatColor.WHITE + "" + ChatColor.DARK_GRAY),
    SCOREBOARD_TEAMSCORE("teamscore", ChatColor.WHITE + "" + ChatColor.DARK_GREEN),
    SCOREBOARD_PLAYERLOCATION("playerlocation", ChatColor.WHITE + "" + ChatColor.DARK_PURPLE)
    ;


    private String nom;
    private String uniqueColor;

    ScoreboardFields(String nom, String uniqueColor) {
        this.nom = nom;
        this.uniqueColor = uniqueColor;
    }

    @Override
    public String toString() {
        return nom;
    }

    public String getUniqueColor() {
        return uniqueColor;
    }
}
