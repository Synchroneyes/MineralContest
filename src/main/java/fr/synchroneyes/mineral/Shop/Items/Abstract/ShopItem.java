package fr.synchroneyes.mineral.Shop.Items.Abstract;

import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import lombok.AccessLevel;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;

/**
 * Classe représentant un item
 */
public abstract class ShopItem {

    private int nombreUtilisationRestante = getNombreUtilisations();

    // Variable contenant le joueur ayant acheté l'item
    @Setter(AccessLevel.PUBLIC)
    protected Player joueur;

    /**
     * Action effectué lorsqu'un bonus est ajouté à un joueur
     */
    public abstract void onPlayerBonusAdded();

    /**
     * Permet de récuperer le nom de l'item
     *
     * @return
     */
    public abstract String getNomItem();

    /**
     * Récupérer la description d'un item
     *
     * @return
     */
    public abstract String[] getDescriptionItem();

    /**
     * Récuperer l'item à utiliser
     *
     * @return
     */
    public abstract Material getItemMaterial();


    /**
     * Permet de convertir un item en ItemStack
     *
     * @return
     */
    public ItemStack toItemStack(Player joueur) {
        ItemStack item = new ItemStack(getItemMaterial(), 1);
        if (item.getItemMeta() != null) {
            List<String> description = new LinkedList<>();
            ItemMeta itemMeta = item.getItemMeta();


            if (mineralcontest.getPlayerGame(joueur) != null && mineralcontest.getPlayerGame(joueur).getPlayerTeam(joueur) != null) {

                Equipe playerTeam = mineralcontest.getPlayerGame(joueur).getPlayerTeam(joueur);

                if (playerTeam.getScore() >= getPrice()) {

                    itemMeta.setDisplayName(ChatColor.RESET + "" + ChatColor.GREEN + Lang.translate(getNomItem()));
                    for (String ligne : getDescriptionItem())
                        description.add(ChatColor.RESET + "" + ChatColor.GREEN + Lang.translate(ligne));

                    description.add(ChatColor.RESET + "" + ChatColor.GREEN + "" + getPrice() + " " + "points");

                } else {
                    itemMeta.setDisplayName(ChatColor.RESET + "" + ChatColor.RED + Lang.translate(getNomItem()));
                    for (String ligne : getDescriptionItem())
                        description.add(ChatColor.RESET + "" + ChatColor.RED + Lang.translate(ligne));

                    description.add(ChatColor.RESET + "" + ChatColor.RED + "" + getPrice() + " " + "points");


                }


            } else {
                for (String ligne : getDescriptionItem())
                    description.add(ChatColor.RESET + "" + ChatColor.RED + Lang.translate(ligne));
                description.add(ChatColor.RESET + "" + getPrice() + " " + "points");
            }

            itemMeta.setLore(description);

            item.setItemMeta(itemMeta);
        }
        return item;
    }

    /**
     * Permet de savoir si l'item s'active à l'achat, ou au respawn
     *
     * @return
     */
    public abstract boolean isEnabledOnRespawn();

    public abstract boolean isEnabledOnPurchase();

    public abstract boolean isEnabledOnDeathByAnotherPlayer();

    public abstract boolean isEnabledOnDeath();

    /**
     * Récupère le nombre d'utilisation d'un item
     *
     * @return
     */
    public abstract int getNombreUtilisations();


    /**
     * Permet d'utiliser un item sur un joueur
     */
    public abstract void onItemUse();

    /**
     * Récupère le texte d'achat
     *
     * @return
     */
    public abstract String getPurchaseText();


    /**
     * Retourne le prix d'un item
     *
     * @return
     */
    public abstract int getPrice();


}
