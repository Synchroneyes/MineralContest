package fr.synchroneyes.mineral.Core.Boss;

import fr.synchroneyes.custom_events.MCGameStartedEvent;
import fr.synchroneyes.mineral.Core.Boss.BossType.CrazyZombie;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Settings.GameSettings;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedList;
import java.util.List;

/**
 * Classe permettant de gérer les boss présent dans une partie
 * Date de création: 21.10.2020
 * Auteur: Synchroneyes
 */

public class BossManager implements Listener {


    // Temps avant l'apparition du boss, on dit ici qu'il apparait à la 37 ème minute de jeu
    private int time_before_spawn = 37*60;

    // Variable contenant le temps restant avant l'apparition du prochain boss
    private int timeLeftBeforeNewBossSpawn = time_before_spawn;

    // Temps entre chaque annocne dans le chat, on en veut une toute les 7 minutes
    private final int time_before_each_announcement = 7*60;

    private int timeLeftBeforeNextAnnouncement = time_before_each_announcement;

    private Boss nextBoss;

    private BukkitTask boucle;



    /**
     * Liste des boss présent dans la partie
     */
    private List<Boss> bossList;

    /**
     * Partie dans lequel le bossmanager doit agir
     */
    private Game partie;

    public BossManager(Game partie) {
        this.bossList = new LinkedList<>();
        this.partie = partie;

        // On définit le prochain boss
        this.nextBoss = new CrazyZombie();
        this.nextBoss.setChestManager(partie.groupe.getAutomatedChestManager());

        Bukkit.getPluginManager().registerEvents(this, mineralcontest.plugin);
    }

    /**
     * Méthode permettant d'ajouter un boss à un monde
     * @param position
     * @param boss
     */
    public void spawnNewBoss(Location position, Boss boss) {
        boss.setChestManager(partie.groupe.getAutomatedChestManager());
        this.bossList.add(boss);
        boss.spawn(position);

    }

    /**
     * Retourne si oui ou non un joueur a été tué par un boss
     * @param killer - Entité ayant tuer le joueur
     * @return boolean
     */
    public boolean wasPlayerKilledByBoss(Entity killer) {
        for(Boss boss : bossList) {
            if(killer.equals(boss.entity)) return true;
        }
        return false;
    }

    /**
     * Méthode permettant d'appeler la fonction onPlayerKilled d'un boss
     * @param killer
     * @param deadPlayer
     */
    public void fireBossMadeKill(Entity killer, Player deadPlayer) {
        if(!wasPlayerKilledByBoss(killer)) return;

        for(Boss boss: bossList) {
            if(killer.equals(boss.entity)) {
                boss.onPlayerKilled(deadPlayer);
                return;
            }
        }
    }


    /**
     * Méthode permettant d'effectuer un tour de boucle de la tache de gestion de temps avant apparition du boss
     */
    private void doLoopTick() {

        GameSettings gameSettings = partie.groupe.getParametresPartie();
        //boolean isHalloweenEnabled = (gameSettings.getCVAR("enable_halloween_event").getValeurNumerique() == 1);

        // On désactive halloween pour 'isntant
        //isHalloweenEnabled = false;

        // On réduit le temps de 1
        this.timeLeftBeforeNewBossSpawn--;
        if(this.timeLeftBeforeNewBossSpawn == 0) {
            //if(isHalloweenEnabled) this.spawnHalloweenBoss();
            //if(isHalloweenEnabled) this.timeLeftBeforeNewBossSpawn = this.time_before_spawn;
            this.boucle.cancel();
            return;
        }

        // On réduit le temps avant la prochaine annonce
        this.timeLeftBeforeNextAnnouncement--;

        // On regarde si on peut faire une nouvelle annonce
        if(this.timeLeftBeforeNextAnnouncement == 0) {
            //if(isHalloweenEnabled) this.timeLeftBeforeNextAnnouncement = this.time_before_each_announcement;
            //if(isHalloweenEnabled) this.nextBoss.performAnnouncement();
        }

        //Bukkit.getLogger().info("Prochaine Annonce: " + this.timeLeftBeforeNextAnnouncement + " - Prochain Boss: " + this.timeLeftBeforeNewBossSpawn);


    }

    @EventHandler
    public void onGameStart(MCGameStartedEvent event) {
        if(this.boucle == null) {
            this.boucle = new BukkitRunnable() {
                @Override
                public void run() {
                    doLoopTick();
                }
            }.runTaskTimer(mineralcontest.plugin, 0, 20);
        }
    }

    /**
     * Retourne si l'entité passée en paramètre est un boss
     * @param e
     * @return
     */
    public boolean isThisEntityABoss(LivingEntity e) {
        for(Boss b : bossList) {
            if(b.entity == null) continue;
            if(b.entity.equals(e)) return true;
        }
        return  false;
    }

    /**
     * Retourne si l'entité passée en paramètre est un mob ou un boss spawn par un boss
     * @param e
     * @return
     */
    public boolean isThisEntitySpawnedByBoss(Entity e) {
        for(Boss b : bossList)
            if(b.isThisEntitySpawnedByBoss(e)) return true;
        return false;
    }



}
