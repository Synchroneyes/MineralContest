package fr.synchroneyes.achievements.Rewards;

import fr.synchroneyes.mineral.Core.MCPlayer;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;

public class MultipleArticleReward extends AbstractReward{


    private List<ItemStack> items;

    public MultipleArticleReward() {
        this.items = new LinkedList<>();
    }

    /**
     * Méthode permettant d'ajouter un item
     * @param itemStack
     */
    public void addItem(ItemStack itemStack) {
        this.items.add(itemStack);
    }

    @Override
    public void giveToPlayer() {
        World playerWorld = getJoueur().getWorld();
        MCPlayer mcPlayer = getMcPlayer();

        for(ItemStack item : items)
            // On vérifie si son inventaire est plein. Si il est plein on le drop à côté de lui
            if(getMcPlayer().isInventoryFull()) playerWorld.dropItemNaturally(getJoueur().getLocation(), item);
            else mcPlayer.getJoueur().getInventory().addItem(item);

    }

    @Override
    public String getRewardText() {
        return "Vous avez reçu une récompense dans votre inventaire! Si votre inventaire est plein, les objets ont été déposés à côté de vous.";
    }
}
