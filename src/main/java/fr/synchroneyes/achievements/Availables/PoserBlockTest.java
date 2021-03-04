package fr.synchroneyes.achievements.Availables;

import fr.synchroneyes.achievements.AchievementManager;
import fr.synchroneyes.achievements.Rewards.AbstractReward;
import fr.synchroneyes.achievements.Rewards.SingleArticleReward;
import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class PoserBlockTest extends AbstractAchievement{

    public PoserBlockTest(AchievementManager manager) {
        super(manager);
    }

    @Override
    public String getNom() {
        return "vive l'écologie";
    }

    @Override
    public String getObjectifTexte() {
        return "Posez un bloc de bouse!";
    }

    @Override
    public AbstractReward getReward() {
        return new SingleArticleReward(new ItemStack(Material.DIAMOND_SWORD));
    }


    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event) {

        if(event.isCancelled()) return;

        MCPlayer mcPlayer = mineralcontest.plugin.getMCPlayer(event.getPlayer());
        if(mcPlayer == null) return;

        if(getManager().doesPlayerHaveThisAchievement(mcPlayer, this)) {
            if(event.getBlock().getType() == Material.DIRT) {
                getManager().playerDidAchievement(mcPlayer, this);
            }
        }
    }
}
