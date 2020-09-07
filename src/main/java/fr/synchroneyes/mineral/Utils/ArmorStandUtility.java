package fr.synchroneyes.mineral.Utils;

import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ArmorStandUtility {

    /**
     * Méthode permettant de créer une armure avec une couleur, un nom, et un item en main
     * @param location - Position où spawn l'armorstand
     * @param customName - Le nom de l'armorstand
     * @param color - La couleur des matériaux
     * @param itemInMainHand - Items en main
     * @return
     */
    public static ArmorStand createArmorStandWithColoredLeather(Location location, String customName, Color color, Material itemInMainHand) {

        ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);

        armorStand.setInvulnerable(true);
        armorStand.setCollidable(false);
        armorStand.setCustomNameVisible(true);
        armorStand.setArms(true);
        armorStand.setCanPickupItems(false);
        armorStand.setBasePlate(true);
        armorStand.setCustomName(customName);
        armorStand.getEquipment().setItemInMainHand(new ItemStack(itemInMainHand));

        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helmetMeta.setColor(color);
        helmet.setItemMeta(helmetMeta);

        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta chestplateItemMeta = (LeatherArmorMeta) chestplate.getItemMeta();
        chestplateItemMeta.setColor(color);
        chestplate.setItemMeta(chestplateItemMeta);

        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggings.getItemMeta();
        leggingsMeta.setColor(color);
        leggings.setItemMeta(leggingsMeta);

        ItemStack boots = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();
        bootsMeta.setColor(color);
        boots.setItemMeta(bootsMeta);

        armorStand.getEquipment().setHelmet(helmet);
        armorStand.getEquipment().setChestplate(chestplate);
        armorStand.getEquipment().setLeggings(leggings);
        armorStand.getEquipment().setBoots(boots);

        return armorStand;
    }
}
