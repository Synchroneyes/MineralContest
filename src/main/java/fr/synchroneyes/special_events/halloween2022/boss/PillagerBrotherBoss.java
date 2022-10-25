package fr.synchroneyes.special_events.halloween2022.boss;

import fr.synchroneyes.mineral.Core.Boss.Boss;
import fr.synchroneyes.special_events.halloween2022.animations.TNTEndermanThunderAnimation;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PillagerBrotherBoss extends Boss {
    @Override
    public String getName() {
        return ChatColor.RED + "Chef des Protecteurs de OhcnyS";
    }

    @Override
    public double getSanteMax() {
        return 100;
    }

    @Override
    public double getDegatsParAttaque() {
        return 8;
    }

    @Override
    public EntityType getMobType() {
        return EntityType.PILLAGER;
    }

    @Override
    public int getRayonDetectionJoueur() {
        return 5;
    }

    @Override
    public void onPlayerTarget(Player targetedPlayer) {

    }

    @Override
    public List<ItemStack> getKillRewards() {
        List<ItemStack> reward = new ArrayList<>();

        reward.add(new ItemStack(Material.DIAMOND_HELMET));
        reward.add(new ItemStack(Material.DIAMOND_CHESTPLATE));
        reward.add(new ItemStack(Material.DIAMOND_LEGGINGS));
        reward.add(new ItemStack(Material.DIAMOND_BOOTS));
        reward.add(new ItemStack(Material.DIAMOND, 10));
        reward.add(new ItemStack(Material.REDSTONE, 64));
        reward.add(new ItemStack(Material.REDSTONE, 64));

        return reward;
    }

    @Override
    public boolean shouldEntityGlow() {
        return true;
    }

    @Override
    public BarColor getBossBarColor() {
        return BarColor.RED;
    }

    @Override
    public BarStyle getBarStyle() {
        return BarStyle.SEGMENTED_10;
    }

    @Override
    public void doMobSpecialAttack() {
        String[] phrases = new String[]{
                "Je suis le chef des Protecteurs !",
                "Je vais t'infliger des dégâts comme jamais !",
                "Arriveras-tu à me battre ??",
                "Bats moi, montre moi ta force, et un avantage tu auras!",
                "Mode enervé: " + ChatColor.GREEN + "ACTIF",
                "Il va te falloir plus que ça pour me battre..."
        };

        this.entity.getNearbyEntities(this.getRayonDetectionJoueur(), this.getRayonDetectionJoueur(), this.getRayonDetectionJoueur())
                .stream().
                filter((entity) -> (entity instanceof Player)).forEach((entity) -> {
                    Player p = (Player) entity;
                    p.sendMessage(getName() + ChatColor.RESET + ": " + phrases[new Random().nextInt(phrases.length)]);
                });

    }

    @Override
    public int getSpecialAttackTimer() {
        return 3;
    }

    @Override
    public int getBossBarDetectionRadius() {
        return 20;
    }

    @Override
    public void defineCustomAttributes() {

    }

    @Override
    public void onBossDeath() {
        this.entity.getWorld().playSound(this.entity.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 0.8f, 1);
        TNTEndermanThunderAnimation animation = new TNTEndermanThunderAnimation();
        animation.playAnimation(this.entity);
    }

    @Override
    public void onBossSpawn() {

    }

    @Override
    protected void performAnnouncement() {

    }

    @Override
    protected boolean canSpawnMobs() {
        return false;
    }
}
