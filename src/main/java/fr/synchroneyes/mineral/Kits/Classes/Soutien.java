package fr.synchroneyes.mineral.Kits.Classes;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Kits.KitAbstract;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Kit soutien, heal ses amis proche de lui, possède -5 coeurs et -15% de speed
 */
public class Soutien extends KitAbstract {

    // Le nombre de coeur qu'un joueur doit avoir en utilisant cette classe
    private double nombreCoeur = 5.0;

    // Le pourcentage de réduction de vitesse du joueur
    private double pourcentageReductionVitesse = 15.0;

    // Rayon en bloc dans lequel le joueur peut heal son équipe
    private double radiusHeal = 5;

    // La vie à redonner à un joueur se faisant heal
    private double healToGive = 1;

    @Override
    public String getNom() {
        return Lang.kit_support_title.toString();
    }

    @Override
    public String getDescription() {
        return Lang.kit_support_description.toString();
    }

    @Override
    public Material getRepresentationMaterialForSelectionMenu() {
        return Material.GOLDEN_APPLE;
    }


    /**
     * Permet de donner de la vie aux joueurs proche de la personne et, qui sont dans la même équipe
     *
     * @param joueur
     */
    public void healAroundPlayer(Player joueur) {


        // On commence par récupérer l'équipe du joueur
        // Ainsi que sa partie
        Game partie = mineralcontest.getPlayerGame(joueur);
        if (partie == null) return;

        // Un arbitre n'a pas à heal ses potes
        if (partie.isReferee(joueur)) return;

        // On récupère l'équipe du joueur
        Equipe playerTeam = partie.getPlayerTeam(joueur);

        // Si le joueur n'a pas d'équipe, on s'arrête
        if (playerTeam == null) return;

        // On va récuperer les entitées proches du joueur
        List<Entity> entites = joueur.getNearbyEntities(radiusHeal, radiusHeal, radiusHeal);

        // Pour chaque entité
        for (Entity entite : entites) {
            // On vérifie si c'est un joueur
            if (entite instanceof Player) {
                Player otherPlayer = (Player) entite;

                // On récupère l'équipe de l'autre joueur
                Equipe otherPlayerTeam = partie.getPlayerTeam(otherPlayer);

                // Si ils sont de la même équipe, on le heal
                if (playerTeam.equals(otherPlayerTeam)) {
                    double maxPlayerHealth = otherPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

                    // Si le joueur possède déjà toute sa vie, on passe à l'entité suivante
                    if (otherPlayer.getHealth() >= maxPlayerHealth) continue;

                    // Sinon, on ajoute la vie au joueur
                    double currentPlayerHealth = otherPlayer.getHealth();

                    // On s'assure que sa vie sera inférieur ou égale à sa capacité maximale
                    double newPlayerHealth = Math.min(healToGive + currentPlayerHealth, maxPlayerHealth);

                    // On applique sa vie
                    otherPlayer.setHealth(newPlayerHealth);

                    // On joue des particules pour informer le heal

                    Location playerLocation = joueur.getLocation().clone();
                    Location otherPlayerLocation = otherPlayer.getLocation().clone();

                    playerLocation.setY(playerLocation.getY() + 3);
                    otherPlayerLocation.setY(otherPlayerLocation.getY() + 3);


                    World currentWorld = joueur.getWorld();
                    currentWorld.spawnParticle(Particle.VILLAGER_HAPPY, playerLocation, 20);
                    currentWorld.spawnParticle(Particle.VILLAGER_HAPPY, otherPlayerLocation, 20);
                }
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

        // On met le joueur à 15 coeurs
        joueur.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(nombreCoeur * 2);

        // On calcule sa nouvelle vitesse
        double newSpeed = currentSpeed - (currentSpeed * pourcentageReductionVitesse / 100);


        joueur.setWalkSpeed((float) newSpeed);
    }
}
