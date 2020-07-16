package fr.synchroneyes.mineral.Shop.Items.Abstract;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Shop.Players.PlayerBonus;
import fr.synchroneyes.mineral.mineralcontest;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Classe représentant un item avec level et donc différents bonus
 */
public abstract class LevelableItem extends ShopItem {

    // Permet de savoir si le bonus est activé ou non pour un joueur
    @Getter
    @Setter
    private boolean playerBonusEnabled = true;

    /**
     * Permet de définir le niveau requis avant d'acheter cet item
     *
     * @return
     */
    public abstract Class getRequiredLevel();

    public static LevelableItem fromClass(Class c) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return (LevelableItem) c.getConstructor().newInstance();
    }



    public boolean isEnabledOnPurchase() {
        return true;
    }

    public String getPurchaseText() {
        return "Vous avez acheté l'amélioration: " + getNomItem();
    }


    public void onPlayerBonusAdded() {
        onItemUse();
    }




    /**
     * Fonction permettant d'effectuer une action lorsqu'un niveau est ajouté
     */
    public void onLevelAdded() {
        // On désactive le niveau précédent
        Game partie = mineralcontest.getPlayerGame(joueur);
        if (partie == null) return;

        PlayerBonus playerBonusManager = partie.getPlayerBonusManager();

        // On vérifie si le joueur a le niveau requis
        if (playerBonusManager.doesPlayerHaveThisBonus(getRequiredLevel(), joueur)) {
            // On regarde si le bonus est actif
            LinkedBlockingQueue<ShopItem> bonus_joueur = playerBonusManager.getListeBonusJoueur(joueur);

            if (bonus_joueur == null) return;

            // Pour chaque bonus
            for (ShopItem bonus : bonus_joueur) {
                // On regarde si le bonus est le bonus requis
                if (bonus.getClass().equals(getRequiredLevel())) {
                    //Le bonus est requis! On vérifie si il est activé
                    if (PlayerBonus.isLevelableBonus(bonus)) {
                        // C'est un levelable!
                        LevelableItem bonus_ = (LevelableItem) bonus;

                        // Si le bonus est activé, on le désactive
                        if (bonus_.isPlayerBonusEnabled()) {
                            bonus_.setPlayerBonusEnabled(false);
                            // Et on peut s'arrêter là
                            return;
                        }
                    }
                }
            }


        }
    }

    /**
     * Fonction qui permet de déterminer si l'utilisateur peut utiliser ce niveau de bonus
     *
     * @return
     */
    public boolean canPlayerUseThisLevel() {
        Game playerGame = mineralcontest.getPlayerGame(joueur);

        // Si le joueur n'est pas dans une partie, on dit non
        if (playerGame == null) return false;

        PlayerBonus bonusManager = playerGame.getPlayerBonusManager();

        LinkedBlockingQueue<ShopItem> bonus_joueur = bonusManager.getListeBonusJoueur(joueur);

        // Si le joueur n'a pas d'item, on ne peut pas utiliser cet item
        if (bonus_joueur.isEmpty()) return false;

        if (!bonusManager.doesPlayerHaveThisBonus(this.getClass(), joueur)) return false;

        // On regarde pour chaque bonus du joueur, si ce bonus est activé ou non
        for (ShopItem bonus : bonus_joueur) {
            if (bonus.getClass().equals(this.getClass())) {
                LevelableItem bonus_ = (LevelableItem) bonus;

                // Et on retourne le fait que le bonus soit activé ou non
                return bonus_.isPlayerBonusEnabled();
            }
        }

        return false;


    }
}
