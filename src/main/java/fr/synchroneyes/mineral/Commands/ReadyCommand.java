package fr.synchroneyes.mineral.Commands;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.*;
import fr.synchroneyes.groups.Utils.Etats;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.DeathAnimations.Animations.HeartAnimation;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.ErrorReporting.Error;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Effect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.io.File;

public class ReadyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {


        HeartAnimation animation = new HeartAnimation();
        animation.playAnimation((LivingEntity) commandSender);
        if(1==1) return true;


        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Lang.error_command_can_only_be_used_in_game.toString());
            return false;
        }

        Player player = (Player) commandSender;


        if (mineralcontest.isInAMineralContestWorld(player)) {
            Game partie = mineralcontest.getPlayerGame(player);
            if (partie == null) {
                commandSender.sendMessage(mineralcontest.prefixErreur + Lang.error_command_can_only_be_used_in_game.toString());
                return false;
            }
            if (command.getName().equalsIgnoreCase("ready")) {

                if (partie.groupe.getEtatPartie().equals(Etats.EN_ATTENTE)) {
                    try {
                        partie.setPlayerReady(player);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (partie.groupe.getEtatPartie().equals(Etats.ATTENTE_DEBUT_PARTIE)) {
                    try {
                        if (!partie.isPlayerReady(player)) partie.setPlayerReady(player);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Error.Report(e, partie);
                    }
                }
            }
        }
        return false;
    }
}
