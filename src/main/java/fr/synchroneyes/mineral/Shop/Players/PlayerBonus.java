package fr.synchroneyes.mineral.Shop.Players;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Shop.Items.Abstract.ConsumableItem;
import fr.synchroneyes.mineral.Shop.Items.Abstract.LevelableItem;
import fr.synchroneyes.mineral.Shop.Items.Abstract.PermanentItem;
import fr.synchroneyes.mineral.Shop.Items.Abstract.ShopItem;
import fr.synchroneyes.mineral.Shop.Items.ProchainCoffreAreneItem;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Classe permettant de gérer les bonus du joueur
 */
public class PlayerBonus {

    // Liste contenant tout les bonus actifs
    public static List<ShopItem> listeBonusActif;

    public Map<Player, List<ShopItem>> bonus_par_joueur;

    @Getter
    private Game partie;

    public PlayerBonus(Game g) {

        // Si aucun bonus n'a été ajouté, on enregistre tous les bonus dispo
        if (listeBonusActif == null) enregistrerBonus();

        this.bonus_par_joueur = new HashMap<>();

        this.partie = g;
    }

    private void enregistrerBonus() {
        if (listeBonusActif == null) listeBonusActif = new LinkedList<>();

        listeBonusActif.add(new ProchainCoffreAreneItem());
    }


    /**
     * Fonction permettant de traiter la logique d'ajout d'un bonus à un joueur
     *
     * @param bonus
     * @param joueur
     */
    public void ajouterBonusPourJoueur(ShopItem bonus, Player joueur) {
        if (!bonus_par_joueur.containsKey(joueur)) bonus_par_joueur.put(joueur, new LinkedList<ShopItem>());

        List<ShopItem> liste_bonus_joueur = bonus_par_joueur.get(joueur);

        // On regarde si le joueur possède déja ce bonus
        for (ShopItem bonus_joueur : liste_bonus_joueur) {

            // On regarde si ce sont les même bonus
            if (bonus_joueur.getClass().equals(bonus.getClass())) {

                // Si on est sur un bonus levelable, on a une vérif supplémentaire à faire
                if (isLevelableBonus(bonus)) {

                    // On récupère la classe requise
                    Class classe_requise = ((LevelableItem) bonus_joueur).getRequiredLevel().getClass();

                    // Si le joueur ne possède pas la classe requise
                    if (!doesPlayerHaveThisBonus(classe_requise, joueur))
                        joueur.sendMessage("Vous n'avez pas le bonus " + classe_requise.getName());
                    else {
                        for (ShopItem shopItem : liste_bonus_joueur)
                            if (shopItem.getClass().equals(classe_requise)) {
                                liste_bonus_joueur.remove(shopItem);
                                break;
                            }
                    }
                    return;
                }
                return;
            }
        }

        // Le joueur ne possède pas ce bonus !
        liste_bonus_joueur.add(bonus);

        // On ajoute la modification
        bonus_par_joueur.replace(joueur, liste_bonus_joueur);
    }


    /**
     * Permet à un utilisateur d'acheter un item
     *
     * @param joueur
     * @param item
     */
    public void purchaseItem(Player joueur, ShopItem item) {

        // Si c'est un item levelable
        if (isLevelableBonus(item)) {
            purchaseItem(joueur, item, ((LevelableItem) item).getRequiredLevel().getClass());
            return;
        }


        joueur.sendMessage(item.getPurchaseText());
        item.setJoueur(joueur);

        // Logique permettant de savoir si l'utilisateur peut acheter l'item
        if (canPlayerAffordItem(item, joueur)) {
            ajouterBonusPourJoueur(item, joueur);
        } else {
            joueur.sendMessage("Vous n'avez pas assez de sous, requis: " + item.getPrice() + " " + item.getCurrency().toString());
        }
    }

    /**
     * Permet à un utilisateur d'acheter un item
     *
     * @param joueur
     * @param item   TODO
     */
    private void purchaseItem(Player joueur, ShopItem item, Class old_required_level) {
        joueur.sendMessage(item.getPurchaseText());
        item.setJoueur(joueur);

        // Logique permettant de savoir si l'utilisateur peut acheter l'item
        if (canPlayerAffordItem(item, joueur)) {

            if (doesPlayerHaveThisBonus(old_required_level, joueur)) {
                ajouterBonusPourJoueur(item, joueur);
            } else {
                joueur.sendMessage("Vous devez d'abord acheter " + old_required_level.getName() + "");
            }

        } else {
            joueur.sendMessage("Vous n'avez pas assez de sous, requis: " + item.getPrice() + " " + item.getCurrency().toString());
        }
    }

    /**
     * Retourne si un joueur peut s'offrir un item
     *
     * @param joueur
     * @return
     */
    public boolean canPlayerAffordItem(ShopItem bonus, Player joueur) {
        // Table de hachage contenant la liste des items que le joueur possède ainsi que sa quantité
        Map<Material, Integer> liste_item_joueur = new HashMap<>();

        // Pour chaque item de l'inventaire du joueur
        for (ItemStack item : joueur.getInventory().getContents())
            // Si l'item n'est pas null
            if (item != null)
                // On regarde si la liste contient déjà l'item en question
                if (!liste_item_joueur.containsKey(item.getType()))
                    liste_item_joueur.put(item.getType(), item.getAmount());
                    // Si elle la contient déjà, on ajoute le contenu de l'item
                else
                    liste_item_joueur.replace(item.getType(), liste_item_joueur.get(item.getType()) + item.getAmount());

        // la boucle est terminé, on regarde si le joueur possède l'item et si il a assez d'item
        if (!liste_item_joueur.containsKey(bonus.getCurrency())) return false;

        // Si dans notre liste, le joueur possède le nombre requis ou plus d'item, on retourne vrai
        return (liste_item_joueur.get(bonus.getCurrency()) >= bonus.getPrice());
    }


    private boolean isConsummableBonus(ShopItem c) {
        return c instanceof ConsumableItem;
    }

    private boolean isPermanentBonus(ShopItem c) {
        return c instanceof PermanentItem;
    }

    private boolean isLevelableBonus(ShopItem c) {
        return c instanceof LevelableItem;
    }

    /**
     * Retourne si un joueur possède le bonus passé en paramètre
     *
     * @param c
     * @return
     */
    private boolean doesPlayerHaveThisBonus(Class c, Player joueur) {
        if (!bonus_par_joueur.containsKey(joueur)) return false;
        List<ShopItem> liste_bonus = bonus_par_joueur.get(joueur);

        // On regarde pour chaque bonus si le joueur possède le bonus passé en paramètre
        for (ShopItem bonus : liste_bonus)

            // SI la classe est la même, alors le joueur possède ce bonus
            if (bonus.getClass().equals(c)) return true;

        return false;
    }
}
