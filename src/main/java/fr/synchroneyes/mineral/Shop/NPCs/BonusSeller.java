package fr.synchroneyes.mineral.Shop.NPCs;

import fr.synchroneyes.mineral.Shop.Categories.*;
import fr.synchroneyes.mineral.Shop.Items.ProchainCoffreAreneItem;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.LinkedList;
import java.util.List;

/**
 * Classe permettant de vendre des items à des joueurs
 */
public class BonusSeller extends NPCTemplate {

    public List<Category> categories_dispo;

    public BonusSeller() {
        categories_dispo = new LinkedList<>();

        // On crée les catégories ainsi que l'ajout d'item

        Informations categorieInfo = new Informations();
        categorieInfo.addItemToInventory(new ProchainCoffreAreneItem(), 1);


        categories_dispo.add(categorieInfo);

        categories_dispo.add(new BonusPermanent());
        categories_dispo.add(new BonusEquipe());
        categories_dispo.add(new Nourriture());
        categories_dispo.add(new Potions());


    }

    @Override
    public String getNomAffichage() {
        return "BONUS";
    }

    @Override
    public Villager.Profession getNPCType() {
        return Villager.Profession.ARMORER;
    }

    @Override
    public void onNPCRightClick(Player joueur) {
        joueur.openInventory(getInventory());
    }

    @Override
    public void onNPCLeftClick(Player joueur) {

    }

    @Override
    public void onInventoryItemClick(Event event) {
        if (event instanceof InventoryClickEvent) {
            InventoryClickEvent inventoryClickEvent = (InventoryClickEvent) event;

            Player joueur = (Player) inventoryClickEvent.getWhoClicked();

            // On regarde pour chaque catégorie, si l'item cliqué appartient à cette catégorie
            for (Category category : categories_dispo) {
                if (category.toItemStack().equals(inventoryClickEvent.getCurrentItem())) {
                    category.openMenuToPlayer(joueur);
                    return;
                }
            }

        }
    }

    /**
     * écupère l'inventaire du vendeur
     *
     * @return
     */
    @Override
    public Inventory getInventory() {

        this.inventaire.clear();

        // Pour chaque catégorie
        for (Category category : categories_dispo) {
            inventaire.addItem(category.toItemStack());
        }

        return inventaire;
    }
}
