package fr.synchroneyes.achievements.Availables;

import fr.synchroneyes.achievements.AchievementManager;
import fr.synchroneyes.achievements.Rewards.AbstractReward;
import fr.synchroneyes.achievements.Rewards.PointsReward;
import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

public class TuerZombie extends AbstractAchievement{
    public TuerZombie(AchievementManager manager) {
        super(manager);
    }

    @Override
    public String getNom() {
        return "The Walking Dead";
    }

    @Override
    public String getObjectifTexte() {
        return "Tuez un zombie. Simple non ?";
    }

    @Override
    public AbstractReward getReward() {
        return new PointsReward(500);
    }


    @EventHandler
    public void onZombieKilled(EntityDeathEvent event) {
        if(event.getEntity().getKiller() != null) {

            Player joueur = event.getEntity().getKiller();

            MCPlayer mcPlayer = mineralcontest.plugin.getMCPlayer(joueur);
            if(mcPlayer == null) return;

            if(getManager().doesPlayerHaveThisAchievement(mcPlayer, this)) {
                if(event.getEntity() instanceof Zombie) {
                    getManager().playerDidAchievement(mcPlayer, this);
                }
            }
        }
    }
}
