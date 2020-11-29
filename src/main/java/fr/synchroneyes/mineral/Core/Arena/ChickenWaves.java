package fr.synchroneyes.mineral.Core.Arena;

import fr.synchroneyes.mineral.Settings.GameSettings;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.ErrorReporting.Error;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;
import java.util.Random;

public class ChickenWaves {
    private Location spawnCoffre;
    private Arene arene;
    private World monde;
    private boolean started = false;
    private boolean enabled = false;


    private LinkedList<LivingEntity> pouletsEnVie;

    // Nombre de poulet dans la prochaine vague
    private int nextWaveChickenCount = 0;
    // Temps restant avant la prochaine vague
    private int tempsRestantAvantProchaineVague = 0;

    public ChickenWaves(Arene arene) {

        this.arene = arene;
        this.pouletsEnVie = new LinkedList<>();

    }


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {

        if(!enabled) {
            for(LivingEntity poulet : pouletsEnVie) poulet.remove();
        }
        this.enabled = enabled;
    }

    public boolean isStarted() {
        return started;
    }

    /**
     * Démarre les vagues de poulets
     */
    public void start() {
        if (started) return;
        if(arene.groupe.getGame().isGameEnded()) return;
        this.started = true;
        this.enabled = true;

        arene.groupe.sendToEveryone(ChatColor.GOLD + "----------------------");
        arene.groupe.sendToEveryone(mineralcontest.prefixGlobal + "Les vagues d'apparition de poulet dans l'arène ont débuté !");
        arene.groupe.sendToEveryone(ChatColor.GOLD + "----------------------");
        genererProchaineVague();
        handleChickenWaveTimer();
    }

    /**
     * Gestion des vagues de poulets
     */
    private void handleChickenWaveTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (enabled) {
                    tempsRestantAvantProchaineVague--;
                    if (tempsRestantAvantProchaineVague <= 0) {
                        apparitionPoulets();
                        genererProchaineVague();

                        try {
                            tempsRestantAvantProchaineVague = arene.groupe.getParametresPartie().getCVAR("chicken_spawn_interval").getValeurNumerique();
                        } catch (Exception e) {
                            Error.Report(e, arene.groupe.getGame());
                        }

                    }
                }

            }
        }.runTaskTimer(mineralcontest.plugin, 0, 20);

    }

    /**
     * Fait apparaitre les poulets dans l'arène
     */
    public void apparitionPoulets() {

        if(!enabled) return;
        if(arene.groupe.getGame().isGameEnded()) return;

        this.monde = arene.groupe.getMonde();
        try {
            this.spawnCoffre = arene.getCoffre().getLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }

        GameSettings parametres = arene.groupe.getParametresPartie();

        int chicken_spawn_min_count = 1;
        int chicken_spawn_max_count = 2;

        try {
            chicken_spawn_min_count = parametres.getCVAR("chicken_spawn_min_count").getValeurNumerique();
            chicken_spawn_max_count = parametres.getCVAR("chicken_spawn_max_count").getValeurNumerique();
        } catch (Exception e) {
            Error.Report(e, arene.groupe.getGame());
        }


        boolean isHalloweenEnabled = (parametres.getCVAR("enable_halloween_event").getValeurNumerique() == 1);

        // On désactive halloween pour l'instant
        isHalloweenEnabled = false;


        Random random = new Random();
        int nombreDePouletASpawn = random.nextInt((chicken_spawn_max_count - chicken_spawn_min_count) - 1) + chicken_spawn_min_count;


        for (int i = 0; i < nombreDePouletASpawn; ++i) {
            LivingEntity entity = null;

            if(!isHalloweenEnabled) pouletsEnVie.add((Chicken) monde.spawnEntity(spawnCoffre, EntityType.CHICKEN));
            else {
                pouletsEnVie.add((Zombie) monde.spawnEntity(spawnCoffre, EntityType.ZOMBIE_VILLAGER));


            }

            entity = pouletsEnVie.getLast();


            double currentSpeed = entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
            entity.setCustomName(Lang.custom_chicken_name.toString());
            if(isHalloweenEnabled) ((Zombie)entity).setAdult();
            entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(currentSpeed * 1.5);
            entity.setCanPickupItems(false);
            entity.setCustomNameVisible(false);
        }


    }

    /**
     * Génère la prochaine vague de poulet
     */
    public void genererProchaineVague() {
        GameSettings parametres = arene.groupe.getParametresPartie();
        // Formule pour generer un nombre entre [X .... Y]
        // ((Y - X) + 1) + X

        try {
            int maxPoulet = parametres.getCVAR("chicken_spawn_max_count").getValeurNumerique();
            int minPoulet = parametres.getCVAR("chicken_spawn_min_count").getValeurNumerique();
            Random random = new Random();
            nextWaveChickenCount = random.nextInt((maxPoulet - minPoulet) + 1) + minPoulet;
        } catch (Exception e) {
            Error.Report(e, arene.groupe.getGame());
        }

    }


    public boolean isFromChickenWave(LivingEntity e) {
        return this.pouletsEnVie.contains(e);
    }

}
