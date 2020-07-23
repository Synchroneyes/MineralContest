package fr.synchroneyes.mineral.Kits.Classes;

import fr.synchroneyes.custom_events.MCPlayerRespawnEvent;
import fr.synchroneyes.custom_events.PlayerKitSelectedEvent;
import fr.synchroneyes.mineral.Kits.KitAbstract;
import org.bukkit.attribute.Attribute;
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
        return "Agile";
    }

    @Override
    public String getDescription() {
        return "Vous permet de vous déplacer 25% plus vite, et retire vos dégats de chute";
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
        double defaultSpeed = 0.1;

        double nouvelleValeur = (defaultSpeed + (defaultSpeed * vitesseSupplementairePourcentage / 100));

        joueur.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(nouvelleValeur);
    }
}
