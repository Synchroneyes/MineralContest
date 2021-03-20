package fr.synchroneyes.mineral.DeathAnimations.Animations;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.*;
import fr.synchroneyes.mineral.DeathAnimations.DeathAnimation;
import fr.synchroneyes.mineral.DeathAnimations.EffectAnimation;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.io.File;

public class HeartAnimation extends EffectAnimation {

    @Override
    public String getAnimationName() {
        return "Araignée d'eau";
    }

    @Override
    public Material getIcone() {
        return Material.APPLE;
    }

    @Override
    protected Class getEffectClass() {
        return ImageEffect.class;
    }

    @Override
    public int getDuration() {
        return 5;
    }

    @Override
    public int getHeighOffset() {
        return 0;
    }

    @Override
    public void applyCustomSettings(Effect e) {

    }
}
