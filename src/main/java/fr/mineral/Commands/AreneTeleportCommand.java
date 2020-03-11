package fr.mineral.Commands;

import fr.mineral.Teams.Equipe;
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
            if(command.getName().equals("arene") || command.getName().equals("arena")) {
                Player joueur = (Player) sender;

                if(mineralcontest.plugin.getGame().isReferee(joueur)) {
                    teleportToArena(joueur);
                    return false;
                }

                if(mineralcontest.plugin.getGame().getArene().isTeleportAllowed()) {
                    Equipe team = mineralcontest.plugin.getGame().getPlayerTeam(joueur);

                    for(Player membre : team.getJoueurs()) {
                        teleportToArena(membre);
                    }
                } else {
                    joueur.sendMessage(mineralcontest.prefixErreur + Lang.translate(Lang.arena_teleport_disabled.toString()));
                }
            }
        }
        return false;
    }

    private void teleportToArena(Player p) {
        p.teleport(mineralcontest.plugin.getGame().getArene().getTeleportSpawn());
        p.sendMessage(mineralcontest.prefixPrive + Lang.translate(Lang.arena_teleporting.toString()));
    }
}
