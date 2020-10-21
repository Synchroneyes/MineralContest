package fr.synchroneyes.mineral.Core.Boss;

import org.bukkit.Location;

import java.util.LinkedList;
import java.util.List;

/**
 * Classe permettant de gérer les boss présent dans une partie
 * Date de création: 21.10.2020
 * Auteur: Synchroneyes
 */

public class BossManager {

    /**
     * Liste des boss présent dans la partie
     */
    private List<Boss> bossList;

    public BossManager() {
        this.bossList = new LinkedList<>();
    }

    /**
     * Méthode permettant d'ajouter un boss à un monde
     * @param position
     * @param boss
     */
    public void spawnNewBoss(Location position, Boss boss) {
        this.bossList.add(boss);
        boss.spawn(position);
    }

}
