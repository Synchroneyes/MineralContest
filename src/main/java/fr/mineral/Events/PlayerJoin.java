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


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws Exception {
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

        /*
                Si la partie n'a pas encore démarré, et que le joueur se connecte au serveur, alors
                on le téléporte sur la plateforme
                ET on le remet à zero
         */

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
        /*if(mineralcontest.plugin.getGame().votemap.isVoteEnded()) {
            if(!game.havePlayerDisconnected(p.getDisplayName()) && !p.isOp()) {
                p.kickPlayer(Lang.kick_game_already_in_progress.toString());
            } else if(p.isOp()) {
                // Sinon, on le met arbitre
                mineralcontest.plugin.getGame().addReferee(p);
            }
        }*/


            if (!game.isGameStarted())
                mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.hud_awaiting_players.toString());


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
    }
}
