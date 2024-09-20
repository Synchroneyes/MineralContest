package fr.synchroneyes.mineral.Core.Arena;

import fr.synchroneyes.custom_events.MCArenaChestSpawnEvent;
import fr.synchroneyes.custom_events.MCArenaChestTickEvent;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Arena.Zones.DeathZone;
import fr.synchroneyes.mineral.Core.Coffre.AutomatedChestAnimation;
import fr.synchroneyes.mineral.Core.Coffre.Coffres.CoffreArene;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.ErrorReporting.Error;
import fr.synchroneyes.mineral.Utils.Player.PlayerUtils;
import fr.synchroneyes.mineral.Utils.Radius;
import fr.synchroneyes.mineral.mineralcontest;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;
import java.util.List;

/*
    Classe représentant une arène
 */
public class Arene {

    /*
        Une arène contient:
            - Un coffre
            - Une zone de spawn
            - Une deathZone (même si on meurt en dehors)
            - Un contour ??
     */

    private Location teleportSpawn;
    //private CoffreAvecCooldown coffre;

    // Coffre de l'arène avec animation
    private AutomatedChestAnimation coffreArene;

    private boolean allowTeleport;
    private DeathZone deathZone;
    private int MAX_TIME_BETWEEN_CHEST = 0; // mins
    private int MIN_TIME_BETWEEN_CHEST = 0;

    @Getter
    private int TIME_BEFORE_CHEST = 0;

    // Temps restant necessaire avant d'annoncer l'arrivée du coffre
    private int TIMELEFT_REQUIRED_BEFORE_WARNING = 0;

    private double TELEPORT_TIME_LEFT = 0;
    private double TELEPORT_TIME_LEFT_VAR = 0;
    private boolean CHEST_SPAWNED = false;
    private boolean CHEST_INITIALIZED = false;
    public boolean CHEST_USED = false;
    public int arenaRadius = 60;
    private BossBar teleportStatusBar;

    public Groupe groupe;

    public ChickenWaves chickenWaves;

    // Liste des équipes à notifier avant l'apparition du coffre
    private List<Equipe> teamsToNotify;

    // Liste des équipes à téléporter automatiquement à l'apparition du coffre
    private List<Equipe> teamsToAutomaticallyTeleport;

    // Liste des équipes où; il faudra seulement TP le joueur qui fait /arene
    private List<Equipe> teamsToSingleTeleport;

    public boolean isChestSpawned() {
        return CHEST_SPAWNED;
    }

    public void setChestSpawned(boolean CHEST_SPAWNED) {
        this.CHEST_SPAWNED = CHEST_SPAWNED;
    }

    public Arene(Groupe g) {
        this.groupe = g;
        this.deathZone = new DeathZone(g);
        this.chickenWaves = new ChickenWaves(this);


        this.teamsToNotify = new LinkedList<>();
        this.teamsToAutomaticallyTeleport = new LinkedList<>();
        this.teamsToSingleTeleport = new LinkedList<>();

        // Coffre d'arène avec animations
        this.coffreArene = new CoffreArene(groupe.getAutomatedChestManager(), this);

        try {
            MAX_TIME_BETWEEN_CHEST = g.getParametresPartie().getCVAR("max_time_between_chests").getValeurNumerique();
            MIN_TIME_BETWEEN_CHEST = g.getParametresPartie().getCVAR("min_time_between_chests").getValeurNumerique();
            TELEPORT_TIME_LEFT = g.getParametresPartie().getCVAR("max_teleport_time").getValeurNumerique();
            TELEPORT_TIME_LEFT_VAR = TELEPORT_TIME_LEFT;
            TIMELEFT_REQUIRED_BEFORE_WARNING = g.getParametresPartie().getCVAR("arena_warn_chest_time").getValeurNumerique();

        } catch (Exception e) {
            e.printStackTrace();
            Error.Report(e, groupe.getGame());
        }

        // If the min time is greater than the max time
        if (MAX_TIME_BETWEEN_CHEST < MIN_TIME_BETWEEN_CHEST) {
            int tmp = MIN_TIME_BETWEEN_CHEST;
            MIN_TIME_BETWEEN_CHEST = MAX_TIME_BETWEEN_CHEST;
            MAX_TIME_BETWEEN_CHEST = tmp;
        }

    }

    /**
     * Fonction permettant d'ajouter une équipe à notifier avant l'apparition du coffre
     *
     * @param equipe
     */
    public void addTeamToNotify(Equipe equipe) {
        if (!teamsToNotify.contains(equipe)) this.teamsToNotify.add(equipe);
    }

    /**
     * Permet d'ajouter une équipe à téléporter de manière automatique
     *
     * @param equipe
     */
    public void addTeamToAutomatedTeleport(Equipe equipe) {
        if (!teamsToAutomaticallyTeleport.contains(equipe)) teamsToAutomaticallyTeleport.add(equipe);
    }

    /**
     * Permet d'ajouter une équipe où il faudra seulement téléporter le joueur qui fait /arene
     *
     * @param equipe
     */
    public void addTeamToSinglePlayerTeleport(Equipe equipe) {
        if (!teamsToSingleTeleport.contains(equipe)) {
            teamsToSingleTeleport.add(equipe);
        }
    }

    /**
     * Permet de vider la liste des équipes où il faut seulement TP un joueur
     */
    public void clearSingleTeleportTeams() {
        this.teamsToSingleTeleport.clear();
    }


    public Location getTeleportSpawn() {
        return this.teleportSpawn;
    }

    public DeathZone getDeathZone() {
        return this.deathZone;
    }

    // ON vide le contenu du coffre
    public void clear() {
        if (this.coffreArene != null) this.coffreArene.getInventory().clear();
        removePlayerTeleportBar();
        this.chickenWaves.setEnabled(false);

    }

    public void generateTimeBetweenChest() {

        try {
            MAX_TIME_BETWEEN_CHEST = groupe.getParametresPartie().getCVAR("max_time_between_chests").getValeurNumerique();
            MIN_TIME_BETWEEN_CHEST = groupe.getParametresPartie().getCVAR("min_time_between_chests").getValeurNumerique();
            TIMELEFT_REQUIRED_BEFORE_WARNING = groupe.getParametresPartie().getCVAR("arena_warn_chest_time").getValeurNumerique();

        } catch (Exception e) {
            e.printStackTrace();
            Error.Report(e, groupe.getGame());
        }


        // If the min time is greater than the max time
        if (MAX_TIME_BETWEEN_CHEST < MIN_TIME_BETWEEN_CHEST) {
            int tmp = MIN_TIME_BETWEEN_CHEST;
            MIN_TIME_BETWEEN_CHEST = MAX_TIME_BETWEEN_CHEST;
            MAX_TIME_BETWEEN_CHEST = tmp;
        }


        // On va générer une valeur comprise entre MAX_TIME et MIN_TIME en minute
        // puis y ajouter des secondes
        int time = (int) ((Math.random() * ((MAX_TIME_BETWEEN_CHEST - MIN_TIME_BETWEEN_CHEST) + 1)) + MIN_TIME_BETWEEN_CHEST);
        time *= 60;
        // On y ajoute des secondes (entre 1 et 59)
        time += (int) ((Math.random() * ((59 - 1) + 1)) + 1);

        TIME_BEFORE_CHEST = time;
        TELEPORT_TIME_LEFT = TELEPORT_TIME_LEFT_VAR;
        CHEST_INITIALIZED = true;
    }


    // Supprime les mobs autour de l'arène
    /*public void startAutoMobKill() {
        new BukkitRunnable() {
            public void run() {
                try {
                    if (groupe.getParametresPartie().getCVAR("enable_monster_in_protected_zone").getValeurNumerique() != 1) {
                        for (Entity entite : groupe.getMonde().getEntities()) {
                            if (entite instanceof Monster) {
                                try {
                                        entite.remove();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Error.Report(e, groupe.getGame());
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Error.Report(e, groupe.getGame());
                }

            }

        }.runTaskTimer(mineralcontest.plugin, 0, 20);
    }*/

    public void startAutoMobKill() {
        new BukkitRunnable() {
            @Override
            public void run() {

                List<Entity> list_entity = groupe.getMonde().getEntities();
                list_entity.removeIf(entite -> !(entite instanceof Monster));
                list_entity.removeIf(entite -> !Radius.isBlockInRadius(coffreArene.getLocation(), entite.getLocation(), groupe.getParametresPartie().getCVAR("protected_zone_area_radius").getValeurNumerique()));

                if(groupe.getParametresPartie().getCVAR("enable_monster_in_protected_zone").getValeurNumerique() != 1) {
                    for(Entity entite : list_entity) {

                        // On est dans le rayon de l'arène
                        // On doit vérifier si on est dans les vagues de poulet
                        if(groupe.getGame().getArene().chickenWaves.isFromChickenWave((LivingEntity) entite)) {
                            // Si oui, on ne fait rien
                            return;
                        }

                        // On vérifie maintenant si c'est une chauve souris!
                        if(entite instanceof Bat) {
                            return;
                        }

                        // On vérifie maintenant si c'est le boss!
                        if(groupe.getGame().getBossManager().isThisEntityABoss((LivingEntity) entite)) {
                            return;
                        }
                        // On vérifie maintenant si c'est spawn par un boss!
                        if(groupe.getGame().getBossManager().isThisEntitySpawnedByBoss(entite)) {
                                return;
                        }
                        Bukkit.getLogger().info("Removing entity: " + entite.getName());
                        entite.remove();
                    }
                }
            }
        }.runTaskTimer(mineralcontest.plugin, 0, 40);
    }

    /**
     * Permet d'informer les équipes que le coffre va arriver !
     */
    private void notifyTeams() {
        for (Equipe equipe : teamsToNotify)
            equipe.sendMessage(mineralcontest.prefixTeamChat + Lang.translate(Lang.arena_chest_will_spawn_in.toString(), groupe));

        teamsToNotify.clear();
    }

    /**
     * Permet de téléporter les équipes qui ont acheté le bonus de TP auto à l'arène
     */
    public void automaticallyTeleportTeams() {
        // Pour chaque équipe
        for (Equipe equipe : teamsToAutomaticallyTeleport)
            // Pour chaque joueur de l'équipe
            for (Player membre : equipe.getJoueurs())
                PlayerUtils.teleportPlayer(membre, getTeleportSpawn().getWorld(), getTeleportSpawn());

        teamsToAutomaticallyTeleport.clear();
    }


    // Gère le coffre d'arene
    public void startArena() {

        generateTimeBetweenChest();
        // Coffre initialisé
        new BukkitRunnable() {
            public void run() {

                if (groupe.getGame().isGameStarted() && !groupe.getGame().isGamePaused()) {
                    try {


                        // Si le coffre est initialisé et n'est pas encore apparu
                        if (CHEST_INITIALIZED) {
                            // Le coffre n'est pas encore disponible
                            if (TIME_BEFORE_CHEST > 0) {

                                // On appelle l'event qui annonce un tick passé
                                MCArenaChestTickEvent mcArenaChestTickEvent = new MCArenaChestTickEvent(TIME_BEFORE_CHEST, groupe.getGame());
                                Bukkit.getPluginManager().callEvent(mcArenaChestTickEvent);


                                TIME_BEFORE_CHEST--;
                                if (TIME_BEFORE_CHEST == TIMELEFT_REQUIRED_BEFORE_WARNING)
                                    notifyTeams();
                            } else {
                                // LE coffre doit apparaitre !
                                coffreArene.getLocation().getBlock().setType(Material.AIR);
                                coffreArene.spawn();
                                MCArenaChestSpawnEvent mcArenaChestSpawnEvent = new MCArenaChestSpawnEvent(groupe.getGame());
                                Bukkit.getPluginManager().callEvent(mcArenaChestSpawnEvent);

                            }

                        }

                        // SI le coffre a été utilisé, on regenère un temps
                        if (CHEST_USED) {
                            CHEST_SPAWNED = false;
                            disableTeleport();
                            TELEPORT_TIME_LEFT = TELEPORT_TIME_LEFT_VAR;
                            CHEST_USED = false;
                        }

                        // Les joueurs disposent de 15 sec pour se Tp une fois le TP actif

                        if (getCoffre().isChestSpawned() && isTeleportAllowed()) {
                            updateTeleportBar();
                            TELEPORT_TIME_LEFT--;

                            if (TELEPORT_TIME_LEFT <= 0) {
                                disableTeleport();
                                removePlayerTeleportBar();
                                TELEPORT_TIME_LEFT = TELEPORT_TIME_LEFT_VAR;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Error.Report(e, groupe.getGame());
                    }

                }


            }
        }.runTaskTimer(mineralcontest.plugin, 0, 20);

    }

    public boolean isTeleportAllowed() {
        return this.allowTeleport;
    }


    /**
     * Permet de savoir si une équipe peut utiliser le single teleport ou non
     *
     * @param e
     * @return
     */
    public boolean canTeamUseSingleTeleport(Equipe e) {
        return teamsToSingleTeleport.contains(e);
    }


    public void enableTeleport() {
        String separator = ChatColor.GOLD + "----------------";
        for (Player online : groupe.getPlayers()) {
            online.sendMessage(separator);
            online.sendMessage(mineralcontest.prefixGlobal + Lang.arena_chest_spawned.toString());
            online.sendMessage(separator);
        }
        //online.sendTitle(ChatColor.GREEN + Lang.translate(Lang.arena_chest_spawned.toString()), Lang.translate(Lang.arena_teleport_now_enabled.toString()), 20, 20*3, 20);

        this.allowTeleport = true;
        createTeleportBar();
    }

    private void createTeleportBar() {
        if (teleportStatusBar == null)
            teleportStatusBar = Bukkit.createBossBar(Lang.arena_teleport_now_enabled.toString(), BarColor.BLUE, BarStyle.SOLID);
    }

    public void removePlayerTeleportBar() {
        if (teleportStatusBar != null) teleportStatusBar.removeAll();
    }

    public void updateTeleportBar() {
        createTeleportBar();
        double status = (TELEPORT_TIME_LEFT / TELEPORT_TIME_LEFT_VAR);
        teleportStatusBar.setProgress(status);

        for (Player player : groupe.getPlayers()) {
            teleportStatusBar.removePlayer(player);
            teleportStatusBar.addPlayer(player);
        }
    }

    public void disableTeleport() {
        String separator = ChatColor.GOLD + "----------------";

        TELEPORT_TIME_LEFT = TELEPORT_TIME_LEFT_VAR;

        if (allowTeleport) {
            for (Player online : groupe.getPlayers()) {
                online.sendMessage(separator);
                online.sendMessage(mineralcontest.prefixGlobal + Lang.arena_teleport_now_disabled.toString());
                online.sendMessage(separator);
            }

        }
        removePlayerTeleportBar();

        clearSingleTeleportTeams();
        this.allowTeleport = false;

    }


    public void setTeleportSpawn(Location z) {
        if (mineralcontest.debug)
            mineralcontest.plugin.getLogger().info(mineralcontest.prefixGlobal + Lang.arena_spawn_added.toString());
        this.teleportSpawn = z;
    }

    // Set le coffre de l'arène
    public void setCoffre(Location position) {

        if (position == null) {
            Bukkit.getLogger().severe("Position is null !");
        }
        this.coffreArene.setChestLocation(position);
        if (position.getBlock() != null) position.getBlock().setType(Material.AIR);

        groupe.getAutomatedChestManager().replace(CoffreArene.class, coffreArene);
        //if (mineralcontest.debug)
            mineralcontest.plugin.getLogger().info(mineralcontest.prefixGlobal + Lang.arena_chest_added.toString());
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public AutomatedChestAnimation getCoffre() {
        return this.coffreArene;
    }


}
