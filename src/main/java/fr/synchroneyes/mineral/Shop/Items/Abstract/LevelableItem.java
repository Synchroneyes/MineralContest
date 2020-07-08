package fr.synchroneyes.mineral.Shop.Items.Abstract;

/**
 * Classe représentant un item avec level et donc différents bonus
 */
public abstract class LevelableItem extends ShopItem {

    /**
     * Permet de définir le niveau requis avant d'acheter cet item
     *
     * @return
     */
    public abstract ShopItem getRequiredLevel();

    /**
     * Permet d'effectuer une action sur un objet
     * Par exemple sur un evenement
     *
     * @param target
     */
    public abstract void performLevelAction(Object target);

    public boolean isEnabledOnPurchase() {
        return true;
    }


    public void onPlayerBonusAdded() {

        // Quand on ajoute le bonus au joueur
        // On augmente le niveau actuel du levelable item
        try {
            this.addOneLevel();
        } catch (Exception e) {
            joueur.sendMessage(e.getMessage());
        }
    }

    /**
     * Fonction permettant de gérer les ajouts de niveauxx
     */
    private void addOneLevel() throws Exception {

    }

    /**
     * Fonction permettant d'effectuer une action lorsqu'un niveau est ajouté
     */
    public abstract void onLevelAdded();
}
