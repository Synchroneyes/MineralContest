package fr.synchroneyes.mineral.Translation;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Utils.ErrorReporting.Error;
import fr.synchroneyes.mineral.Utils.Log.GameLogger;
import fr.synchroneyes.mineral.Utils.Log.Log;
import fr.synchroneyes.mineral.Utils.TimeConverter;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;

public enum Lang {

    title("title", "%white%[%gold%Mineral%blue%Contest%white%]"),
    cvar_diamond_score("cvar_diamond_score", "Le score du diamand est maintenant de"),
    cvar_iron_score("cvar_iron_score", "Le score du fer est maintenant de "),
    cvar_emerald_score("cvar_emerald_score", "Le score de l'émeraude est maintenant de "),
    cvar_gold_score("cvar_gold_score", "Le score de l'or est maintenant de "),
    error("error", "[Erreur]"),
    global("global", "[Global]"),
    _private("private", "[Privé]"),
    admin("admin", " [Admin]"),
    web("web", "[Web]"),
    group("group", "[Groupe]"),
    error_when_resume("error_when_resume", "Impossible de reprendre la partie, elle n'est pas en pause ou une équipe n'est pas pleine"),
    game_already_started("game_already_started", "La partie a déjà commencé !"),
    not_enought_player_connected("not_enought_player_connected", "Il n'y a pas assez de joueurs connecté"),
    must_be_in_team("must_be_in_team", "Vous devez être dans une équipe"),
    arena_not_defined("arena_not_defined", "L'arène n'est pas défini"),
    game_not_started("game_not_started", "La partie n'a pas encore commencé"),
    chest_not_defined("chest_not_defined", "Le coffre n'a pas encore été défini"),
    arena_spawnzone_not_added("arena_spawnzone_not_added", "La zone de spawn de l'arene n'est pas defini"),
    team_penalty("team_penalty", " point(s) de pénalité"),
    team_is_full("team_is_full", "L'équipe %coloredTeamName% est pleine"),
    team_chest_not_defined("chest_not_defined", "Le coffre de l'équipe %coloredTeamName% n'a pas encore été défini"),
    team_house_location_not_added("team_house_location_not_added", "Le spawn de l'équipe %coloredTeamName% n'a pas été ajouté"),
    team_house_location_added("team_house_location_added", "Le spawn de l'équipe %coloredTeamName% a été ajouté"),
    team_got_penality("team_got_penality", "L'équipe %coloredTeamName% possède %penality% points de pénalité"),
    team_got_penality_reseted("team_got_penality_reseted", "L'équipe %coloredTeamName% n'a plus de pénalité"),
    cant_teleport_player_without_team("cant_teleport_player_without_team", "Impossible de téléporter un joueur sans équipe"),
    vote_map("vote_map", "Voter pour la carte: %map%"),
    currently_no_vote("currently_no_vote", "Personne n'a voté"),
    vote_already_voted("vote_already_voted", "Vous avez déjà voté !"),
    vote_not_enabled("vote_not_enabled", "Les votes ne sont pas actif"),
    vote_selected_biome_doesnt_exist("vote_selected_biome_doesnt_exist", "Le biome demandé n'existe pas"),
    vote_already_started("vote_already_started", "Le vote est déjà démarré"),
    cant_break_block_here("cant_break_block_here", "Vous ne pouvez pas casser de bloc ici"),
    cant_interact_block_pre_game("cant_interact_block_pre_game", "Vous ne pouvez pas interagir avec des blocs avant le début d'une partie"),
    cant_interact_block_hub("cant_interact_block_hub", "Vous ne pouvez pas intéragir avec des blocs ici"),
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
    vote_you_voted_for_map("vote_you_voted_for_map", "Vous avez voté pour la carte: %map%"),
    vote_winning_biome("vote_winning_biome", "Le biome selectionné est %winningBiome%"),
    vote_title("vote_title", "%gold%Vote pour le biome à jouer"),
    vote_count("vote_count", "Vote(s)"),
    vote_snow("vote_snow", "Neige"),
    vote_desert("vote_desert", "Desert"),
    vote_forest("vote_forest", "Foret"),
    vote_plain("vote_plain", "Plaine"),
    vote_mountain("vote_mountain", "Montagne"),
    vote_swamp("vote_swamp", "Marécage"),
    vote_started("vote_started", "Le vote a démarré ! Vous pouvez voter pour votre biome préféré avec la commande /vote <numero du monde>"),
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
    hud_game_starting("hud_game_starting", "Démarrage dans %preGameTime% secondes"),
    hud_player_paused("hud_player_paused", "PAUSE !"),
    hud_player_resume_soon("hud_player_resume_soon", "La partie reprendra bientôt"),
    hud_admin_resume_help("hud_admin_resume_help", "Pour reprendre la partie, faites /resume"),
    hud_you_are_not_in_team("hud_you_are_not_in_team", "Vous n'êtes pas dans une équipe"),
    hud_team_name_no_score("hud_team_name_no_score", "%teamColor% Equipe %teamName%"),
    hud_team_name_score("hud_team_name_score", "%teamColor% Equipe %teamName% : %teamScore% points"),
    hud_time_left("hud_time_left", "Temps restant: %timeLeft%"),
    hud_awaiting_players("hud_awaiting_players", "%onlinePlayers%/%requiredPlayers% joueurs connectés"),
    admin_played_tried_to_login("admin_played_tried_to_login", "Le joueur %playerName% a tenté de se connecter alors que la partie est déjà en cours. Pour le laisser se connecter, faites pause puis attribuez lui une equipe avec la commande /switch <pseudo> <equipe>"),
    admin_played_logged_in_pause_without_team("admin_played_logged_in_pause_without_team", "Le joueur %playerName% s'est connecté alors qu'il ne faisait pas partie d'une équipe"),
    admin_team_non_empty("admin_team_non_empty", "L'équipe %coloredTeamName% n'est pas pleine"),
    admin_switch_command_help("admin_switch_command_help", "Vous pouvez changer un joueur d'équipe avec la commande /switch  "),
    admin_team_will_be_randomized("admin_team_will_be_randomized", "Les équipes seront attribué de manière aléatoire. Pour changer ça, l'admin doit entrer la commande: mp_randomize_team 0"),
    arena_spawn_added("arena_spawn_added", "Le spawn pour l'arène a bien été ajouté"),
    arena_teleporting("arena_teleporting", "Téléportation vers l'arène"),
    arena_teleport_disabled("arena_teleport_disabled", "La téléportation de l'arène n'est pas active"),
    arena_now_teleporting("arena_now_teleporting", "Téléportation vers l'arène ..."),
    arena_chest_title("arena_chest_title", "%gold%Coffre d'arène !"),
    arena_chest_title_being_opened("arena_chest_title", "%red%Ouverture en cours!"),
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
    blue_team("blue_team", "Blue"),
    no_longer_referee("no_longer_referee", "Vous n'êtes plus arbitre !"),
    now_referee("now_referee", "Vous êtes désormais arbitre de la partie"),
    cant_remove_admin_game_in_progress("cant_remove_admin_game_in_progress", "Une game est en cours, impossible de se retirer des arbitres"),
    referee_item_name("referee_item_name", "Livre d'arbitrage"),
    referee_item_resume("referee_item_resume", "Reprendre la partie"),
    referee_item_start("referee_item_start", "Démarrer la partie"),
    referee_item_pause("referee_item_pause", "Mettre la partie en pause"),
    referee_item_leaderboard("referee_item_leaderboard", "Tableau des scores"),
    referee_item_spawn_arena_chest("referee_item_spawn_arena_chest", "Faire apparaître le coffre d'arène"),
    referee_item_spawn_drop_chest("referee_item_spawn_drop_chest", "Faire apparaitre un largage"),
    referee_item_start_vote("referee_item_start_vote", "Démarrer le vote"),
    referee_item_force_biome("referee_item_force_biome", "Forcer le biome"),
    referee_will_now_select_biome("referee_will_now_select_biome", "l'arbitre va désormais choisir le biome à jouer"),
    referee_action_not_available("referee_action_not_available", "Cette action n'est pas disponible pour le moment"),
    vote_required_to_start_game("vote_required_to_start_game", "Le vote doit d'abord avoir eu lieu avant de pouvoir démarrer la partie"),
    vote_is_in_progress_cant_start_game("vote_is_in_progress_cant_start_game", "Un vote est en cours, il n'est pas possible de démarrer la partie"),
    map_has_been_restored("map_has_been_restored", "La map a été remise à zéro"),
    admin_how_to_enable_vote("admin_how_to_enable_vote", "Pour forcer le démarrage du vote, faites /mp_start_vote"),
    metrics_are_now_enabled("metrics_are_now_enabled", "Les données de votre partie mineral contest sont désormais envoyé à l'auteur du plugin, merci à vous !"),
    metrics_are_now_disabled("metrics_are_now_disabled", "L'envoie de donnée est désormais désactivé"),
    ready_tag("ready_tag", "[PRET]"),
    not_ready_tag("not_ready_tag", "[NON PRET]"),
    player_is_now_ready("player_is_now_ready", "%playerName% est désormais prêt"),
    player_is_no_longer_ready("player_is_no_longer_ready", "%playerName% n'est plus prêt"),
    warn_player_you_dont_have_a_team("warn_player_you_dont_have_a_team", "Attention, vous n'êtes pas dans une équipe. Vous pouvez rejoindre une équipe avec la commande /join <nomEquipe>"),
    set_yourself_as_ready_to_start_game("set_yourself_as_ready_to_start_game", "Tous les joueurs doivent être prêt pour lancer la partie, marquez vous comme étant prêt avec la commande /ready "),
    set_yourself_as_ready_to_start_votemap("set_yourself_as_ready_to_start_votemap", "Tous les joueurs doivent être prêt pour démarrer le vote, marquez-vous comme prêt avec la commande /ready"),
    teamChat("teamChat", "[TEAM]"),
    cvar_friendly_fire_enabled("cvar_friendly_fire_enabled", "Les dégats entre coéquipiers sont désormais actif"),
    cvar_friendly_fire_disabled("cvar_friendly_fire_disabled", "Les dégats entre coéquipiers sont désormais désactivés"),
    cvar_block_adding_enabled("cvar_block_adding_enabled", "La pose de bloc est maintenant activée"),
    cvar_block_adding_disabled("cvar_block_adding_disabled", "La pose de block est désormais désactivé"),
    cvar_old_pvp_enabled("cvar_old_pvp_enabled", "L'ancien système de PVP est désormais activé"),
    cvar_old_pvp_disabled("cvar_old_pvp_disabled", "L'ancien système de PVP est désormais désactivé"),
    block_not_allowed_to_be_placed("block_not_allowed_to_be_placed", "L'ajout de ce bloc est interdit"),
    arena_chest_opened("arena_chest_opened", "Le coffre d'arène a été ouvert !"),
    error_command_only_when_game_is_started("error_command_only_when_game_is_started", "Cette commande ne peut être utilisé que lorsqu'une partie est en cours."),
    error_command_can_only_be_used_in_game("error_command_can_only_be_used_in_game", "Cette commande ne peut être utilisé qu'en jeu."),
    error_you_already_have_a_group("error_you_already_have_a_group", "Vous avez déjà un groupe."),
    error_group_with_this_name_already_exists("error_group_with_this_name_already_exists", "Un groupe avec ce nom existe déjà"),
    success_group_successfully_created("success_group_successfully_created", "Le groupe a été crée avec succès"),
    error_command_can_only_be_used_hub_world("error_command_can_only_be_used_hub_world", "Cette commande ne peut être utilisé que dans le monde %hubWorldName%."),
    error_you_must_be_in_a_group("error_you_must_be_in_a_group", "Vous devez être dans un groupe."),
    error_you_must_be_group_admin("error_you_must_be_group_admin", "Vous devez être administrateur du groupe"),
    error_no_player_with_this_name("error_no_player_with_this_name", "Il n'y a pas de joueurs avec ce nom"),
    error_you_cant_join_this_group("error_you_cant_join_this_group", "Vous ne pouvez pas rejoindre ce groupe."),
    error_group_doesnt_exists("error_group_doesnt_exists", "Ce groupe n'existe pas"),
    successfully_joined_a_group("successfully_joined_a_group", "Vous avez rejoin le groupe %groupName% avec succès !"),
    player_joined_our_group("player_joined_our_group", "Le joueur %playerName% a rejoin le groupe !"),
    you_got_invited_to_a_group("you_got_invited_to_a_group", "Vous avez été invité à rejoindre le groupe %groupName%. Vous pouvez le rejoindre avec la commande /joingroupe %groupName%"),
    error_player_already_have_a_group("error_player_already_jave_a_group", "Le joueur %playerName% a déjà un groupe."),
    error_player_already_in_this_group("error_player_already_in_this_group", "Le joueur %playerName% est déjà dans votre groupe."),
    player_successfully_invited_to_group("player_successfully_invited_to_group", "Le joueur %playerName% a été invité dans le groupe."),
    error_you_cant_kick_yourself_from_group("error_you_cant_kick_yourself_from_group", "Vous ne pouvez pas vous exclure vous même d'un groupe"),
    you_were_kicked_from_a_group("you_were_kicked_from_a_group", "Vous avez été exclu du groupe %groupName%"),
    player_got_kicked_from_group("player_got_kicked_from_group", "Le joueur %playerName% a été exclu du groupe."),
    error_you_cant_kick_this_player_from_the_group("error_you_cant_kick_this_player_from_the_group", "Vous ne pouvez pas exclure ce joueur du groupe"),
    group_got_deleted("group_got_deleted", "Le groupe a été dissous"),
    you_left_the_group("you_left_the_group", "Vous avez quitté le groupe %groupName%"),
    error_you_must_be_group_owner("error_you_must_be_group_owner", "Vous devez être le créateur du groupe"),
    error_player_not_in_our_group("error_player_not_in_our_group", "Le joueur n'est pas dans le groupe."),
    player_is_now_group_admin("player_is_now_group_admin", "Le joueur %playerName% est désormais admin du groupe"),
    error_player_is_not_admin("error_player_is_not_admin", "Le joueur %playerName% n'est pas admin du groupe"),
    player_is_no_longer_a_group_admin("player_is_no_longer_a_group_admin", "Le joueur %playerName% n'est plus un admin"),
    error_you_cant_remove_this_admin("error_you_cant_remove_this_admin", "Vous ne pouvez pas retirer cet admin"),
    error_group_is_not_locked("error_group_is_not_locked", "Le groupe n'est pas verouillé"),
    error_group_is_locked("error_group_is_locked", "Le groupe est verouillé"),
    group_is_now_locked("group_is_now_locked", "Le groupe est maintenant verouillé"),
    group_is_now_unlocked("group_is_now_unlocked", "Le groupe est maintenant déverouillé"),
    error_switch_fail_team_doesnt_exists("error_switch_fail_team_doesnt_exists", "Le switch n'a pas fonctionné, l'équipe donnée n'existe pas."),
    team_available_list_text("team_available_list_text", "Liste des équipes disponible: "),
    hud_game_state("hud_game_state", "Etat: %playerGroupState%"),
    hud_referee_text("hud_referee_text", "Rôle: Arbitre"),
    hud_map_name("hud_map_name", "Map actuelle: %mapName%"),
    cvar_error_invalid_team_name("cvar_error_invalid_team_name", "Cette équipe n'existe pas"),
    error_vote_available_only_when_no_game("error_vote_available_only_when_no_game", "Le démarrage du vote est disponible seulement avant le début d'une partie"),
    group_finished_their_game_winner_display("group_finished_their_game_winner_display", "Le groupe %groupName% a terminé sa partie, l'équipe gagnante est %coloredWinningTeamName% !"),
    custom_chicken_name("custom_chicken_name", "POULET DE RESSOURCE"),
    error_command_unavailable_in_this_version("error_command_unavailable_in_this_version", "Cette commande n'est pas disponible dans cette version du plugin"),
    error_you_must_be_server_admin("error_you_must_be_server_admin", "Vous devez être OP sur le serveur"),
    error_cant_load_game_settings_file("error_cant_load_game_settings_file", "Impossible de charger le fichier de configuration de la carte. Ce chargement %red%n'est pas obligatoire %white%et n'empêche pas le chargement du monde"),
    referee_item_teleport_to_player_description("referee_item_teleport_to_player_description", "Cet objet permet de vous téléporter à "),
    referee_item_teleport_to_player_title("referee_item_teleport_to_player_title", "Se téléporter à "),
    referee_item_teleport_inventory_title("referee_item_teleport_inventory_title", "Menu de téléportation"),
    referee_item_teleporting_you_to_player("referee_item_teleporting_you_to_player", "Téléportation vers le joueur en cours ..."),
    referee_inventory_teleport_description("referee_inventory_teleport_description", "Menu affichant la liste des joueurs de la partie, permettant de s'y téléporter"),
    referee_item_teleport_to_house_title("referee_item_teleport_to_house_title", "Se téléporter vers la base %coloredTeamName%"),
    referee_item_teleport_to_house_description("referee_item_teleport_to_house_description", "Vous permet de vous téléporter vers la base de l'équipe %coloredTeamName%"),
    referee_item_inventory_of_player_title("referee_item_inventory_of_player_title", "Inventaire de "),
    referee_item_inventory_of_player_description("referee_item_inventory_of_player_description", "Ouvrir l'inventaire du joueur "),
    referee_item_player_inventory_title("referee_item_player_inventory_title", "Liste des inventaires"),
    referee_item_player_inventory_description("referee_item_player_inventory_description", "Ouvrir le menu pour les inventaires"),
    referee_item_team_chest_inventory_title("referee_item_team_chest_inventory_title", "Inventaire des coffres d'équipes"),
    referee_item_team_chest_inventory_description("referee_item_team_chest_inventory_description", "Permet d'ouvrir les coffres d'équipes à distance"),
    referee_item_team_chest_item_title("referee_item_team_chest_item_title", "Coffre de l'équipe %coloredTeamName%"),
    referee_item_team_chest_item_description("referee_item_team_chest_item_description", "Ouvrir le coffre de l'équipe %coloredTeamName%"),
    referee_team_current_score("referee_team_current_score", "Le score actuel de l'équipe %coloredTeamName% est de: %teamScore% points"),
    referee_item_inventory_map_selector_title("referee_item_inventory_map_selector_title", "Selectionner une carte à jouer"),
    referee_item_inventory_map_selector_description("referee_item_inventory_map_selector_description", "Permet de choisir la carte à jouer"),
    referee_item_map_selector_description("referee_item_map_selector_description", "Charge la carte: "),
    referee_error_map_selector_only_hub("referee_error_map_selector_only_hub", "La selection de map ne peut se faire que dans le hub"),
    referee_inventory_game_title("referee_inventory_game_title", "Gestion de la partie"),
    referee_inventory_game_description("referee_inventory_game_description", "Gérer la partie (spawn coffre, début vagues de poulet, pause....)"),
    referee_item_enable_disable_chicken_wave_title("referee_item_enable_disable_chicken_wave_title", "Activer/Désactiver poulets"),
    referee_item_enable_disable_chicken_wave_description("referee_item_enable_disable_chicken_wave_description", "Activer ou désactiver les vagues de poulets"),
    referee_item_start_chicken_wave_title("referee_item_start_chicken_wave_title", "Démarrer les vagues de poulets"),
    chiken_wave_now_enabled("chiken_wave_now_enabled", "Les vagues de poulets sont désormais activé"),
    chiken_wave_now_disabled("chiken_wave_now_disabled", "Les vagues de poulets sont désormais désactivé"),
    chicken_wave_error_disabled("chicken_wave_error_disabled", "Les vagues de poulets sont désactivé."),
    chicken_wave_spawned("chicken_wave_spawned", "Des poulets sont apparu dans l'arène !"),
    referee_item_inventory_stopgame_title("referee_item_inventory_stopgame_title", "Arrêter la partie"),
    referee_item_inventory_stopgame_description("referee_item_inventory_stopgame_description", "Affiche le menu de confirmation d'arrêt de partie"),
    referee_item_cancel_game_stop_title("referee_item_cancel_game_stop_title", "Annuler"),
    referee_item_confirm_game_stop_title("referee_item_confirm_game_stop_title", "Confirmer"),
    referee_item_show_score_to_admin_title("referee_item_show_score_to_admin_title", "Afficher le score aux admins"),
    referee_item_show_score_to_admin_description("referee_item_show_score_to_admin_description", "Le score ne sera affiché qu'au admins."),
    referee_item_show_score_to_everyone_title("referee_item_show_score_to_everyone_title", "Afficher le score à tout le monde"),
    referee_item_show_score_to_everyone_description("referee_item_show_score_to_everyone_description", "Le score sera affiché à tout le monde (Tout le monde verra les points !"),
    map_downloader_inventory_name("map_downloader_inventory_name", "Menu de téléchargement de maps"),
    map_downloader_inventory_maps_list_name("map_downloader_inventory_maps_list_name", "Liste des maps disponible"),
    error_already_downloading_a_map("error_already_downloading_a_map", "Un téléchargement est déjà en cours, veuillez patienter"),
    downloading_map_progress("downloading_map_progress", "Téléchargement de la map %mapName% en cours: %percentage%%"),
    downloading_map_done_now_extracting("downloading_map_done_now_extracting", "Téléchargement de la carte terminée, démarrage de l'extraction de la map ..."),
    downloading_map_extracted("downloading_map_extracted", "Extraction de la map terminée, vous pouvez désormais jouer dessus"),
    map_downloader_delete_title("map_downloader_delete_title", "Menu de suppression de map"),
    map_downloader_delete_description("map_downloader_delete_description", "Voulez-vous vraiment supprimer la map ? Un menu de confirmation s'ouvrira après"),
    player_base_item_inventory_title("player_base_item_inventory_title", "Inventaire de réapparition"),
    player_base_item_close_inventory_item_title("player_base_item_close_inventory_item_title", "Fermer le menu"),
    error_no_maps_downloaded_to_start_game("error_no_maps_downloaded_to_start_game", "Aucune map n'a été téléchargée, un joueur OP peut en télécharger avec la commande /mcdownloader"),
    error_no_teams_with_player("error_no_teams_with_player", "Il n'y a aucune équipe avec des joueurs!"),
    currently_no_player_in_this_team("currently_no_player_in_this_team", "Aucun joueur dans cette équipe pour le moment"),
    item_team_selection_title("item_team_selection_title", "Choisir une équipe"),
    arena_chest_will_spawn_in("arena_chest_will_spawn_in", "Le coffre d'arène va apparaitre dans %arenaChestTimeLeft% secondes!"),


    stats_arena_chest_title("stats_arena_chest_title", "Indiana Jones"),
    stats_arena_chest_subtitle("stats_arena_chest_subtitle", "Avec %d coffre d'ouvert(s)"),
    stats_builder_title("stats_builder_title", "Jean le maçon"),
    stats_builder_subtitle("stats_builder_subtitle", "Avec %d bloc(s) de poser"),
    stats_chicken_killer_title("stats_chicken_killer_title", "Cuisiner chez KFC"),
    stats_chicken_killer_subtitle("stats_chicken_killer_subtitle", "Avec %d de poulet(s) tuer"),
    stats_kill_best_ranked_title("stats_kill_best_ranked_title", "Jack l'éventreur"),
    stats_kill_worst_ranked_title("stats_kill_worst_ranked_title", "Victime"),
    stats_kill_best_ranked_subtitle("stats_kill_best_ranked_subtitle", "Avec %d meurtre(s)"),
    stats_kill_worst_ranked_subtitle("stats_kill_worst_ranked_subtitle", "Avec %d décès"),
    stats_miner_best_ranked_title("stats_miner_best_ranked_title", "La foreuse humaine"),
    stats_miner_worst_ranked_title("stats_miner_worst_ranked_title", "René la taupe"),
    stats_miner_subtitle("stats_miner_subtitle", "Avec %d bloc(s) de miner"),
    stats_monster_killer_title("stats_monster_killer_title", "The Witcher"),
    stats_monster_killer_subtitle("stats_monster_killer_subtitle", "Avec %d de monstre(s) tué(s)"),
    stats_most_talking_title("stats_most_talking_title", "Le blabla-teur"),
    stats_most_talking_subtitle("stats_most_talking_subtitle", "Avec %d message(s) envoyé(s)"),
    stats_menu_title("stats_menu_title", "Menu de stats de fin de partie"),
    non_ready_hud("non_ready_hud", "Non prêt: "),
    non_voted_hud("non_voted_hud", "Sans vote: "),
    stats_parachute_hit_title("stats_parachute_hit_title", "Tireur du ciel"),
    stats_parachute_hit_subtitle("stats_parachute_hit_subtitle", "Avec %d tirs réussis!"),

    airdrop_title("airdrop_title", "Largage %red%aérien"),
    airdrop_subtitle("airdrop_subtitle", "Un %red%largage aérien%white% a été repéré en X: %red%%x %white%Z: %red%%z"),
    airdrop_chest_opening_title("airdrop_chest_title", "%red%Ouverture en cours..."),
    airdrop_chest_opened_title("airdrop_chest_opened_title", "%green%Le coffre a été ouvert!"),

    informations_category_title("informations_category_title", "Informations"),
    shopitem_next_airdrop_location_title("shopitem_next_airdrop_location_title", "Position du prochain largage"),
    shopitem_next_airdrop_description_1("shopitem_next_airdrop_description_1", "Vous voulez vous préparer pour le prochain largage ?"),
    shopitem_next_airdrop_description_2("shopitem_next_airdrop_description_2", "Ce bonus est fait pour vous!"),
    shopitem_next_airdrop_description_3("shopitem_next_airdrop_description_3", "Vous aurez la position exacte du prochain largage."),
    shopitem_next_airdrop_onitemuse_location("shopitem_next_airdrop_onitemuse_location", "Le prochain largage va apparaitre en %red%X: %x Z: %z"),

    shopitem_next_airdrop_time_title("shopitem_next_airdrop_time_title", "Temps restant avant le prochain largage"),
    shopitem_next_airdrop_time_desc("shopitem_next_airdrop_time_desc", "Vous aurez le temps restant avant le prochain largage !"),
    shopitem_next_airdrop_onitemuse_timeleft("shopitem_next_airdrop_onitemuse_timeleft", "Le prochain largage va apparaitre dans %time"),

    shopitem_next_arenachest_time_title("shopitem_next_arenachest_time_title", "Prochaine apparition du coffre d'arène"),
    shopitem_next_arenachest_time_desc("shopitem_next_arenachest_time_desc", "Vous affiche à quel moment le coffre d'arène va apparaitre"),
    shopitem_next_arenachest_onitemuse("shopitem_next_arenachest_onitemuse", "Le coffre d'arène va apparaitre dans %time"),


    items_category_title("items_category_title", "Items"),
    shopitem_knockback_item_title("shopitem_knockback_item_title", "Matraque"),
    shopitem_knockback_item_desc1("shopitem_knockback_item_desc1", "Vous vous sentez aggressé et vous souhaitez vous défendre?"),
    shopitem_knockback_item_desc2("shopitem_knockback_item_desc2", "Ce bâton est fait pour vous!"),
    shopitem_fireball_title("shopitem_fireball_title", "Boule de feu"),
    shopitem_fireball_desc("shopitem_fireball_desc", "Cet item vous permet de lancer une boule de feu!"),
    shopitem_compass_title("shopitem_compass_title", "Boussole maison"),
    shopitem_compass_desc("shopitem_compass_desc", "Vous indique la direction vers votre maison"),
    shopitem_oak_title("shopitem_oak_title", "Bûche"),
    shopitem_oak_desc("shopitem_oak_desc", "Achetez une buche"),
    shopitem_goldenapple_title("shopitem_goldenapple_title", "Pomme doré"),
    shopitem_goldenapple_desc("shopitem_goldenapple_desc", "Vous donne une pomme doré"),
    shopitem_waterbucket_title("shopitem_waterbucket_title", "Seau d'eau"),
    shopitem_waterbucket_desc("shopitem_waterbucket_desc", "Achetez un seau d'eau"),

    potions_category_title("potions_category_title", "Potions"),
    shopitem_haste_title("shopitem_haste_title", "Effet de haste"),
    shopitem_haste_desc("shopitem_haste_desc", "Améliore votre vitesse de minage"),
    shopitem_invisibility_title("shopitem_invisibility_title", "Potion d'invisibilité"),
    shopitem_invisibility_desc("shopitem_invisibility_desc", "Vous donne une potion d'invisibilité qui durera 2 minutes"),
    shopitem_speed1_title("shopitem_speed1_title", "Speed I"),
    shopitem_speed1_desc("shopitem_speed1_desc", "Vous donne une potion Vitesse I qui dure 5 minutes"),
    shopitem_speed2_title("shopitem_speed2_title", "Speed II"),
    shopitem_speed2_desc("shopitem_speed2_desc", "Vous donne une potion Vitesse II qui dure 5 minutes"),

    permanent_category_title("permanent_category_title", "Bonus permanents"),
    shopitem_more_health_title("shopitem_more_health_title", "Immortalité"),
    shopitem_more_health_desc("shopitem_more_health_desc", "Vous ajoute 5 coeurs à chaque réapparition"),
    shopitem_auto_ingots_title("shopitem_auto_ingots_title", "Auto-Fourneau"),
    shopitem_auto_ingots_desc("shopitem_auto_ingots_desc", "Permet de cuire les items lors de la destruction du minerai"),
    shopitem_diamond_sword_title("shopitem_diamond_sword_title", "Epée en diamant"),
    shopitem_diamond_sword_desc("shopitem_diamond_sword_desc", "Vous octroie une épée en diamant à chaque respawn"),

    teambonus_category_title("teambonus_category_title", "Bonus équipe"),
    shopitem_enable_next_arenachest_announce_title("shopitem_enable_next_arenachest_announce_title", "Annonce prochain coffre"),
    shopitem_enable_next_arenachest_announce_desc1("shopitem_enable_next_arenachest_announce_desc1", "Permet de donner un avantage à son équipe!"),
    shopitem_enable_next_arenachest_announce_desc2("shopitem_enable_next_arenachest_announce_desc2", "Cet objet affichera à votre équipe un message avant l'apparition du coffre d'arène"),
    shopitem_singleteleport_title("shopitem_singleteleport_title", "Téléportation solitaire"),
    shopitem_singleteleport_desc1("shopitem_singleteleport_desc1", "Permet de seulement téléporter le joueur faisant /arene"),
    shopitem_singleteleport_desc2("shopitem_singleteleport_desc2", "Fonctionne pour le prochain coffre"),
    shopitem_auto_arena_teleport_title("shopitem_auto_arena_teleport_title", "Auto /arene"),
    shopitem_auto_arena_teleport_desc1("shopitem_auto_arena_teleport_desc1", "Ce bonus permet de téléporter toute l'équipe à l'arène"),
    shopitem_auto_arena_teleport_desc2("shopitem_auto_arena_teleport_desc2", "Fonctionne pour le prochain coffre"),

    personnal_category_title("personnal_category_title", "Bonus personnel"),
    shopitem_temp_max_health_title("shopitem_temp_max_health_title", "Santé supplémentaire"),
    shopitem_temp_max_health_desc("shopitem_temp_max_health_desc", "Permet d'ajouter 3 coeurs jusqu'à votre mort"),
    shopitem_martyr_title("shopitem_martyr_title", "Dernière chance"),
    shopitem_martyr_desc1("shopitem_martyr_desc1", "Fait apparaitre de la tnt où vous mourrez"),
    shopitem_martyr_desc2("shopitem_martyr_desc2", "Ce bonus s'active lorsque vous êtes tué par un autre joueur"),
    shopitem_martyr_onitemuse("shopitem_martyr_onitemuse", "Le bonus Dernière Chance vient de s'activer"),
    shopitem_experience_title("shopitem_experience_title", "Expérience"),
    shopitem_experience_desc("shopitem_experience_desc", "Vous permet d'acheter un niveau d'expérience"),

    upgrades_category_title("upgrades_category_title", "Améliorations"),
    shopitem_pickaxelvl1_title("shopitem_pickaxelvl1_title", "Pioche I"),
    shopitem_pickaxelvl1_desc("shopitem_pickaxelvl1_desc", "Obtenez une pioche en fer avec Fortune I"),
    shopitem_pickaxelvl2_title("shopitem_pickaxelvl2_title", "Pioche II"),
    shopitem_pickaxelvl2_desc("shopitem_pickaxelvl2_desc", "Obtenez une pioche en diamant avec Fortune I"),
    shopitem_pickaxelvl3_title("shopitem_pickaxelvl3_title", "Pioche III"),
    shopitem_pickaxelvl3_desc("shopitem_pickaxelvl3_desc", "Obtenez une pioche en diamant avec Fortune I et Efficacité II"),

    shopitem_bonus_required("shopitem_bonus_required", "Vous devez d'abord acheter le bonus: %bonus"),
    shopitem_not_enought_credit("shopitem_not_enought_credit", "Vous n'avez pas assez de points"),
    shopitem_player_purchased("shopitem_player_purchased", "Le joueur %p a acheté le bonus %bonus"),
    shopitem_bonus_already_purchased("shopitem_bonus_already_purchased", "Vous avez déjà ce bonus"),
    shopitem_npc_title("shopitem_npc_title", "Boutique"),
    shopitem_player_with_no_team_cant_buy("shopitem_player_with_no_team_cant_buy", "Les joueurs sans équipe ne peuvent pas acheter dans la boutique"),

    // new hud vars
    hud_timeleft_text("hud_timeleft_text", "Temps restant"),
    hud_timeleft_value("hud_timeleft_value", "%timeLeft%"),
    hud_team_text("hud_team_text", "Equipe:"),
    hud_score_text("hud_score_text", "Score:"),
    hud_title_text1("hud_title_text1", "Mineral"),
    hud_title_text2("hud_title_text2", "Contest"),

    stats_mostpoints_title("stats_mostpoints_title", "Meilleur joueur"),
    stats_mostpoints_subtitle("stats_mostpoints_subtitle", "Avec %d points rapporté"),

    stats_mostpoints_taken_title("stats_mostpoints_taken_title", "Petit coquin"),
    stats_mostpoints_taken_subtitle("stats_mostpoints_taken_subtitle", "Avec %d points retirés aux autres équipes"),

    kitmanager_player_selected_kit("kitmanager_player_selected_kit", "Le joueur %playerName% a choisit son kit."),
    kitmanager_player_selected_kit_team("kitmanager_player_selected_kit_team", "Le joueur %playerName% a choisit le kit: %k"),
    kitmanager_player_list_without_kits("kitmanager_player_list_without_kits", "Liste des joueurs n'ayant pas encore de kit: "),
    kitmanager_inventory_kitSelectionTitle("kitmanager_inventory_kitSelectionTitle", "Selectionnez votre kit"),

    kit_agile_title("kit_agile_title", "Agile"),
    kit_agile_description("kit_agile_description", "Vous permet de vous déplacer 25% plus vite, et retire vos dégats de chute (sauf chute mortelle!)"),
    kit_wizard_title("kit_wizard_title", "Enchanteur"),
    kit_wizard_description("kit_wizard_description", "Vous réapparaissez avec 15 niveaux d'experience, 32 lapis lazuli, et 3 livres d'enchantement"),
    kit_warrior_title("kit_warrior_title", "Guerrier"),
    kit_warrior_description("kit_warrior_description", "Vous faites 25% de dégats en plus, mais vous avez 5 coeurs en moins"),
    kit_miner_title("kit_miner_title", "Mineur"),
    kit_miner_description("kit_miner_description", "Cuit instantanément vos minerais, mais vous retire une ligne dans votre inventaire"),
    kit_miner_item_denied("kit_miner_item_denied", "Bloqué"),
    kit_crazy_bet_title("kit_crazy_bet_title", "Parieur fou"),
    kit_crazy_bet_description("kit_crazy_bet_description", "Lorsque vous minez de l'émeraude, vous avez 20% de chance d'avoir 2 émeraudes, et 20% d'avoir 2 blocs de terre."),
    kit_toughguy_title("kit_toughguy_title", "Robuste"),
    kit_toughguy_description("kit_toughguy_description", "Vous avez 15 coeurs, 15% de dégats en moins, et 15% de vitesse en moins"),
    kit_support_title("kit_support_title", "Soutien"),
    kit_support_description("kit_support_description", "Vous donnez de la vie à vos alliés proche de vous, vous avez 5 coeurs en moins et 15% de vitesse en moins"),

    kit_cowboy_title("kit_cowboy_title", "Cowboy"),
    kit_cowboy_description("kit_cowboy_description", "Il réapparait avec un cheval, il se déplace 15% moins vite à pieds, mais ne peut pas utiliser d'armes à cheval, sauf des projectiles (ex: arc, arbalète ...). Il peut bien évidemment utiliser ses armes quand il n'est pas sur son cheval"),
    kit_spy_title("kit_spy_title", "Informateur"),
    kit_spy_description("kit_spy_description", "Il reçoit des informations sur les largages & coffre d'arène ... Il connait également la position du largage à son apparition! En revanche, il ne peut pas ouvrir les coffres d'arènes, ni les largages"),
    kit_spy_airdrop_will_spawn("kit_spy_airdrop_will_spawn", "%red%Le drop va apparaitre dans 30 secondes"),


    shopitem_not_compatible_with_kits("shopitem_not_compatible_with_kits", "Désactivé lorsque les kits sont actifs"),

    hud_currently_spectating("hud_currently_spectating", "Spectateur: "),
    hud_players_count("hud_players_count", "Joueurs: "),
    hud_current_game_state("hud_current_game_state", "Etat: "),
    hud_currently_waiting_game_start("hud_currently_waiting_game_start", "En attente");






    private String path;
    private String def;
    private static YamlConfiguration LANG = new YamlConfiguration();

    public static String langDataFolderName = "language";


    /**
     * Lang enum constructor.
     * @param path The string path.
     * @param start The default string.
     */
    Lang(String path, String start) {
        this.path = path;
        this.def = start;

    }

    public static void copyLangFilesFromRessources() {
        mineralcontest plugin = mineralcontest.plugin;
        String LangFileName;
        // For each language, we copy the langfile
        for (Language langage : Language.values()) {
            LangFileName = langage.getLanguageName() + ".yml";
            File langFile = new File(plugin.getDataFolder() + File.separator + Lang.langDataFolderName, LangFileName);

            if (!langFile.exists()) {
                plugin.saveResource(langDataFolderName + File.separator + LangFileName, false);
                Bukkit.getLogger().info("Created " + LangFileName + " file");
                GameLogger.addLog(new Log("copyLangFileFromRessources", LangFileName + " created", "plugin_startup"));

            }
        }
    }

    public static void loadLang(String lang) {
        mineralcontest plugin = mineralcontest.plugin;
        File langFile;

        Bukkit.getLogger().info("[MINERALC] Loading " + lang + " language");

        langFile = new File(plugin.getDataFolder() + File.separator + Lang.langDataFolderName, lang + ".yml");
        if (!langFile.exists()) {
            Bukkit.getLogger().severe(lang + ".yml lang file doesnt exists or could not be loaded.");
            GameLogger.addLog(new Log("loadLang", lang + " doesnt exists", "plugin_error"));

            return;
        }

        YamlConfiguration conf = YamlConfiguration.loadConfiguration(langFile);
        for (Lang item : Lang.values()) {
            if (conf.getString(item.getPath()) == null) {
                conf.set(item.getPath(), item.getDefault());
            }
        }
        Lang.setFile(conf);


        try {
            conf.save(langFile);
            Bukkit.getLogger().info("[MINERALC] Loaded " + lang + " language");
            GameLogger.addLog(new Log("loadLang", lang + " loaded", "plugin_lang_loaded"));
            mineralcontest.prefix = Lang.title.toString() + ChatColor.WHITE;
            mineralcontest.prefixErreur = Lang.title.toString() + ChatColor.RED + Lang.error.toString() + ChatColor.WHITE + " ";
            mineralcontest.prefixGlobal = Lang.title.toString() + ChatColor.GREEN + Lang.global.toString() + ChatColor.WHITE + " ";
            mineralcontest.prefixPrive = Lang.title.toString() + ChatColor.YELLOW + Lang._private.toString() + ChatColor.WHITE + " ";
            mineralcontest.prefixAdmin = Lang.title.toString() + ChatColor.RED + Lang.admin.toString() + ChatColor.WHITE + " ";
            mineralcontest.prefixTeamChat = Lang.title.toString() + ChatColor.BLUE + Lang.teamChat.toString() + ChatColor.WHITE + " ";
            mineralcontest.prefixGroupe = Lang.title.toString() + ChatColor.GOLD + Lang.group.toString() + ChatColor.WHITE + " ";
            mineralcontest.prefixWeb = Lang.title.toString() + ChatColor.AQUA + Lang.web.toString() + ChatColor.WHITE + " ";


        } catch (IOException ioe) {
            plugin.getLogger().log(Level.WARNING, "MineralContest: Failed to save lang.yml.");
            ioe.printStackTrace();
            Error.Report(ioe, null);
            GameLogger.addLog(new Log("error", "failed to save lang.yml", "plugin_error"));

        } catch (Exception e) {
            plugin.getLogger().severe("ERREUR");
            e.printStackTrace();
            Error.Report(e, null);
        }


    }

    /**
     * Set the {@code YamlConfiguration} to use.
     * @param config The config to set.
     */
    public static void setFile(YamlConfiguration config) {
        LANG = config;
    }

    private Lang findByValue(String value) {
        for (Lang item : Lang.values()) {
            if (item.getDefault().equals(value)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public String toString() {

        //return get(findByValue(LANG.getString(this.path, def)).getPath());
        return translate((Objects.requireNonNull(LANG.getString(this.path, def))));
        //return "toString" + LANG.getString(this.path, def);
    }

    public static String get(String key) {
        String result = "";
        try {
            result = translate(Lang.valueOf(key).getDefault());
        } catch (Exception e) {
            Bukkit.getLogger().severe("GET ERROR");
            e.printStackTrace();
            Error.Report(e, null);
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
        string = translate(string, p);
        return string;
    }

    public static String translate(String string, Equipe team) {
        if (string.contains("%coloredTeamName%"))
            string = string.replace("%coloredTeamName%", team.getCouleur() + team.getNomEquipe() + ChatColor.WHITE);
        if (string.contains("%penality%")) string = string.replace("%penality%", team.getPenalty() + "");
        if (string.contains("%teamScore%"))
            string = string.replace("%teamScore%", (team.getPenalty() != 0) ? "" + team.getScore() + " (%red%" + team.getPenalty() + Lang.team_penalty.toString() + ")" : team.getScore() + " point(s)");
        if (string.contains("%teamColor%")) string = string.replace("%teamColor%", "" + team.getCouleur());
        if (string.contains("%teamName%")) string = string.replace("%teamName%", team.getNomEquipe());
        string = translate(string);
        return string;
    }

    public static String translate(String string, Groupe groupe) {
        if (string.contains("%groupName%")) string = string.replace("%groupName%", groupe.getNom());
        if (string.contains("%playerGroupState%"))
            string = string.replace("%playerGroupState%", groupe.getEtatPartie().getNom());
        if (string.contains("%mapName%")) string = string.replace("%mapName%", groupe.getMapName());
        if (string.contains("%arenaChestTimeLeft%"))
            string = string.replace("%arenaChestTimeLeft%", groupe.getParametresPartie().getCVAR("arena_warn_chest_time").getValeurNumerique() + "");
        string = translate(string);
        return string;
    }

    public static String translate(String string, Player player) {
        if (string.contains("%deathTime%"))
            string = string.replace("%deathTime%", "" + Objects.requireNonNull(mineralcontest.getPlayerGame(player)).getArene().getDeathZone().getPlayerDeathTime(player));
        if (string.contains("%playerName%")) string = string.replace("%playerName%", player.getDisplayName());
        if (string.contains("%deadPlayer%")) string = string.replace("%deadPlayer%", player.getDisplayName());

        string = translate(string);
        return string;

    }

    public static String translate(String string, Player player1, Player player2) {
        if (string.contains("%deadPlayer%")) string = string.replace("%deadPlayer%", player1.getDisplayName());
        if (string.contains("%killingPlayer%")) string = string.replace("%killingPlayer%", player2.getDisplayName());
        string = translate(string);
        return string;
    }

    public static String translate(String string) {
        if (string.contains("%black%")) string = string.replace("%black%", "" + ChatColor.BLACK);
        if (string.contains("%dark_blue%")) string = string.replace("%dark_blue%", "" + ChatColor.DARK_BLUE);
        if (string.contains("%dark_green%")) string = string.replace("%dark_green%", "" + ChatColor.DARK_GREEN);
        if (string.contains("%dark_aqua%")) string = string.replace("%dark_aqua%", "" + ChatColor.DARK_AQUA);
        if (string.contains("%dark_red%")) string = string.replace("%dark_red%", "" + ChatColor.DARK_RED);
        if (string.contains("%dark_purple%")) string = string.replace("%dark_purple%", "" + ChatColor.DARK_PURPLE);
        if (string.contains("%gold%")) string = string.replace("%gold%", "" + ChatColor.GOLD);
        if (string.contains("%gray%")) string = string.replace("%gray%", "" + ChatColor.GRAY);
        if (string.contains("%dark_gray%")) string = string.replace("%dark_gray%", "" + ChatColor.DARK_GRAY);
        if (string.contains("%blue%")) string = string.replace("%blue%", "" + ChatColor.BLUE);
        if (string.contains("%green%")) string = string.replace("%green%", "" + ChatColor.GREEN);
        if (string.contains("%aqua%")) string = string.replace("%aqua%", "" + ChatColor.AQUA);
        if (string.contains("%red%")) string = string.replace("%red%", "" + ChatColor.RED);
        if (string.contains("%light_purple%")) string = string.replace("%light_purple%", "" + ChatColor.LIGHT_PURPLE);
        if (string.contains("%yellow%")) string = string.replace("%yellow%", "" + ChatColor.YELLOW);
        if (string.contains("%white%")) string = string.replace("%white%", "" + ChatColor.WHITE);
        if (string.contains("%magic%")) string = string.replace("%magic%", "" + ChatColor.MAGIC);
        if (string.contains("%bold%")) string = string.replace("%bold%", "" + ChatColor.BOLD);
        if (string.contains("%strikethrough%"))
            string = string.replace("%strikethrough%", "" + ChatColor.STRIKETHROUGH);
        if (string.contains("%underline%")) string = string.replace("%underline%", "" + ChatColor.UNDERLINE);
        if (string.contains("%italic%")) string = string.replace("%italic%", "" + ChatColor.ITALIC);


        if (string.contains("%hubWorldName%"))
            string = string.replace("%hubWorldName%", mineralcontest.getPluginConfigValue("world_name").toString());


        return string;
    }

    public static String translate(String string, Game partie) {
        if (string.contains("%timeLeft%"))
            string = string.replace("%timeLeft%", TimeConverter.intToString(partie.getTempsPartie()));
        if (string.contains("%preGameTime%")) string = string.replace("%preGameTime%", "" + partie.PreGameTimeLeft);

        //     "Le groupe %groupName% a terminé sa partie, l'équipe gagnante est %coloredWinningTeamName% !");
        if (string.contains("%groupName%")) string = string.replace("%groupName%", "" + partie.groupe.getNom());
        if (string.contains("%coloredWinningTeamName%"))
            string = string.replace("%coloredWinningTeamName%", "" + partie.getWinningTeam().getCouleur() + partie.getWinningTeam().getNomEquipe());


        if (string.contains("%onlinePlayers%"))
            string = string.replace("%onlinePlayers%", partie.groupe.getPlayerCount() + "");

        return string;

    }

}
