package fr.synchroneyes.mineral.Core.Coffre.Coffres;

import fr.synchroneyes.mineral.Core.Arena.ArenaChestContent.ArenaChestContentGenerator;
import fr.synchroneyes.mineral.Core.Arena.Arene;
import fr.synchroneyes.mineral.Core.Coffre.Animations;
import fr.synchroneyes.mineral.Core.Coffre.AutomatedChestAnimation;
import fr.synchroneyes.mineral.Core.Coffre.AutomatedChestManager;
import fr.synchroneyes.mineral.Statistics.Class.ArenaChestStat;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;

/**
 * Classe gérant le coffre d'arène
 */
public class CoffreArene extends AutomatedChestAnimation {

    // Variable utilisée pour pouvoir récupérer le groupe du coffre

    // Variable utilisée pour pouvoir générer le contenu du coffre
    private ArenaChestContentGenerator arenaChestContentGenerator;

    // Variable utilisée pour pouvoir gérer l'arène
    private Arene arene;


    public CoffreArene(AutomatedChestManager manager, Arene arene) {
        super(3 * 9, manager);
        this.arenaChestContentGenerator = new ArenaChestContentGenerator(manager.getGroupe());
        this.arene = arene;

        if (arene != null) updateManager();
    }


    public ArenaChestContentGenerator getArenaChestContentGenerator() {
        return arenaChestContentGenerator;
    }

    @Override
    public int playNoteOnTick() {
        return 1;
    }

    @Override
    public int playNoteOnEnd() {
        return 24;
    }

    @Override
    public void actionToPerformBeforeSpawn() {
        this.arene.getCoffre().remove();

        this.arene.setChestSpawned(true);
        this.arene.generateTimeBetweenChest();
        this.arene.enableTeleport();
        this.arene.automaticallyTeleportTeams();

    }

    @Override
    public void actionToPerformAfterAnimationOver() {
        this.arene.setChestSpawned(false);
        this.arene.disableTeleport();
        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.arena_chest_opened.toString(), arene.groupe);
        arene.groupe.getGame().getStatsManager().register(ArenaChestStat.class, openingPlayer, null);


        // Register chest opened

    }

    @Override
    public boolean displayWaitingItems() {
        return true;
    }

    @Override
    public String getOpeningChestTitle() {
        return Lang.arena_chest_title_being_opened.toString();
    }

    @Override
    public String getOpenedChestTitle() {
        return Lang.arena_chest_opened.toString();
    }

    @Override
    public ItemStack getWaitingItemMaterial() {
        return new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
    }

    @Override
    public ItemStack getUsedItemMaterial() {
        return new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
    }

    @Override
    public LinkedList<Integer> getOpeningSequence() {
        return Animations.THREE_LINES_SIMPLE_PROGRESS_BAR.toList();
    }

    @Override
    public Material getChestMaterial() {
        return Material.CHEST;
    }

    @Override
    public int getAnimationTime() {
        return arene.groupe.getParametresPartie().getCVAR("chest_opening_cooldown").getValeurNumerique();
    }

    @Override
    public boolean canChestBeOpenedByMultiplePlayers() {
        return false;
    }

    @Override
    public List<ItemStack> genererContenuCoffre() {
        LinkedList<ItemStack> items = new LinkedList<>();

        try {
            for (ItemStack item : arenaChestContentGenerator.generateInventory().getContents())
                if (item != null) items.add(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public boolean automaticallyGiveItemsToPlayer() {
        return true;
    }
}
