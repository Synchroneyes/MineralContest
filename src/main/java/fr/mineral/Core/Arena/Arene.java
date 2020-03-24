package fr.mineral.Core.Arena;

import fr.mineral.Core.Arena.Zones.DeathZone;
import fr.mineral.Teams.Equipe;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.Radius;
import fr.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
    private CoffreAvecCooldown coffre;
    private boolean allowTeleport;
    private DeathZone deathZone;
    private int MAX_TIME_BETWEEN_CHEST = 15; // mins
    private int MIN_TIME_BETWEEN_CHEST = 13;
    private int TIME_BEFORE_CHEST = 0;
    private int TELEPORT_TIME_LEFT = 15;
    private boolean CHEST_SPAWNED = false;
    private boolean CHEST_INITIALIZED = false;
    public boolean CHEST_USED = false;
    public int arenaRadius = 60;


    public void clear() {
        this.coffre.clear();
    }

    public void generateTimeBetweenChest() {
        // On va générer une valeur comprise entre MAX_TIME et MIN_TIME en minute
        // puis y ajouter des secondes
        int time = (int) ((Math.random() * ((MAX_TIME_BETWEEN_CHEST - MIN_TIME_BETWEEN_CHEST) + 1)) + MIN_TIME_BETWEEN_CHEST);
        time *= 60;
        // On y ajoute des secondes (entre 1 et 59)
        time += (int) ((Math.random() * ((59 - 1) + 1)) + 1);

        TIME_BEFORE_CHEST = time;
        TELEPORT_TIME_LEFT = 15;
        CHEST_INITIALIZED = true;
    }


    // Supprime les mobs autour de l'arène
    public void startAutoMobKill() {
        new BukkitRunnable() {
            public void run() {
                for(Entity entite : mineralcontest.plugin.getServer().getWorld("world").getEntities()) {
                    if(entite instanceof Monster) {
                        try {
                            if(Radius.isBlockInRadius(mineralcontest.plugin.getGame().getArene().getCoffre().getPosition(), entite.getLocation(), 100))
                                entite.remove();
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }.runTaskTimer(mineralcontest.plugin, 0, 20);
    }


    // Gère le coffre d'arene
    public void startArena() {

        generateTimeBetweenChest();
        // Coffre initialisé

        new BukkitRunnable() {
            public void run() {

                if(mineralcontest.plugin.getGame().isGameStarted() && !mineralcontest.plugin.getGame().isGamePaused()) {
                    try {
                        // Si le coffre est initialisé et n'est pas encore apparu
                        if(CHEST_INITIALIZED && !getCoffre().isChestSpawned()) {
                            // Le coffre n'est pas encore disponible
                            if(TIME_BEFORE_CHEST > 0) {
                                TIME_BEFORE_CHEST--;
                            } else {
                                // LE coffre doit apparaitre !
                                coffre.spawn();
                                //enableTeleport();
                                //generateTimeBetweenChest();

                            }

                        }

                        // SI le coffre a été utilisé, on regenère un temps
                        if(CHEST_USED) {
                            CHEST_SPAWNED = false;
                            disableTeleport();
                            TELEPORT_TIME_LEFT = 15;
                            CHEST_USED = false;
                        }

                    // Les joueurs disposent de 15 sec pour se Tp une fois le TP actif

                        if(getCoffre().isChestSpawned() && isTeleportAllowed()) {
                            TELEPORT_TIME_LEFT--;
                            if(TELEPORT_TIME_LEFT <= 0) {
                                disableTeleport();
                                TELEPORT_TIME_LEFT = 15;
                            }
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            }
        }.runTaskTimer(mineralcontest.plugin, 0, 20);

    }

    public boolean isTeleportAllowed() { return this.allowTeleport; }


    public void enableTeleport() {
        for(Player online : mineralcontest.plugin.pluginWorld.getPlayers())
            online.sendTitle(ChatColor.GREEN + Lang.translate(Lang.arena_chest_spawned.toString()), Lang.translate(Lang.arena_teleport_now_enabled.toString()), 20, 20*3, 20);
        this.allowTeleport = true;
    }
    public void disableTeleport() {
        for(Player online : mineralcontest.plugin.pluginWorld.getPlayers())
            online.sendTitle("", Lang.arena_teleport_now_disabled.toString(), 20, 20*3, 20);

        this.allowTeleport = false;
    }
    public Location getTeleportSpawn() { return this.teleportSpawn; }
    public DeathZone getDeathZone() { return this.deathZone; }


    public Arene() {
        this.deathZone = new DeathZone();
    }

    public void setTeleportSpawn(Location z) {
        mineralcontest.plugin.getLogger().info(mineralcontest.prefixGlobal + Lang.arena_spawn_added.toString());
        this.teleportSpawn = z;
    }

    // Set le coffre de l'arène
    public void setCoffre(Location position) {
        this.coffre = new CoffreAvecCooldown(position);
        this.coffre.setPosition(position);
        mineralcontest.plugin.getLogger().info(mineralcontest.prefixGlobal + Lang.arena_chest_added.toString());

    }

    public CoffreAvecCooldown getCoffre() throws Exception {
        return this.coffre;
    }


    public void teleportPlayerToArena(Player joueur) throws Exception {
        if(this.getTeleportSpawn() == null) {
            throw new Exception("ArenaTeleportZoneNotAdded");
        }

        Equipe team = mineralcontest.plugin.getGame().getPlayerTeam(joueur);

        if(team == null) {
            throw new Exception(Lang.cant_teleport_player_without_team.toString());
        }

        for(Player membre : team.getJoueurs()) {
            if(allowTeleport){
                membre.sendMessage(mineralcontest.prefixPrive + Lang.arena_now_teleporting.toString());
                membre.teleport(getTeleportSpawn());
            } else {
                membre.sendMessage(mineralcontest.prefixPrive + Lang.arena_teleport_disabled.toString());
            }
        }
    }

}
