package fr.synchroneyes.mineral.Kits.Classes;

import fr.synchroneyes.custom_events.MCGameStartedEvent;
import fr.synchroneyes.custom_events.MCPlayerRespawnEvent;
import fr.synchroneyes.custom_events.PlayerKitSelectedEvent;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Kits.KitAbstract;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

/**
 * Fait 25% de dégats en plus, mais perds se déplace 15% moins vite
 */
public class Guerrier extends KitAbstract {


    private double bonusPercentage = 25d;

    // Le pourcentage de réduction de vitesse du joueur
    private double pourcentageReductionVitesse = 15.0;

    private double vieEnMoins = 2.5;



    @Override
    public String getNom() {
        return Lang.kit_warrior_title.toString();
    }

    @Override
    public String getDescription() {
        return Lang.kit_warrior_description.toString();
    }

    @Override
    public Material getRepresentationMaterialForSelectionMenu() {
        return Material.GOLDEN_SWORD;
    }


    /**
     * Fonction appelé lors du respawn d'un guerrier
     *
     * @param event
     */
    @EventHandler
    public void onRespawn(MCPlayerRespawnEvent event) {

        if (!isPlayerUsingThisKit(event.getJoueur())) return;

        setPlayerBonus(event.getJoueur());

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
     * Evenement appelé lors du démarrage d'une game
     *
     * @param event
     */
    @EventHandler
    public void onGameStart(MCGameStartedEvent event) {
        Game partie = event.getGame();

        // Pour chaque joueur de la partie
        for (Player joueur : partie.groupe.getPlayers()) {
            // On vérifie si ils ont ce kit
            if (isPlayerUsingThisKit(joueur)) {
                // Et on réduit sa vitesse, et on retire sa vie
                setPlayerBonus(joueur);
            }
        }
    }

    /**
     * Permet d'ajouter ce bonus à un joueur passé en paramètre
     */
    private void setPlayerBonus(Player joueur) {
        double valeurDegatsParDefaut = 1.0;
        double currentSpeed = 0.2f;

        double vieParDefaut = 20d;

        double nouvelleVie = vieParDefaut - (vieEnMoins * 2);



        // On calcule sa nouvelle vitesse
        double newSpeed = currentSpeed - (currentSpeed * pourcentageReductionVitesse / 100);
        joueur.setWalkSpeed((float) newSpeed);



        double nouvelleValeur = valeurDegatsParDefaut + (valeurDegatsParDefaut * bonusPercentage / 100);
        joueur.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(nouvelleVie);
        joueur.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(nouvelleValeur);


    }


}
