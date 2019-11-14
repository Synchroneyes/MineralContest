package fr.mineral.Events;

import fr.mineral.Core.Equipe;
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
            p.kickPlayer("Une partie est déjà en cours");
            for(Player online : mineralcontest.plugin.getServer().getOnlinePlayers())
                if(online.isOp())
                    online.sendMessage(mineralcontest.prefixAdmin + "Le joueur " + p.getDisplayName() + " a tenté de se connecter alors qu'une partie est déjà en cours");
            mineralcontest.plugin.getServer().getLogger().info(mineralcontest.prefixAdmin + "Le joueur " + p.getDisplayName() + " a tenté de se connecter alors qu'une partie est déjà en cours");
        }


        // Lorsque le premier joueur se connecte, on génère tous les points de spawn etc ...
        if(mineralcontest.plugin.getServer().getOnlinePlayers().size() == 1 && !mineralcontest.plugin.getGame().isGameInitialized){
            try {

                // 111 168 -166
                Block block = mineralcontest.plugin.getServer().getWorld("world").getBlockAt(111, 168, -166);

                if(block == null || !block.getType().toString().equalsIgnoreCase("iron_block")){
                    ConsoleCommandSender console = mineralcontest.plugin.getServer().getConsoleSender();
                    mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixErreur + "Mauvaise map chargée, merci de télécharger la bonne map. Disponible sur le github");
                    mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixErreur + "http://github.com/jaunefra/mineralcontest");
                    mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixErreur + "Désactivation du plugin ...");
                    Bukkit.getPluginManager().disablePlugin(mineralcontest.plugin);
                }else{

                    /*if(!mineralcontest.isGameInitialized) {
                        FileToGame fg = new FileToGame();
                        fg.readFile(mineralcontest.currentWorld);

                        Bukkit.getWorld("world").setAutoSave(false);
                        mineralcontest.isGameInitialized = true;
                    }*/

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
                    p.kickPlayer("Erreur: Toutes les équipes sont déjà pleine.");
                } else {
                    for(Player online : mineralcontest.plugin.getServer().getOnlinePlayers()) {

                        if(online.isOp()) {
                            online.sendMessage(mineralcontest.prefixPrive + "Le joueur " + ChatColor.RED + p.getDisplayName()  + ChatColor.WHITE + " vient de se connecté et il ne fait pas partie d'une équipe.");
                            online.sendMessage(mineralcontest.prefixPrive + "Il peut rejoindre l'équipe " + teamNonPleine.getCouleur() + teamNonPleine.getNomEquipe());
                            online.sendMessage(mineralcontest.prefixPrive + "Pour se faire, veuillez taper " + ChatColor.RED + "/switch " + p.getDisplayName() + " " + teamNonPleine.getNomEquipe());
                        }
                    }
                }
            }
        }





    }

}
