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
    SCOREBOARD_PLAYER_NOT_READY("player_not_ready", ChatColor.WHITE + "" + ChatColor.LIGHT_PURPLE),
    SCOREBOARD_PLAYER_READY("player_ready", ChatColor.WHITE + "" + ChatColor.BLUE),
    SCOREBOARD_ADMINS("admins", ChatColor.WHITE + "" + ChatColor.DARK_AQUA),

    SCOREBOARD_TEAMNAME_TEXT("team_name_text", ChatColor.WHITE + "" + ChatColor.DARK_BLUE),
    SCOREBOARD_TEAMNAME_VALUE("team_name_value", ChatColor.RED + "" + ChatColor.DARK_BLUE),


    SCOREBOARD_TIMELEFT_TEXT("timeleft_text", ChatColor.WHITE + "" + ChatColor.DARK_GRAY),
    SCOREBOARD_TIMELEFT_VALUE("timeleft_value", ChatColor.RED + "" + ChatColor.DARK_GRAY),

    SCOREBOARD_TEAMSCORE_TEXT("teamscore_text", ChatColor.WHITE + "" + ChatColor.GREEN),
    SCOREBOARD_TEAMSCORE_VALUE("teamscore_value", ChatColor.RED + "" + ChatColor.GREEN),

    SCOREBOARD_PLAYERLOCATION_TEXT("playerloc_text", ChatColor.WHITE + "" + ChatColor.DARK_PURPLE),
    SCOREBOARD_PLAYERLOCATION_VALUE("playerloc_value", ChatColor.RED + "" + ChatColor.DARK_PURPLE),
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
