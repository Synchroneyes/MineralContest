package fr.synchroneyes.mineral.DeathAnimations;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public abstract class EffectAnimation extends DeathAnimation{

    private EffectManager manager;

    private Effect effect;

    /**
     * Classe de l'effect
     * @return
     */
    protected abstract Class getEffectClass();

    /**
     * Durée en seconde
     * @return
     */
    public abstract int getDuration();

    /**
     * Méthode permettant d'augmenter la hauteur de l'effet
     * @return
     */
    public abstract int getHeighOffset();

    public abstract void applyCustomSettings(Effect e);


    public EffectAnimation(){
        manager = mineralcontest.plugin.effectManager;
    }

    @Override
    public void playAnimation(LivingEntity player) {

        try {
            Class<?> clzz = Class.forName(getEffectClass().getName());
            Constructor<?> constructor = clzz.getConstructor(EffectManager.class);
            Effect instance = (Effect) constructor.newInstance(manager);
            instance.duration = getDuration() * 1000;

            applyCustomSettings(instance);

            Location location = player.getLocation();
            if(getHeighOffset() > 0) {
                location.setY(location.getY() + getHeighOffset());
            }

            instance.setLocation(location);

            instance.start();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
