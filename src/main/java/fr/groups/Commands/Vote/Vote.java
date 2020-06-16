package fr.groups.Commands.Vote;

import fr.groups.Commands.CommandTemplate;
import fr.groups.Core.Groupe;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Vote extends CommandTemplate {

    public Vote() {
        super();
        addArgument("ID du biome", true);

        this.accessCommande.add(PLAYER_COMMAND);
        this.accessCommande.add(GROUP_REQUIRED);
        this.accessCommande.add(GROUP_VOTE_STARTED);
    }

    @Override
    public boolean performCommand(CommandSender commandSender, String command, String[] args) {
        Bukkit.getLogger().severe("Player voted");
        Player joueur = (Player) commandSender;
        Groupe playerGroupe = null;
        int mapVoter = -1;
        try {
            mapVoter = Integer.parseInt(args[0]);

            if (mapVoter < 0) throw new NumberFormatException();
            playerGroupe = mineralcontest.getPlayerGroupe(joueur);
            if (mapVoter >= playerGroupe.getMapVote().getMaps().size()) throw new NumberFormatException();

        } catch (NumberFormatException nfe) {
            commandSender.sendMessage(mineralcontest.prefixErreur + this.getUsage());
            return false;
        }

        // On a plus qu'a enregistrer le vote
        playerGroupe.getMapVote().enregistrerVoteJoueur(mapVoter, joueur);
        return false;
    }

    @Override
    public String getCommand() {
        return "vote";
    }


    @Override
    public String getDescription() {
        return "Permet de voter pour un biome";
    }

    @Override
    public String getPermissionRequise() {
        return null;
    }
}
