package fr.synchroneyes.mineral.Commands;

import fr.synchroneyes.groups.Commands.CommandTemplate;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class SpawnDrop extends CommandTemplate {

    public SpawnDrop() {
        accessCommande.add(PLAYER_COMMAND);
        accessCommande.add(PLAYER_ADMIN);
        accessCommande.add(GROUP_REQUIRED);

        addArgument("spawnParachute", false);
    }

    @Override
    public boolean performCommand(CommandSender commandSender, String command, String[] args) {
        Player joueur = (Player) commandSender;
        Groupe playerGroupe = mineralcontest.getPlayerGroupe(joueur);

        if (args.length > 0) playerGroupe.getParachuteManager().spawnNewParachute(joueur.getLocation());
        else {

        }
        /*else {
            AutomatedChestAnimation test = playerGroupe.getAutomatedChestManager().getFromClass(TestAnimation.class);
            test.spawn(joueur.getLocation().getBlock().getLocation());

            playerGroupe.getAutomatedChestManager().replace(TestAnimation.class, test);
        }*/

        return false;
    }

    @Override
    public String getCommand() {
        return "spawndrop";
    }

    @Override
    public String getDescription() {
        return "Spawn un coffre";
    }

    @Override
    public String getPermissionRequise() {
        return null;
    }

}
