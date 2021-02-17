package fr.synchroneyes.mineral.Core;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.Player.BaseItem.PlayerBaseItem;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Utils.DisconnectedPlayer;
import fr.synchroneyes.mineral.mineralcontest;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;

import javax.swing.*;
import java.util.HashMap;


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

    // ID Stocké en base de donnée pour le joueur
    @Getter @Setter
    private int databasePlayerId = 0;

    // Variable stockant le nombre de point rapporté par le joueur
    @Getter @Setter
    private int score_brought = 0;

    // Variable stockant le nombre de point que le joueur a fait perdre aux autres équipes
    @Getter @Setter
    private int score_lost = 0;

    private HashMap<World, Location> player_world_locations;

    private boolean isInPlugin = true;

    /**
     * Constructeur, prend un joueur en paramètre
     * @param joueur
     */
    public MCPlayer(Player joueur) {
        this.joueur = joueur;
        this.player_world_locations = new HashMap<>();
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


    /**
     * Méthode permettant d'ajouter le nombre de point apporté par le joueur
     * @param score
     */
    public void addPlayerScore(int score) {
        this.score_brought += score;
    }

    /**
     * Méthode permettant d'ajouter le nombre de point que le joueur a fait perdre aux autres joueurs
     * @param score
     */
    public void addPlayerScorePenalityToOtherTeams(int score) {
        this.score_lost += score;
    }


    public void resetPlayerScores() {
        this.score_lost = 0;
        this.score_brought = 0;
    }

    /**
     * Méthode permettant de définir la position d'un joueur dans un monde
     * @param w
     * @param l
     */
    public void setPlayerWorldLocation(World w, Location l){

        if(!joueur.isOnGround()) return;

        if(player_world_locations.containsKey(w)) {
            player_world_locations.replace(w, l);
        } else {
            player_world_locations.put(w, l);
        }
    }

    /**
     * Méthode permettant de récuperer la position d'un joueur dans un monde
     * @param w
     * @return
     */
    public Location getPLayerLocationFromWorld(World w){
        return player_world_locations.getOrDefault(w, null);
    }

    public boolean isInPlugin() {
        return isInPlugin;
    }

    public void setInPlugin(boolean inPlugin) {
        isInPlugin = inPlugin;
    }

    /**
     * Méthode permettant de déconnecter un joueur du plugin
     */
    public void disconnectPlayer() {



        // On traite les actions de son groupe
        if(getGroupe() != null) {

            // On l'ajoute à la liste des personnes déconnectée
            getGroupe().addDisconnectedPlayer(joueur, joueur.getLocation());


            // Si le joueur est dans un groupe, on le retire des joueurs prêts
            getGroupe().getGame().removePlayerReady(joueur);

            // On le retire des arbitres
            getGroupe().getGame().removeReferee(joueur, false);

            // On le retire des admins
            getGroupe().removeAdmin(joueur);

            // On le retire des joueurs
            getGroupe().removePlayer(joueur);


            // On le retire de son équipe si il en a une
            if(getEquipe() != null) {
                getEquipe().removePlayer(this.joueur);
            }

            // SI une game est en cours
            if(getPartie().isGameStarted()) {

                // On ferme la porte
                getMaison().getPorte().forceCloseDoor();
            }
        }

        // On retire le joueur de la liste des joueurs connecté au plugin
        mineralcontest.plugin.removePlayer(joueur);

        // On affiche un message
        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + joueur.getDisplayName() + " s'est déconnecté");
    }

    /**
     * Méthode permettant de reconnecter un joueur déconnecté
     * @param disconnectedPlayer
     */
    public void reconnectPlayer(DisconnectedPlayer disconnectedPlayer) {
        /*
            private UUID playerUUID;
            private Equipe oldPlayerTeam;
            private Groupe oldPlayerGroupe;
            private CouplePlayer oldPlayerDeathTime;
            private Location oldPlayerLocation;
            private List<ItemStack> oldPlayerInventory;
            private LinkedBlockingQueue<ShopItem> bonus;
            private KitAbstract kit;
         */

        // ON le remet dans son groupe
        if(disconnectedPlayer.getOldPlayerGroupe() != null) {
            Player joueur = Bukkit.getPlayer(disconnectedPlayer.getPlayerUUID());
            disconnectedPlayer.getOldPlayerGroupe().playerHaveReconnected(joueur);
        }
    }


}

