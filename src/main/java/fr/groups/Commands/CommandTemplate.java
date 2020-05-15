package fr.groups.Commands;

import fr.groups.Core.Groupe;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public abstract class CommandTemplate extends BukkitCommand {

    protected final int GROUP_REQUIRED = 0;
    protected final int NO_GROUP = 1;
    protected final int GROUP_ADMIN = 2;
    protected final int GROUP_CREATOR = 3;
    protected final int PLAYER_COMMAND = 4;
    protected final int CONSOLE_COMMAND = 5;

    public abstract String getCommand();

    public abstract String getDescription();

    public abstract String getPermissionRequise();

    protected LinkedList<String> arguments;
    protected LinkedList<Integer> accessCommande;


    public String getErrorMessage() {
        return "You do not have access to this command";
    }

    protected CommandTemplate() {
        super("");
        this.description = getDescription();
        this.setName(getCommand());
        this.setPermission(this.getPermissionRequise());
        this.setPermissionMessage(getErrorMessage());
        this.setUsage("Usage: /" + getCommand() + " " + getArgumentsString());
        this.accessCommande = new LinkedList<>();
        this.arguments = new LinkedList<>();
    }

    protected CommandTemplate(String name) {
        super("");
        this.description = getDescription();
        this.setName(getCommand());
        this.setPermission(this.getPermissionRequise());
        this.setPermissionMessage(getErrorMessage());
        this.setUsage("Usage: /" + getCommand() + " " + getArgumentsString());
        this.arguments = new LinkedList<>();
        this.accessCommande = new LinkedList<>();

    }


    protected void canPlayerUseCommand(CommandSender p, String[] receivedArgs) throws Exception {
        Groupe playerGroupe = null;

        for (int condition : accessCommande) {

            if (condition == PLAYER_COMMAND) {
                if (!(p instanceof Player)) throw new Exception(Lang.error_command_can_only_be_used_in_game.toString());
                playerGroupe = mineralcontest.getPlayerGroupe((Player) p);
            }

            if (condition == GROUP_REQUIRED)
                if (playerGroupe == null) throw new Exception(Lang.error_you_must_be_in_a_group.toString());

            if (condition == GROUP_ADMIN) {
                if (!playerGroupe.isAdmin((Player) p))
                    throw new Exception(Lang.error_you_must_be_group_admin.toString());
            }

            if (condition == NO_GROUP) {
                if (playerGroupe != null) throw new Exception(Lang.error_you_already_have_a_group.toString());
            }

            if (condition == GROUP_CREATOR) {
                if (!playerGroupe.isGroupeCreateur((Player) p))
                    throw new Exception(Lang.error_you_must_be_group_owner.toString());
            }
        }

        if (arguments.size() != receivedArgs.length) throw new Exception(mineralcontest.prefixErreur + getUsage());
    }


    public String getArgumentsString() {
        StringBuilder sb = new StringBuilder();
        if (arguments == null) this.arguments = new LinkedList<>();
        for (String arg : arguments)
            sb.append("<" + arg + "> ");
        return sb.toString();
    }

    public void constructArguments() {
        this.setUsage("Usage: /" + this.getCommand() + " " + getArgumentsString());
    }


}
