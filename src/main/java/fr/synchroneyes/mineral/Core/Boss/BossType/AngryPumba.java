package fr.synchroneyes.mineral.Core.Boss.BossType;

import fr.synchroneyes.mineral.Core.Boss.Boss;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;

public class AngryPumba extends Boss {
    @Override
    public String getName() {
        return "Défenseur de largage";
    }

    @Override
    public double getSanteMax() {
        return 500;
    }

    @Override
    public double getDegatsParAttaque() {
        return 7.5;
    }

    @Override
    public EntityType getMobType() {
        return EntityType.HOGLIN;
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
        List<ItemStack> items = new LinkedList<>();

        for(int i = 0; i < 10; ++i)
            items.add(new ItemStack(Material.EMERALD, 64));

        return items;
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
        return BarStyle.SOLID;
    }

    @Override
    public void doMobSpecialAttack() {

    }

    @Override
    public int getSpecialAttackTimer() {
        return 1000;
    }

    @Override
    public int getBossBarDetectionRadius() {
        return 10;
    }

    @Override
    public void defineCustomAttributes() {

    }

    @Override
    public void onBossDeath() {

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
