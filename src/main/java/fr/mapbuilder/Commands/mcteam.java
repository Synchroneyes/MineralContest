package fr.mapbuilder.Commands;

import fr.groups.Commands.CommandTemplate;
import fr.mapbuilder.Blocks.BlocksDataColor;
import fr.mapbuilder.Core.Monde;
import fr.mapbuilder.MapBuilder;
import fr.mineral.Core.House;
import fr.mineral.Teams.Equipe;
import fr.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class mcteam extends CommandTemplate {


    private LinkedList<String> actionsPossible;
    public static Monde monde = MapBuilder.monde;

    private static HashMap<House, LinkedList<Block>> porteEquipe;
    private static HashMap<House, Player> attributionEquipeJoueur;

    public mcteam() {

        if (porteEquipe == null) porteEquipe = new HashMap<>();
        if (attributionEquipeJoueur == null) attributionEquipeJoueur = new HashMap<>();

        this.actionsPossible = new LinkedList<>();
        actionsPossible.add("creer");
        actionsPossible.add("supprimer");
        actionsPossible.add("setSpawn");
        actionsPossible.add("addPorte");
        actionsPossible.add("setCoffre");


        addArgument("action", true);
        addArgument("nom equipe", true);
        addArgument("couleur", false);

        if (monde == null) monde = new Monde();

        accessCommande.add(PLAYER_COMMAND);
        constructArguments();
    }

    @Override
    public boolean performCommand(CommandSender commandSender, String command, String[] args) {
        Player joueur = (Player) commandSender;
        if (args[0].equalsIgnoreCase("creer")) {
            if (args.length == 3) {
                return creerEquipeHandler(commandSender, args);
            } else {
                commandSender.sendMessage(mineralcontest.prefixErreur + getUsage());
                return false;
            }
        }


        if (args[0].equalsIgnoreCase("supprimer")) {
            if (args.length == 2) {
                return supprimerEquipeHandler(commandSender, args);
            } else {
                commandSender.sendMessage(mineralcontest.prefixErreur + getUsage());
                return false;
            }
        }

        if (args[0].equalsIgnoreCase("setSpawn")) {
            if (args.length == 2) {
                try {
                    return setSpawnHandler(commandSender, args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                commandSender.sendMessage(mineralcontest.prefixErreur + getUsage());
                return false;
            }
        }

        if (args[0].equalsIgnoreCase("setCoffre")) {
            if (args.length == 2) {
                try {
                    return setCoffreHandler(commandSender, args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                commandSender.sendMessage(mineralcontest.prefixErreur + getUsage());
                return false;
            }
        }

        if (args[0].equalsIgnoreCase("addPorte")) {
            if (args.length == 2) {
                try {
                    return addPorteHandler(commandSender, args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                commandSender.sendMessage(mineralcontest.prefixErreur + getUsage());
                return false;
            }
        }

        return false;
    }

    @Override
    public String getCommand() {
        return "mcteam";
    }



    /**
     * Utilisé pour l'ajout de porte à une maison, à partir d'un joueur, on récupère la maison à laquelle il souhaite ajouter des portes
     *
     * @param p - Le joueur où on souhaite récuperer la maison
     * @return La maison ou non si aucune
     */
    public static House getPlayerAllocatedHouse(Player p) {
        for (Map.Entry<House, Player> couple : attributionEquipeJoueur.entrySet())
            if (couple.getValue().equals(p)) return couple.getKey();
        return null;
    }

    /**
     * Retourne le couple (maison, portes) à partir d'une maison
     *
     * @param house - La maison
     * @return couple(maison, porte) ou null si n'existe pas
     */
    public static Map.Entry<House, LinkedList<Block>> getPorteMaison(House house) {
        for (Map.Entry<House, LinkedList<Block>> couple : porteEquipe.entrySet())
            if (couple.getKey().equals(house)) return couple;
        return null;
    }


    /**
     * Crée le couple (maison, blocks de porte) à partir d'une équipe donné
     *
     * @param nomEquipe - Le nom de l'équipe
     * @param p         - Le joueur ayant demander la création de la porte
     */
    private void creerPorteMaison(String nomEquipe, Player p) {
        House maison = monde.getHouseFromNom(nomEquipe);
        if (maison == null) return;

        if (getPorteMaison(maison) == null) {
            porteEquipe.put(maison, new LinkedList<Block>());
            attributionEquipeJoueur.put(maison, p);
        }
    }

    /**
     * Attribue une maison à un joueur, pour définir les blocs porte de la maison
     * Appelé lorsque le joueur fait /mcteam addPorte TEAM
     *
     * @param commandSender - Un joueur
     * @param args          - Les arguments de la fonction
     * @return false
     */
    private boolean addPorteHandler(CommandSender commandSender, String[] args) {
        Player joueur = (Player) commandSender;
        House playerHouse = getPlayerAllocatedHouse(joueur);

        // Si le joueur est déjà attritré à une maison, alors on sauvegarde les portes de l'équipe
        if (playerHouse != null) {
            // On sauvegarde les blocs de la maison
            Map.Entry<House, LinkedList<Block>> couple = getPorteMaison(playerHouse);
            LinkedList<Block> blocks = couple.getValue();
            for (Block block : blocks)
                playerHouse.getPorte().addToDoor(block);

            joueur.sendMessage(mineralcontest.prefixPrive + "La porte de l'équipe " + playerHouse.getTeam().getCouleur() + playerHouse.getTeam().getNomEquipe() + ChatColor.WHITE + " a bien été enregistrée");
            attributionEquipeJoueur.remove(playerHouse);
            porteEquipe.remove(playerHouse);
            return false;
        }

        // Le joueur n'a pas de maison, on doit récuperer la maison passé en argument
        String nomMaison = args[1];

        creerPorteMaison(nomMaison, joueur);
        playerHouse = monde.getHouseFromNom(nomMaison);
        joueur.sendMessage(mineralcontest.prefixPrive + "Vous allez désormais définir les blocks de la porte de l'équipe " + playerHouse.getTeam().getCouleur() + playerHouse.getTeam().getNomEquipe());
        joueur.sendMessage(mineralcontest.prefixPrive + "Veuillez cliquer sur les blocks à ajouter");
        return false;

    }


    /**
     * Supprime une équipe à partir de son nom
     *
     * @param commandSender - Le joueur ayant effectué la commande
     * @param args          - Les arguments
     * @return false
     */
    private boolean supprimerEquipeHandler(CommandSender commandSender, String[] args) {
        String nomEquipe = args[1];
        House equipe = monde.getHouseFromNom(nomEquipe);
        if (equipe == null) {
            commandSender.sendMessage(mineralcontest.prefixErreur + "Cette équipe n'existe pas");
            return false;
        }

        monde.supprimerEquipe(nomEquipe);
        commandSender.sendMessage(mineralcontest.prefixPrive + "L'équipe " + equipe.getTeam().getNomEquipe() + " a bien été supprimée");
        return false;
    }


    /**
     * Attribue le spawn de l'équipe donnée en argument en fonction de la position du joueur ayant fait la commande
     *
     * @param commandSender - Le joueur ayant effectué la commande
     * @param args          - Les arguments
     * @return false
     * @throws Exception si la maison en question n'a pas de spawn de défini
     */
    private boolean setSpawnHandler(CommandSender commandSender, String[] args) throws Exception {
        String nomEquipe = args[1];
        House equipe = monde.getHouseFromNom(nomEquipe);
        if (equipe == null) {
            commandSender.sendMessage(mineralcontest.prefixErreur + "Cette équipe n'existe pas");
            return false;
        }

        Player p = (Player) commandSender;
        equipe.setHouseLocation(p.getLocation().getBlock().getLocation());

        p.sendMessage(mineralcontest.prefixPrive + "Le spawn de l'équipe " + equipe.getTeam().getCouleur() + equipe.getTeam().getNomEquipe() + ChatColor.WHITE + " a été défini en " + equipe.getHouseLocation().toVector().toString());


        return false;
    }

    private boolean setCoffreHandler(CommandSender commandSender, String[] args) throws Exception {
        String nomEquipe = args[1];
        House equipe = monde.getHouseFromNom(nomEquipe);
        if (equipe == null) {
            commandSender.sendMessage(mineralcontest.prefixErreur + "Cette équipe n'existe pas");
            return false;
        }

        Player p = (Player) commandSender;
        equipe.setCoffreEquipe(p.getLocation().getBlock().getLocation());

        p.sendMessage(mineralcontest.prefixPrive + "Le coffre de l'équipe " + equipe.getTeam().getCouleur() + equipe.getTeam().getNomEquipe() + ChatColor.WHITE + " a été défini en " + equipe.getHouseLocation().toVector().toString());


        return false;
    }

    /**
     * Crée une équipe à partir d'un nom, et d'une couleur
     *
     * @param commandSender - Le joueur ayant effectué la commande
     * @param args          - Le nom de l'équipe et la couleur
     * @return false
     */
    private boolean creerEquipeHandler(CommandSender commandSender, String[] args) {
        String nomEquipe = args[1];
        try {
            ChatColor couleur = ChatColor.valueOf(args[2].toUpperCase());
            monde.ajouterEquipe(nomEquipe, couleur);
            return false;
        } catch (IllegalArgumentException i) {
            StringBuilder couleursDispo = new StringBuilder();

            String couleurDispoText = "BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY, DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE";
            commandSender.sendMessage(mineralcontest.prefixErreur + "Couleur dispo: " + couleurDispoText);
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "Gestion des équipes du monde";
    }

    @Override
    public String getPermissionRequise() {
        return "admin";
    }


    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (alias.equalsIgnoreCase("mcteam")) {
            if (args.length == 0 || args.length == 1) return actionsPossible;

            if (args.length == 2) {
                if (!args[0].equalsIgnoreCase("creer")) return monde.getNomsEquipe();
            }
        }
        return new LinkedList<>();
    }
}
