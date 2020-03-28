package fr.mineral.Events;

import fr.mineral.Core.Game;
import fr.mineral.Core.GameSettingsCvar;
import fr.mineral.Teams.Equipe;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.Player.CouplePlayerTeam;
import fr.mineral.Utils.Player.PlayerBaseItem;
import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;



public class PlayerJoin implements Listener {

    private boolean isPlayerFirstJoinAttempt = false;
    private boolean isPlayerAllowedToJoin = false;
    private boolean havePlayerDisconnectedEarlier = false;
    private boolean thereIsAnAdminLoggedIn = false;
    // If true, we allow every admin login
    private boolean enable_admin_login = true;


    private Game game;
    private String playerName;

    private Player player;
    private Equipe oldPlayerTeam;


    private void initVariables(Player player) {
        mineralcontest plugin = mineralcontest.plugin;
        this.game = plugin.getGame();
        this.thereIsAnAdminLoggedIn = game.isThereAnAdminLoggedIn();
        this.player = player;
        this.playerName = player.getDisplayName();
        // havePlayerTriedTOLogin returns true if player have already tried to login
        this.isPlayerFirstJoinAttempt = !game.havePlayerTriedToLogin(playerName);
        this.isPlayerAllowedToJoin = (!this.isPlayerFirstJoinAttempt) && game.isPlayerAllowedToLogIn(playerName);
        this.havePlayerDisconnectedEarlier = game.havePlayerDisconnected(playerName);
        this.oldPlayerTeam = (this.havePlayerDisconnectedEarlier) ? game.getDisconnectedPlayerTeam(playerName) : null;
    }





    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws Exception {
        World worldEvent = event.getPlayer().getWorld();
        if (worldEvent.equals(mineralcontest.plugin.pluginWorld)) {

            initVariables(event.getPlayer());


            // First, we check if the map is correct
            mineralcontest.checkIfMapIsCorrect();


            // If the game is started
            if(game.isGameStarted()) {
                // We need to check if the player have disconnected earlier
                if(havePlayerDisconnectedEarlier) {
                    // Player was connected earlier

                    // if oldPLayerTeam is null, then he wasn't in a team
                    // If he is OP, then he should be referee
                    // Else, admin need to decide what to do
                    if(oldPlayerTeam == null) {

                        if(player.isOp()) {
                            // If player is op and oldteam is null, then he is a referee
                            game.addReferee(player);
                            mineralcontest.log.info("Player " + playerName + " was OP, had no team but was registered inside the disconnected players list, so we added him to the referees");
                            return;
                        } else {
                            // Player is not OP
                            if(thereIsAnAdminLoggedIn) {
                                // Admin needs to decide what to do, should'nt happen
                                mineralcontest.broadcastMessageToAdmins(playerName + " joined the game, he was connected earlier but it seems like we dont know his team. You can switch him into a team by using /switch " + playerName + " <team>");
                                mineralcontest.log.info("Player " + playerName + " joined the game, he was NOT OP, he had no team but was registered inside the disconnected players, admin needs to decide what to do.");
                                if(!game.isGamePaused()) game.pauseGame();

                            } else {
                                player.kickPlayer("PlayerJoin-L-74 - There is no admin logged in and the system doesn't know what to do. Please contact the plugin author");
                                mineralcontest.log.severe("PlayerJoin-L-74 - " + playerName + " tried to join but There is no admin logged in and the system doesn't know what to do. Please contact the plugin author");
                            }
                            return;
                        }

                    } else {
                        // His old team isnt null, we add him back
                        oldPlayerTeam.addPlayerToTeam(player, true);
                        game.resumeGame();
                        return;
                    }

                } else {
                    // The player was not connected earlier
                    // This is a "random" player that tries to join
                    // If the player is OP and we allow admin to join, then he should be added as a referee
                    if(player.isOp() && enable_admin_login) { game.addReferee(player); return;}
                    // The player is now (op and enable_admin_login = false) or this is his first attempt
                    // If this is his first attemps, that mean he shouldnt be allowed to join, except if he is an admin
                    if(isPlayerFirstJoinAttempt) {
                        // We register him into the player attempts list
                        game.addPlayerTriedToLogin(playerName);
                        // We inform the admins
                        informAdminsThatAPlayerTriedToJoin(playerName);
                        player.kickPlayer(Lang.kick_game_already_in_progress.toString());
                        return;
                    } else {
                        // This is not player first attempt
                        if(! isPlayerAllowedToJoin) {
                            // Player is NOT allowed to join, an admin needs to do /allow <playername>
                            player.kickPlayer(Lang.kick_game_already_in_progress.toString());
                            // We inform the admins
                            informAdminsThatAPlayerTriedToJoin(playerName);
                        } else {
                            //Player is allowed to join!
                            informAdminThatAPlayerNeedASwitch(playerName);
                            game.pauseGame();
                            return;
                        }
                    }
                }
            }

            // If we are in the pregame timer (game starting in ...)
            if(game.isPreGame()) {
                // If the player have disconnected during preGame
                if(havePlayerDisconnectedEarlier) {
                    // We put him back into his old team
                    this.oldPlayerTeam.addPlayerToTeam(player, true);
                    return;
                }

                if(isPlayerFirstJoinAttempt) {
                    if(thereIsAnAdminLoggedIn) {
                        informAdminsThatAPlayerTriedToJoin(playerName);
                    }
                    game.addPlayerTriedToLogin(playerName);
                    player.kickPlayer(Lang.kick_game_already_in_progress.toString());
                } else {
                    if(isPlayerAllowedToJoin) {
                        game.cancelPreGame();
                        informAdminThatAPlayerNeedASwitch(playerName);
                    } else {
                        game.addPlayerTriedToLogin(playerName);
                        if(thereIsAnAdminLoggedIn) informAdminsThatAPlayerTriedToJoin(playerName);
                        player.kickPlayer(Lang.kick_game_already_in_progress.toString());
                    }
                }

            }

            if(game.isGamePaused()) {
                if(isPlayerFirstJoinAttempt) {
                    informAdminsThatAPlayerTriedToJoin(playerName);
                    game.addPlayerTriedToLogin(playerName);
                    player.kickPlayer(Lang.kick_game_already_in_progress.toString());
                } else {
                    // Not the first player's login
                    if(havePlayerDisconnectedEarlier) {
                        if(oldPlayerTeam == null && player.isOp()) game.addReferee(player);
                        else if(oldPlayerTeam == null) {
                            player.kickPlayer("PlayerJoin-L-172 - There is no admin logged in and the system doesn't know what to do. Please contact the plugin author");
                            mineralcontest.log.severe("PlayerJoin-L-173 - " + playerName + " tried to join but There is no admin logged in and the system doesn't know what to do. Please contact the plugin author");
                        }
                        else this.oldPlayerTeam.addPlayerToTeam(player, true);
                    } else {

                        if(isPlayerAllowedToJoin) {
                            informAdminThatAPlayerNeedASwitch(playerName);
                            game.pauseGame();
                        } else {
                            // not allowed to join
                            informAdminsThatAPlayerTriedToJoin(playerName);
                            player.kickPlayer(Lang.kick_game_already_in_progress.toString());
                            return;
                        }
                    }
                }
            }

            // Every player login when game is not started
            game.teleportToLobby(player);
            PlayerUtils.clearPlayer(player);

            Bukkit.getScheduler().scheduleSyncDelayedTask(mineralcontest.plugin, () -> {
                mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.hud_awaiting_players.toString());
                if(game.areAllPlayerLoggedIn()) game.votemap.enableVote(false);
            }, 20);




        }
    }



    private void informAdminsThatAPlayerTriedToJoin(String playerName) {
        mineralcontest.broadcastMessageToAdmins("Player " + playerName + " tried to join, you can allow him to join by typing /allow " + playerName);
    }

    private void informAdminThatAPlayerNeedASwitch(String playerName) {
        mineralcontest.broadcastMessageToAdmins("Player " + playerName + " have joined the game; switch him into a team with /switch " + playerName + " <team>");
    }

    /*public void onPlayerJoin(PlayerJoinEvent event) throws Exception {
        World worldEvent = event.getPlayer().getWorld();
        if(worldEvent.equals(mineralcontest.plugin.pluginWorld)) {


            mineralcontest.checkIfMapIsCorrect();
            Player p = event.getPlayer();
            PlayerUtils.setMaxHealth(p);




            // SI la game n'a pas démarré et que tout le monde est connecté
            Game game = mineralcontest.plugin.getGame();

            // Lorsque le premier joueur se connecte, on génère tous les points de spawn etc ...
            if (mineralcontest.plugin.pluginWorld.getPlayers().size() == 1 && !game.isGameInitialized) {
                mineralcontest.checkIfMapIsCorrect();
            }


                Si la partie n'a pas encore démarré, et que le joueur se connecte au serveur, alors
                on le téléporte sur la plateforme
                ET on le remet à zero


            if (!game.isGameStarted() &&
                    !game.isPreGame() &&
                    !game.isGamePaused()) {
                // Téléportation à la plateforme initiale
                PlayerUtils.teleportToLobby(p);
                PlayerUtils.clearPlayer(p);
            }

            try {
                PlayerBaseItem.givePlayerItems(p, PlayerBaseItem.onFirstSpawnName);
            }catch (Exception e) {
                mineralcontest.broadcastMessage(mineralcontest.prefixErreur + e.getMessage());
                e.printStackTrace();
            }


            // Si le joueur se connecte (et qu'il n'est pas admin) et que le vote est terminé, c'est trop tard
            // Si il s'était déconnecté, on le laisse revenir
        if(mineralcontest.plugin.getGame().votemap.isVoteEnded()) {
            if(!game.havePlayerDisconnected(p.getDisplayName()) && !p.isOp()) {
                p.kickPlayer(Lang.kick_game_already_in_progress.toString());
            } else if(p.isOp()) {
                // Sinon, on le met arbitre
                mineralcontest.plugin.getGame().addReferee(p);
            }
        }


            //if (!game.isGameStarted())
                //mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.hud_awaiting_players.toString());


            if (game.isPreGame() && p.isOp()) {
                mineralcontest.plugin.getGame().addReferee(p);
            }


            if (game.isGameStarted() && !game.isGamePaused()) {
                if (p.isOp()) {
                    mineralcontest.plugin.getGame().addReferee(p);
                } else {
                    p.kickPlayer(Lang.kick_game_already_in_progress.toString());
                    for (Player online : mineralcontest.plugin.pluginWorld.getPlayers())
                        if (online.isOp())
                            online.sendMessage(mineralcontest.prefixAdmin + Lang.translate(Lang.admin_played_tried_to_login.toString(), p));
                    mineralcontest.plugin.getServer().getLogger().info(mineralcontest.prefixAdmin + Lang.translate(Lang.admin_played_tried_to_login.toString(), p));
                }


            }


            if (game.isGameStarted() && game.isGamePaused()) {
                // On regarde si le joueur connecté était un joueur qui s'est déconnecté
                if (game.havePlayerDisconnected(p.getDisplayName())) {
                    // Il s'était déconnecté
                    CouplePlayerTeam infoJoueur = game.getDisconnectedPlayerInfo(p.getDisplayName());
                    try {
                        // On le remet dans son équipe automatiquement
                        infoJoueur.getTeam().addPlayerToTeam(p);
                        game.resumeGame();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // Un nouveau joueur s'est connecté
                    Equipe teamNonPleine = game.getEquipeNonPleine();
                    if (teamNonPleine == null) {
                        p.kickPlayer(Lang.kick_game_already_in_progress.toString());
                    } else {
                        for (Player online : mineralcontest.plugin.pluginWorld.getPlayers()) {

                            if (online.isOp()) {
                                online.sendMessage(mineralcontest.prefixPrive + Lang.translate(Lang.admin_played_logged_in_pause_without_team.toString(), p));
                                online.sendMessage(mineralcontest.prefixPrive + Lang.translate(Lang.admin_team_non_empty.toString(), teamNonPleine));
                                online.sendMessage(mineralcontest.prefixPrive + Lang.admin_switch_command_help.toString());
                            }
                        }
                    }
                }
            }
        }
    }*/

    /*
    // If the game is already started
            if(game.isGameStarted()) {
                // If player was disconnected
                if(game.havePlayerDisconnected(player.getDisplayName())) {
                    CouplePlayerTeam playerInfos = game.getDisconnectedPlayerInfo(player.getDisplayName());
                    Equipe playerOldTeam = playerInfos.getTeam();

                    // IF he wasn't in a team, he must be a referee
                    if(playerOldTeam == null) {
                        if(player.isOp()) {
                            game.addReferee(player);

                        } else {
                            // He isnt op ... But has disconnected. So, an admin should switch him
                            mineralcontest.broadcastMessage("A player that was disconnted have joined the game, but he doesn't have a team.");
                        }
                    } else {
                        // We put him back in his team
                        playerOldTeam.addPlayerToTeam(player);
                    }
                    // We remove the user from disconnected players
                    game.removePlayerFromDisconnected(player);
                    return;
                }
            } else {
                // Game is not started
                if(game.isPreGame()) {
                    // IF it's the pregame, he should get kicked
                    game.addPlayerTriedToLogin(player.getDisplayName());
                    mineralcontest.broadcastMessageToAdmins("A player tried to join the game, his username is:" + playerName);
                    mineralcontest.broadcastMessageToAdmins("To allow him to join, enter the following command /allow " + playerName);
                    player.kickPlayer(Lang.kick_game_already_in_progress.toString());
                }
            }
     */
}
