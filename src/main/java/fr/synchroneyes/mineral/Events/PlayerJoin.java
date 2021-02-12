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

            // On ajoute le joueur au plugin
            mineralcontest.plugin.addNewPlayer(joueur);

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


                // Si la game est démarré
                // On le met comme spectateur
                // Et on averti les admins
                // Si le joueur est OP, on le met comme arbitre


                // Si la game est démarrée
                if (defaultGroupe.getGame().isGameStarted()) {
                    if (joueur.isOp()) {
                        defaultGroupe.addAdmin(joueur);
                        defaultGroupe.getGame().addReferee(joueur);
                    } else {
                        // Sinon, il devient spectateur
                        defaultGroupe.addJoueur(joueur);
                        // On le TP au centre de l'arène si la partie est chargé
                        if (defaultGroupe.getMonde() != null && defaultGroupe.getGame() != null && defaultGroupe.getGame().getArene() != null && defaultGroupe.getGame().getArene().getCoffre() != null && defaultGroupe.getGame().getArene().getCoffre().getLocation() != null)
                            PlayerUtils.teleportPlayer(joueur, defaultGroupe.getMonde(), defaultGroupe.getGame().getArene().getCoffre().getLocation());

                        joueur.setGameMode(GameMode.SPECTATOR);

                        // Et on rend les autres joueurs visible par ce spectateur 5 secondes après sa connexion
                        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
                            for (Player membre_groupe : defaultGroupe.getPlayers())
                                joueur.showPlayer(mineralcontest.plugin, membre_groupe);
                        }, 5 * 20);

                        defaultGroupe.sendToadmin(mineralcontest.prefixAdmin + "Le joueur " + joueur.getDisplayName() + " s'est connecté et a été mis en spectateur");
                    }
                } else {
                    // La partie n'est pas encore démarré
                    if (joueur.isOp()) {

                        // Le jouuer est OP
                        defaultGroupe.addAdmin(joueur);
                    } else {
                        // Le joueur n'est pas OP
                        defaultGroupe.addJoueur(joueur);
                    }
                }




            }

        }
    }



}
