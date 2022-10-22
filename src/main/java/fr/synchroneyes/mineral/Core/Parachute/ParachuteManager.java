package fr.synchroneyes.mineral.Core.Parachute;

import fr.synchroneyes.custom_events.MCAirDropSpawnEvent;
import fr.synchroneyes.custom_events.MCAirDropTickEvent;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.Log.GameLogger;
import fr.synchroneyes.mineral.Utils.Log.Log;
import fr.synchroneyes.mineral.Utils.Radius;
import fr.synchroneyes.mineral.mineralcontest;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Classe gérant les drop
 */
public class ParachuteManager {

    private Groupe groupe;
    private List<Parachute> parachutes;

    // Variable contenant le temps restant avant le prochain drop
    @Getter
    private int timeleft_before_next_drop = Integer.MIN_VALUE;

    // Variable contenant la position du prochain drop
    @Getter
    private Location nextDropLocation = null;

    private BukkitTask dropsHandler = null;

    public ParachuteManager(Groupe groupe) {
        this.groupe = groupe;
        this.parachutes = new LinkedList<>();
    }


    public Groupe getGroupe() {
        return groupe;
    }


    /**
     * Permet de faire apparaitre le parachute!
     */
    public void spawnNewParachute() {




        if (nextDropLocation == null) {
            generateRandomLocation();
        }

        // On appelle l'event de spawn de parachute
        MCAirDropSpawnEvent event = new MCAirDropSpawnEvent(nextDropLocation, groupe.getGame());
        Bukkit.getPluginManager().callEvent(event);


        GameLogger.addLog(new Log("parachute_spawn", "Parachute spawned @ " + nextDropLocation.getX() + ", " + nextDropLocation.getY() + ", " + nextDropLocation.getZ(), "parachute_time_reached"));

        Parachute parachute = new Parachute(6, this);
        parachute.spawnParachute(nextDropLocation);
        this.parachutes.add(parachute);

        String chestLocationText = Lang.airdrop_subtitle.toString();

        chestLocationText = chestLocationText.replace("%x", nextDropLocation.getBlockX() + "");
        chestLocationText = chestLocationText.replace("%z", nextDropLocation.getBlockZ() + "");

        int nombreSecondeAffichage = groupe.getParametresPartie().getCVAR("drop_display_time").getValeurNumerique();

        // On averti les joueurs qu'un largage vient d'apparaitre
        for (Player joueur : groupe.getPlayers()) {
            joueur.sendTitle(Lang.airdrop_title.toString(), chestLocationText, 20, 20 * nombreSecondeAffichage, 20);
            joueur.playSound(joueur.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
        }

        // On envoie les coordo aux arbitres
        for (Player joueur : groupe.getGame().getReferees())
            joueur.sendMessage(mineralcontest.prefixPrive + "Drop: " + nextDropLocation.getBlockX() + " Y: " + nextDropLocation.getBlockY() + " Z: " + nextDropLocation.getBlockZ());





        // On génère le prochain tour
        generateRandomLocation();
        generateTimeleftBeforeNextDrop();


    }


    /**
     * Boucle qui gère l'apparition des largages
     */
    public void handleDrops() {

        // On fera un tour de boucle chaque seconde
        dropsHandler = Bukkit.getScheduler().runTaskTimer(mineralcontest.plugin, () -> {

            // ON appelle l'evenement de tick de parachute
            MCAirDropTickEvent event = new MCAirDropTickEvent(timeleft_before_next_drop, groupe.getGame());
            Bukkit.getPluginManager().callEvent(event);

            // On ne veut pas continuer le timer si la game est en pause ou en attente de démarrage
            if (groupe.getGame().isPreGame() || groupe.getGame().isGamePaused() || !groupe.getGame().isGameStarted())
                return;


            // Premier tour de boucle, on initialise tout!
            if (timeleft_before_next_drop == Integer.MIN_VALUE) {
                generateTimeleftBeforeNextDrop();
                generateRandomLocation();
            }


            if (timeleft_before_next_drop > 0) timeleft_before_next_drop--;
            else spawnNewParachute();

        }, 0, 20);
    }


    /**
     * Permet d'arrêter la boucle des largages
     */
    public void stopDropsHandler() {
        if (dropsHandler != null) dropsHandler.cancel();
    }


    /**
     * Fonction qui génère le temps avant l'apparition du prochain largage
     */
    private void generateTimeleftBeforeNextDrop() {

        // Variables contenant le temps minimum & maximum afin de générer un nombre dans cet interval
        int minTime, maxTime;


        // Temps en minute
        // On veut le résultat en secondes
        minTime = groupe.getParametresPartie().getCVAR("min_time_between_drop").getValeurNumerique() * 60;
        maxTime = groupe.getParametresPartie().getCVAR("max_time_between_drop").getValeurNumerique() * 60;

        Random random = new Random();

        // On génère un nombre dans cet interval
        this.timeleft_before_next_drop = random.nextInt((maxTime - minTime) + 1) + minTime;
    }

    /**
     * Cette fonction génère une position comprise entre 2 rectangle, les valeurs sont dans le fichier de configuration du plugin!
     *
     * @return
     */
    private void generateRandomLocation() {
        Location randomLocation = groupe.getGame().getArene().getCoffre().getLocation().clone();

        int tentatives = 1;
        int max = groupe.getParametresPartie().getCVAR("max_distance_from_arena").getValeurNumerique();
        int min = groupe.getParametresPartie().getCVAR("min_distance_from_arena").getValeurNumerique();

        int nbGenere;
        Random random = new Random();

        Location centreArene = groupe.getGame().getArene().getCoffre().getLocation();

        while (Radius.isBlockInRadius(centreArene, randomLocation, min)) {
            nbGenere = random.nextInt(max);
            randomLocation.setX((nbGenere % 2 == 0) ? randomLocation.getX() - nbGenere : randomLocation.getX() + nbGenere);

            // Si on est encore dans le rayon du petit rectangle, on ajoute un nombre compris entre [min; max]
            if (Radius.isBlockInRadius(centreArene, randomLocation, min)) {
                nbGenere = random.nextInt((max - min) - 1) + min;
                randomLocation.setZ((nbGenere % 2 == 0) ? centreArene.getZ() - nbGenere : centreArene.getZ() + nbGenere);

            } else {
                // On est pas dans le rayon,
                // On peut donc ajouter une valeur comprise entre [-max; +max]
                nbGenere = random.nextInt(max);
                randomLocation.setZ((nbGenere % 2 == 0) ? randomLocation.getZ() - nbGenere : randomLocation.getZ() + nbGenere);

            }

            ++tentatives;

            // Si on a fait trop de tentatives, on drop le ballon sur l'arène, BALLEKOUILLE
            if (tentatives > 50) {
                randomLocation = centreArene.clone();
                break;
            }

        }


        randomLocation.setY(130);
        // Maintenant on doit déterminer la hauteur du sol
        Location groundLocation = randomLocation.clone();
        while (groundLocation.getBlock().getType() == Material.AIR) {
            groundLocation.setY(groundLocation.getY() - 1);
        }

        // On veut mettre le ballon à 100 bloc de haut
        randomLocation.setY(groundLocation.getY() + 100);

        this.nextDropLocation = randomLocation;
    }


    public List<Parachute> getParachutes() {
        return parachutes;
    }



}
