package fr.synchroneyes.mineral.Commands;

import fr.synchroneyes.groups.Commands.CommandTemplate;
import fr.synchroneyes.mineral.Core.Boss.Boss;
import fr.synchroneyes.mineral.Core.Boss.BossType.AngryPumba;
import fr.synchroneyes.mineral.Core.Boss.BossType.CrazyZombie;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

/**
 * Commande permettant de tester les fonctionnalité Halloween
 */
public class Halloween extends CommandTemplate {



    private static int compteur = 1;


    public Halloween() {
        accessCommande.add(PLAYER_COMMAND);
        accessCommande.add(GAME_STARTED);
        accessCommande.add(GROUP_REQUIRED);
        accessCommande.add(GROUP_ADMIN);


    }

    @Override
    public String getCommand() {
        return "halloween";
    }

    @Override
    public String getDescription() {
        return "null";
    }

    @Override
    public String getPermissionRequise() {
        return "null";
    }

    @Override
    public boolean performCommand(CommandSender commandSender, String command, String[] args) {

        Player joueur = (Player) commandSender;
        Game playerGame = mineralcontest.getPlayerGame(joueur);
        playerGame.getBossManager().spawnHalloweenBoss();

        /*if(boucle != null && bar != null && zombie != null) {
            boucle.cancel();
            bar.removeAll();
            zombie.remove();

        }

        bar = Bukkit.createBossBar("TEST MECHANT MOB", BarColor.RED, BarStyle.SOLID );

        bar.setProgress(0);
        bar.setVisible(true);
        Player joueur = (Player) commandSender;

        zombie = (Zombie) joueur.getWorld().spawnEntity(joueur.getLocation(), EntityType.ZOMBIE);
        zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(500);
        zombie.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(15);
        zombie.setCustomNameVisible(true);
        zombie.setCustomName("BIG BOSS");
        zombie.setGlowing(true);
        zombie.setHealth(500);

        for(Player j : joueur.getWorld().getPlayers()) bar.addPlayer(j);

        boucle = Bukkit.getScheduler().runTaskTimer(mineralcontest.plugin, () -> {
            bar.setProgress( zombie.getHealth() / (zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() )
            );

            if(!zombie.isDead()) {
                List<Entity> entites = zombie.getNearbyEntities(5,5,5);
                entites.removeIf(entite -> !(entite instanceof Player));
                Bukkit.broadcastMessage(entites.size() + "");


                if(!entites.isEmpty() && entites.get(0) instanceof Player) {
                    zombie.setTarget((LivingEntity)entites.get(0));
                    Bukkit.broadcastMessage("Targetting " + ((Player)entites.get(0)).getDisplayName());
                }
            }

            if(compteur % 150 == 0) {
                for(int i = 0; i < 3; ++i) {
                    zombie.getWorld().spawnEntity(zombie.getLocation(), EntityType.SPIDER);
                }

                zombie.getWorld().playEffect(zombie.getLocation(), Effect.END_GATEWAY_SPAWN, 1);
            }

            if(zombie == null) {
                bar.removeAll();
                boucle.cancel();
            }

            if(zombie.isDead()) {
                zombie.remove();
                bar.removeAll();
                boucle.cancel();
            }

            compteur++;


        }, 0, 5);*/




        return false;
    }
}
