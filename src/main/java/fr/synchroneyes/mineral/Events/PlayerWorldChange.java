package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.custom_events.MCPlayerWorldChangeEvent;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.House;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Utils.ErrorReporting.Error;
import fr.synchroneyes.mineral.Utils.Log.GameLogger;
import fr.synchroneyes.mineral.Utils.Log.Log;
import fr.synchroneyes.mineral.Utils.Pair;
import fr.synchroneyes.mineral.Utils.Player.PlayerUtils;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerWorldChange implements Listener {


    private HashMap<Player, Pair<Location, Location>> oldPlayerTeleport = new HashMap<>();


    /**
     * Dans cette méthode; il est necessaire de sauvegarder la position du joueur d'où il vient
     * Dans le cas où event.getTo().getWorld() == null; on refait un check quelqs tick plus tard
     * @param event
     */
    @EventHandler
    public void OnPlayerTeleport(PlayerTeleportEvent event) {
        Player joueur = event.getPlayer();

        // Seulement si on est dans le plugin

            // Il est possible que event.getTo().getWorld() soit null. Dans ce cas; on attendra un tick; et on réessaiera

            // Dans le cas où le monde est pas encore chargé
            if(event.getTo().getWorld() == null) {
                Bukkit.getScheduler().runTaskTimer(mineralcontest.plugin, () -> {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "event.getTo().getWorld() == null");
                    if(event.getTo().getWorld() == null) return;
                    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "event.getTo().getWorld() != null");

                    // Monde chargé!
                    callMCPlayerChangeWorldEvent(event.getFrom(), event.getTo(), joueur);

                }, 0, 1);
            } else {
                // Le monde n'est pas null
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "WORLD NOT NULL BY DEFAULT, " + event.getTo().getWorld());
                callMCPlayerChangeWorldEvent(event.getFrom(), event.getTo(), joueur);
            }

    }

    @EventHandler
    public void onPlayerchangeWorld(MCPlayerWorldChangeEvent event) {


    }


    /**
     * Méthode qui simule une déconnxion du joueur
     * @param playerToDisconnect
     */
    private void disconnectPlayer(Player playerToDisconnect, Location previous_player_location) {

        World oldWorld = previous_player_location.getWorld();

        if (mineralcontest.isAMineralContestWorld(oldWorld)) {
            Player joueur = playerToDisconnect;
            Game partie = mineralcontest.getWorldGame(oldWorld);


            GameLogger.addLog(new Log("PlayerDisconnect", "Player " + joueur.getDisplayName() + " disconnected", "player_disconnect"));

            if (partie == null && mineralcontest.communityVersion) return;

            if (!mineralcontest.communityVersion) {

                if (partie == null) return;

                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + " L65");
                partie.groupe.addDisconnectedPlayer(joueur, joueur.getLocation());

                mineralcontest.plugin.getNonCommunityGroup().retirerJoueur(joueur);
                return;
            }
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + " L71");

            partie.groupe.addDisconnectedPlayer(joueur, previous_player_location);
            partie.groupe.retirerJoueur(joueur);



            Equipe team = partie.getPlayerTeam(joueur);
            House house = partie.getPlayerHouse(joueur);


            if (partie.isPlayerReady(joueur)) partie.removePlayerReady(joueur);

            try {
                if (joueur.isOp())
                    if (partie.isReferee(joueur)) partie.removeReferee(joueur, false);
            }catch (Exception e) {
                e.printStackTrace();
            }


            if ((partie.isGameStarted() || partie.isPreGame())) {
                //partie.pauseGame();

                if (house != null) house.getPorte().forceCloseDoor();
            }


            if (team != null)
                team.removePlayer(joueur);

            // On supprime le joueur du plugin
            // On ajoute le joueur au plugin
            mineralcontest.plugin.removePlayer(joueur);



            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
                int number_of_player_online = partie.groupe.getPlayers().size();
                if (number_of_player_online == 0) {
                    partie.resetMap();
                    if (partie.isGameStarted()) {
                        try {
                            partie.terminerPartie();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Error.Report(e, partie);
                        }
                    }
                }
            }, 20 * 5);

            joueur.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4d);

            joueur.getScoreboard().getEntries().clear();
            joueur.getScoreboard().getObjectives().clear();
            joueur.getScoreboard().getTeams().clear();


        }
    }

    /**
     * Méthode qui simule une connexion du joueur
     * @param playerToLogin
     */
    private void loginPlayer(Player playerToLogin, Location newLocation){
        World worldEvent = newLocation.getWorld();

        playerToLogin.sendMessage("mineralcontest.isAMineralContestWorld(worldEvent) => " + (mineralcontest.isAMineralContestWorld(worldEvent)) + " | " + worldEvent.toString());

        // On vérifie si c'est un monde du plugin
        if (mineralcontest.isAMineralContestWorld(worldEvent)) {

            Player joueur = playerToLogin;

            // On ajoute le joueur au plugin
            mineralcontest.plugin.addNewPlayer(joueur);

            // On applique le système de pvp au joueur
            PlayerUtils.applyPVPtoPlayer(joueur);

            // On est dans un monde mineral contest
            // On doit d'abord vérifier si le joueur s'est déconnecté plus tot
            for (Groupe groupe : mineralcontest.plugin.getGroupes()) {

                playerToLogin.sendMessage("groupe.havePlayerDisconnected(joueur) => " + (groupe.havePlayerDisconnected(joueur)));
                // Si le joueur s'était déconnecté avant
                if (groupe.havePlayerDisconnected(joueur)) {
                    // On le reconnecte, tout va bien
                    groupe.playerHaveReconnected(joueur);
                    Bukkit.getLogger().info("PLAYER RECONNECTED");
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


    private void callMCPlayerChangeWorldEvent(Location from, Location to, Player p) {
        if(mineralcontest.isAMineralContestWorld(from.getWorld()) || mineralcontest.isAMineralContestWorld(to.getWorld())){

            // On vérifie que la TP actuelle n'est pas la même que celle déjà faite juste avant
            if(oldPlayerTeleport.get(p) != null) {
                Pair<Location, Location> oldLocation = oldPlayerTeleport.get(p);

                if(oldLocation.getKey().equals(from) || oldLocation.getValue().equals(to)) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "TELEPORTING TO OLD POSITION " + System.currentTimeMillis());
                    return;
                } else {
                    oldPlayerTeleport.put(p, new Pair<Location, Location>(from, to));
                }

            } else {
                oldPlayerTeleport.put(p, new Pair<Location, Location>(from, to));
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "ADDED PLAYER LOCATION " + System.currentTimeMillis());
            }


            MCPlayerWorldChangeEvent event = new MCPlayerWorldChangeEvent(from, to, p);
            Bukkit.getPluginManager().callEvent(event);
        }

    }
}
