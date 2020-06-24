package fr.synchroneyes.custom_events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.TimerTask;

public class PermissionCheckerLoop implements Runnable {

    int secondsDelay = 2;


    private JavaPlugin plugin;

    public PermissionCheckerLoop(JavaPlugin plugin, int delayBetweenEachCheck) {
        this.plugin = plugin;
        this.secondsDelay = delayBetweenEachCheck;
    }

    // Variable qui définit le temps entre chaque vérif

    @Override
    public synchronized void run() {
        // Liste des joueurs avec les perms d'admin <Joueur, boolean estOP>
        HashMap<Player, Boolean> playerCurrentPerm = new HashMap<>();

        for (Player online_player : plugin.getServer().getOnlinePlayers())
            playerCurrentPerm.put(online_player, online_player.isOp());

        new BukkitRunnable() {
            @Override
            public synchronized void run() {
                // On regarde chaque joueur
                for (Player online_player : plugin.getServer().getOnlinePlayers()) {

                    // Si le joueur est déjà stocké dans nos joueurs
                    if (playerCurrentPerm.containsKey(online_player)) {

                        boolean currentSavedOp = playerCurrentPerm.get(online_player);

                        // Si la valeur stocké dans notre liste est différente, ça veut dire qu'il a changé de permission !
                        if (!playerCurrentPerm.get(online_player).equals(online_player.isOp())) {
                            PlayerPermissionChangeEvent event = new PlayerPermissionChangeEvent(online_player, opToString(currentSavedOp), opToString(online_player.isOp()));
                            Bukkit.getPluginManager().callEvent(event);
                            playerCurrentPerm.replace(online_player, online_player.isOp());
                        }

                    } else {
                        // Le joueur n'est pas stocké, on l'ajoute
                        playerCurrentPerm.put(online_player, online_player.isOp());
                    }
                }
            }
        }.runTaskTimer(plugin, 0, secondsDelay * 20);
    }

    public String opToString(boolean op) {
        return (op) ? "op" : "non_op";
    }
}
