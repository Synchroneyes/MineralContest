package fr.synchroneyes.mineral.Kits.Classes;

import fr.synchroneyes.mineral.Kits.KitAbstract;

/**
 * Classe robuste, 15 coeurs, -15% de dégat, -15% de vitesse
 */
public class Robuste extends KitAbstract {
    @Override
    public String getNom() {
        return "Robuste";
    }

    @Override
    public String getDescription() {
        return "Vous avez 15 coeurs, 15% de dégats en moins, et 15% de vitesse en moins";
    }


}
