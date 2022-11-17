package fr.synchroneyes.special_events.halloween2022.events;

import fr.synchroneyes.custom_events.MCGameStartedEvent;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.mineralcontest;
import fr.synchroneyes.special_events.halloween2022.utils.Vector3D;
import fr.synchroneyes.special_events.halloween2022.weapons.ShulkerStick;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;

import java.util.HashMap;

public class PlayerInteractEvent implements Listener {

    private HashMap<Player, Integer> playerCooldown = new HashMap<>();


    // source https://bukkit.org/threads/get-player-in-line-of-sight.146323/#post-1664622
    @EventHandler
    public void onPlayerLeftClick(org.bukkit.event.player.PlayerInteractEvent event){

        if(event.getAction() == Action.LEFT_CLICK_AIR) {
            Player p = event.getPlayer();

            Game partie = mineralcontest.getPlayerGame(p);
            int tempsPartie = partie.getTempsPartie();



            if(playerCooldown.containsKey(p) && (playerCooldown.get(p)-ShulkerStick.cooldownBetweenAttacks()) < tempsPartie) {
                p.sendMessage(mineralcontest.prefixErreur + "Vous ne pouvez pas utiliser cet objet pour le moment...");
                return;
            }


            if(p.getInventory().getItemInMainHand().equals(ShulkerStick.getItem())) {


                // ----------------------

                Location observerPos = p.getEyeLocation();
                Vector3D observerDir = new Vector3D(observerPos.getDirection());

                Vector3D observerStart = new Vector3D(observerPos);
                // distance = 100mn
                Vector3D observerEnd = observerStart.add(observerDir.multiply(100));

                Player hit = null;

                // Get nearby entities
                for (Player target : p.getWorld().getPlayers()) {
                    // Bounding box of the given player
                    Vector3D targetPos = new Vector3D(target.getLocation());
                    Vector3D minimum = targetPos.add(-0.5, 0, -0.5);
                    Vector3D maximum = targetPos.add(0.5, 1.67, 0.5);

                    if (target != p && hasIntersection(observerStart, observerEnd, minimum, maximum)) {
                        if (hit == null ||
                                hit.getLocation().distanceSquared(observerPos) >
                                        target.getLocation().distanceSquared(observerPos)) {

                            hit = target;
                        }
                    }
                }


                if(hit != null) {
                    ShulkerBullet bullet = (ShulkerBullet) p.getWorld().spawnEntity(p.getLocation(), EntityType.SHULKER_BULLET);
                    bullet.setGlowing(true);
                    playerCooldown.putIfAbsent(p, tempsPartie);
                    playerCooldown.replace(p, tempsPartie);
                }
            }
        }
    }


    // source https://bukkit.org/threads/get-player-in-line-of-sight.146323/#post-1664622
    private boolean hasIntersection(Vector3D p1, Vector3D p2, Vector3D min, Vector3D max) {
        final double epsilon = 0.0001f;

        Vector3D d = p2.subtract(p1).multiply(0.5);
        Vector3D e = max.subtract(min).multiply(0.5);
        Vector3D c = p1.add(d).subtract(min.add(max).multiply(0.5));
        Vector3D ad = d.abs();

        if (Math.abs(c.x) > e.x + ad.x)
            return false;
        if (Math.abs(c.y) > e.y + ad.y)
            return false;
        if (Math.abs(c.z) > e.z + ad.z)
            return false;

        if (Math.abs(d.y * c.z - d.z * c.y) > e.y * ad.z + e.z * ad.y + epsilon)
            return false;
        if (Math.abs(d.z * c.x - d.x * c.z) > e.z * ad.x + e.x * ad.z + epsilon)
            return false;
        if (Math.abs(d.x * c.y - d.y * c.x) > e.x * ad.y + e.y * ad.x + epsilon)
            return false;

        return true;
    }
}

