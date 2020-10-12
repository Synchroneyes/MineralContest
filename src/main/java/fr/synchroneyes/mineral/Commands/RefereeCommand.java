package fr.synchroneyes.mineral.Commands;

import fr.synchroneyes.groups.Commands.CommandTemplate;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RefereeCommand extends CommandTemplate {
    @Override
    public String getCommand() {
        return "referee";
    }

    @Override
    public String getDescription() {
        return "Permet de devenir arbitre";
    }

    @Override
    public String[] setCommands() {
        return new String[]{"arbitre"};
    }

    @Override
    public String getPermissionRequise() {
        return null;
    }

    public RefereeCommand() {
        super();


        accessCommande.add(PLAYER_COMMAND);
        accessCommande.add(GROUP_REQUIRED);
        accessCommande.add(GROUP_ADMIN);

        addArgument("joueurCible", false);
    }

    @Override
    public boolean performCommand(CommandSender commandSender, String command, String[] args) {
        Player joueur = (Player) commandSender;

        if(args.length == 1) {
            joueur = Bukkit.getPlayer(args[0]);
            if(joueur == null) {
                commandSender.sendMessage(mineralcontest.prefixErreur + "Joueur inconnu.");
                return false;
            }
        }

        Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);


        try {
            if(playerGroup.getGame().isReferee(joueur)) {
                commandSender.sendMessage(mineralcontest.prefixAdmin + joueur.getDisplayName() + " n'est plus arbitre");
                playerGroup.getGame().removeReferee(joueur, true);
            } else {
                playerGroup.getGame().addReferee(joueur);
                commandSender.sendMessage(mineralcontest.prefixAdmin + joueur.getDisplayName() + " est désormais arbitre");

            }
        }catch (Exception e) {

        }
        // SI le joueur est arbitre, on le supprime

        return false;
    }
    /*@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // todo
        Player player = (Player) sender;
        if (mineralcontest.isInAMineralContestWorld(player)) {
            if (command.getName().equalsIgnoreCase("referee") || command.getName().equalsIgnoreCase("arbitre")) {
                if (sender.isOp()) {
                    Game game = mineralcontest.getPlayerGame(player);
                    if (game == null) {
                        sender.sendMessage(mineralcontest.prefixErreur + Lang.error_command_can_only_be_used_in_game.toString());
                        return false;
                    }
                    if (!game.isReferee(player)) {
                        game.addReferee(player);
                        return false;
                    } else {

                        try {
                            game.removeReferee(player, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return false;
                    }
                }
            }
            return false;
        }
        return false;
    }*/
}
