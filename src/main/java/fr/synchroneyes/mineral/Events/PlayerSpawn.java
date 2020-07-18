package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.Referee.Referee;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Utils.Player.PlayerUtils;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerSpawn implements Listener {


    /**
     * Evenement appelé lors du respawn d'un joueur, cette fonction est appelé lorsqu'un joueur meurs d'une façon minecraft (avec écran de respawn)
     *
     * @param e
     */
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player joueur = e.getPlayer();

        // On ne s'occupe que des joueurs du plugin
        if (mineralcontest.isInAMineralContestWorld(joueur)) {
            // On récupère la partie du joueur
            Groupe playerGroupe = mineralcontest.getPlayerGroupe(joueur);

            // Si le groupe du joueur est null, c'est qu'il est mort dans le lobby
            if (playerGroupe == null) {
                teleportToLobby(joueur);
                return;
            }

            // Le groupe du joueur n'est pas nul, deux cas de figure:
            /*
                - Le joueur est dans une partie en cours
                    - Il est arbitre
                    - Il est dans une équipe
                    - Il n'a pas de team ????
                - Le joueur n'est pas encore dans une partie
                    - Il est dans le hub
                    - Il est dans son monde
            */

            // On va traiter le cas où le joueur est dans une partie
            Game playerGame = playerGroupe.getGame();
            if (playerGame == null) {
                teleportToLobby(joueur);
                return;
            }

            // On récupère l'état de la partie
            if (playerGame.isGameStarted()) {
                // La partie est démarré
                // On récupère l'équipe du joueur
                Equipe playerTeam = playerGame.getPlayerTeam(joueur);

                // Si il n' pas d'équipe, soit il est arbitre, soit il n'a pas d'équipe, makes sense mdr
                // De toute façon, personne ne lit mes commentaires
                if (playerTeam == null) {
                    // Deux cas, il n'a pas de team OU il est arbitre
                    if (playerGame.isReferee(joueur)) {
                        // Le joueur est arbitre
                        // Et on lui donne son livre
                        joueur.getInventory().addItem(Referee.getRefereeItem());
                    }
                    // On le TP au centre de l'arène
                    PlayerUtils.teleportPlayer(joueur, playerGroupe.getMonde(), playerGame.getArene().getCoffre().getLocation());
                    return;
                }

                // Le joueur possède une équipe ... On l'ajoute à la deathzone
                playerGame.getArene().getDeathZone().add(joueur);
                return;


            } else {
                // La partie n'est pas démarré
                // On regarde si on est dans le hub ou non
                if (mineralcontest.isInMineralContestHub(joueur)) {
                    // Le joueur est dans le hub, on le re tp
                    PlayerUtils.teleportPlayer(joueur, mineralcontest.plugin.pluginWorld, mineralcontest.plugin.defaultSpawn);
                    return;
                } else {
                    // Le joueur est dans son monde, on le TP au centre de l'arène
                    if (playerGroupe.getMonde() == null) {
                        // En fait il est dans le hub ... Donc on le TP au hub
                        Bukkit.getLogger().warning("[MC] A player is in a group, but should be in a loaded world, but in fact is in the hub. That's weird and should not happen. Please inform the staff");
                        PlayerUtils.teleportPlayer(joueur, mineralcontest.plugin.pluginWorld, mineralcontest.plugin.defaultSpawn);
                        return;
                    }

                    // On TP le joueur dans le centre de l'arène
                    PlayerUtils.teleportPlayer(joueur, playerGroupe.getMonde(), playerGame.getArene().getCoffre().getLocation());
                }
            }

        }

    }

    private void teleportToLobby(Player joueur) {
        Location hubSpawnLocation = mineralcontest.plugin.defaultSpawn;
        PlayerUtils.getPluginWorld();
        hubSpawnLocation = mineralcontest.plugin.defaultSpawn;
        PlayerUtils.teleportPlayer(joueur, hubSpawnLocation.getWorld(), hubSpawnLocation);
    }

        /*World worldEvent = e.getPlayer().getWorld();
        if (mineralcontest.isAMineralContestWorld(worldEvent)) {
            Game partie = mineralcontest.getWorldGame(worldEvent);

            if (partie != null)
                if (partie.isReferee(e.getPlayer()))
                    e.getPlayer().getInventory().setItemInMainHand(Referee.getRefereeItem());


            if (partie != null && partie.isGameStarted()) {
                // Si la game est démarrée
                Player joueur = e.getPlayer();
                // Si le joueur était dans la deathzone
                PlayerUtils.resetPlayerDeathZone(joueur);
                return;

            }


            new BukkitRunnable() {
                @Override
                public void run() {
                    // On le TP au centre de l'arène
                    if (partie != null && partie.groupe.getMonde() != null) {
                        Player joueur = e.getPlayer();
                        joueur.teleport(partie.groupe.getMonde().getSpawnLocation());
                    }
                }
            }.runTaskLater(mineralcontest.plugin, 20);


        }

    }*/
}
