package fr.mineral.Commands.Developper;

import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCommand implements CommandExecutor {

    /*
            Old command, saved just in case ...
     */

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player joueur = (Player) sender;
        if(command.getName().equalsIgnoreCase("set")) {
            // On a plusieurs cas à traiter
            // Dans tous les cas, on doit être admin.
            if(sender.isOp()) {
                /*
                    Liste des cas:
                                0       1       2
                        - /set coffre <house> <couleur>
                        - /set coffre <arene>
                        - /set spawn <house> <couleur>
                        - /set spawn <arene> <death/tp>
                 */

                String[] interations = {"coffre", "spawn"};
                String[] couleurs = {"red", "r", "rouge", "jaune", "yellow", "y", "j", "bleu", "blue", "b"};
                String[] lieux = {"house", "arene"};
                String[] tp = {"death", "deathzone", "tp"};

                for(String lieu : lieux)
                    if(lieu.equalsIgnoreCase(args[1]))
                        // On a un lieu à traiter
                        switch(lieu) {
                            case "arene":
                                for(String interaction : interations)
                                    if(interaction.equalsIgnoreCase(args[0]))
                                        switch(interaction) {
                                            case "coffre":
                                                try {
                                                    mineralcontest.plugin.getGame().getArene().setCoffre(joueur.getLocation());
                                                    sender.sendMessage(mineralcontest.prefixPrive + "Coffre d'arene ajouté");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                break;

                                            case "spawn":
                                                for(String teleport : tp)
                                                    if(teleport.equalsIgnoreCase(args[2]))
                                                        switch(teleport) {
                                                            case "death":
                                                            case "deathzone":
                                                                mineralcontest.plugin.getGame().getArene().getDeathZone().setSpawnLocation(joueur.getLocation());
                                                                break;

                                                            case "tp":
                                                                mineralcontest.plugin.getGame().getArene().setTeleportSpawn(joueur.getLocation());
                                                                break;
                                                        }
                                        }
                                break;


                            case "house":
                                // On check la couleur
                                for(String couleur : couleurs)
                                    if(couleur.equalsIgnoreCase(args[2]))
                                        switch (couleur) {
                                            case "r":
                                            case "rouge":
                                            case "red":
                                                for(String interation : interations)
                                                    if(interation.equalsIgnoreCase(args[0]))
                                                        switch(interation) {
                                                            case "coffre" :
                                                                // On spawn le coffre rouge
                                                                joueur.sendMessage(mineralcontest.prefixPrive + "Ajout du coffre de l'équipe ROUGE");
                                                                mineralcontest.plugin.getGame().getRedHouse().setCoffreEquipe(joueur.getLocation());
                                                                break;
                                                            case "spawn":
                                                                mineralcontest.plugin.getGame().getRedHouse().setHouseLocation(joueur.getLocation());
                                                                break;
                                                        }
                                                break;

                                            case "b":
                                            case "bleu":
                                            case "blue":
                                                for(String interation : interations)
                                                    if(interation.equalsIgnoreCase(args[0]))
                                                        switch(interation) {
                                                            case "coffre" :
                                                                // On spawn le coffre rouge
                                                                joueur.sendMessage(mineralcontest.prefixPrive + "Ajout du coffre de l'équipe BLEU");
                                                                mineralcontest.plugin.getGame().getBlueHouse().setCoffreEquipe(joueur.getLocation());
                                                                break;
                                                            case "spawn":
                                                                mineralcontest.plugin.getGame().getBlueHouse().setHouseLocation(joueur.getLocation());
                                                                break;
                                                        }
                                                break;

                                            case "j":
                                            case "y":
                                            case "jaune":
                                            case "yellow":
                                                for(String interation : interations)
                                                    if(interation.equalsIgnoreCase(args[0]))
                                                        switch(interation) {
                                                            case "coffre" :
                                                                // On spawn le coffre rouge
                                                                joueur.sendMessage(mineralcontest.prefixPrive + "Ajout du coffre de l'équipe JAUNE");
                                                                mineralcontest.plugin.getGame().getYellowHouse().setCoffreEquipe(joueur.getLocation());
                                                                break;
                                                            case "spawn":
                                                                mineralcontest.plugin.getGame().getYellowHouse().setHouseLocation(joueur.getLocation());
                                                                break;
                                                        }
                                                break;
                                        }
                                break;

                        }


            } else {
                sender.sendMessage(mineralcontest.prefixErreur + "Vous n'avez pas accès à cette commande.");
            }
        }

        return false;
    }
}
