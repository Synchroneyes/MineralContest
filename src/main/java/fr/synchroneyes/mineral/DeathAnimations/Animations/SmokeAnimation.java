package fr.synchroneyes.mineral.DeathAnimations.Animations;

import fr.synchroneyes.mineral.DeathAnimations.DeathAnimation;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class SmokeAnimation extends DeathAnimation {
    @Override
    public String getAnimationName() {
        return "Partie en fumée";
    }

    @Override
    public Material getIcone() {
        return Material.SMOKER;
    }

    @Override
    public void playAnimation(LivingEntity player) {

        // ON récupère la position
        Location location = player.getLocation();

        // On fait un effet de fumée
        AreaEffectCloud effectCloud = (AreaEffectCloud) location.getWorld().spawnEntity(location, EntityType.AREA_EFFECT_CLOUD);
        effectCloud.setColor(Color.BLACK);
        effectCloud.setRadius(3);
        effectCloud.setDuration(20*5);


    }
}
