package fr.synchroneyes.mineral.Shop;

import fr.synchroneyes.file_manager.FileList;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Shop.NPCs.BonusSeller;
import fr.synchroneyes.mineral.mineralcontest;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

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


}
