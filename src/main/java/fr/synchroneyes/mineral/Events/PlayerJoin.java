package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Settings.GameSettings;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.Player.PlayerUtils;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;



public class PlayerJoin implements Listener {

    private boolean isPlayerFirstJoinAttempt = false;
    private boolean isPlayerAllowedToJoin = false;
    private boolean havePlayerDisconnectedEarlier = false;
    private boolean thereIsAnAdminLoggedIn = false;


    private Game game;
    private String playerName;

    private Player player;
    private Equipe oldPlayerTeam;




    /*private void initVariables(Player player) {
        mineralcontest plugin = mineralcontest.plugin;
        this.game = mineralcontest.getPlayerGame(player);

        if (game == null) return;
        this.thereIsAnAdminLoggedIn = game.isThereAnAdminLoggedIn();
        this.player = player;
        this.playerName = player.getDisplayName();
        // havePlayerTriedTOLogin returns true if player have already tried to login
        this.isPlayerFirstJoinAttempt = !game.havePlayerTriedToLogin(playerName);
        this.isPlayerAllowedToJoin = (!this.isPlayerFirstJoinAttempt) && game.isPlayerAllowedToLogIn(playerName);
        this.havePlayerDisconnectedEarlier = game.groupe.havePlayerDisconnected(player);
        this.oldPlayerTeam = (this.havePlayerDisconnectedEarlier) ? game.getDisconnectedPlayerTeam(playerName) : null;
    }*/


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws Exception {
        World worldEvent = event.getPlayer().getWorld();

        if (mineralcontest.isAMineralContestWorld(worldEvent)) {

            Player joueur = event.getPlayer();
            Groupe playerGroupe = mineralcontest.getPlayerGroupe(joueur);

            if (playerGroupe == null) {
                if (!mineralcontest.communityVersion) {
                    if (joueur.isOp()) mineralcontest.plugin.getNonCommunityGroup().addAdmin(joueur);
                    else mineralcontest.plugin.getNonCommunityGroup().addJoueur(joueur);
                    playerGroupe = mineralcontest.getPlayerGroupe(joueur);

                    if (playerGroupe.havePlayerDisconnected(joueur)) playerGroupe.playerHaveReconnected(joueur);
                    return;

                }
            }


            if (game == null) return;


            GameSettings settings = game.groupe.getParametresPartie();
            // We need to apply the pvp system
            if (settings.getCVAR("mp_enable_old_pvp").getValeurNumerique() == 1) {
                player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(1024d);
            } else {
                player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);

            }


            // If the game is started
            if (game.isGameStarted()) {
                // We need to check if the player have disconnected earlier
                if (havePlayerDisconnectedEarlier) {
                    // Player was connected earlier

                    // We reconnect the player
                    game.groupe.playerHaveReconnected(player);
                    return;


                } else {
                    // The player was not connected earlier
                    // This is a "random" player that tries to join
                    // If the player is OP and we allow admin to join, then he should be added as a referee
                    // If true, we allow every admin login
                    if (player.isOp()) {
                        game.addReferee(player);
                        return;
                    }
                    // The player is now (op and enable_admin_login = false) or this is his first attempt
                    // If this is his first attempt, that mean he should'nt be allowed to join, except if he is an admin
                    if (isPlayerFirstJoinAttempt) {
                        // We register him into the player attempts list
                        game.addPlayerTriedToLogin(playerName);
                        // We inform the admins
                        informAdminsThatAPlayerTriedToJoin(playerName);
                        player.kickPlayer(Lang.kick_game_already_in_progress.toString());
                        return;
                    } else {
                        // This is not player first attempt
                        if (!isPlayerAllowedToJoin) {
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
            if (game.isPreGame()) {
                // If the player have disconnected during preGame
                if (havePlayerDisconnectedEarlier) {
                    // We put him back into his old team
                    this.oldPlayerTeam.addPlayerToTeam(player, true, false);
                    game.removePlayerFromDisconnected(player);
                    if (game.getDisconnectedPlayersCount() == 0) game.resumeGame();
                    return;
                }

                if (isPlayerFirstJoinAttempt) {
                    if (thereIsAnAdminLoggedIn) {
                        informAdminsThatAPlayerTriedToJoin(playerName);
                    }
                    game.addPlayerTriedToLogin(playerName);
                    player.kickPlayer(Lang.kick_game_already_in_progress.toString());
                } else {
                    if (isPlayerAllowedToJoin) {
                        game.cancelPreGame();
                        informAdminThatAPlayerNeedASwitch(playerName);
                    } else {
                        game.addPlayerTriedToLogin(playerName);
                        if (thereIsAnAdminLoggedIn) informAdminsThatAPlayerTriedToJoin(playerName);
                        player.kickPlayer(Lang.kick_game_already_in_progress.toString());
                    }
                }

            }

            if (game.isGamePaused()) {
                if (isPlayerFirstJoinAttempt) {
                    informAdminsThatAPlayerTriedToJoin(playerName);
                    game.addPlayerTriedToLogin(playerName);
                    player.kickPlayer(Lang.kick_game_already_in_progress.toString());
                } else {
                    // Not the first player's login
                    if (havePlayerDisconnectedEarlier) {
                        if (oldPlayerTeam == null && player.isOp()) game.addReferee(player);
                        else if (oldPlayerTeam == null) {
                            player.kickPlayer("PlayerJoin-L-172 - There is no admin logged in and the system doesn't know what to do. Please contact the plugin author");
                            mineralcontest.log.severe("PlayerJoin-L-173 - " + playerName + " tried to join but There is no admin logged in and the system doesn't know what to do. Please contact the plugin author");
                        } else this.oldPlayerTeam.addPlayerToTeam(player, true, false);
                    } else {

                        if (isPlayerAllowedToJoin) {
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
            if (!mineralcontest.debug) game.teleportToLobby(player);
            PlayerUtils.clearPlayer(player);
            player.sendMessage(ChatColor.GREEN + "[] Server running Mineral Contest v" + mineralcontest.plugin.getDescription().getVersion());


            if (player.isOp()) mineralcontest.afficherMessageVersionToPlayer(player);


        }
    }


    private void informAdminsThatAPlayerTriedToJoin(String playerName) {
        mineralcontest.broadcastMessageToAdmins("Player " + playerName + " tried to join, you can allow him to join by typing /allow " + playerName);
        Bukkit.getLogger().info("Player " + playerName + " tried to join, you can allow him to join by typing /allow " + playerName);
    }

    private void informAdminThatAPlayerNeedASwitch(String playerName) {
        mineralcontest.broadcastMessageToAdmins("Player " + playerName + " have joined the game; switch him into a team with /switch " + playerName + " <team>");
        Bukkit.getLogger().info("Player " + playerName + " have joined the game; switch him into a team with /switch " + playerName + " <team>");

    }

}
