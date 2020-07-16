package fr.synchroneyes.mineral.Shop.Items.Abstract;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Shop.Players.PlayerBonus;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;

/**
 * Classe permettant de gérer les items permanents
 */
public abstract class PermanentItem extends ShopItem {

    public int getNombreUtilisations() {
        return Integer.MAX_VALUE;
    }

    public int getNombreUtilisationsRestantes() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isEnabledOnRespawn() {
        return true;
    }

    @Override
    public boolean isEnabledOnPurchase() {
        return true;
    }

    @Override
    public void onPlayerBonusAdded() {
        onItemUse();
    }

    @Override
    public String getPurchaseText() {
        return "Vous avez acheté le bonus permanent: " + getNomItem();
    }

    @Override
    public boolean isEnabledOnDeathByAnotherPlayer() {
        return false;
    }

    @Override
    public boolean isEnabledOnDeath() {
        return false;
    }

    @Override
    public ItemStack toItemStack(Player joueur) {

        ItemStack defaultItem = defaultItemStack(joueur);
        if (mineralcontest.getPlayerGame(joueur) != null && mineralcontest.getPlayerGame(joueur).getPlayerTeam(joueur) != null) {
            Game partie = mineralcontest.getPlayerGame(joueur);
            PlayerBonus bonusManager = partie.getPlayerBonusManager();

            if (bonusManager == null) return defaultItem;

            if (bonusManager.doesPlayerHaveThisBonus(this.getClass(), joueur)) {
                ItemMeta meta = defaultItem.getItemMeta();
                meta.setDisplayName(ChatColor.STRIKETHROUGH + "" + ChatColor.BLUE + Lang.translate(getNomItem()));
                defaultItem.setItemMeta(meta);

                return defaultItem;
            } else {
                return defaultItem;
            }


        } else {
            return defaultItem;
        }
    }

    private ItemStack defaultItemStack(Player joueur) {
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
}
