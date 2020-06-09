package fr.mineral.Core.Arena;

import fr.mineral.Settings.GameSettings;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;
import java.util.Random;

public class ChickenWaves {
    private Location spawnCoffre;
    private Arene arene;
    private World monde;
    private boolean started = false;

    private LinkedList<LivingEntity> pouletsEnVie;

    // Nombre de poulet dans la prochaine vague
    private int nextWaveChickenCount = 0;
    // Temps restant avant la prochaine vague
    private int tempsRestantAvantProchaineVague = 0;

    public ChickenWaves(Arene arene) {

        this.arene = arene;
        this.pouletsEnVie = new LinkedList<>();

    }

    public boolean isStarted() {
        return started;
    }

    /**
     * Démarre les vagues de poulets
     */
    public void start() {
        if (started) return;
        this.started = true;
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
        }.runTaskTimer(mineralcontest.plugin, 0, 20);

    }

    /**
     * Fait apparaitre les poulets dans l'arène
     */
    public void apparitionPoulets() {
        this.monde = arene.groupe.getMonde();
        try {
            this.spawnCoffre = arene.getCoffre().getPosition();
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


        Random random = new Random();
        int nombreDePouletASpawn = random.nextInt((chicken_spawn_max_count - chicken_spawn_min_count) - 1) + chicken_spawn_min_count;


        for (int i = 0; i < nombreDePouletASpawn; ++i) {
            Chicken poulet = (Chicken) monde.spawnEntity(spawnCoffre, EntityType.CHICKEN);
            double currentSpeed = poulet.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
            poulet.setCustomName(Lang.custom_chicken_name.toString());
            poulet.setAdult();
            poulet.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(currentSpeed * 1.5);
            poulet.setCanPickupItems(false);
            poulet.setCustomNameVisible(false);
            pouletsEnVie.add(poulet);
        }


    }

    /**
     * Génère la prochaine vague de poulet
     */
    private void genererProchaineVague() {
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

}
