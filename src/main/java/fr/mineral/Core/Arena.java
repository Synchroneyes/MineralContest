package fr.mineral.Core;

import fr.mineral.Coffre;
import fr.mineral.Core.Zones.DeathZone;
import fr.mineral.Teams.Equipe;
import fr.mineral.mineralcontest;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/*
    Classe représentant une arène
 */
public class Arena {

    /*
        Une arène contient:
            - Un coffre
            - Une zone de spawn
            - Une deathZone (même si on meurt en dehors)
            - Un contour ??
     */

    private Location teleportSpawn;
    private Coffre coffre;
    private boolean allowTeleport;
    private DeathZone deathZone;


    public void enableTeleport() { this.allowTeleport = true; }
    public void disableTeleport() { this.allowTeleport = false; }
    public Location getTeleportSpawn() { return this.teleportSpawn; }
    public DeathZone getDeathZone() { return this.deathZone; }


    public Arena() {
        this.deathZone = new DeathZone();
    }

    public void setTeleportSpawn(Location z) {
        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + "Ajout de la position de téléportation vers l'arène");
        this.teleportSpawn = z;
    }

    // Set le coffre de l'arène
    public void setCoffre(Location position) {
        this.coffre = new Coffre();
        this.coffre.setPosition(position);
        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + "Position du coffre d'arene ajoutée");

    }

    public Coffre getCoffre() throws Exception {
        return this.coffre;
    }


    public void teleportPlayerToArena(Player joueur) throws Exception {
        if(this.getTeleportSpawn() == null) {
            throw new Exception("La zone de spawn de l'arene n'est pas defini");
        }

        Equipe team = mineralcontest.plugin.getGame().getPlayerTeam(joueur);

        if(team == null) {
            throw new Exception("Impossible de téléporter un joueur sans équipe.");
        }

        for(Player membre : team.getJoueurs()) {
            if(allowTeleport){
                membre.sendMessage(mineralcontest.prefixPrive + mineralcontest.ARENA_TELEPORTING);
                membre.teleport(getTeleportSpawn());
            } else {
                membre.sendMessage(mineralcontest.prefixPrive + mineralcontest.ARENA_TELEPORT_DISABLED);
            }
        }
    }

}
