package fr.synchroneyes.mineral.Core.Boss.BossType;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Boss.Boss;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.House;
import fr.synchroneyes.mineral.DeathAnimations.Animations.HalloweenHurricaneAnimation;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class AngryZombie extends Boss {

    private int maxSbire = 5;

    private List<LivingEntity> list_sbire;

    public AngryZombie() {
        list_sbire = new ArrayList<>();
    }

    @Override
    public void onBossRemove() {
        for(LivingEntity sbire : list_sbire)
            sbire.remove();
    }


    @Override
    public String getName() {
        return "???";
    }

    @Override
    public double getSanteMax() {
        return 20;
    }

    @Override
    public double getDegatsParAttaque() {
        return 10;
    }

    @Override
    public EntityType getMobType() {
        return EntityType.ZOMBIE;
    }

    @Override
    public int getRayonDetectionJoueur() {
        return 5;
    }

    @Override
    public void onPlayerTarget(Player targetedPlayer) {
    }

    @Override
    public List<ItemStack> getKillRewards() {
        List<ItemStack> items = new LinkedList<>();

        // On donne 5 émeraudes
        for(int i = 0; i < 5; ++i)
            items.add(new ItemStack(Material.EMERALD, 1));

        // 15 diams
        for(int i = 0; i < 15; ++i)
            items.add(new ItemStack(Material.DIAMOND, 1));

        // 20 or
        for(int i = 0; i < 4; ++i)
            items.add(new ItemStack(Material.GOLD_INGOT, 5));

        // 30 fer
        for(int i = 0; i < 3; ++i)
            items.add(new ItemStack(Material.IRON_INGOT, 10));



        return items;
    }


    @Override
    public boolean shouldEntityGlow() {
        return true;
    }

    @Override
    public BarColor getBossBarColor() {
        return BarColor.GREEN;
    }

    @Override
    public BarStyle getBarStyle() {
        return BarStyle.SOLID;
    }

    @Override
    public void doMobSpecialAttack() {

        if(list_sbire.size() >= maxSbire) return;


        int nb_sbire_genere = 3;


        for(int i = 0; i < nb_sbire_genere; ++i) {

            if(list_sbire.size() >= maxSbire) break;

            ZombieVillager zombieVillager = this.addSbire(this.entity.getLocation());
            list_sbire.add(zombieVillager);
            this.spawnedEntities.add(zombieVillager);

        }

        this.entity.getWorld().strikeLightningEffect(this.entity.getLocation());
        this.entity.getWorld().playEffect(this.entity.getLocation(), Effect.END_GATEWAY_SPAWN, 1);

        // On veut également "avertir" les joueurs proche de lui
        // On récupère les joueurs autour de lui
        List<Entity> joueurs = this.entity.getNearbyEntities(getRayonDetectionJoueur(),getRayonDetectionJoueur(),getRayonDetectionJoueur());
        // On retire ce qui n'est pas un joueur
        joueurs.removeIf(entity1 -> !(entity1 instanceof Player));

        int duree_effet = 5;

        for(Entity joueur : joueurs) {
            Player j = (Player) joueur;
            j.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20*duree_effet, 5));
            j.sendTitle(ChatColor.GREEN + getName(), getRandomInfectionMessage(), 20, 20*duree_effet/2, 20);
        }

    }

    @Override
    public int getSpecialAttackTimer() {
        return 15;
    }

    @Override
    public int getBossBarDetectionRadius() {
        return 20;
    }

    @Override
    public void defineCustomAttributes() {
        this.entity.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK).setBaseValue(2);
    }

    @Override
    public void onBossDeath() {

        for(LivingEntity sbire : list_sbire)
            sbire.setHealth(0);

        HalloweenHurricaneAnimation animationMort = new HalloweenHurricaneAnimation();
        animationMort.playAnimation(entity);


    }

    @Override
    public void onBossSpawn() {

        Bukkit.broadcastMessage("onBossSpawn");
        // On s'assure que le zombie est adulte
        if(entity instanceof Zombie) {
            Zombie zombie = (Zombie) entity;
            zombie.setAdult();
        }

        // On fait apparaitre un sbire sur chaque personne d'une équipe
        Groupe groupe = getChestManager().getGroupe();

        // Pour chaque Maison
        for(House maison : groupe.getGame().getHouses()) {
            // On récupère un joueur aléatoire
            int team_member_cout = maison.getTeam().getJoueurs().size();
            if(team_member_cout > 0){
                Player joueurAleatoire = maison.getTeam().getJoueurs().get(new Random().nextInt(team_member_cout));
                // Et on spawn un sbire sur lui
                addSbire(joueurAleatoire.getLocation());
            }


        }

        this.entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 99999999, 10));
    }

    @Override
    protected void performAnnouncement() {
    }

    @Override
    protected boolean canSpawnMobs() {
        return true;
    }

    @Override
    public void onPlayerKilled(Player p) {
        HalloweenHurricaneAnimation animationMort = new HalloweenHurricaneAnimation();
        animationMort.playAnimation(p);

        mineralcontest.broadcastMessage(ChatColor.RED + getName() + ChatColor.RESET + ": " + p.getDisplayName() + "a succombé face à ma force légendaire. À qui le tour?");
    }


    /**
     * Fonction retournant un message random
     * @return
     */
    private String getRandomInfectionMessage() {
        String[] messages = {
                "Tu me donne envie de vomir",
                "Ma beauté t'ébloui?",
                "Mes copains sont là pour toi",
                "C'est tout ce que t'as?",
                "Bats toi comme un homme",
                "Sombre bloc de bouse",
                "Même Herobrine fait mieux que toi"
        };

        return messages[new Random().nextInt(messages.length)];
    }

    private ZombieVillager addSbire(Location position) {

        ZombieVillager zombieSbire = (ZombieVillager) this.entity.getWorld().spawnEntity(position, EntityType.ZOMBIE_VILLAGER);
        if(zombieSbire.isBaby())zombieSbire.setAdult();
        zombieSbire.setCustomNameVisible(true);
        zombieSbire.setCustomName("???");
        zombieSbire.setMetadata("boss", new FixedMetadataValue(mineralcontest.plugin, true));
        zombieSbire.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getSanteMax()/3);
        zombieSbire.setHealth(getSanteMax()/3);

        zombieSbire.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(getDegatsParAttaque()/3);

        new BukkitRunnable() {
            @Override
            public void run() {
                if(zombieSbire.isDead()) {
                    this.cancel();
                }

                zombieSbire.setCustomName("Sbire " + ((int)zombieSbire.getHealth()) + ChatColor.RED + "♥" + ChatColor.RESET);
            }
        }.runTaskTimer(mineralcontest.plugin, 0, 5);

        return zombieSbire;
    }
}
