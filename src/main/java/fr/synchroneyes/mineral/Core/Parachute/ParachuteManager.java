package fr.synchroneyes.mineral.Core.Parachute;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

/**
 * Classe gérant les drop
 */
public class ParachuteManager {

    private Groupe groupe;
    private List<Parachute> parachutes;

    public ParachuteManager(Groupe groupe) {
        this.groupe = groupe;
        this.parachutes = new LinkedList<>();
    }


    public Groupe getGroupe() {
        return groupe;
    }


    public void spawnNewParachute(Location l) {
        Parachute parachute = new Parachute(20);
        parachute.spawnParachute(l);
        this.parachutes.add(parachute);

        // On averti les joueurs qu'un largage vient d'apparaitre
        for (Player joueur : groupe.getPlayers())
            joueur.sendTitle("Largage " + ChatColor.RED + "aérien", Lang.translate("Un %red%largage aérien%white% a été repéré en X: %red%" + (int) (l.getBlockX()) + "%white% Z: %red%" + l.getBlockZ()), 20, 20 * 5, 20);
    }



}
