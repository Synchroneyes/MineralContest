package fr.synchroneyes.mineral.Shop.Players;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Shop.Items.Abstract.ConsumableItem;
import fr.synchroneyes.mineral.Shop.Items.Abstract.LevelableItem;
import fr.synchroneyes.mineral.Shop.Items.Abstract.PermanentItem;
import fr.synchroneyes.mineral.Shop.Items.Abstract.ShopItem;
import fr.synchroneyes.mineral.Shop.Items.Informations.ProchainCoffreAreneItem;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import lombok.Getter;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Classe permettant de gérer les bonus du joueur
 */
public class PlayerBonus {

    // Liste contenant tout les bonus actifs
    public static LinkedBlockingQueue<ShopItem> listeBonusActif;

    public Map<Player, LinkedBlockingQueue<ShopItem>> bonus_par_joueur;

    @Getter
    private Game partie;

    public PlayerBonus(Game g) {

        // Si aucun bonus n'a été ajouté, on enregistre tous les bonus dispo
        if (listeBonusActif == null) enregistrerBonus();

        this.bonus_par_joueur = new HashMap<>();

        this.partie = g;
    }

    private void enregistrerBonus() {
        if (listeBonusActif == null) listeBonusActif = new LinkedBlockingQueue<>();

        listeBonusActif.add(new ProchainCoffreAreneItem());
    }


    public void setPlayerBonusList(Player joueur, LinkedBlockingQueue liste) {

        if (liste == null) return;
        for (Object bonus_achete : liste)
            ((ShopItem) bonus_achete).setJoueur(joueur);

        if (bonus_par_joueur.containsKey(joueur)) bonus_par_joueur.replace(joueur, liste);
        else bonus_par_joueur.put(joueur, liste);


    }

    /**
     * Retourne la liste des bonus pour un joueur donné
     *
     * @param joueur
     * @return
     */
    public LinkedBlockingQueue<ShopItem> getListeBonusJoueur(Player joueur) {
        return bonus_par_joueur.get(joueur);
    }

    /**
     * Fonction permettant de traiter la logique d'ajout d'un bonus à un joueur
     *
     * @param bonus
     * @param joueur
     */
    public void ajouterBonusPourJoueur(ShopItem bonus, Player joueur) {


        if (!bonus_par_joueur.containsKey(joueur)) bonus_par_joueur.put(joueur, new LinkedBlockingQueue<ShopItem>());

        LinkedBlockingQueue<ShopItem> liste_bonus_joueur = bonus_par_joueur.get(joueur);




        boolean doesPlayerAlreadyHaveBonus = false;
        ShopItem currentBonus = null;
        // On regarde si il possède déjà le bonus
        for (ShopItem bonus_joueur : liste_bonus_joueur) {
            if (bonus_joueur.getClass().equals(bonus.getClass())) {

                doesPlayerAlreadyHaveBonus = true;
                currentBonus = bonus_joueur;
                break;
            }
        }

        if (currentBonus != null) {
            if (isConsummableBonus(currentBonus)) {
                ConsumableItem currentBonus_consommable = (ConsumableItem) currentBonus;

                int nb_use_actuel = currentBonus_consommable.getNombreUtilisationRestantes();
                liste_bonus_joueur.remove(currentBonus);

                currentBonus_consommable.setNombreUtilisationRestantes(nb_use_actuel + 1);
                bonus = currentBonus_consommable;
                doesPlayerAlreadyHaveBonus = false;

            }
        }

        // Le joueur ne possède pas ce bonus !
        if (!doesPlayerAlreadyHaveBonus) {


            if (isConsummableBonus(bonus) && currentBonus == null) {
                ((ConsumableItem) bonus).setNombreUtilisationRestantes(bonus.getNombreUtilisations());
            }


            liste_bonus_joueur.add(bonus);
        }


        // Si on est sur un bonus levelable, on a une vérif supplémentaire à faire
        if (isLevelableBonus(bonus)) {

            // On récupère la classe requise

            Class classe_requise = ((LevelableItem) bonus).getRequiredLevel();

            // Si le joueur ne possède pas la classe requise
            if (classe_requise != null && !doesPlayerHaveThisBonus(classe_requise, joueur)) {
                String bonus_required_text = Lang.shopitem_bonus_required.toString();
                bonus_required_text = bonus_required_text.replace("%bonus", classe_requise.getName());
                joueur.sendMessage(mineralcontest.prefixErreur + bonus_required_text);
            } else {
                for (ShopItem shopItem : liste_bonus_joueur)
                    if (shopItem.getClass().equals(classe_requise)) {
                        LevelableItem item = (LevelableItem) shopItem;
                        item.setPlayerBonusEnabled(false);
                        break;
                    }
            }
        }

        // Si c'est un item qui s'active à l'achat
        if (bonus.isEnabledOnPurchase()) {
            bonus.onItemUse();
        }

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

        // Si le joueur possède déjà ce bonus
        if (doesPlayerHaveThisBonus(item.getClass(), joueur) && (isPermanentBonus(item) || isLevelableBonus(item))) {
            joueur.sendMessage(mineralcontest.prefixErreur + Lang.shopitem_bonus_already_purchased.toString());
            return;
        }


        joueur.sendMessage(mineralcontest.prefixPrive + Lang.translate(item.getPurchaseText()));
        item.setJoueur(joueur);


        takePlayerMoney(joueur, item);

        Equipe playerTeam = mineralcontest.getPlayerGame(joueur).getPlayerTeam(joueur);
        String purchaseMessage = Lang.shopitem_player_purchased.toString();
        purchaseMessage = purchaseMessage.replace("%p", joueur.getDisplayName());
        purchaseMessage = purchaseMessage.replace("%bonus", item.getNomItem());

        playerTeam.sendMessage(mineralcontest.prefixTeamChat + purchaseMessage);


        // Logique permettant de savoir si l'utilisateur peut acheter l'item
        ajouterBonusPourJoueur(item, joueur);

        joueur.playNote(joueur.getLocation(), Instrument.PIANO, new Note(24));

    }

    /**
     * Retourne si un joueur peut s'offrir un item
     *
     * @param joueur
     * @return
     */
    public boolean canPlayerAffordItem(ShopItem bonus, Player joueur) {


        Game playerGame = mineralcontest.getPlayerGame(joueur);
        if (playerGame == null) return false;

        Equipe playerTeam = playerGame.getPlayerTeam(joueur);
        if (playerTeam == null) return false;
        int scoreEquipe = playerTeam.getScore();

        if (playerGame.groupe.getKitManager().isKitsEnabled() && !bonus.isBonusCompatibleWithKits()) return false;


        // Si dans notre liste, le joueur possède le nombre requis ou plus d'item, on retourne vrai

        if (isLevelableBonus(bonus)) {
            LevelableItem levelableItem = (LevelableItem) bonus;

            if (levelableItem.getRequiredLevel() == null) return scoreEquipe >= bonus.getPrice();
            else
                return ((doesPlayerHaveThisBonus(levelableItem.getRequiredLevel(), joueur) && scoreEquipe >= bonus.getPrice()));

            //if(levelableItem.getRequiredLevel() == null) return (liste_item_joueur.get(scoreEquipe >= bonus.getPrice());
            //else return (doesPlayerHaveThisBonus(levelableItem.getRequiredLevel(), joueur) && liste_item_joueur.get(bonus.getCurrency()) >= bonus.getPrice());

        } else return (scoreEquipe >= bonus.getPrice());
    }


    /**
     * Prend l'argent nécessaire au joueur en fonction de l'item passé en commentaire
     *
     * @param joueur
     * @param shopItem
     */
    private void takePlayerMoney(Player joueur, ShopItem shopItem) {

        Equipe playerTeam = mineralcontest.getPlayerGame(joueur).getPlayerTeam(joueur);

        int nouveauScoreEquipe = playerTeam.getScore() - shopItem.getPrice();
        playerTeam.setScore(nouveauScoreEquipe);

    }


    /**
     * Permet d'activer les bonus s'activant au respawn
     */
    public void triggerEnabledBonusOnRespawn(Player joueur) {
        LinkedBlockingQueue<ShopItem> bonus_joueur = bonus_par_joueur.get(joueur);

        if (bonus_joueur == null) return;

        // Pour chaque bonus du joueur
        for (ShopItem bonus : bonus_joueur) {

            if (isConsummableBonus(bonus) && bonus.isEnabledOnRespawn()) {
                ConsumableItem bonus_consummable = (ConsumableItem) bonus;
                if (bonus_consummable.getNombreUtilisationRestantes() > 0) {
                    bonus_consummable.onItemUse();
                    bonus_consummable.setNombreUtilisationRestantes(bonus_consummable.getNombreUtilisations() - 1);
                    continue;
                }
            }

            if (bonus.isEnabledOnRespawn()) bonus.onItemUse();
        }


    }

    /**
     * Permet d'activer les bonus à la reconnexion
     *
     * @param joueur
     */
    public void triggerEnabledBonusOnReconnect(Player joueur) {
        LinkedBlockingQueue<ShopItem> bonus_joueur = bonus_par_joueur.get(joueur);

        if (bonus_joueur == null) return;

        // Pour chaque bonus du joueur
        for (ShopItem bonus : bonus_joueur) {

            if (isConsummableBonus(bonus) && bonus.isEnabledOnReconnect()) {
                ConsumableItem bonus_consummable = (ConsumableItem) bonus;
                if (bonus_consummable.getNombreUtilisationRestantes() > 0) {
                    bonus_consummable.onItemUse();
                    bonus_consummable.setNombreUtilisationRestantes(bonus_consummable.getNombreUtilisations() - 1);
                    continue;
                }
            }

            if (bonus.isEnabledOnReconnect()) bonus.onItemUse();
        }
    }

    /**
     * Permet d'activer les bonus s'activant a la mort d'un joueur par une autre personne
     */
    public void triggerEnabledBonusOnPlayerKillerKilled(Player joueur) {
        LinkedBlockingQueue<ShopItem> bonus_joueur = bonus_par_joueur.get(joueur);


        if (bonus_joueur == null) return;


        // Pour chaque bonus du joueur
        int index = -1;
        for (ShopItem bonus : bonus_joueur) {

            index++;

            if (isConsummableBonus(bonus) && bonus.isEnabledOnDeathByAnotherPlayer()) {

                ConsumableItem bonus_consummable = (ConsumableItem) bonus;
                joueur.sendMessage(bonus_consummable.getNombreUtilisationRestantes() + " <");

                if (bonus_consummable.getNombreUtilisationRestantes() > 0) {
                    int nombre_utilisation_restantes = bonus_consummable.getNombreUtilisationRestantes();
                    bonus_consummable.onItemUse();
                    bonus_consummable.setNombreUtilisationRestantes(nombre_utilisation_restantes - 1);



                    continue;
                } else {
                    bonus_joueur.remove(bonus);
                    continue;
                }
            }

            if (bonus.isEnabledOnDeathByAnotherPlayer()) {
                bonus.onItemUse();
            }
        }

        bonus_par_joueur.replace(joueur, bonus_joueur);


    }


    public static boolean isConsummableBonus(ShopItem c) {
        return c instanceof ConsumableItem;
    }

    public static boolean isPermanentBonus(ShopItem c) {
        return c instanceof PermanentItem;
    }

    public static boolean isLevelableBonus(ShopItem c) {
        return c instanceof LevelableItem;
    }

    /**
     * Retourne si un joueur possède le bonus passé en paramètre
     *
     * @param c
     * @return
     */
    public boolean doesPlayerHaveThisBonus(Class c, Player joueur) {
        if (!bonus_par_joueur.containsKey(joueur)) return false;
        LinkedBlockingQueue<ShopItem> liste_bonus = bonus_par_joueur.get(joueur);

        if (liste_bonus == null) return false;

        // On regarde pour chaque bonus si le joueur possède le bonus passé en paramètre
        for (ShopItem bonus : liste_bonus)

            // SI la classe est la même, alors le joueur possède ce bonus
            if (bonus.getClass().equals(c)) return true;

        return false;
    }

    /**
     * Permet de retourner le bonus d'un joueur donné en paramètre, retourne null si l'utilisateur ne le possède pas
     *
     * @param bonusClass
     * @param joueur
     * @return
     */
    public static ShopItem getPlayerBonus(Class bonusClass, Player joueur) {
        Game partie = mineralcontest.getPlayerGame(joueur);

        if (partie == null) return null;

        LinkedBlockingQueue<ShopItem> bonus_joueur = partie.getPlayerBonusManager().getListeBonusJoueur(joueur);

        if (bonus_joueur == null || bonus_joueur.isEmpty()) return null;

        for (ShopItem bonus : bonus_joueur)
            if (bonus.getClass().equals(bonusClass)) return bonus;
        return null;
    }
}
