package fr.synchroneyes.mineral.Commands;

import fr.synchroneyes.groups.Commands.CommandTemplate;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.House;
import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.Settings.GameCVAR;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DisplayScoreCommand extends CommandTemplate {


    public DisplayScoreCommand() {
        accessCommande.add(PLAYER_COMMAND);
        accessCommande.add(GROUP_REQUIRED);
        accessCommande.add(GROUP_ADMIN);
        accessCommande.add(GAME_STARTED);

        addArgument("type", false);
    }

    @Override
    public String getCommand() {
        return "mc_displayscore";
    }

    @Override
    public String getDescription() {
        return "Permet d'afficher le score dans le chat";
    }

    @Override
    public String getPermissionRequise() {
        return null;
    }

    @Override
    public boolean performCommand(CommandSender commandSender, String command, String[] args) {

        // On gère les arguments
        String[] arguments = new String[]{"name", "podium", "all"};
        Game partie = mineralcontest.getPlayerGame((Player) commandSender);

        if(args.length == 1) {
            boolean exists = false;
            for(String arg : arguments)
                if(arg.equals(args[0])) exists = true;
            if(!exists) {
                commandSender.sendMessage(ChatColor.RED + "Argument inconnu");
                return false;
            }



            String argument = args[0];
            switch (argument) {
                case "all":
                    displayScore(partie, true, true, false);
                    break;
                case "name":
                    displayScore(partie, false, true, false);
                    break;
                case "podium":
                    displayScore(partie, false, true, true);
                    break;
            }

            return true;
        }

        displayScore(partie, true, true, false);

        return false;
    }

    /**
     * Méthode permettant d'afficher le score dans une partie
     * @param partie
     * @param displayScore
     * @param displayPlace
     */
    private void displayScore(Game partie, boolean displayScore, boolean displayPlace, boolean displayPodium) {

        List<House> maisons = (List<House>) partie.getHouses().clone();

        int max_score = Integer.MIN_VALUE;
        House best_house = null;
        List<House> ordered_Houses = new LinkedList<>();

        maisons.removeIf(maison -> (maison.getTeam().getJoueurs().isEmpty()));

        // ON va trier dans l'ordre des points
        while(!maisons.isEmpty()) {
            for (House maison : maisons) {
                if (maison.getTeam().getScore() >= max_score) {
                    max_score = maison.getTeam().getScore();
                    best_house = maison;
                }
            }

            ordered_Houses.add(best_house);
            maisons.remove(best_house);

        }


        partie.groupe.sendToEveryone(ChatColor.GOLD +  "===========");
        partie.groupe.sendToEveryone(ChatColor.RED + "" + ChatColor.BOLD + "Leaderboard");
        int indexMaison = 1;
        for(House maison : ordered_Houses) {
            String message_a_publier = "";
            if(displayPlace) message_a_publier += ChatColor.GOLD + "" + indexMaison + " - ";
            message_a_publier += maison.getTeam().getCouleur() + maison.getTeam().getNomEquipe() + ChatColor.RESET;
            if(displayScore) message_a_publier += " " + maison.getTeam().getScore() + " point(s)";

            if(displayPodium && indexMaison > 3) {
                break;
            }

            indexMaison++;
            partie.groupe.sendToEveryone(message_a_publier);
        }

        partie.groupe.sendToEveryone(ChatColor.GOLD + "===========");


    }

    @Override
    public List<String> tabComplete(CommandSender sender, String command, String[] arguments) {

        if (sender instanceof Player) {
            Player joueur = (Player) sender;
            Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);
            if (playerGroup == null) return null;

            if (arguments.length == 1) {
                String argument = arguments[0];

                List<String> available_args = new ArrayList<>();
                available_args.add("all");
                available_args.add("podium");
                available_args.add("name");

                List<String> available_argument_complete = new LinkedList<>();


                for (String arg : available_args) {
                    if (arg.equalsIgnoreCase(argument) || arg.toLowerCase().contains(argument.toLowerCase()))
                        available_argument_complete.add(arg);
                }

                if (available_argument_complete.isEmpty()) available_argument_complete.add("No results");
                return available_argument_complete;
            }
        }

        return null;
    }
}
