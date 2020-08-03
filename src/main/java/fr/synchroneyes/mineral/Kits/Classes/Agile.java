package fr.synchroneyes.mineral.Kits.Classes;

import fr.synchroneyes.custom_events.MCGameStartedEvent;
import fr.synchroneyes.custom_events.MCPlayerRespawnEvent;
import fr.synchroneyes.custom_events.PlayerKitSelectedEvent;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Kits.KitAbstract;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Se déplace 25% plus rapidement, et n'a pas de dégat de chute, sauf les chutes mortelles
 */
public class Agile extends KitAbstract {


    // Variable permettant de modifier le pourcentage de vitesse supplémentaire à accorder
    private double vitesseSupplementairePourcentage = 25d;


    @Override
    public String getNom() {
        return Lang.kit_agile_title.toString();
    }

    @Override
    public String getDescription() {
        return Lang.kit_agile_description.toString();
    }

    @Override
    public Material getRepresentationMaterialForSelectionMenu() {
        return Material.GOLDEN_BOOTS;
    }

    /**
     * Fonction appelé lors de la selection de ce kit, on va lui ajouter les 25% de vie supplémentaire
     *
     * @param event
     */
    @EventHandler
    public void onKitSelected(PlayerKitSelectedEvent event) {

        // Si le joueur n'utilise pas le kit, on s'arrête
        if (!isPlayerUsingThisKit(event.getPlayer())) return;

        addPlayerBonus(event.getPlayer());
    }

    /**
     * Appelé lors du respawn du joueur
     *
     * @param event
     */
    @EventHandler
    public void onPlayerRespawn(MCPlayerRespawnEvent event) {
        // Si le joueur n'utilise pas le kit, on s'arrête
        if (!isPlayerUsingThisKit(event.getJoueur())) return;
        addPlayerBonus(event.getJoueur());
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
                // Et applique son bonus
                addPlayerBonus(joueur);
            }
        }
    }

    /**
     * On block les dégats de chute du joueur
     *
     * @param entityDamageEvent
     */
    @EventHandler
    public void onPlayerReceiveFallDamage(EntityDamageEvent entityDamageEvent) {

        if (entityDamageEvent.getEntity() instanceof Player) {
            Player joueur = (Player) entityDamageEvent.getEntity();

            if (!isPlayerUsingThisKit(joueur)) return;

            // Si c'est une chute, on retire les dégats!
            if (entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.FALL)
                entityDamageEvent.setCancelled(true);

        }
    }


    /**
     * Permet d'ajouter à un joueur passé en paramètre le pourcentage de vitesse supplémentaire
     *
     * @param joueur
     */
    private void addPlayerBonus(Player joueur) {
        float defaultSpeed = 0.2f;

        double nouvelleValeur = (defaultSpeed + (defaultSpeed * vitesseSupplementairePourcentage / 100));

        joueur.setWalkSpeed((float) nouvelleValeur);
    }
}
