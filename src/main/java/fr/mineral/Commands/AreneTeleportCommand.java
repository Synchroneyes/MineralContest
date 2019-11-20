package fr.mineral.Commands;

import fr.mineral.Core.Equipe;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AreneTeleportCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(mineralcontest.plugin.getGame().isGameStarted() && !mineralcontest.plugin.getGame().isGamePaused()) {
            if(command.getName().equals("arene")) {
                Player joueur = (Player) sender;

                if(mineralcontest.plugin.getGame().getArene().isTeleportAllowed()) {
                    Equipe team = mineralcontest.plugin.getGame().getPlayerTeam(joueur);

                    for(Player membre : team.getJoueurs()) {
                        membre.teleport(mineralcontest.plugin.getGame().getArene().getTeleportSpawn());
                        membre.sendMessage(mineralcontest.prefixPrive + Lang.translate((String) mineralcontest.LANG.get("arena_teleporting")));
                    }
                } else {
                    joueur.sendMessage(mineralcontest.prefixErreur + Lang.translate((String) mineralcontest.LANG.get("arena_teleport_disabled")));
                }
            }
        }
        return false;
    }
}
