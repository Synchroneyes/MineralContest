package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.House;
import fr.synchroneyes.mineral.Utils.Door.DisplayBlock;
import fr.synchroneyes.mineral.Utils.ErrorReporting.Error;
import fr.synchroneyes.mineral.Utils.Player.PlayerUtils;
import fr.synchroneyes.mineral.Utils.Radius;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

public class PlayerMove implements Listener {
    private int houseRadius = 2;
    private static HashMap<Player, Integer> playerPushedTimer = new HashMap<>();


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        World worldEvent = event.getPlayer().getWorld();
        if (mineralcontest.isAMineralContestWorld(worldEvent)) {
            Game game = mineralcontest.getPlayerGame(event.getPlayer());
            if (game != null && game.isGameStarted() && game.isGameInitialized) {
                House playerTeam = game.getPlayerHouse(event.getPlayer());
                if (playerTeam != null && !game.isReferee(event.getPlayer())) {
                    for (House house : game.getHouses()) {

                        // Pour optimiser, on va regarder si le joueur est proche ou non de la maison
                        // Si le joueur est trop loin, pas besoin de faire toutes les verifs


                        // On récupère la première position
                        Location firstDoorBlock = house.getPorte().getPorte().getFirst().getPosition();
                        Location playerLocation = event.getPlayer().getLocation();

                        // On regarde si le joueur n'est pas trop loin ...
                        double x = Math.pow(firstDoorBlock.getX() - playerLocation.getX(), 2.0);
                        double y = Math.pow(firstDoorBlock.getZ() - playerLocation.getZ(), 2.0);

                        // Le joueur est trop loin de la maison ...
                        if (Math.sqrt(x + y) >= (houseRadius + 3)) {
                            continue;
                        }


                        if (playerTeam != house || PlayerUtils.isPlayerInDeathZone(event.getPlayer())) {
                            try {


                                for (DisplayBlock blockDePorte : house.getPorte().getPorte()) {
                                    Location locationblock = blockDePorte.getBlock().getLocation();
                                    if (Radius.isBlockInRadiusWithDividedYAxis(locationblock, event.getTo(), houseRadius, 2)) {


                                        Location to = event.getFrom();
                                        to.setYaw(event.getTo().getYaw());
                                        to.setPitch(event.getTo().getPitch());
                                        event.getPlayer().teleport(to);
                                        return;
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Error.Report(e, mineralcontest.getPlayerGame(event.getPlayer()));
                            }
                        }
                    }
                }
            }


            if (mineralcontest.getPlayerGame(event.getPlayer()) != null && (mineralcontest.getPlayerGame(event.getPlayer()).isGamePaused() || mineralcontest.getPlayerGame(event.getPlayer()).isPreGameAndGameStarted())) {
                Location to = event.getFrom();
                to.setPitch(event.getTo().getPitch());
                to.setYaw(event.getTo().getYaw());
                event.setTo(to);
            }
        }

    }


}
