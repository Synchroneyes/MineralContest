package fr.synchroneyes.mineral.Commands;

import fr.synchroneyes.groups.Commands.CommandTemplate;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Utils.Radius;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;


public class SpawnDrop extends CommandTemplate {

    public SpawnDrop() {
        accessCommande.add(PLAYER_COMMAND);
        accessCommande.add(PLAYER_ADMIN);
        accessCommande.add(GROUP_REQUIRED);
        accessCommande.add(GAME_STARTED);

        addArgument("spawnParachute", false);
    }

    @Override
    public boolean performCommand(CommandSender commandSender, String command, String[] args) {
        Player joueur = (Player) commandSender;
        Groupe playerGroupe = mineralcontest.getPlayerGroupe(joueur);

        if (args.length > 0) {

            Location randomLocation = playerGroupe.getGame().getArene().getCoffre().getLocation().clone();

            int tentatives = 1;
            int max = 100;
            int min = 50;

            int nbGenere;
            Random random = new Random();

            Location centreArene = playerGroupe.getGame().getArene().getCoffre().getLocation();

            while (Radius.isBlockInRadius(centreArene, randomLocation, min)) {
                nbGenere = random.nextInt(max);
                randomLocation.setX((nbGenere % 2 == 0) ? randomLocation.getX() - nbGenere : randomLocation.getX() + nbGenere);

                // Si on est encore dans le rayon du petit rectangle, on ajoute un nombre compris entre [min; max]
                if (Radius.isBlockInRadius(centreArene, randomLocation, min)) {
                    nbGenere = random.nextInt((max - min) - 1) + min;
                    randomLocation.setZ((nbGenere % 2 == 0) ? centreArene.getZ() - nbGenere : centreArene.getZ() + nbGenere);

                } else {
                    // On est pas dans le rayon,
                    // On peut donc ajouter une valeur comprise entre [-max; +max]

                    nbGenere = random.nextInt(max);
                    randomLocation.setZ((nbGenere % 2 == 0) ? randomLocation.getZ() - nbGenere : randomLocation.getZ() + nbGenere);

                }

                ++tentatives;
                if (tentatives > 100) break;

            }


            randomLocation.setY(130);
            // Maintenant on doit déterminer la hauteur du sol
            Location groundLocation = randomLocation.clone();
            while (groundLocation.getBlock().getType() == Material.AIR) {
                groundLocation.setY(groundLocation.getY() - 1);
            }

            // On veut mettre le ballon à 100 bloc de haut
            randomLocation.setY(groundLocation.getY() + 100);


            //playerGroupe.getGame().getParachuteManager().(randomLocation);
        }
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
