package fr.synchroneyes.mineral.Shop.NPCs;


import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;


/**
 * Classe permettant d'illustrer un NPC
 */
public abstract class NPCTemplate {


    // Permet d'avoir la NPC sur une map
    @Getter
    @Setter
    private Location emplacement;

    protected Inventory inventaire;


    public NPCTemplate() {
        this.inventaire = Bukkit.createInventory(null, 9 * 6, getNomAffichage());
    }


    /**
     * Récupère le nom d'affichage
     *
     * @return
     */
    public abstract String getNomAffichage();

    /**
     * Récupère le type de pnj à spawn
     *
     * @return
     */
    public abstract Villager.Profession getNPCType();


    /**
     * Permet d'effectuer une fonction lors du clic droit sur un npc
     *
     * @param joueur
     */
    public abstract void onNPCRightClick(Player joueur);

    /**
     * Permet d'effectuer une fonction lors du clic gauche sur un npc
     *
     * @param joueur
     */
    public abstract void onNPCLeftClick(Player joueur);

    /**
     * Evenement a appeler en cas de click sur un item d'inventaire
     *
     * @param event
     */
    public abstract void onInventoryItemClick(Event event);

    /**
     * Fonction appelée afin d'obtenir l'inventaire lié au NPC
     */
    public abstract Inventory getInventory();


    /**
     * Permet de faire apparaitre le NPC à la position enregistrée
     **/
    public void spawn() {
        if (this.emplacement == null) return;
        if (this.emplacement.getWorld() == null) return;

        // On récupère le monde où faire spawn l'entité
        World monde = emplacement.getWorld();
        Villager entitySpawned = monde.spawn(emplacement, Villager.class);


        // On change les paramètres de l'entité
        entitySpawned.setAI(false);
        entitySpawned.setAdult();
        entitySpawned.setProfession(getNPCType());
        entitySpawned.setInvulnerable(true);
        entitySpawned.setCustomNameVisible(true);
        entitySpawned.setCustomName(getNomAffichage());
        entitySpawned.setCollidable(false);
        entitySpawned.setRemoveWhenFarAway(false);
        entitySpawned.setAgeLock(true);


    }


}
