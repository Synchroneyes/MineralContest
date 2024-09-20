package fr.synchroneyes.special_events.halloween2024.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ClonedInventory {

    private ItemStack[] items;
    private ItemStack[] armor;
    private ItemStack[] extra;
    private ItemStack[] storageContent;
    private PlayerInventory original;

    public ClonedInventory(PlayerInventory inventory){
        this.items = inventory.getContents();
        this.armor = inventory.getArmorContents();
        this.extra = inventory.getExtraContents();
        this.storageContent = inventory.getStorageContents();
        this.original = inventory;
    }

    public PlayerInventory reset(){
        original.setContents(items);
        original.setArmorContents(armor);
        original.setExtraContents(extra);
        original.setStorageContents(storageContent);
        return original;
    }

}
