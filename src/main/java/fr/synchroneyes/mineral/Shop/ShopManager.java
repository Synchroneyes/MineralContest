package fr.synchroneyes.mineral.Shop;

import fr.synchroneyes.file_manager.FileList;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Shop.Items.Levelable.Pioche.Pioche1;
import fr.synchroneyes.mineral.Shop.Items.Levelable.Pioche.Pioche2;
import fr.synchroneyes.mineral.Shop.Items.Levelable.Pioche.Pioche3;
import fr.synchroneyes.mineral.Shop.Items.Permanent.EpeeDiamant;
import fr.synchroneyes.mineral.Shop.NPCs.BonusSeller;
import fr.synchroneyes.mineral.mineralcontest;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe permettant de gérer les boutiques sur une partie
 * On aura un pnj par maison
 */
public class ShopManager {

    @Getter
    private Game partie;

    // Liste des vendeurs actif
    @Getter
    private List<BonusSeller> liste_pnj;


    /**
     * Contructeur
     *
     * @param partie où le shop doit être géré
     */
    public ShopManager(Game partie) {
        this.partie = partie;
        this.liste_pnj = new LinkedList<>();
    }

    /**
     * Fonction permettant d'ajouter un vendeur à la liste des vendeurs
     *
     * @param seller
     */
    public void ajouterVendeur(BonusSeller seller) {
        if (liste_pnj.contains(seller)) return;
        liste_pnj.add(seller);
    }

    /**
     * Fonction permettant de créer un vendeur à une position donnée
     *
     * @param position
     * @return
     */
    public static BonusSeller creerVendeur(Location position) {
        return new BonusSeller(position);
    }

    /**
     * Permet de récuperer le prix d'un item à partir du fichier
     *
     * @param bonusName
     * @return
     */
    public static int getBonusPriceFromName(String bonusName) {
        File fichierPrix = new File(mineralcontest.plugin.getDataFolder(), FileList.ShopItem_PriceList.toString());

        // Si le fichier n'existe pas, on retourne 1000 ...
        if (!fichierPrix.exists()) return 1000;

        return YamlConfiguration.loadConfiguration(fichierPrix).getInt(bonusName);

    }


    /**
     * Retourne vrai si l'item passé en paramètre est un item obtenu depuis un bonus
     *
     * @param item
     * @return
     */
    public static boolean isAnShopItem(ItemStack item) {
        if (item == null) return false;

        // Si c'est une épée en diams (bonus epée diams)
        if (item.getType() == Material.DIAMOND_SWORD) {
            // On regarde si il y a un nom, et si c'est le même que celui du bonus épée diams
            if (item.getItemMeta() != null && item.getItemMeta().getDisplayName().equals(EpeeDiamant.itemNameColored))
                return true;
            return false;
        }

        // On regarde si c'est un item levelable pioche
        if (item.getType() == Material.IRON_PICKAXE) {
            // ON regarde si c'est le lvl 1
            if (item.getItemMeta() != null) {
                // On check LVL 1
                String itemName = item.getItemMeta().getDisplayName();
                if (itemName.equals(Pioche1.coloredItemName)) return true;
                return false;
            }
        }

        if (item.getType() == Material.DIAMOND_PICKAXE) {
            // ON regarde si c'est le lvl 1
            if (item.getItemMeta() != null) {
                // On check LVL 1
                String itemName = item.getItemMeta().getDisplayName();
                if (itemName.equals(Pioche2.coloredItemName)) return true;
                if (itemName.equals(Pioche3.coloredItemName)) return true;

                return false;
            }
        }

        return false;
    }


    /**
     * Fonction permettant de désactiver le shop
     */
    public void disableShop() {
        // ON récupère tous les PNJ
        for (BonusSeller vendeur : liste_pnj)
            // On le supprime
            vendeur.getEntity().remove();
    }

    /**
     * Fonction permettant de activer le shop
     */
    public void enableShop() {

        // ON récupère tous les PNJ
        for (BonusSeller vendeur : liste_pnj)
            // On le supprime
            vendeur.spawn();
    }




}
