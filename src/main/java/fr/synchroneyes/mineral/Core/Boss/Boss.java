package fr.synchroneyes.mineral.Core.Boss;

import fr.synchroneyes.mineral.Utils.Radius;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

/**
 * Classe permettant de modéliser un boss pour le plugin mineral contest
 * Date de création: 21.10.2020
 * Auteur: Synchroneyes
 */

public abstract class Boss {


    /**
     * Barre de santé du mob
     */
    private BossBar bossBar;

    /**
     * Attribut permettant de stocker le mob
     */
    protected Mob entity;

    /**
     * Variable contenant la boucle de gestion du mob
     */
    private BukkitTask boucle;

    /**
     * Méthode permettant de définir quand le mob doit faire son attaque spéciale
     */
    private int compteur = 1;


    /**
     * Méthode permettant de récuperer le nom du boss
     * @return
     */
    public abstract String getName();

    /**
     * Methode permettant de récuperer la santé max du boss
     * @return
     */
    public abstract double getSanteMax();

    /**
     * Méthode permettant de retourner le nombre de dégat que le mob doit faire en une attaque
     * @return
     */
    public abstract double getDegatsParAttaque();

    /**
     * Permet de définir le type de mob à faire apparaitre
     * @return
     */
    public abstract EntityType getMobType();

    /**
     * Méthode permettant de définir le rayon à partir du quel il peut détecter un joueur à attaquer
     * @return
     */
    public abstract int getRayonDetectionJoueur();

    /**
     * Méthode appelée lorsque le mob doit se focaliser sur un joueur
     * @param targetedPlayer
     */
    public abstract void onPlayerTarget(Player targetedPlayer);

    /**
     * Méthode retournant les récompenses lors du kill du mob
     * @return
     */
    public abstract List<ItemStack> getKillRewards();

    /**
     * Méthode permettant de définir le type de coffre à faire apparaitre contenant les objets de récompenses
     * @return
     */
    public abstract Material getChestType();

    /**
     * Méthode permettant de définir si un mob doit être luisant ou non (visible à travers les murs)
     * @return
     */
    public abstract boolean shouldEntityGlow();


    /**
     * Permet de définir la couleur de la bossBar
     * @return
     */
    public abstract BarColor getBossBarColor();

    /**
     * Méthode permettant de définir le type de bossbar
     * @return
     */
    public abstract BarStyle getBarStyle();

    /**
     * Méthode permettant au mob de faire une attaque spéciale
     */
    public abstract void doMobSpecialAttack();

    /**
     * Méthode permettant de définir le temps entre chaque attaque
     * @return
     */
    public abstract int getSpecialAttackTimer();

    /**
     * Méthode permettant de définir le rayon necessaire afin de faire apparaitre la bossbar
     * @return
     */
    public abstract int getBossBarDetectionRadius();

    /**
     * Permet de définir des attributs customs
     */
    public abstract void defineCustomAttributes();



    /**
     * Méthode permettant de faire apparaitre le monstre
     * @param position - Position où faire apparaitre le joueur
     */
    public void spawn(Location position) {

        // Récupération du monde où faire apparaitre le mob
        World monde = position.getWorld();

        // Dans le cas où il n'y a pas de monde, on s'arrête
        if(monde == null) {
            Bukkit.getLogger().severe("Unable to spawn the boss, invalid location given.");
            return;
        }

        // On crée l'entité et on définit ses attributs
        this.entity = (Mob) monde.spawnEntity(position, getMobType());

        // Il doit avoir un nom visible constamment
        this.entity.setCustomNameVisible(true);

        // On définit si il doit être luisant ou non
        this.entity.setGlowing(shouldEntityGlow());

        // On définit son nom
        this.entity.setCustomName(getName());

        // On définit sa santé max
        this.entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getSanteMax());

        // ON lui met sa vie à fond
        this.entity.setHealth(getSanteMax());

        // On définit ses dégats
        this.entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(getDegatsParAttaque());

        // On crée la bossbar
        if(bossBar != null) removeBossBar();
        this.bossBar = Bukkit.createBossBar(getName(), getBossBarColor(), getBarStyle());

        // On définit la première cible du mob
        this.entity.setTarget(getNearestPlayer());

        // On démarre la boucle de gestion du mob
        this.startMobTask();

    }


    /**
     * Méthode permettant de supprimer la bossbar
     */
    private void removeBossBar() {
        if(bossBar == null) return;
        this.bossBar.removeAll();
        this.bossBar.setProgress(0);
        this.bossBar.setVisible(false);
        this.bossBar = null;
    }

    /**
     * Méthode permettant de retourner le joueur le plus proche du mob
     * @return
     */
    private Player getNearestPlayer() {

        // Si le mob n'existe pas ou qu'il est mort, on s'arrête
        if(entity == null || entity.isDead()) return null;


        // On récupère les entités proche du mob
        int radius = getRayonDetectionJoueur();
        List<Entity> entite_proche = this.entity.getNearbyEntities(radius, radius, radius);

        // ON supprime tout ce qui n'est pas un joueur
        entite_proche.removeIf(entity1 -> !(entity1 instanceof Player));

        // On supprime les arbitres
        entite_proche.removeIf(entity1 -> mineralcontest.getPlayerGame((Player) entity1).isReferee((Player) entity1));


        // Si il n'y a pas d'entité, on s'arrête
        if(entite_proche.isEmpty()) return null;

        // On récupère le joueur visé


        // On retourne le premier element
        return (Player) entite_proche.get(0);
    }


    /**
     * Méthode permettant de démarrer la boucle de gestion du mob
     */
    private void startMobTask(){

        // On arrête la précedente boucle si elle existait
        if(this.boucle != null) {
            this.boucle.cancel();
            this.boucle = null;

            // Dans le doute, on supprime la bossbar
            removeBossBar();
        }

        // On crée la nouvelle boucle
        this.boucle = Bukkit.getScheduler().runTaskTimer(mineralcontest.plugin, () -> {

            // On vérifie si le mob existe ou est mort
            if(entity == null) {
                this.boucle.cancel();
                removeBossBar();
                return;
            }

            // On vérifie si le mob est mort
            if(entity.isDead()) {
                removeBossBar();
                spawnMobKillRewards();
                if(entity.getKiller() != null) mineralcontest.broadcastMessage(entity.getKiller().getDisplayName() + " a tué " + getName());
                this.boucle.cancel();
                return;
            }

            // On fait en sorte à ce que le mob target un nouveau joueur
            LivingEntity targetedPlayer = this.entity.getTarget();
            if(targetedPlayer instanceof Player) {
                Player targeted = (Player) targetedPlayer;
                // Si le joueur precedemment visé n'est pas le même
                if(!targeted.equals(getNearestPlayer())) {

                    // On appelle la méthode onPlayerTarget
                    onPlayerTarget(targeted);
                    this.entity.setTarget(getNearestPlayer());
                }
            }

            // On vérifie si le mob peut faire son attaque spéciale
            if(this.compteur % getSpecialAttackTimer() == 0) this.doMobSpecialAttack();

            // On gère la bossbar et son rayon de detection
            handleCrossBar();

            // On change le nom du mob
            this.entity.setCustomName(getNameWithHealth());

            // On incrémente le compteur
            this.compteur++;
        }, 0, 20);
    }

    /**
     * Méthode permettant de faire apparaitre les récompenses de mort d'un boss
     */
    private void spawnMobKillRewards() {
        List<ItemStack> items = getKillRewards();


    }

    /**
     * Méthode permettant de gérer les joueurs à qui il faut afficher la crossbar ou non
     */
    private void handleCrossBar() {

        // Si il n'y a pas de boss, on s'arrête
        if(entity == null) return;

        // ON récupère le monde du boss
        World monde = entity.getWorld();

        // On récupère les joueurs dans le monde du boss
        List<Player> joueurs = monde.getPlayers();

        // Ceux qui sont dans le rayon du boss, on leur fait apparaitre le bar
        for(Player joueur : joueurs) {
            if(Radius.isBlockInRadius(entity.getLocation(), joueur.getLocation(), getBossBarDetectionRadius())) this.bossBar.addPlayer(joueur);
            else this.bossBar.removePlayer(joueur);
        }

        this.bossBar.setProgress(entity.getHealth() / getSanteMax());


    }

    private String getNameWithHealth() {
        if(this.entity.getHealth() == 0) return getName();
        return getName() + " " + ((int)entity.getHealth()) + ChatColor.RED + "♥" + ChatColor.RESET;
    }

}
