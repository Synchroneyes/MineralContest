package fr.mineral.Utils;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FreezeLibrary {
    public static void freezePlayer(Player player) {
        player.setWalkSpeed(0.0F);
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10000, 128));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10000, 128));

    }

    public static void unfreezePlayer(Player player) {
        player.setWalkSpeed(0.2F);
        player.setSprinting(true);
        player.removePotionEffect(PotionEffectType.JUMP);
        player.removePotionEffect(PotionEffectType.SLOW);

    }
}
