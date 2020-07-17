package fr.synchroneyes.mineral.Shop.NPCs;

import fr.synchroneyes.mineral.Shop.Categories.Abstract.Category;
import fr.synchroneyes.mineral.Shop.Categories.*;
import fr.synchroneyes.mineral.Shop.Items.AmeliorationTemporaire.AjouterVieSupplementaire;
import fr.synchroneyes.mineral.Shop.Items.AmeliorationTemporaire.DerniereChance;
import fr.synchroneyes.mineral.Shop.Items.AmeliorationTemporaire.PotionExperience;
import fr.synchroneyes.mineral.Shop.Items.Equipe.ActiverAnnonceProchainCoffre;
import fr.synchroneyes.mineral.Shop.Items.Equipe.SingleAreneTeleport;
import fr.synchroneyes.mineral.Shop.Items.Equipe.TeleportEquipeAreneAuto;
import fr.synchroneyes.mineral.Shop.Items.Informations.ProchainCoffreAreneItem;
import fr.synchroneyes.mineral.Shop.Items.Informations.ProchainLargageAerienPosition;
import fr.synchroneyes.mineral.Shop.Items.Informations.ProchainLargageAerienTemps;
import fr.synchroneyes.mineral.Shop.Items.Items.*;
import fr.synchroneyes.mineral.Shop.Items.Levelable.Pioche.Pioche1;
import fr.synchroneyes.mineral.Shop.Items.Levelable.Pioche.Pioche2;
import fr.synchroneyes.mineral.Shop.Items.Levelable.Pioche.Pioche3;
import fr.synchroneyes.mineral.Shop.Items.Permanent.AjoutCoeursPermanent;
import fr.synchroneyes.mineral.Shop.Items.Permanent.AutoLingot;
import fr.synchroneyes.mineral.Shop.Items.Permanent.EpeeDiamant;
import fr.synchroneyes.mineral.Shop.Items.Potions.PotionHaste;
import fr.synchroneyes.mineral.Shop.Items.Potions.PotionInvisibilite;
import fr.synchroneyes.mineral.Shop.Items.Potions.PotionSpeed1;
import fr.synchroneyes.mineral.Shop.Items.Potions.PotionSpeed2;
import fr.synchroneyes.mineral.Translation.Lang;
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

        super(4);

        categories_dispo = new LinkedList<>();

        this.setEmplacement(position);


        // On crée les catégories ainsi que l'ajout d'item

        Informations informations = new Informations(this);
        informations.addItemToInventory(new ProchainLargageAerienPosition(), 0);
        informations.addItemToInventory(new ProchainLargageAerienTemps(), 1);
        informations.addItemToInventory(new ProchainCoffreAreneItem(), 2);


        Items items = new Items(this);
        items.addItemToInventory(new BatonKnockback(), 0);
        items.addItemToInventory(new BouleDeFeu(), 1);
        items.addItemToInventory(new Boussole(), 2);
        items.addItemToInventory(new Buche(), 3);
        items.addItemToInventory(new PommeDoree(), 4);
        items.addItemToInventory(new SceauDeau(), 5);

        Potions potions = new Potions(this);
        potions.addItemToInventory(new PotionHaste(), 0);
        potions.addItemToInventory(new PotionInvisibilite(), 1);
        potions.addItemToInventory(new PotionSpeed1(), 2);
        potions.addItemToInventory(new PotionSpeed2(), 3);


        BonusPermanent bonusPermanent = new BonusPermanent(this);
        bonusPermanent.addItemToInventory(new AjoutCoeursPermanent(), 0);
        bonusPermanent.addItemToInventory(new AutoLingot(), 1);
        bonusPermanent.addItemToInventory(new EpeeDiamant(), 2);

        BonusEquipe bonusEquipe = new BonusEquipe(this);
        bonusEquipe.addItemToInventory(new ActiverAnnonceProchainCoffre(), 0);
        bonusEquipe.addItemToInventory(new SingleAreneTeleport(), 1);
        bonusEquipe.addItemToInventory(new TeleportEquipeAreneAuto(), 2);

        BonusPersonnel bonusPersonnel = new BonusPersonnel(this);
        bonusPersonnel.addItemToInventory(new AjouterVieSupplementaire(), 0);
        bonusPersonnel.addItemToInventory(new DerniereChance(), 2);
        bonusPersonnel.addItemToInventory(new PotionExperience(), 3);


        Ameliorations ameliorations = new Ameliorations(this);
        ameliorations.addItemToInventory(new Pioche1(), 0);
        ameliorations.addItemToInventory(new Pioche2(), 1);
        ameliorations.addItemToInventory(new Pioche3(), 3);


        categories_dispo.add(informations);
        categories_dispo.add(items);
        categories_dispo.add(potions);
        categories_dispo.add(bonusPermanent);
        categories_dispo.add(bonusEquipe);
        categories_dispo.add(bonusPersonnel);
        categories_dispo.add(ameliorations);




    }

    @Override
    public String getNomAffichage() {
        return Lang.shopitem_npc_title.toString();
    }

    @Override
    public Villager.Profession getNPCType() {
        return null;
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
