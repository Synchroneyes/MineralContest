package fr.synchroneyes.mineral.Shop.Items.Abstract;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.Potion;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.LinkedList;
import java.util.List;

public abstract class PotionItem extends ShopItem {

    @Override
    public ItemStack toItemStack(Player joueur) {

        Game partie = mineralcontest.getPlayerGame(joueur);

        if (partie != null) {
            Equipe playerTeam = mineralcontest.getPlayerGame(joueur).getPlayerTeam(joueur);

            String nomItem = "";
            if (playerTeam == null) nomItem = getNomItem();
            else if (playerTeam.getScore() >= getPrice())
                nomItem = ChatColor.RESET + "" + ChatColor.GREEN + getNomItem();
            else nomItem = ChatColor.RESET + "" + ChatColor.RED + getNomItem();


            ItemStack potion = Potion.createPotion(getPotionType(), getPotionLevel(), getPotionDuration(), nomItem);
            ItemMeta potionMeta = potion.getItemMeta();

            List<String> description = new LinkedList<>();


            for (String ligne : getDescriptionItem())
                if (playerTeam == null) description.add(ChatColor.RESET + Lang.translate(ligne));
                else if (playerTeam.getScore() >= getPrice())
                    description.add(ChatColor.RESET + "" + ChatColor.GREEN + Lang.translate(ligne));
                else description.add(ChatColor.RESET + "" + ChatColor.RED + Lang.translate(ligne));

            if (playerTeam == null) description.add("Price: " + getPrice() + " " + "points");
            else if (playerTeam.getScore() >= getPrice())
                description.add(ChatColor.RESET + "" + ChatColor.GREEN + "Price: " + getPrice() + " " + "points");
            else description.add(ChatColor.RESET + "" + ChatColor.RED + "Price: " + getPrice() + " " + "points");
            potionMeta.setLore(description);

            potion.setItemMeta(potionMeta);

            return potion;
        } else {
            ItemStack potion = Potion.createPotion(getPotionType(), getPotionLevel(), getPotionDuration(), getNomItem());
            ItemMeta potionMeta = potion.getItemMeta();

            List<String> description = new LinkedList<>();

            for (String ligne : getDescriptionItem())
                description.add(ChatColor.RESET + Lang.translate(ligne));

            description.add("Price: " + getPrice() + " " + "points");
            potionMeta.setLore(description);

            potion.setItemMeta(potionMeta);

            return potion;
        }

    }

    @Override
    public void onPlayerBonusAdded() {
        onItemUse();
    }

    /**
     * Définir le type de potion
     *
     * @return
     */
    public abstract PotionEffectType getPotionType();

    /**
     * Définir le niveau de la potion
     *
     * @return
     */
    public abstract int getPotionLevel();

    /**
     * Définir la durée en minute de la potion
     *
     * @return
     */
    public abstract int getPotionDuration();


    public Material getItemMaterial() {
        return Material.POTION;
    }


    @Override
    public boolean isEnabledOnRespawn() {
        return false;
    }

    @Override
    public boolean isEnabledOnPurchase() {
        return true;
    }

    @Override
    public int getNombreUtilisations() {
        return 1;
    }

    @Override
    public void onItemUse() {

        ItemStack potion = toItemStack(joueur);

        ItemMeta meta = potion.getItemMeta();
        meta.setLore(null);

        potion.setItemMeta(meta);


        joueur.getInventory().addItem(potion);
    }

    @Override
    public String getPurchaseText() {
        return "Vous avez acheté une potion: " + getNomItem();
    }

    @Override
    public boolean isEnabledOnDeathByAnotherPlayer() {
        return false;
    }

    @Override
    public boolean isEnabledOnDeath() {
        return false;
    }

}
