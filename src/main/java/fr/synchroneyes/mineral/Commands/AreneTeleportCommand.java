package fr.synchroneyes.mineral.Commands;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.Player.PlayerUtils;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AreneTeleportCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Lang.error_command_can_only_be_used_in_game.toString());
            return false;
        }

        Player player = (Player) sender;


        if (mineralcontest.isInAMineralContestWorld(player)) {
            Game partie = mineralcontest.getPlayerGame(player);
            if (partie == null) {
                sender.sendMessage(mineralcontest.prefixErreur + Lang.error_command_can_only_be_used_in_game.toString());
                return false;
            }

            if (partie.isGameStarted() && !partie.isGamePaused()) {
                if (command.getName().equals("arene") || command.getName().equals("arena")) {
                    Player joueur = (Player) sender;

                    if (partie.isReferee(joueur)) {
                        teleportToArena(joueur);
                        return false;
                    }

                    if (PlayerUtils.isPlayerInDeathZone(joueur)) return false;

                    if (partie.getArene().isTeleportAllowed()) {
                        Equipe team = partie.getPlayerTeam(joueur);

                        // Si le joueur a acheté le bonus équipe permettant de seulement tp le joueur faisant /arene
                        if (partie.getArene().canTeamUseSingleTeleport(team)) {
                            // On téléport le joueur uniquement
                            teleportToArena(joueur);
                        } else {
                            // On téléport toute l'équipe
                            for (Player membre : team.getJoueurs()) {
                                if (!partie.isReferee(membre) && !PlayerUtils.isPlayerInDeathZone(membre))
                                    teleportToArena(membre);
                            }
                        }


                    } else {
                        joueur.sendMessage(mineralcontest.prefixErreur + Lang.translate(Lang.arena_teleport_disabled.toString()));
                    }
                }
            }
        }

        return false;
    }

    private void teleportToArena(Player p) {

        Game partie = mineralcontest.getPlayerGame(p);

        PlayerUtils.teleportPlayer(p, partie.groupe.getMonde(), partie.getArene().getTeleportSpawn());
        p.sendMessage(mineralcontest.prefixPrive + Lang.translate(Lang.arena_teleporting.toString()));
    }
}
