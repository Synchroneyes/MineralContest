package fr.synchroneyes.mineral.Kits.Classes;

import fr.synchroneyes.custom_events.MCGameStartedEvent;
import fr.synchroneyes.custom_events.MCPlayerRespawnEvent;
import fr.synchroneyes.custom_events.PlayerDeathByPlayerEvent;
import fr.synchroneyes.custom_events.PlayerKitSelectedEvent;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Kits.KitAbstract;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

/**
 * Spawn avec un cheval, -25% de vitesse à pied, et ne peux pas faire de dégats avec des armes, uniquement des projectiles
 */
public class CowBoy extends KitAbstract {


    private double reductionVitesse = 15.0;

    // Liste des joueurs avec leurs chevaux respectifs
    public HashMap<UUID, Horse> chevaux_joueurs;

    public CowBoy() {
        super();

        this.chevaux_joueurs = new HashMap<>();
    }

    @Override
    public String getNom() {
        return Lang.kit_cowboy_title.toString();
    }

    @Override
    public String getDescription() {
        return Lang.kit_cowboy_description.toString();
    }


    /**
     * Fonction appelée lors de la selection du kit
     * On enregistre le joueur et on lui attribu un cheval
     */
    @EventHandler
    public void onKitSelected(PlayerKitSelectedEvent event) {
        // On vérifie que le joueur utilise bien ce kit
        if (!isPlayerUsingThisKit(event.getPlayer())) return;

        // On regarde si le joueur est déjà enregistré
        if (chevaux_joueurs.containsKey(event.getPlayer().getUniqueId())) return;

        // Si il ne l'est pas, on l'enregistre
        chevaux_joueurs.put(event.getPlayer().getUniqueId(), null);
    }



    @Override
    public Material getRepresentationMaterialForSelectionMenu() {
        return Material.GOLDEN_HORSE_ARMOR;
    }

    /**
     * Fonction appelée au démarrage de la partie
     *
     * @param event
     */
    @EventHandler
    public void OnGameStart(MCGameStartedEvent event) {
        Game partie = event.getGame();

        // Pour chaque joueur de la partie
        for (Player joueur : partie.groupe.getPlayers()) {
            // On vérifie si ils ont ce kit
            if (isPlayerUsingThisKit(joueur)) {
                // Et si c'est le cas, on équipe le joueur
                Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> spawnHorseToPlayer(joueur), 5);
                // Et on réduit sa vitesse
                applyEffectToPlayer(joueur);
            }
        }
    }


    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent entityEvent) {
        if (entityEvent.getDamager() instanceof Player) {

            // On va blocker les dégats fait par le joueur si ce n'est pas fait depuis un projectile
            Player joueur = (Player) entityEvent.getDamager();

            if (entityEvent.getCause() != EntityDamageEvent.DamageCause.PROJECTILE && joueur.getVehicle() != null && joueur.getVehicle() instanceof Horse) {
                joueur.playSound(joueur.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1f);
                joueur.sendMessage(mineralcontest.prefixErreur + "Malheureusement, vous ne pouvez pas attaquer avec cet item sur votre cheval. Utilisez un " + ChatColor.RED + "arc");
                entityEvent.setCancelled(true);
            }


        }
    }


    /**
     * Méthode appelée lorsqu'un joueur monte sur un cheval
     * @param event
     */
    @EventHandler
    public void onPlayerRideHorse(VehicleEnterEvent event) {
        // On vérifie qu'on traite bien un joueur
        if(event.getEntered() instanceof Player) {

            // On vérifie que l'entité est bien un cheval
            if(event.getVehicle() instanceof Horse) {

                Horse cheval = (Horse) event.getVehicle();

                // Si ce n'est pas le propriétaire du cheval, on empeche de monter
                if(cheval.getOwner() != null && !cheval.getOwner().equals(event.getEntered())) {
                    event.setCancelled(true);
                }
            }
        }
    }


    /**
     * Méthode appelée lorsque le joueur respawn
     *
     * @param event
     */
    @EventHandler
    public void onPlayerRespawn(MCPlayerRespawnEvent event) {
        Player joueur = event.getJoueur();

        if (!isPlayerUsingThisKit(joueur)) return;

        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> spawnHorseToPlayer(joueur), 5);

        applyEffectToPlayer(joueur);
    }

    /**
     * Evenemnt appelé lors de la mort d'un joueur
     *
     * @param event
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathByPlayerEvent event) {
        Player victime = event.getPlayerDead();

        // On vérifie que le mort était dans cette classe
        if (!isPlayerUsingThisKit(victime)) return;

        // On supprime son cheval
        killPlayerHorse(victime);
    }


    /**
     * Permet de réduire la vitesse du joueur
     *
     * @param joueur
     */
    private void applyEffectToPlayer(Player joueur) {
        // On récupère la vitesse de base d'un joueur
        double currentSpeed = 0.2f;

        // On calcule sa nouvelle vitesse
        double newSpeed = currentSpeed - (currentSpeed * reductionVitesse / 100);

        joueur.setWalkSpeed((float) newSpeed);

        joueur.setHealth(joueur.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
    }


    /**
     * Permet de récuperer l'item faisant apparaitre un cheval
     *
     * @return
     */
    public static ItemStack getItemCheval() {
        return new ItemStack(Material.HORSE_SPAWN_EGG);
    }

    /**
     * Permet de faire apparaitre le cheval de la classe CowBoy au joueur passé en paramètre
     *
     * @param joueur
     */
    private void spawnHorseToPlayer(Player joueur) {

        // Si le joueur avait déjà un cheval
        if (chevaux_joueurs.containsKey(joueur.getUniqueId())) {
            // On récupère l'ancien cheval
            Horse ancienCheval = chevaux_joueurs.get(joueur.getUniqueId());

            // On le tue
            killPlayerHorse(joueur);
        } else {
            chevaux_joueurs.put(joueur.getUniqueId(), null);
        }


        // On fait spawn le cheval
        Horse cheval = (Horse) joueur.getWorld().spawn(joueur.getLocation(), Horse.class);
        cheval.setAdult();
        cheval.setStyle(Horse.Style.BLACK_DOTS);

        cheval.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        cheval.getInventory().setArmor(new ItemStack(Material.DIAMOND_HORSE_ARMOR));
        cheval.setTamed(true);

        cheval.setOwner(joueur);
        cheval.addPassenger(joueur);


        // Et on remplace le cheval du joueur
        chevaux_joueurs.replace(joueur.getUniqueId(), cheval);

    }


    /**
     * Permet de tuer le cheval d'un joueur
     *
     * @param joueur
     */
    private void killPlayerHorse(Player joueur) {
        if (chevaux_joueurs.get(joueur.getUniqueId()) == null) return;

        Horse cheval = chevaux_joueurs.get(joueur.getUniqueId());

        // On le tue
        cheval.getInventory().clear();
        cheval.setOwner(null);
        cheval.getInventory().setSaddle(null);
        cheval.setTamed(false);
        cheval.remove();
    }
}
