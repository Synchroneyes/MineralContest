package fr.mineral.Core.Referee.Items;

import fr.mineral.Core.Referee.Inventory.InventoryTemplate;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class RefereeItemTemplate implements RefereeItem {

    protected Object target;
    protected InventoryTemplate inventaireSource;
    protected String customName = "";

    public RefereeItemTemplate(String customName, Object target, InventoryTemplate inventaireSource) {
        super();
        this.target = target;
        this.customName = customName;
        this.inventaireSource = inventaireSource;
    }

    public RefereeItemTemplate(Object target, InventoryTemplate inventaireSource) {
        super();
        this.target = target;
        this.inventaireSource = inventaireSource;
    }

    public boolean isFromCustomInventory() {
        return inventaireSource != null;
    }

    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(getItemMaterial(), 1);
        ItemMeta itemMeta = item.getItemMeta();
        if (customName.length() == 0) itemMeta.setDisplayName(getNomItem());
        else itemMeta.setDisplayName(customName);

        List<String> description = new ArrayList<>();
        description.add(getDescriptionItem());

        itemMeta.setLore(description);

        item.setItemMeta(itemMeta);
        return item;
    }

}
