package fr.synchroneyes.mineral.Core.Boss;

import fr.synchroneyes.mineral.Core.Game.Game;
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

    /**
     * Partie dans lequel le bossmanager doit agir
     */
    private Game partie;

    public BossManager(Game partie) {
        this.bossList = new LinkedList<>();
        this.partie = partie;
    }

    /**
     * Méthode permettant d'ajouter un boss à un monde
     * @param position
     * @param boss
     */
    public void spawnNewBoss(Location position, Boss boss) {
        this.bossList.add(boss);
        boss.spawn(position);
        boss.setChestManager(partie.groupe.getAutomatedChestManager());
    }

}
