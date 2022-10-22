package fr.synchroneyes.mineral.Core.Boss.BossType;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Boss.Boss;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.House;
import fr.synchroneyes.mineral.DeathAnimations.Animations.HalloweenHurricaneAnimation;
import fr.synchroneyes.mineral.Statistics.Class.BossKiller;
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

public class CrazyZombie extends Boss {

    private int maxSbire = 1;

    private List<LivingEntity> list_sbire;

    private int currentAnnouncementId = 0;

    private boolean lastAnnouncementPlayed = false;

    public CrazyZombie() {
        list_sbire = new ArrayList<>();
    }


    @Override
    public String getName() {
        return "Frankenstein";
    }

    @Override
    public double getSanteMax() {
        return 50;
    }

    @Override
    public double getDegatsParAttaque() {
        return 1;
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
        // On veut jouer un son lors de son apparitio à tous les joueurs
        // Et temporairement les aveugler
        List<Player> joueurs_cible = entity.getWorld().getPlayers();

        int duree_annonce = 5;

        // Pour chaque joueur du monde
        for(Player joueur : joueurs_cible) {
            // On joue un son
            joueur.playSound(joueur.getLocation(), Sound.AMBIENT_CAVE, 1,1);

            // On en joue un second un peu après
            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> joueur.playSound(joueur.getLocation(), Sound.ENTITY_WITCH_CELEBRATE, 1,1), 20);

            // On aveugle temporairement le joueur pendant 3 secondes
            joueur.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duree_annonce*20, 10));
            joueur.sendMessage(ChatColor.GOLD + "???: " + ChatColor.RESET + "Je suis " + getName() + ", venez m'affronter dans l'arène !");

            // On envoie un titre
            joueur.sendTitle(ChatColor.RED + getName(), "Venez m'affronter dans l'arène. Si vous survivez à mon premier sbire...", 20, 20*duree_annonce, 20);
        }

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
    }

    @Override
    protected void performAnnouncement() {
        String[] titres = new String[]{
                ChatColor.RED + "ERROR",
                ChatColor.BLUE + "Etrange ...",
                ChatColor.GREEN + "Sensation de déjà vu...",
                ChatColor.GOLD + " ### ATTENTION ###"
        };

        String[] messages = new String[]{
                "Encore un bug causé par ce débile de Synchro...",
                "Pourquoi le temps " + ChatColor.RED + "n'avance pas" + ChatColor.RESET + "? Il fait encore noir..",
                "J'ai un mauvais préssentiment...",
                "Une quantité " + ChatColor.RED + "importante d'électricité" + ChatColor.RESET + " a été détectée sous " + ChatColor.RED + "l'arène..."
        };

        // On récupère la liste des joueurs
        List<Player> joueurs = this.getChestManager().getGroupe().getPlayers();

        // Dans le cas où on est pas sur la dernière annonce, un simple titre suffit (+ message chat)
        if(this.currentAnnouncementId != messages.length) {
            // Pour chaque joueur
            for(Player joueur : joueurs){
                // On envoit un titre
                joueur.sendTitle(titres[this.currentAnnouncementId], messages[currentAnnouncementId], 20, 20*5, 20);
                // On envoit un message dans le chat
                joueur.sendMessage(ChatColor.GOLD + "???: " + ChatColor.RESET + messages[this.currentAnnouncementId]);

                // On aveugle le joueur temporairement
                joueur.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 7*20, 50));

                joueur.playSound(joueur.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,1);

            }

            // On incrémente le compteur
            this.currentAnnouncementId++;
            return;
        }

        // Sinon, on est sur la dernière annonce ...
        // Si elle a déjà été jouée, on s'arrête là
        if(this.lastAnnouncementPlayed) return;

        // On fait apparaitre un effet de particule sur le coffre d'arène, bleu
        Game partie = this.getChestManager().getGroupe().getGame();
        Location coffreArene = partie.getArene().getCoffre().getLocation();


        // On joue un nuage bleu pendant 2 minutes
        AreaEffectCloud effet_bleu_nuage = (AreaEffectCloud) coffreArene.getWorld().spawnEntity(coffreArene, EntityType.AREA_EFFECT_CLOUD);
        effet_bleu_nuage.setColor(Color.BLUE);
        // 2 * 60 secondes * 20 ticks
        effet_bleu_nuage.setDuration(2 * 60 * 20);

        // On joue un nuage noir pendant 2 minutes
        AreaEffectCloud effet_noir_nuage = (AreaEffectCloud) coffreArene.getWorld().spawnEntity(coffreArene, EntityType.AREA_EFFECT_CLOUD);
        effet_noir_nuage.setColor(Color.BLACK);
        // 2 * 60 secondes * 20 ticks
        effet_bleu_nuage.setDuration(2 * 60 * 20);

        // On averti les joueurs
        for(Player joueur : joueurs) {
            joueur.playSound(joueur.getLocation(), Sound.ENTITY_WITCH_CELEBRATE, 1,1);
            joueur.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 7*20, 20));
            joueur.sendTitle(ChatColor.RED + "/!\\ ANOMALIE DETECTEE /!\\", "Rendez-vous vite dans l'arène, j'ai peur de ce qui peut s'y passer...", 20, 5*50, 20);
            joueur.sendMessage(ChatColor.GOLD + "???: " + ChatColor.RESET + "Rendez-vous vite dans l'arène, j'ai peur de ce qui peut s'y passer...");
        }

        this.lastAnnouncementPlayed = true;


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
        zombieSbire.setCustomName("Sbire");
        zombieSbire.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getSanteMax()/3);
        zombieSbire.setHealth(getSanteMax()/3);
        zombieSbire.setMetadata("isBoss", new FixedMetadataValue(mineralcontest.plugin, true));

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
