package fr.mineral.Events;

import fr.mineral.Core.Game.Game;
import fr.mineral.Core.House;
import fr.mineral.Utils.Door.DisplayBlock;
import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.Utils.Radius;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

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
                if(playerTeam != null && !game.isReferee(event.getPlayer())) {
                    for (House house : game.getHouses()) {
                        if (playerTeam != house) {
                            try {

                                for (DisplayBlock blockDePorte : house.getPorte().getPorte()) {
                                    Location locationblock = blockDePorte.getBlock().getLocation();
                                    if (Radius.isBlockInRadiusWithDividedYAxis(locationblock, event.getTo(), houseRadius, 2)) {

                                        //pushPlayerBack(event.getPlayer(), locations, multiplier);
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
