package fr.synchroneyes.mineral.Commands;

import fr.synchroneyes.groups.Commands.CommandTemplate;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Coffre.AutomatedChestAnimation;
import fr.synchroneyes.mineral.Core.Coffre.TestAnimation;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class SpawnDrop extends CommandTemplate {

    public SpawnDrop() {
        accessCommande.add(PLAYER_COMMAND);
        this.accessCommande.add(PLAYER_ADMIN);
        accessCommande.add(GROUP_REQUIRED);
    }

    @Override
    public boolean performCommand(CommandSender commandSender, String command, String[] args) {
        Player joueur = (Player) commandSender;
        Groupe playerGroupe = mineralcontest.getPlayerGroupe(joueur);

        //playerGroupe.getParachuteManager().spawnNewParachute(joueur.getLocation());

        AutomatedChestAnimation test = playerGroupe.getAutomatedChestManager().getFromClass(TestAnimation.class);
        test.spawn(joueur.getLocation().getBlock().getLocation());

        playerGroupe.getAutomatedChestManager().replace(TestAnimation.class, test);

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