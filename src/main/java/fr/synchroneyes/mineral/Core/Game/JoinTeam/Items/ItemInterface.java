package fr.synchroneyes.mineral.Core.Game.JoinTeam.Items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public abstract class ItemInterface {


    public abstract Material getItemMaterial();

    public abstract String getNomInventaire();

    public abstract List<String> getDescriptionInventaire();

    public abstract void performClick(Player joueur);

    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(getItemMaterial(), 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(getNomInventaire());


        itemMeta.setLore(getDescriptionInventaire());

        item.setItemMeta(itemMeta);
        return item;
    }

}
