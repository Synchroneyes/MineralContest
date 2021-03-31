package fr.synchroneyes.mineral.Core.Coffre.Coffres;

import fr.synchroneyes.mineral.Core.Arena.ArenaChestContent.ArenaChestContentGenerator;
import fr.synchroneyes.mineral.Core.Coffre.Animations;
import fr.synchroneyes.mineral.Core.Coffre.AutomatedChestAnimation;
import fr.synchroneyes.mineral.Core.Coffre.AutomatedChestManager;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;

public class CoffreParachute extends AutomatedChestAnimation {

    private ArenaChestContentGenerator generator;

    private AutomatedChestManager automatedChestManager;

    // Variable contenant le nombre minimum & maximum d'items à générer
    int minItems, maxItems;

    /**
     * Constructeur, permet de donner en paramètre le nom de l'inventaire ainsi que la taille
     */
    public CoffreParachute(AutomatedChestManager manager) {
        // On veut 5 lignes
        super(5 * 9, manager);
        generator = new ArenaChestContentGenerator(null);
        this.automatedChestManager = manager;
    }


    public void setMinItems(int minItems) {
        this.minItems = minItems;
    }


    public void setMaxItems(int maxItems) {
        this.maxItems = maxItems;
    }

    @Override
    public int playNoteOnTick() {
        return 24;
    }

    @Override
    public int playNoteOnEnd() {
        return 24;
    }

    @Override
    public void actionToPerformBeforeSpawn() {

    }

    @Override
    public void actionToPerformAfterAnimationOver() {

    }

    @Override
    public boolean displayWaitingItems() {
        return true;
    }

    @Override
    public String getOpeningChestTitle() {
        return Lang.airdrop_chest_opening_title.toString();
    }

    @Override
    public String getOpenedChestTitle() {
        return Lang.airdrop_chest_opened_title.toString();
    }

    @Override
    public ItemStack getWaitingItemMaterial() {
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) meta.setDisplayName(" ");
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public ItemStack getUsedItemMaterial() {
        ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) meta.setDisplayName(" ");
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public LinkedList<Integer> getOpeningSequence() {
        return Animations.FIVE_LINES_AROUND_THEN_CENTER.toList();
    }

    @Override
    public Material getChestMaterial() {
        return Material.CHEST;
    }

    @Override
    public int getAnimationTime() {
        return automatedChestManager.getGroupe().getParametresPartie().getCVAR("drop_opening_time").getValeurNumerique();
    }

    @Override
    public boolean canChestBeOpenedByMultiplePlayers() {
        return false;
    }

    @Override
    public List<ItemStack> genererContenuCoffre() {

        LinkedList<ItemStack> items = new LinkedList<>();
        try {
            for (ItemStack item : generator.generateAirDropInventory(minItems, maxItems).getContents())
                if (item != null) items.add(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public boolean automaticallyGiveItemsToPlayer() {
        return false;
    }
}
