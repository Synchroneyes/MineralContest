package fr.synchroneyes.mineral.Shop.Items.Levelable.Pioche;

import fr.synchroneyes.mineral.Shop.Items.Abstract.LevelableItem;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 * Ce bonus offre une pioche en diamant avec l'enchantement Fortune I, Efficacité II
 */
public class Pioche3 extends LevelableItem {
    @Override
    public Class getRequiredLevel() {
        return Pioche2.class;
    }


    @Override
    public String getNomItem() {
        return Lang.shopitem_pickaxelvl3_title.toString();
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{Lang.shopitem_pickaxelvl3_desc.toString()};
    }


    @Override
    public Material getItemMaterial() {
        return Material.DIAMOND_PICKAXE;
    }

    @Override
    public boolean isEnabledOnRespawn() {
        return true;
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
    public int getNombreUtilisations() {
        return 1;
    }


    @Override
    public void onItemUse() {


        ItemStack oldLevelPioche = new ItemStack(Material.DIAMOND_PICKAXE);
        oldLevelPioche.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1);


        for (ItemStack item : joueur.getInventory().getContents())
            if (item != null && item.equals(oldLevelPioche)) {
                item.setAmount(0);
                break;
            }

        ItemStack pioche = new ItemStack(Material.DIAMOND_PICKAXE);
        pioche.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1);
        pioche.addEnchantment(Enchantment.DIG_SPEED, 2);

        joueur.getInventory().addItem(pioche);


    }


    @Override
    public int getPrice() {
        return 1000;
    }
}
