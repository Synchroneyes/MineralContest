package fr.synchroneyes.challenges.Rewards;

import fr.synchroneyes.mineral.Core.MCPlayer;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public class SingleArticleReward extends AbstractReward{

    private ItemStack item;

    public SingleArticleReward(ItemStack itemToReward) {
        this.item = itemToReward;
    }

    @Override
    public void giveToPlayer() {
        World playerWorld = getJoueur().getWorld();
        MCPlayer mcPlayer = getMcPlayer();
        // On vérifie si son inventaire est plein. Si il est plein on le drop à côté de lui
        if(getMcPlayer().isInventoryFull()) playerWorld.dropItemNaturally(getJoueur().getLocation(), item);
        else mcPlayer.getJoueur().getInventory().addItem(item);

    }

    @Override
    public String getRewardText() {
        return "Vous avez reçu une récompense dans votre inventaire! Si votre inventaire est plein, l'objet a été déposé à côté de vous.";
    }


}
