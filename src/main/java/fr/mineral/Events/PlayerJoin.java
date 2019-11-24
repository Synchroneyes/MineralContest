package fr.mineral.Events;

import fr.mineral.Teams.Equipe;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.Player.CouplePlayerTeam;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;



public class PlayerJoin implements Listener {


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws Exception {
        Player p = event.getPlayer();

        // SI la game n'a pas démarré et que tout le monde est connecté
        if(!mineralcontest.plugin.getGame().isGameStarted() && mineralcontest.plugin.getServer().getOnlinePlayers().size() == mineralcontest.teamMaxPlayers * 3){
            mineralcontest.plugin.getGame().votemap.enableVote();
        }

        if(mineralcontest.plugin.getGame().isGameStarted() && !mineralcontest.plugin.getGame().isGamePaused()) {
            p.kickPlayer(Lang.kick_game_already_in_progress.toString());
            for(Player online : mineralcontest.plugin.getServer().getOnlinePlayers())
                if(online.isOp())
                    online.sendMessage(mineralcontest.prefixAdmin + Lang.translate(Lang.admin_played_tried_to_login.toString(), p));
            mineralcontest.plugin.getServer().getLogger().info(mineralcontest.prefixAdmin + Lang.translate(Lang.admin_played_tried_to_login.toString(), p));
        }


        // Lorsque le premier joueur se connecte, on génère tous les points de spawn etc ...
        if(mineralcontest.plugin.getServer().getOnlinePlayers().size() == 1 && !mineralcontest.plugin.getGame().isGameInitialized){
            try {

                // 111 168 -166
                Block block = mineralcontest.plugin.getServer().getWorld("world").getBlockAt(111, 168, -166);

                if(block == null || !block.getType().toString().equalsIgnoreCase("iron_block")){
                    ConsoleCommandSender console = mineralcontest.plugin.getServer().getConsoleSender();
                    mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixErreur + Lang.bad_map_loaded.toString());
                    mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixErreur + Lang.github_link.toString());
                    mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixErreur + Lang.plugin_shutdown.toString());
                    Bukkit.getPluginManager().disablePlugin(mineralcontest.plugin);
                }



            }catch (Exception e) {
                e.printStackTrace();
            }
        }


        if(mineralcontest.plugin.getGame().isGameStarted() && mineralcontest.plugin.getGame().isGamePaused()) {
            // On regarde si le joueur connecté était un joueur qui s'est déconnecté
            if(mineralcontest.plugin.getGame().havePlayerDisconnected(p.getDisplayName())) {
                // Il s'était déconnecté
                CouplePlayerTeam infoJoueur = mineralcontest.plugin.getGame().getDisconnectedPlayerInfo(p.getDisplayName());
                try {
                    // On le remet dans son équipe automatiquement
                    infoJoueur.getTeam().addPlayerToTeam(p);
                    mineralcontest.plugin.getGame().resumeGame();
                }catch(Exception e) {
                    e.printStackTrace();
                }
            } else {
                // Un nouveau joueur s'est connecté
                Equipe teamNonPleine = mineralcontest.plugin.getGame().getEquipeNonPleine();
                if(teamNonPleine == null) {
                    p.kickPlayer(Lang.kick_game_already_in_progress.toString());
                } else {
                    for(Player online : mineralcontest.plugin.getServer().getOnlinePlayers()) {

                        if(online.isOp()) {
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
