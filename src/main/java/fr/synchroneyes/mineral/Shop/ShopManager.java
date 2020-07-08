package fr.synchroneyes.mineral.Shop;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Shop.NPCs.BonusSeller;
import lombok.Getter;
import org.bukkit.Location;

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


}
