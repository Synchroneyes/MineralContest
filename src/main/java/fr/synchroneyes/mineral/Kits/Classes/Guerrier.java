package fr.synchroneyes.mineral.Kits.Classes;

import fr.synchroneyes.custom_events.MCPlayerRespawnEvent;
import fr.synchroneyes.custom_events.PlayerKitSelectedEvent;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Kits.KitAbstract;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

/**
 * Fait 25% de dégats en plus, mais perds 2.5 coeurs
 */
public class Guerrier extends KitAbstract {


    private double bonusPercentage = 25d;
    private double vieEnMoins = 5d;


    @Override
    public String getNom() {
        return "Guerrier";
    }

    @Override
    public String getDescription() {
        return "Vous faites 25% de dégats en plus, mais vous avez 5 coeurs en moins";
    }


    /**
     * Fonction appelé lors du respawn d'un guerrier
     *
     * @param event
     */
    @EventHandler
    public void onRespawn(MCPlayerRespawnEvent event) {
        Player joueur = event.getJoueur();
        Groupe groupe = mineralcontest.getPlayerGroupe(joueur);

        setPlayerBonus(joueur);

        joueur.sendMessage("Vous êtes " + getNom() + " et vous avez respawn!");

    }

    /**
     * Fonction appelée lors de la selection du kit par le joueur
     *
     * @param event
     */
    @EventHandler
    public void onKitSelected(PlayerKitSelectedEvent event) {
        if (!isPlayerUsingThisKit(event.getPlayer())) return;

        setPlayerBonus(event.getPlayer());
    }

    /**
     * Permet d'ajouter ce bonus à un joueur passé en paramètre
     */
    private void setPlayerBonus(Player joueur) {
        double valeurDegatsParDefaut = 1.0;
        double vieParDefaut = 20d;

        double nouvelleVie = vieParDefaut - vieEnMoins;
        double nouvelleValeur = valeurDegatsParDefaut + (valeurDegatsParDefaut * bonusPercentage / 100);


        joueur.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(nouvelleValeur);
        joueur.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(nouvelleVie);

    }


}
