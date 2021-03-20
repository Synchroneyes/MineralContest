package fr.synchroneyes.mineral.DeathAnimations.Animations;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.effect.FountainEffect;
import fr.synchroneyes.mineral.DeathAnimations.EffectAnimation;
import org.bukkit.Material;
import org.bukkit.Particle;

public class WaterSpiderAnimation extends EffectAnimation {

    @Override
    public String getAnimationName() {
        return "Araignée d'eau";
    }

    @Override
    public Material getIcone() {
        return Material.SPIDER_EYE;
    }

    @Override
    protected Class getEffectClass() {
        return FountainEffect.class;
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
        FountainEffect effect = (FountainEffect) e;
        effect.height = 1F;
        effect.radius = 3F;
        effect.strands = 8;
        effect.heightSpout = 0F;
        effect.particlesSpout = 15;
        effect.particlesStrand = 15;
        effect.particle = Particle.DRIP_WATER;

    }
}
