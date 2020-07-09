package fr.synchroneyes.mineral.Shop.NPCs;

import fr.synchroneyes.mineral.Shop.Categories.*;
import fr.synchroneyes.mineral.Shop.Items.AmeliorationTemporaire.AjouterVieSupplementaire;
import fr.synchroneyes.mineral.Shop.Items.Equipe.ActiverAnnonceProchainCoffre;
import fr.synchroneyes.mineral.Shop.Items.Equipe.SingleAreneTeleport;
import fr.synchroneyes.mineral.Shop.Items.Equipe.TeleportEquipeAreneAuto;
import fr.synchroneyes.mineral.Shop.Items.Informations.ProchainCoffreAreneItem;
import fr.synchroneyes.mineral.Shop.Items.Informations.ProchainLargageAerienPosition;
import fr.synchroneyes.mineral.Shop.Items.Informations.ProchainLargageAerienTemps;
import fr.synchroneyes.mineral.Shop.Items.Items.Buche;
import fr.synchroneyes.mineral.Shop.Items.Items.PommeDoree;
import fr.synchroneyes.mineral.Shop.Items.Permanent.AjoutCoeursPermanent;
import fr.synchroneyes.mineral.Shop.Items.Permanent.EpeeDiamant;
import fr.synchroneyes.mineral.Shop.Items.Potions.PotionHaste;
import fr.synchroneyes.mineral.Shop.Items.Potions.PotionInvisibilite;
import fr.synchroneyes.mineral.Shop.Items.Potions.PotionSpeed1;
import fr.synchroneyes.mineral.Shop.Items.Potions.PotionSpeed2;
import lombok.Getter;
import org.bukkit.Location;
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

    @Getter
    private List<Category> categories_dispo;

    public BonusSeller(Location position) {

        super(1);

        categories_dispo = new LinkedList<>();

        // On crée les catégories ainsi que l'ajout d'item

        Informations categorieInfo = new Informations();
        categorieInfo.addItemToInventory(new ProchainCoffreAreneItem(), 0);
        categorieInfo.addItemToInventory(new ProchainLargageAerienTemps(), 1);
        categorieInfo.addItemToInventory(new ProchainLargageAerienPosition(), 2);


        BonusEquipe categorieBonusEquipe = new BonusEquipe();
        categorieBonusEquipe.addItemToInventory(new ActiverAnnonceProchainCoffre(), 0);
        categorieBonusEquipe.addItemToInventory(new TeleportEquipeAreneAuto(), 1);
        categorieBonusEquipe.addItemToInventory(new SingleAreneTeleport(), 2);

        BonusPermanent categorieBonusPermanent = new BonusPermanent();
        categorieBonusPermanent.addItemToInventory(new EpeeDiamant(), 0);
        categorieBonusPermanent.addItemToInventory(new AjoutCoeursPermanent(), 1);


        BonusPersonnel categorieBonusPersonnel = new BonusPersonnel();
        categorieBonusPersonnel.addItemToInventory(new AjouterVieSupplementaire(), 0);

        Potions categoriePotion = new Potions();
        categoriePotion.addItemToInventory(new PotionInvisibilite(), 0);
        categoriePotion.addItemToInventory(new PotionSpeed1(), 1);
        categoriePotion.addItemToInventory(new PotionSpeed2(), 2);
        categoriePotion.addItemToInventory(new PotionHaste(), 3);

        Items categorieNourriture = new Items();
        categorieNourriture.addItemToInventory(new PommeDoree(), 0);
        categorieNourriture.addItemToInventory(new Buche(), 1);

        categories_dispo.add(categorieInfo);

        categories_dispo.add(categorieBonusPermanent);

        categories_dispo.add(categorieBonusEquipe);

        categories_dispo.add(categorieBonusPersonnel);

        categories_dispo.add(categorieNourriture);
        categories_dispo.add(categoriePotion);

        this.setEmplacement(position);


    }

    @Override
    public String getNomAffichage() {
        return "Boutique";
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
