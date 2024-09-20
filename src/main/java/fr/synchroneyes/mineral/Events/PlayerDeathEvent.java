package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.custom_events.PlayerDeathByPlayerEvent;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.Kits.Classes.Mineur;
import fr.synchroneyes.mineral.Shop.ShopManager;
import fr.synchroneyes.mineral.Statistics.Class.KillStat;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.Radius;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.LinkedList;

public class PlayerDeathEvent implements Listener {


    /**
     * FOnction appelé à la mort du joueur
     */
    @EventHandler
    public void onPlayerDeath(org.bukkit.event.entity.PlayerDeathEvent event) {
        Player joueur = event.getEntity();

        // On vérifie que le joueur fasse partie du plugin
        if (mineralcontest.isInAMineralContestWorld(joueur)) {
            // Le joueur fait parti du plugin, on vérifie si il est dans une partie ou non
            Game partie = mineralcontest.getPlayerGame(joueur);

            if (partie == null) return;

            // Il est dans une partie, on vérifie si elle a démarré ou non
            if (partie.isGameStarted()) {

                // Dans le cas où le joueur est tué par un mob
                if(joueur.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                    EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) joueur.getLastDamageCause();

                    // On peut vérifier si ça vient d'un boss
                    if(partie.getBossManager().wasPlayerKilledByBoss(entityDamageByEntityEvent.getDamager())) {
                        // Le meurtre provient d'un boss ! :(
                        partie.getBossManager().fireBossMadeKill(entityDamageByEntityEvent.getDamager(), joueur);
                    }
                }

                if(joueur.getKiller() == null) event.setDeathMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.player_died.toString(), joueur));
                else event.setDeathMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.player_killed.toString(), joueur, joueur.getKiller()));

                // On doit clear les drops seulement si le joueur est proche de l'arène (zone protégé) et que les coffres sont activé
                int radiusProtection = partie.groupe.getParametresPartie().getCVAR("protected_zone_area_radius").getValeurNumerique();
                Location arenaCenter = partie.getArene().getCoffre().getLocation();

                if(partie.groupe.getParametresPartie().getCVAR("drop_chest_on_death").getValeurNumerique() == 1 && !Radius.isBlockInRadius(arenaCenter, joueur.getLocation(), radiusProtection))
                    event.getDrops().clear();

                // On retire les barrieres du mineur des drops
                event.getDrops().removeIf(item -> item.isSimilar(Mineur.getBarrierItem()));
                event.getDrops().removeIf(item -> item.getType() == Material.POTION);
                event.getDrops().removeIf(ShopManager::isAnShopItem);


                // Liste des items à drop
                LinkedList<Material> item_a_drop = new LinkedList<Material>();
                item_a_drop.add(Material.IRON_INGOT);
                item_a_drop.add(Material.GOLD_INGOT);
                item_a_drop.add(Material.DIAMOND);
                item_a_drop.add(Material.EMERALD);
                item_a_drop.add(Material.IRON_ORE);
                item_a_drop.add(Material.GOLD_ORE);
                item_a_drop.add(Material.DIAMOND_ORE);
                item_a_drop.add(Material.EMERALD_ORE);
                item_a_drop.add(Material.IRON_ORE);
                item_a_drop.add(Material.GOLD_ORE);
                item_a_drop.add(Material.EMERALD_ORE);
                item_a_drop.add(Material.DIAMOND_ORE);

                item_a_drop.add(Material.POTION);
                item_a_drop.add(Material.REDSTONE);
                item_a_drop.add(Material.ENCHANTED_BOOK);


                // On doit gérer mp_enable_item_drop
                switch(partie.groupe.getParametresPartie().getCVAR("mp_enable_item_drop").getValeurNumerique()) {
                    // Dans le cas où c'est 0, on drop rien
                    case 0: event.getDrops().clear(); break;
                    // Dans le cas où c'est 1, on drop uniquement les minerais
                    case 1: event.getDrops().removeIf(item -> !item_a_drop.contains(item.getType())); break;
                }


                MCPlayer mcPlayer = mineralcontest.plugin.getMCPlayer(joueur);

                // On execute ces actions 1 tick plus tard
                if(mcPlayer != null) {
                    mcPlayer.cancelDeathEvent();
                }

                Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> partie.getArene().getDeathZone().add(joueur), 1);

                PlayerDeathByPlayerEvent event1 = new PlayerDeathByPlayerEvent(joueur, joueur.getKiller(), partie);
                Bukkit.getPluginManager().callEvent(event1);

                // On gère la mort du joueur; savoir si on donne des points ou non
                int nombre_points_bonus = partie.groupe.getParametresPartie().getCVAR("points_per_kill").getValeurNumerique();

                // si le nombre de points > 0
                if(nombre_points_bonus > 0 && event.getEntity().getKiller() != null && !event.getEntity().getKiller().equals(event.getEntity())) {
                    // Si il n'y a pas de teamkill
                    if(partie.getPlayerTeam(joueur) != partie.getPlayerTeam(joueur.getKiller())) {
                        // On ajoute des points à l'équipe ayant fait le kill
                        Equipe equipe_tueuse = partie.getPlayerTeam(joueur.getKiller());
                        int teamScore = equipe_tueuse.getScore();
                        teamScore += nombre_points_bonus;
                        equipe_tueuse.setScore(teamScore);

                        // On envoie un message à'léquie
                        equipe_tueuse.sendMessage(mineralcontest.prefixTeamChat + "Vous avez reçu " + nombre_points_bonus + " points grâce au kill fait par " + joueur.getKiller().getDisplayName());
                    }
                }


                // On enregistre les stats, au cas où ...
                if (joueur.getKiller() != null) {
                    // Si le joueur a été tué par un autre joueur
                    partie.getStatsManager().register(KillStat.class, joueur, joueur.getKiller());
                } else {
                    partie.getStatsManager().register(KillStat.class, joueur, joueur);

                }

            }


        }
    }
}
