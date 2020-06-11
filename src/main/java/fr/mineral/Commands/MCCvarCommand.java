package fr.mineral.Commands;

import fr.groups.Commands.CommandTemplate;
import fr.groups.Core.Groupe;
import fr.mineral.Settings.GameCVAR;
import fr.mineral.Settings.GameSettings;
import fr.mineral.Utils.ChatColorString;
import fr.mineral.mineralcontest;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

public class MCCvarCommand extends CommandTemplate {

    public MCCvarCommand() {
        super();
        addArgument("parametre", true);
        addArgument("valeur", false);

        accessCommande.add(PLAYER_COMMAND);
        accessCommande.add(GROUP_REQUIRED);
        accessCommande.add(GROUP_ADMIN);

    }


    @Override
    public String getCommand() {
        return "mcvar";
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {

        try {
            canPlayerUseCommand(commandSender, args);
        } catch (Exception e) {
            commandSender.sendMessage(mineralcontest.prefixErreur + e.getMessage());
            return false;
        }

        Player joueur = (Player) commandSender;
        Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);
        Bukkit.getLogger().severe(playerGroup.toString());
        if (args.length == 1) {
            for (GameCVAR cvar : playerGroup.getParametresPartie().getParametres())
                if (cvar.getCommand().equalsIgnoreCase(args[0])) {
                    joueur.sendMessage(ChatColor.GREEN + "-----------------");
                    joueur.sendMessage(mineralcontest.prefixPrive + cvar.getCommand() + " => " + cvar.getValeur());
                    joueur.sendMessage(mineralcontest.prefixPrive + cvar.getDescription());
                    joueur.sendMessage(ChatColor.GREEN + "-----------------");
                    return false;
                }
            joueur.sendMessage(mineralcontest.prefixErreur + "Paramètre " + args[0] + " non trouvé");
            return false;
        }

        if (args.length == 2) {
            for (GameCVAR cvar : playerGroup.getParametresPartie().getParametres())
                if (cvar.getCommand().equalsIgnoreCase(args[0])) {
                    if (cvar.isNumber() && !StringUtils.isNumeric(args[1])) {
                        joueur.sendMessage(mineralcontest.prefixErreur + cvar.getCommand() + " attend un nombre en paramètre");
                        return false;
                    }
                    cvar.setValeur(args[1]);
                    joueur.sendMessage(mineralcontest.prefixPrive + "Valeur mise à jour, " + cvar.getCommand() + " => " + cvar.getValeur());

                    if (cvar.getCommand().equalsIgnoreCase("enable_monster_in_protected_zone") && cvar.getValeurNumerique() == 0) {
                        for (Entity entite : joueur.getWorld().getEntities())
                            if (entite instanceof Monster) entite.remove();

                    }

                    return false;
                }

        }

        return false;
    }

    @Override
    public String getDescription() {
        return "Permet de modifier un paramètre de partie";
    }

    @Override
    public String getPermissionRequise() {
        return null;
    }
}
