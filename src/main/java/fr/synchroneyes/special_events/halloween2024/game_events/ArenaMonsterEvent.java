package fr.synchroneyes.special_events.halloween2024.game_events;

import fr.synchroneyes.mineral.Core.Boss.Boss;
import fr.synchroneyes.mineral.Core.Boss.BossType.ArenaMonster;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Utils.Player.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ArenaMonsterEvent extends  HalloweenEvent{

    private Game partie;
    private Boss boss;

    public ArenaMonsterEvent(Game partie) {
        super(partie);
        this.partie = partie;
    }

    @Override
    public String getEventName() {
        return "ArenaMonster";
    }

    @Override
    public void executionContent() {
        this.boss = new ArenaMonster(partie);
        this.partie.getBossManager().spawnNewBoss(this.partie.getArene().getCoffre().getLocation(), this.boss);
    }

    @Override
    public void beforeExecute() {
        for(Player player : this.partie.groupe.getPlayers()) {
            player.playEffect(EntityEffect.THORNS_HURT);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 20*5, 1));
        }
    }

    @Override
    public void afterExecute() {

    }

    @Override
    public String getEventTitle() {
        return "Commendant de l'enfer";
    }

    @Override
    public String getEventDescription() {
        return "Je vous attend dans l'arène ...";
    }

    @Override
    public boolean isTextMessageNotificationEnabled() {
        return true;
    }

    @Override
    public boolean isNotificationDelayed() {
        return false;
    }
}
