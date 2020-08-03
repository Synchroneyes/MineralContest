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
 * Classe robuste, 15 coeurs, -15% de dégat, -15% de vitesse
 */
public class Robuste extends KitAbstract {

    // Le nombre de coeur qu'un joueur doit avoir en utilisant cette classe
    private double nombreCoeur = 15.0;

    // Le pourcentage de réduction des dégats effecutés par le joueur
    private double pourcentageReductionDegats = 15.0;

    // Le pourcentage de réduction de vitesse du joueur

    private double pourcentageReductionVitesse = 15.0;

    @Override
    public String getNom() {
        return Lang.kit_toughguy_title.toString();
    }

    @Override
    public String getDescription() {
        return Lang.kit_toughguy_description.toString();
    }

    @Override
    public Material getRepresentationMaterialForSelectionMenu() {
        return Material.GOLDEN_CHESTPLATE;
    }


    /**
     * Méthode appelée lors de la selection de ce kit par le joueur
     *
     * @param event
     */
    @EventHandler
    public void onKitSelected(PlayerKitSelectedEvent event) {
        if (!isPlayerUsingThisKit(event.getPlayer())) return;
        setPlayerEffects(event.getPlayer());
    }

    /**
     * Méthode appelée lors du respawn d'un jouuer
     *
     * @param event
     */
    @EventHandler
    public void onPlayerRespawn(MCPlayerRespawnEvent event) {
        if (!isPlayerUsingThisKit(event.getJoueur())) return;

        setPlayerEffects(event.getJoueur());
    }


    /**
     * Evenement appelé lors du démarrage de la game
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
                setPlayerEffects(joueur);
            }
        }

    }


    /**
     * Fonction permettant d'ajouter à un joueur, les effets de  ce  kit
     *
     * @param joueur
     */
    private void setPlayerEffects(Player joueur) {

        // On récupère la vitesse de base d'un joueur
        double currentSpeed = 0.2f;

        // Valeur des dégats infligé de base
        double currentDamage = 1.0;

        // On met le joueur à 15 coeurs
        joueur.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(nombreCoeur * 2);

        // On calcule sa nouvelle vitesse
        double newSpeed = currentSpeed - (currentSpeed * pourcentageReductionVitesse / 100);

        // On calcule ses nouveaux dégats
        double newDamage = currentDamage - (currentDamage * pourcentageReductionDegats / 100);

        // On lui affecte les nouvelles valeurs
        joueur.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(newDamage);

        joueur.setWalkSpeed((float) newSpeed);

        joueur.setHealth(joueur.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
    }

}
