package fr.synchroneyes.mineral.Core;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.Player.BaseItem.PlayerBaseItem;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.mineralcontest;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;

import javax.swing.*;


/**
 * Classe représentant un joueur du plugin
 * Cette classe contient toutes les informations nécessaire afin de représenter un joueur
 */
public class MCPlayer {

    // Groupe du joueur
    @Getter
    private Groupe groupe;

    // Partie du joueur
    @Getter
    private Game partie;

    // Le joueur possède une équipe
    @Getter
    private Equipe equipe;

    // La maison du joueur (liée au joueur)
    @Getter
    private House maison;

    // Joueur
    @Getter
    private Player joueur;

    /**
     * Constructeur, prend un joueur en paramètre
     * @param joueur
     */
    public MCPlayer(Player joueur) {
        this.joueur = joueur;
    }


    /**
     * Méthode permettant d'affecter le groupe ainsi que la partie du joueur
     * @param groupe
     */
    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
        this.partie = groupe.getGame();
    }

    /**
     * Méthode permettant d'affecter une équipe à un joueur
     * @param equipe
     */
    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
        this.maison = equipe.getMaison();
    }

    /**
     * Méthode permettant d'affecter une maison à un joueur
     * @param house
     */
    public void setMaison(House house) {
        this.maison = house;
        this.equipe = house.getTeam();
    }

    /* ---------------------------------------- */

    /**
     * Méthode permettant de passer au travers de l'écran de mort
     */
    public void cancelDeathEvent() {

        // On met les niveaux de vie & faim du joueur à fond
        setMaxHealth();
        setMaxFood();

        // On ouvre son inventaire 1 tick plus tard
        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
            this.joueur.openInventory(this.joueur.getInventory());

            // Et un tick plus tard, on le ferme
            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> this.joueur.closeInventory(), 1);

        }, 1);


    }

    /**
     * Méthode permettant de mettre la vie à fond du joueur
     */
    public void setMaxHealth() {
        double maxPlayerHealth = (joueur.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) ? joueur.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() : 20;
        this.joueur.setHealth(maxPlayerHealth);
    }

    /**
     * Méthode permettant de mettre la nourriture au max du joueur
     */
    public void setMaxFood() {
        int maxHungerLevel = 20;
        this.joueur.setFoodLevel(maxHungerLevel);
    }

    /**
     * Méthode permettant de retirer toutes les effets de potions du joueur
     */
    public void clearPlayerPotionEffects() {
        for (PotionEffect potion : joueur.getActivePotionEffects())
            joueur.removePotionEffect(potion.getType());
    }

    /**
     * Méthode permettant de clear l'inventaire du joueur
     */
    public void clearInventory() {
        joueur.getInventory().clear();
    }


    /**
     * Méthode permettant de donner les items de base au joueur
     */
    public void giveBaseItems() {
        if(groupe != null) groupe.getPlayerBaseItem().giveItemsToPlayer(this.joueur);
    }

    /**
     *
     */
    public void teleportToHouse() {
       if(maison != null) this.joueur.teleport(maison.getHouseLocation());
    }

    /**
     * Méthode permettant de jouer des feux d'artifices du un joueur
     */
    public void playFireworks(Color couleur) {
        Firework firework = joueur.getWorld().spawn(joueur.getLocation(), Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        // On ajoute un effet
        fireworkMeta.addEffect(FireworkEffect.builder()
                .flicker(true)
                .trail(true)
                .withColor(couleur)
                .withFade(Color.WHITE)
                .build()

        );

        fireworkMeta.setPower(1);
        firework.setFireworkMeta(fireworkMeta);
    }

    /**
     * Méthode permettant d'envoyer un message privé
     * @param message
     */
    public void sendPrivateMessage(String message) {
        this.joueur.sendMessage(mineralcontest.prefixPrive + message);
    }



}

