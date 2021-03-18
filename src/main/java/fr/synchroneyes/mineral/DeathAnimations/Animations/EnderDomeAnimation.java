package fr.synchroneyes.mineral.DeathAnimations.Animations;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.effect.ShieldEffect;
import fr.synchroneyes.mineral.DeathAnimations.EffectAnimation;
import org.bukkit.Particle;

public class EnderDomeAnimation extends EffectAnimation {

    @Override
    public String getAnimationName() {
        return "EnderDome";
    }

    @Override
    protected Class getEffectClass() {
        return ShieldEffect.class;
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
        ShieldEffect effect = (ShieldEffect) e;
        effect.radius = 2F;
        effect.particle = Particle.REVERSE_PORTAL;
    }
}
