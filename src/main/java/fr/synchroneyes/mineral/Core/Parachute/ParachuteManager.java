package fr.synchroneyes.mineral.Core.Parachute;

import fr.synchroneyes.groups.Core.Groupe;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe gérant les drop
 */
public class ParachuteManager {

    private Groupe groupe;
    private HashMap<String, ParachuteBlock> parachute;
    private double health;

    public ParachuteManager(Groupe groupe) {
        this.groupe = groupe;
    }


    public Groupe getGroupe() {
        return groupe;
    }


    /**
     * Retourne vrai si le parachute est touché par une flèche
     *
     * @param fleche La flèche tirée
     * @return
     */
    public boolean isParachuteHit(Arrow fleche) {
        return isParachuteHit(fleche.getLocation().getBlock().getLocation());
    }

    /**
     * Retourne vrai si la localisation donnée est une localisation d'un des blocs du parachute
     *
     * @param loc
     * @return
     */
    public boolean isParachuteHit(Location loc) {
        for (Map.Entry<String, ParachuteBlock> blockDeParachute : getParachute().entrySet())
            if (blockDeParachute.getValue().getLocation().equals(loc)) return true;
        return false;
    }

    /**
     * Retourne le parachute
     *
     * @return HashMap parachute
     */
    public HashMap<String, ParachuteBlock> getParachute() {
        return parachute;
    }
}
