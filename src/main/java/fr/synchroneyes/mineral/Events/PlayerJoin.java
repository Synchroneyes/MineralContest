package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Utils.Player.PlayerUtils;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;



public class PlayerJoin implements Listener {



    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws Exception {
        World worldEvent = event.getPlayer().getWorld();

        // On vérifie si c'est un monde du plugin
        if (mineralcontest.isAMineralContestWorld(worldEvent)) {

            Player joueur = event.getPlayer();

            // On applique le système de pvp au joueur
            PlayerUtils.applyPVPtoPlayer(joueur);

            // On est dans un monde mineral contest
            // On doit d'abord vérifier si le joueur s'est déconnecté plus tot
            for (Groupe groupe : mineralcontest.plugin.getGroupes()) {

                // Si le joueur s'était déconnecté avant
                if (groupe.havePlayerDisconnected(joueur)) {
                    // On le reconnecte, tout va bien
                    groupe.playerHaveReconnected(joueur);
                    return;
                }
            }

            // On arrive ici, le joueur n'avait pas de groupe, c'est sa première reconnexion
            // On vérifie si c'est la version communautaire ou non
            if (!mineralcontest.communityVersion) {
                // On est dans la version non communautaire, la version publique quoi :)
                // On récupère le groupe de base du plugin
                Groupe defaultGroupe = mineralcontest.plugin.getNonCommunityGroup();


                // On le met comme spectateur
                // Et on averti les admins
                // Si le joueur est OP, on le met comme arbitre

                if (joueur.isOp()) {
                    defaultGroupe.addAdmin(joueur);
                    defaultGroupe.getGame().addReferee(joueur);
                } else {
                    // Sinon, il devient spectateur
                    defaultGroupe.addJoueur(joueur);
                    // On le TP au centre de l'arène si la partie est chargé
                    if (defaultGroupe.getMonde() != null)
                        PlayerUtils.teleportPlayer(joueur, defaultGroupe.getMonde(), defaultGroupe.getGame().getArene().getCoffre().getLocation());

                    joueur.setGameMode(GameMode.SPECTATOR);

                    // Et on rend les autres joueurs visible par ce spectateur 5 secondes après sa connexion
                    Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
                        for (Player membre_groupe : defaultGroupe.getPlayers())
                            joueur.showPlayer(mineralcontest.plugin, membre_groupe);
                    }, 5 * 20);

                    defaultGroupe.sendToadmin(mineralcontest.prefixAdmin + "Le joueur " + joueur.getDisplayName() + " s'est connecté et a été mis en spectateur");
                }


            }

        }

        /*if (mineralcontest.isAMineralContestWorld(worldEvent)) {

            Player joueur = event.getPlayer();
            Groupe playerGroupe = mineralcontest.getPlayerGroupe(joueur);
            game = playerGroupe.getGame();

            if(playerGroupe == null) Bukkit.broadcastMessage("NULL GROUP");

            if (!mineralcontest.communityVersion) {

                // Si on est pas sur la version communautaire
                GameSettings settings = game.groupe.getParametresPartie();
                if (settings.getCVAR("mp_enable_old_pvp").getValeurNumerique() == 1) {
                    player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(1024d);
                } else {
                    player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
                }

                if (joueur.isOp()) mineralcontest.plugin.getNonCommunityGroup().addAdmin(joueur);
                else mineralcontest.plugin.getNonCommunityGroup().addJoueur(joueur);
                playerGroupe = mineralcontest.getPlayerGroupe(joueur);

                if (playerGroupe.havePlayerDisconnected(joueur)) playerGroupe.playerHaveReconnected(joueur);
                else {

                    // Le joueur se connecte pour la première fois ... On le en spectateur
                    // Et on averti les admins
                    joueur.setGameMode(GameMode.SPECTATOR);
                    playerGroupe.sendToadmin(mineralcontest.prefixAdmin + joueur.getDisplayName() + " s'est connecté, il a été mis spectateur");

                }
                return;

            } else {
                for (Groupe groupe : mineralcontest.plugin.groupes)
                    if (groupe.havePlayerDisconnected(joueur))
                        groupe.playerHaveReconnected(joueur);
            }



            if (game == null) return;

            Bukkit.broadcastMessage("game is not null!");

            GameSettings settings = game.groupe.getParametresPartie();
            // We need to apply the pvp system
            if (settings.getCVAR("mp_enable_old_pvp").getValeurNumerique() == 1) {
                player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(1024d);
            } else {
                player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
            }

            Bukkit.broadcastMessage("pvp applied!");

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


        }*/
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
