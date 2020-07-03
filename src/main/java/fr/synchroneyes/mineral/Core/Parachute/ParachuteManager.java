package fr.synchroneyes.mineral.Core.Parachute;

import fr.synchroneyes.groups.Core.Groupe;

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



}
