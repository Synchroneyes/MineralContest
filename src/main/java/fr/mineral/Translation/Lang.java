package fr.mineral.Translation;

import fr.mineral.Teams.Equipe;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public enum Lang {

    title("title", "%white%[%gold%Mineral%blue%Contest%white%]"),
    error("error", "[Erreur]"),
    global("global", "[Global]"),
    _private("private", "[Privé]"),
    admin("admin", " [Admin]"),
    error_when_resume("error_when_resume", "Impossible de reprendre la partie, elle n'est pas en pause ou une équipe n'est pas pleine"),
    game_already_started("game_already_started", "La partie a déjà commencé !"),
    all_team_not_full("all_team_not_full", "Au moins une équipe n'est pas complète. Il faut %teamNumber% joueurs par équipe."),
    not_enought_player_connected("not_enought_player_connected", "Il n'y a pas assez de joueurs connecté"),
    must_be_in_team("must_be_in_team","Vous devez être dans une équipe"),
    arena_not_defined("arena_not_defined", "L'arène n'est pas défini"),
    game_not_started("game_not_started", "La partie n'a pas encore commencé"),
    chest_not_defined("chest_not_defined", "Le coffre n'a pas encore été défini"),
    arena_spawnzone_not_added("arena_spawnzone_not_added", "La zone de spawn de l'arene n'est pas defini"),
    team_penalty("team_penalty", " point(s) de pénalité"),
    team_is_full("team_is_full", "L'équipe %coloredTeamName% est pleine"),
    team_chest_not_defined("chest_not_defined", "Le coffre de l'équipe %coloredTeamName% n'a pas encore été défini"),
    team_house_location_not_added("team_house_location_not_added", "Le spawn de l'équipe %coloredTeamName% n'a pas été ajouté"),
    team_house_location_added("team_house_location_added", "Le spawn de l'équipe %coloredTeamName% a pas été ajouté"),
    team_got_penality("team_got_penality", "L'équipe %coloredTeamName% a reçu %penality% points de pénalité"),
    team_got_penality_reseted("team_got_penality_reseted", "L'équipe %coloredTeamName% n'a plus de pénalité"),
    cant_teleport_player_without_team("cant_teleport_player_without_team", "Impossible de téléporter un joueur sans équipe"),
    vote_already_voted("vote_already_voted", "Vous avez déjà voté !"),
    vote_not_enabled("vote_not_enabled", "Les votes ne sont pas actif"),
    vote_selected_biome_doesnt_exist("vote_selected_biome_doesnt_exist", "Le biome demandé n'existe pas"),
    cant_break_block_here("cant_break_block_here", "Vous ne pouvez pas casser de bloc ici"),
    cant_interact_block_pre_game("cant_interact_block_pre_game", "Vous ne pouvez pas interagir avec des blocs avant le début d'une partie"),
    bad_map_loaded("bad_map_loaded", "Mauvaise map chargée, merci de télécharger la bonne map. Disponible sur le github"),
    github_link("github_link", "http://github.com/jaunefra/mineralcontest"),
    plugin_shutdown("plugin_shutdown", "Désactivation du plugin ..."),
    kick_game_already_in_progress("kick_game_already_in_progress", "Une partie est déjà en cours"),
    deathzone_spawn_location_added("deathzone_spawn_location_added", "Position de la deathzone ajoutée"),
    deathzone_spawn_location_undefined("deathzone_spawn_location_undefined", "La position de spawn de la deathzone n'est pas défini"),
    deathzone_you_are_dead("deathzone_you_are_dead", "Vous êtes mort"),
    deathzone_respawn_in("deathzone_respawn_in", "Vous allez réapparaitre dans %deathTime% secondes"),
    deathzone_respawned("deathzone_respawned", "De retour au combat !"),
    vote_you_voted_for("vote_you_voted_for", "Vous avez voté pour le biome %votedBiome%"),
    vote_winning_biome("vote_winning_biome", "Le biome selectionné est %winningBiome%"),
    vote_title("vote_title", "%gold%Vote pour le biome à jouer"),
    vote_count("vote_count", "Vote(s)"),
    vote_snow("vote_snow", "Neige"),
    vote_desert("vote_desert", "Desert"),
    vote_forest("vote_forest", "Foret"),
    vote_plain("vote_plain", "Plaine"),
    vote_mountain("vote_mountain", "Montagne"),
    vote_swamp("vote_swamp", "Marécage"),
    vote_started("vote_started", "Le vote a démarré ! Vous pouvez voter pour votre biome préféré avec la commande /vote <numero du biome>"),
    vote_ended("vote_ended", "Le vote est terminé"),
    vote_explain("vote_explain", "Ex: pour voter neige: /vote 0"),
    game_successfully_started("game_successfully_started", "La partie vient de commencer"),
    game_starting("game_starting", "La partie va démarrer"),
    game_resumed("game_resumed", "La partie a repris !"),
    game_already_paused("game_already_paused", "La partie est déjà en pause"),
    team_score("team_score", "Score de l'équipe %coloredTeamName%: %teamScore% points"),
    team_winning("team_winning", "L'équipe %coloredTeamName% remporte la partie avec %teamScore% points"),
    game_over("game_over", "La partie est terminée !"),
    player_killed("player_killed", "Le joueur %deadPlayer% a été tué par %killingPlayer%"),
    player_died("player_died", "Le joueur %deadPlayer% est mort"),
    hud_game_resumed("hud_game_resumed", "Go go go !"),
    hud_game_paused("hud_game_paused", "La partie est en pause"),
    hud_game_waiting_start("hud_game_waiting_start", "En attente du démarrage de la partie"),
    hud_game_starting("hud_game_starting", "La partie va démarrer dans %preGameTime% secondes"),
    hud_player_paused("hud_player_paused", "PAUSE !"),
    hud_player_resume_soon("hud_player_resume_soon", "La partie reprendra bientôt"),
    hud_admin_resume_help("hud_admin_resume_help", "Pour reprendre la partie, faites /resume"),
    hud_you_are_not_in_team("hud_you_are_not_in_team", "Vous n'êtes pas dans une équipe"),
    hud_team_name_no_score("hud_team_name_no_score", "%teamColor% Equipe %teamName%"),
    hud_team_name_score("hud_team_name_score", "%teamColor% Equipe %teamName% : %teamScore% points"),
    hud_time_left("hud_time_left", "Temps restant: %timeLeft%"),
    admin_played_tried_to_login("admin_played_tried_to_login", "Le joueur %playerName% a tenté de se connecter alors que la partie est déjà en cours"),
    admin_played_logged_in_pause_without_team("admin_played_logged_in_pause_without_team", "Le joueur %playerName% s'est connecté alors qu'il ne faisait pas partie d'une équipe"),
    admin_team_non_empty("admin_team_non_empty", "L'équipe %coloredTeamName% n'est pas pleine"),
    admin_switch_command_help("admin_switch_command_help", "Vous pouvez changer un joueur d'équipe avec la commande /switch  "),
    admin_team_will_be_randomized("admin_team_will_be_randomized", "Les équipes seront attribué de manière aléatoire. Pour changer ça, l'admin doit entrer la commande: mp_randomize_team 0"),
    arena_spawn_added("arena_spawn_added", "Le spawn pour l'arène a bien été ajouté"),
    arena_teleporting("arena_teleporting", "Téléportation vers l'arène"),
    arena_teleport_disabled("arena_teleport_disabled", "La téléportation de l'arène n'est pas active"),
    arena_now_teleporting("arena_now_teleporting", "Téléportation vers l'arène ..."),
    arena_chest_title("arena_chest_title", "Coffre d'arène !"),
    arena_chest_being_opened("arena_chest_being_opened", "Quelqu'un ouvre déjà le coffre"),
    arena_teleport_now_enabled("arena_teleport_now_enabled", "Vous pouvez vous téléporter vers l'arène avec la commande /arene"),
    arena_teleport_now_disabled("arena_teleport_now_disabled", "Il n'est plus possible de se téléporter vers l'arène"),
    arena_chest_added("arena_chest_added", "Le coffre d'arène a bien été ajouté"),
    arena_chest_spawned("arena_chest_spawned", "Le coffre d'arène vient d'apparaitre"),
    team_randomizer_begin("team_randomizer_begin", "Vous allez être attribué à une équipe aléatoire"),
    team_kicked("team_kicked", "Vous avez été retiré de votre équipe"),
    team_welcome("team_welcome", "Bienvenue dans l'équipe %coloredTeamName%"),
    team_score_now("team_score_now", "Votre score est maintenant de %teamScore% points"),
    team_chest_added("team_chest_added", "Le coffre de l'équipe %coloredTeamName% a été ajouté"),
    team_player_joined("team_player_joined", "Le joueur %playerName% a rejoint l'équipe %coloredTeamName%"),
    red_team("red_team", "Red"),
    yellow_team("yellow_team", "Yellow"),
    blue_team("blue_team", "Blue");


    private String path;
    private String def;
    private static YamlConfiguration LANG = new YamlConfiguration();
    public static String defaultLang = "fr_fr";


    /**
     * Lang enum constructor.
     * @param path The string path.
     * @param start The default string.
     */
    Lang(String path, String start) {
        this.path = path;
        this.def = start;

    }

    /**
     * Set the {@code YamlConfiguration} to use.
     * @param config The config to set.
     */
    public static void setFile(YamlConfiguration config) {
        LANG = config;
    }

    private Lang findByValue(String value) {
        for(Lang item : Lang.values()) {
            if(item.getDefault().equals(value)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public String toString() {

        //return get(findByValue(LANG.getString(this.path, def)).getPath());
        return translate((LANG.getString(this.path, def)));
        //return "toString" + LANG.getString(this.path, def);
    }

    public static String get(String key) {
        String result = "";
        try {
            result = translate(Lang.valueOf(key).getDefault());
        }catch(Exception e) {
            Bukkit.getLogger().severe("GET ERROR");
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Get the default value of the path.
     * @return The default value of the path.
     */
    public String getDefault() {
        return this.def;
    }

    /**
     * Get the path to the string.
     * @return The path to the string.
     */
    public String getPath() {
        return this.path;
    }



    public static String translate(String string, Equipe team, Player p) {
        string = translate(string, team);
        string =  translate(string, p);
        return string;
    }

    public static String translate(String string, Equipe team) {
        if(string.contains("%coloredTeamName%")) string = string.replace("%coloredTeamName%", team.getCouleur() + team.getNomEquipe() + ChatColor.WHITE);
        if(string.contains("%penality%")) string = string.replace("%penality", team.getPenalty() + "");
        if(string.contains("%teamScore%")) string = string.replace("%teamScore%", (team.getPenalty() != 0) ? ""  + team.getScore() + " (%red%" + team.getPenalty() + Lang.team_penalty.toString() + ")" : team.getScore() + " point(s)");
        if(string.contains("%teamColor%")) string = string.replace("%teamColor%", ""  + team.getCouleur());
        if(string.contains("%teamName%")) string = string.replace("%teamName%", team.getNomEquipe());
        string = translate(string);
        return  string;
    }

    public static String translate(String string, Player player) {
        if(string.contains("%deathTime%")) string = string.replace("%deathTime%", "" + mineralcontest.plugin.getGame().getArene().getDeathZone().getPlayerDeathTime(player));
        if(string.contains("%votedBiome%")) string = string.replace("%votedBiome%", mineralcontest.plugin.getGame().votemap.getPlayerVote(player));
        if(string.contains("%playerName%")) string = string.replace("%playerName%", player.getDisplayName());
        if(string.contains("%deadPlayer%") && !string.contains("%killingPlayer%"))
        if(string.contains("%deadPlayer%")) string = string.replace("%deadPlayer%", player.getDisplayName());

        string = translate(string);
        return string;

    }

    public static String translate(String string, Player player1, Player player2) {
        if(string.contains("%deadPlayer%")) string = string.replace("%deadPlayer%", player1.getDisplayName());
        if(string.contains("%killingPlayer%")) string = string.replace("%killingPlayer%", player2.getDisplayName());
        string = translate(string);
        return string;
    }

    public static String translate(String string) {
        if(string.contains("%black%")) string = string.replace("%black%", "" + ChatColor.BLACK);
        if(string.contains("%dark_blue%")) string = string.replace("%dark_blue%", "" + ChatColor.DARK_BLUE);
        if(string.contains("%dark_green%")) string = string.replace("%dark_green%", "" + ChatColor.DARK_GREEN);
        if(string.contains("%dark_aqua%")) string = string.replace("%dark_aqua%", "" + ChatColor.DARK_AQUA);
        if(string.contains("%dark_red%")) string = string.replace("%dark_red%", "" + ChatColor.DARK_RED);
        if(string.contains("%dark_purple%")) string = string.replace("%dark_purple%", "" + ChatColor.DARK_PURPLE);
        if(string.contains("%gold%")) string = string.replace("%gold%", "" + ChatColor.GOLD);
        if(string.contains("%gray%")) string = string.replace("%gray%", "" + ChatColor.GRAY);
        if(string.contains("%dark_gray%")) string = string.replace("%dark_gray%", "" + ChatColor.DARK_GRAY);
        if(string.contains("%blue%")) string = string.replace("%blue%", "" + ChatColor.BLUE);
        if(string.contains("%green%")) string = string.replace("%green%", "" + ChatColor.GREEN);
        if(string.contains("%aqua%")) string = string.replace("%aqua%", "" + ChatColor.AQUA);
        if(string.contains("%red%")) string = string.replace("%red%", "" + ChatColor.AQUA);
        if(string.contains("%light_purple%")) string = string.replace("%light_purple%", "" + ChatColor.LIGHT_PURPLE);
        if(string.contains("%yellow%")) string = string.replace("%yellow%", "" + ChatColor.YELLOW);
        if(string.contains("%white%")) string = string.replace("%white%", "" + ChatColor.WHITE);
        if(string.contains("%magic%")) string = string.replace("%magic%", "" + ChatColor.MAGIC);
        if(string.contains("%bold%")) string = string.replace("%bold%", "" + ChatColor.BOLD);
        if(string.contains("%strikethrough%")) string = string.replace("%strikethrough%", "" + ChatColor.STRIKETHROUGH);
        if(string.contains("%underline%")) string = string.replace("%underline%", "" + ChatColor.UNDERLINE);
        if(string.contains("%italic%")) string = string.replace("%italic%", "" + ChatColor.ITALIC);

        if(string.contains("%timeLeft%")) string = string.replace("%timeLeft%", mineralcontest.plugin.getGame().getTempsRestant());
        if(string.contains("%preGameTime%")) string = string.replace("%preGameTime%", "" + mineralcontest.plugin.getGame().PreGameTimeLeft);
        if(string.contains("%winningBiome%")) string = string.replace("%winningBiome%", mineralcontest.plugin.getGame().votemap.getWinnerBiome(false));
        if(string.contains("%teamNumber%")) string = string.replace("%teamNumber%", "" + mineralcontest.plugin.teamMaxPlayers);

        return string;
    }

}
