package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.custom_events.PlayerDeathByPlayerEvent;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Statistics.Class.KillStat;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.Player.PlayerUtils;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;



public class EntityDamage implements Listener {


    /**
     * Evenement appelé dès lors qu'un joueur reçoit un dégat, ou se blesse
     *
     * @param event
     */
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        // On ne traite que les joueurs
        if (event.getEntity() instanceof Player) {

            Player joueur = (Player) event.getEntity();
            Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);
            // On vérifie si on est dans un monde mineral contest ou non, si ce n'est pas le cas, les dégats on s'en fou
            if (!mineralcontest.isInAMineralContestWorld(joueur)) return;

            // Le joueur est dans un monde mineralcontest
            // On doit bloquer les dégats si la game du joueur n'est pas démarré ou si il est dans le lobby, ou si on est en pregame
            if (mineralcontest.isInMineralContestHub(joueur) || playerGroup == null || !playerGroup.getGame().isGameStarted() || playerGroup.getGame().isPreGame() || playerGroup.getGame().isGamePaused()) {
                event.setCancelled(true);
                return;
            }


            // On doit bloquer les dégats si ils sont causé par un autre joueur de la même équipe
            if (event instanceof EntityDamageByEntityEvent) {

                EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) event;

                // On vérifie les dégats fait par un joueur
                if (entityDamageByEntityEvent.getDamager() instanceof Player) {
                    Player attaquant = (Player) entityDamageByEntityEvent.getDamager();

                    if (playerGroup.getPlayerTeam(attaquant) == null) {
                        event.setCancelled(true);
                        return;
                    }

                    if (playerGroup.getPlayerTeam(joueur).equals(playerGroup.getPlayerTeam(attaquant))) {
                        // Si les deux sont de la même équipe et que les dégats entre coéquipier sont désactivé, on annule l'event
                        if (playerGroup.getParametresPartie().getCVAR("mp_enable_friendly_fire").getValeurNumerique() == 0) {
                            event.setCancelled(true);
                            return;
                        }
                    }
                }


                // On vérifie si le dégat a été causé par une flèche
                if (entityDamageByEntityEvent.getDamager() instanceof Arrow) {
                    Arrow fleche = (Arrow) entityDamageByEntityEvent.getDamager();

                    // On vérifie si le tireur est un joueur et non un squelette par ex
                    if (fleche.getShooter() instanceof Player) {

                        Player tireur = (Player) fleche.getShooter();
                        Equipe equipeTireur = playerGroup.getPlayerTeam(tireur);

                        // Et on vérifie si l'équipe du tireur est la même que la victime
                        if (equipeTireur != null && equipeTireur.equals(playerGroup.getPlayerTeam(joueur))) {

                            // SI c'est le cas, on vérifie si le teamkill est activé ou non
                            if (playerGroup.getParametresPartie().getCVAR("mp_enable_friendly_fire").getValeurNumerique() == 0) {
                                event.setCancelled(true);
                                return;
                            }
                        }
                    }
                }




                // Si le joueur a été blessé par un autre joueur ...
                if (entityDamageByEntityEvent.getDamager() instanceof Player || ((entityDamageByEntityEvent.getDamager() instanceof Arrow) && ((Arrow) entityDamageByEntityEvent.getDamager()).getShooter() instanceof Player)) {

                    // Si la personne recevant des dégats ouvrait le coffre d'arène, on lui ferme
                    if (joueur.equals(playerGroup.getGame().getArene().getCoffre().getOpeningPlayer())) {
                        // On ferme et on annule l'ouverture
                        playerGroup.getGame().getArene().getCoffre().closeInventory();
                        joueur.closeInventory();
                    }

                }
            }


            // On doit vérifier si le joueur est mort ou non ...
            // Un joueur est considéré comme mort SI, les dégats totaux qui seront infligé sont supérieur à la vie du joueur
            if (event.getFinalDamage() > joueur.getHealth()) {
                // Le joueur est désormais considéré comme mort ...
                // Deux cas de figure, le joueur a été tué, ou le joueur s'est suicidé.

                // Si cet event peut s'appliquer à un evenement appelé lors de dégats infligé par une autre entitée, ex: une attaque par un autre joueur
                if (event instanceof EntityDamageByEntityEvent) {
                    EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) event;

                    // Si la personne ayant fait des dégats est un joueur
                    if (entityDamageByEntityEvent.getDamager() instanceof Player) {

                        // On annule l'event, on enregistre le kill, et on ajoute la personne venant de mourir à la deathzone, elle sera donc automatiquement
                        // TP dans sa base avec les effets de mort
                        Player attaquant = (Player) entityDamageByEntityEvent.getDamager();
                        PlayerDeathByPlayerEvent playerDeathByPlayerEvent = new PlayerDeathByPlayerEvent(joueur, attaquant);
                        Bukkit.getPluginManager().callEvent(playerDeathByPlayerEvent);
                        registerKill(joueur, attaquant);
                        event.setCancelled(true);
                        return;
                    }

                    // On peut également vérifier si le joueur est morte d'une flèche ...
                    if (entityDamageByEntityEvent.getDamager() instanceof Projectile) {
                        // Le joueur est mort d'une fleche
                        Projectile fleche = (Projectile) entityDamageByEntityEvent.getDamager();

                        // Si le projectile a été tirée par un joueur ...
                        if (fleche.getShooter() instanceof Player) {
                            Player attaquant = (Player) fleche.getShooter();

                            PlayerDeathByPlayerEvent playerDeathByPlayerEvent = new PlayerDeathByPlayerEvent(joueur, attaquant);
                            Bukkit.getPluginManager().callEvent(playerDeathByPlayerEvent);

                            registerKill(joueur, attaquant);
                            event.setCancelled(true);
                            return;
                        }

                        // Sinon, la flèche a été tirée par un mob...
                        registerPlayerDeadByEntity(joueur);
                        event.setCancelled(true);
                        return;
                    }

                    // Sinon, le joueur a été tué par une entité
                    registerPlayerDeadByEntity(joueur);
                    event.setCancelled(true);
                    return;
                }

                // Si on arrive là, le joueur est mort d'un suicide
                registerPlayerSuicide(joueur);
                event.setCancelled(true);
                return;


            }

        }
    }


    /**
     * Fonction permettant d'enregistrer un kill entre deux joueurs, ajoute aussi la personne morte dans la deathzone
     *
     * @param dead
     * @param attacker
     */
    private void registerKill(Player dead, Player attacker) {
        if (mineralcontest.getPlayerGame(dead) == null) return;
        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.player_killed.toString(), dead, attacker), mineralcontest.getPlayerGame(dead).groupe);

        Game partie = mineralcontest.getPlayerGame(dead);
        if (partie != null && partie.isGameStarted()) {
            partie.getStatsManager().register(KillStat.class, attacker, dead);

        }


        if (partie != null) partie.getPlayerBonusManager().triggerEnabledBonusOnPlayerKillerKilled(dead);

        // On tue le joueur
        PlayerUtils.killPlayer(dead);

        attacker.playSound(attacker.getLocation(), Sound.ENTITY_CHICKEN_DEATH, 1, 1);


        mineralcontest.getPlayerGame(dead).killCounter++;
    }


    /**
     * Fonction permettant d'enregistrer un suicide par un joueur
     *
     * @param dead - Le joueur mort
     */
    private void registerPlayerSuicide(Player dead) {
        Game partie = mineralcontest.getPlayerGame(dead);

        if (partie == null) return;
        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.player_died.toString(), dead), partie.groupe);

        if (partie.isGameStarted()) {
            partie.getStatsManager().register(KillStat.class, dead, dead);
        }

        // On tue le joueur
        PlayerUtils.killPlayer(dead);
    }

    /**
     * Fonction permettant d'enregistrer une mort causée par un monstre
     * note: pourquoi pas add une stats "tué par mob" ?
     *
     * @param dead - Le joueur mort
     */
    private void registerPlayerDeadByEntity(Player dead) {
        Game partie = mineralcontest.getPlayerGame(dead);

        if (partie == null) return;
        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.player_died.toString(), dead), partie.groupe);

        if (partie.isGameStarted()) {
            partie.getStatsManager().register(KillStat.class, dead, dead);
        }

        // On tue le joueur
        PlayerUtils.killPlayer(dead);
    }

}
