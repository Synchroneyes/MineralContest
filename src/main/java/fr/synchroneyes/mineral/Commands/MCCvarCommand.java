package fr.synchroneyes.mineral.Commands;

import fr.synchroneyes.groups.Commands.CommandTemplate;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Settings.GameCVAR;
import fr.synchroneyes.mineral.mineralcontest;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
    public boolean performCommand(CommandSender commandSender, String command, String[] args) {
        Player joueur = (Player) commandSender;
        Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);
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
                    if (cvar.isNumber() && !NumberUtils.isNumber(args[1])) {
                        joueur.sendMessage(mineralcontest.prefixErreur + cvar.getCommand() + " attend un nombre en paramètre");
                        return false;
                    }

                    cvar.setValeur(args[1]);

                    try {
                        playerGroup.getParametresPartie().setCVARValeur(cvar.getCommand(), args[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Si on est pas sur la version communautaire, on peut sauvegarder le fichier
                    playerGroup.getParametresPartie().saveCVAR(cvar);

                    joueur.sendMessage(mineralcontest.prefixPrive + "Valeur mise à jour, " + cvar.getCommand() + " => " + cvar.getValeur());

                    if (cvar.getCommand().equalsIgnoreCase("enable_monster_in_protected_zone") && cvar.getValeurNumerique() == 0) {
                        for (Entity entite : joueur.getWorld().getEntities())
                            if (entite instanceof Monster) entite.remove();

                    }

                    if (cvar.getCommand().equalsIgnoreCase("mp_enable_old_pvp")) {

                        for (Player online : mineralcontest.getPlayerGroupe(joueur).getPlayers()) {
                            if (cvar.getValeurNumerique() == 1) {
                                online.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(1024d);
                            } else {
                                online.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
                            }
                        }

                    }

                    if (cvar.getCommand().equalsIgnoreCase("enable_kits")) {
                        if (cvar.getValeurNumerique() == 1) {
                            playerGroup.getKitManager().setKitsEnabled(true);
                        } else {
                            playerGroup.getKitManager().setKitsEnabled(false);
                        }
                    }

                    return false;
                }

        }

        return false;
    }


    @Override
    public String getCommand() {
        return "mcvar";
    }

    @Override
    public String getDescription() {
        return "Permet de modifier un paramètre de partie";
    }

    @Override
    public String getPermissionRequise() {
        return null;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String command, String[] arguments) {

        if (sender instanceof Player) {
            Player joueur = (Player) sender;
            Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);
            if (playerGroup == null) return null;

            if (arguments.length == 1) {
                String argument = arguments[0];

                List<String> available_cvar = new ArrayList<>();
                for (GameCVAR cvar : playerGroup.getParametresPartie().getParametres()) {
                    String cvar_renamed = cvar.getCommand();
                    cvar_renamed = cvar_renamed.replace("_", "");
                    if (cvar.getCommand().equalsIgnoreCase(argument) || cvar.getCommand().toLowerCase().contains(argument.toLowerCase()) ||
                            cvar_renamed.equalsIgnoreCase(argument) || cvar_renamed.toLowerCase().contains(argument.toLowerCase()))
                        available_cvar.add(cvar.getCommand());
                }

                if (available_cvar.isEmpty()) available_cvar.add("No results");
                return available_cvar;
            }
        }

        return null;
    }

}
