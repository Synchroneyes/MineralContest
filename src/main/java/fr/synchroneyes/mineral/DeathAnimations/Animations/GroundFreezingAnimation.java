package fr.synchroneyes.mineral.DeathAnimations.Animations;

import fr.synchroneyes.mapbuilder.Blocks.SaveableBlock;
import fr.synchroneyes.mineral.DeathAnimations.DeathAnimation;
import fr.synchroneyes.mineral.Utils.Pair;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GroundFreezingAnimation extends DeathAnimation {

    private List<Pair<Location, SaveableBlock>> blocks_affected = new LinkedList<>();

    @Override
    public String getAnimationName() {
        return "FreezingGround";
    }

    @Override
    public Material getIcone() {
        return Material.BLUE_ICE;
    }

    @Override
    public void playAnimation(LivingEntity player) {

        Player killer = player.getKiller();

        if(killer == null && !(player instanceof Player)) return;

        if(killer == null) killer = (Player) player;

        Material MaterialToReplace = Material.FROSTED_ICE;


        killer.sendTitle(ChatColor.BLUE + "\u2744 \u2744 \u2744", "Il fait froid par ici ...", 20, 20*5, 20);

        int nb_second_animation = 10;

        // On multiplie par 4 car on a divisé le nombre de tick par 4
        // 4*5tick = 1 seconde
        AtomicInteger duree_animation = new AtomicInteger(nb_second_animation*4);
        blocks_affected.clear();

        Player finalKiller = killer;
        new BukkitRunnable() {
            @Override
            public void run() {
                if(duree_animation.get() == 0) {
                    this.cancel();
                    revertBlocks();
                    return;
                }

                // On récupère les blocks proches du joueur
                int rayon = 3;
                for(int x = finalKiller.getLocation().getBlockX()-rayon; x < finalKiller.getLocation().getBlockX()+rayon; ++x) {
                    for (int z = finalKiller.getLocation().getBlockZ() - rayon; z < finalKiller.getLocation().getBlockZ() + rayon; ++z) {
                        Location loc = new Location(finalKiller.getWorld(), x, finalKiller.getLocation().getBlockY()-1, z);
                        if(loc.getBlock().getType() != Material.AIR && loc.getBlock().getType() != MaterialToReplace) {
                            if(loc.getBlock().getRelative(BlockFace.DOWN).getType() == MaterialToReplace) continue;
                            blocks_affected.add(new Pair<Location, SaveableBlock>(loc, new SaveableBlock(loc.getBlock())));
                            loc.getBlock().setType(MaterialToReplace);
                        }
                    }
                }

                duree_animation.decrementAndGet();
            }
        }.runTaskTimer(mineralcontest.plugin, 0, 5);



    }

    /**
     * Remet les blocs dans leur état naturel
     */
    private void revertBlocks() {
        for(Pair<Location, SaveableBlock> pair : blocks_affected) {
            pair.getValue().setBlock();
        }
    }
}
