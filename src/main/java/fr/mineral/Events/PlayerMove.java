package fr.mineral.Events;

import fr.mineral.Core.Game.Game;
import fr.mineral.Core.House;
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
    private int houseRadius = 8;
    private static HashMap<Player, Integer> playerPushedTimer = new HashMap<>();
    private int time_between_push = 1;


    public static synchronized void handlePushs() {
        new BukkitRunnable() {

            @Override
            public void run() {

                for (Player online : PlayerUtils.getPluginWorld().getPlayers()) {
                    Game game = mineralcontest.getPlayerGame(online);
                    if (game != null && game.isGameStarted() && !game.isReferee(online)) {
                            reducePlayerTimer(online);
                    }
                }
            }
        }.runTaskTimer(mineralcontest.plugin, 0, 5);

    }


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
                                if (Radius.isBlockInRadiusWithDividedYAxis(house.getHouseLocation(), event.getPlayer().getLocation(), houseRadius, 2)) {
                                    Location[] locations = {event.getFrom(), event.getTo()};
                                    int multiplier = 1;
                                    pushPlayerBack(event.getPlayer(), locations, multiplier);
                                    return;
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

    private static synchronized boolean isPlayerBeingPushed(Player p) {
        for(Map.Entry<Player, Integer> map : playerPushedTimer.entrySet())
            if(map.getKey().equals(p)) return true;
        return false;
    }

    private static synchronized void reducePlayerTimer(Player p) {
        if(!isPlayerBeingPushed(p)) return;
        int newTimer = playerPushedTimer.get(p) - 1;
        playerPushedTimer.replace(p, newTimer);
        if(newTimer <= 0) removePlayerFromBeingPushed(p);
    }

    private static synchronized void removePlayerFromBeingPushed(Player p) {
        playerPushedTimer.remove(p);
    }

    private synchronized void pushPlayerBack(Player player, Location[] locations, int multiplier) {

        if(isPlayerBeingPushed(player)) return;


        if(player.getVelocity().getY() > 0) return;

        Vector from, to;
        from = locations[0].toVector();
        to = locations[1].toVector();
        Vector result = from.subtract(to);
        result = result.normalize();
        result.multiply(multiplier);
        result.setY(0.2);
        player.setVelocity(result);

        playerPushedTimer.put(player, time_between_push);
    }



}
