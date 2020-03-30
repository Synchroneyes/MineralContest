package fr.mineral.Events;

import fr.mineral.Core.Game;
import fr.mineral.Core.House;
import fr.mineral.Teams.Equipe;
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
import org.bukkit.util.Vector;

public class PlayerMove implements Listener {
    private int houseRadius = 8;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        World worldEvent = event.getPlayer().getWorld();
        if(worldEvent.equals(mineralcontest.plugin.pluginWorld)) {
            Game game = mineralcontest.plugin.getGame();
            House playerTeam = game.getPlayerHouse(event.getPlayer());
            House[] houses = {game.getRedHouse(), game.getBlueHouse(), game.getYellowHouse()};
            if(playerTeam != null && !game.isReferee(event.getPlayer())) {
                for(House house : houses) {
                    if(playerTeam != house)
                        if(Radius.isBlockInRadiusWithDividedYAxis(house.getHouseLocation(), event.getPlayer().getLocation(), houseRadius, 2)) {
                            Location[] locations = {event.getFrom(), event.getTo()};
                            int multiplier = 1;
                            pushPlayerBack(event.getPlayer(), locations, multiplier);
                            return;
                        }
                }
            }

            if(mineralcontest.plugin.getGame().isGamePaused() || mineralcontest.plugin.getGame().isPreGameAndGameStarted()) {
                Location to = event.getFrom();
                to.setPitch(event.getTo().getPitch());
                to.setYaw(event.getTo().getYaw());
                event.setTo(to);
            }
        }

    }

    private void pushPlayerBack(Player player, Location[] locations, int multiplier) {

        if(player.getVelocity().getY() > 0) return;

        Vector from, to;
        from = locations[0].toVector();
        to = locations[1].toVector();
        Vector result = from.subtract(to);
        result = result.normalize();
        result.multiply(multiplier);
        result.setY(0.2);

        player.setVelocity(result);
    }



}
