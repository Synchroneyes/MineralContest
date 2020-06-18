package fr.mineral.Utils;

import fr.mineral.Utils.Door.AutomaticDoors;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class Setup {

    // Utile pour savoir si il faut relancer un setup ou non
    public static boolean premierLancement = true;
    public static int etape = 0;
    public static int maxDoorsCount = 9;
    public static Setup instance;
    public static Location emplacementTemporaire;
    public static Player Joueur;
    public static boolean addDoors = false;
    public Setup() {
        Setup.instance = this;
        porteBleu = new LinkedList<>();
        porteRouge = new LinkedList<>();
        porteJaune = new LinkedList<>();
    }

    public static LinkedList<Block> porteBleu;
    public static LinkedList<Block> porteJaune;
    public static LinkedList<Block> porteRouge;

    public static void displayInfos(Player joueur) {

        switch(etape) {
            case 0:
                mineralcontest.debug = true;
                Joueur = joueur;
                joueur.sendMessage("======================");
                joueur.sendMessage(mineralcontest.prefixPrive + "Bienvenue dans la mise en place du plugin.");
                joueur.sendMessage(mineralcontest.prefixPrive + "Vous allez être guidé pour mettre en place votre partie");
                joueur.sendMessage("======================");
                setEtape(1);
                displayInfos(joueur);
                break;

            case 1:
                joueur.sendMessage(mineralcontest.prefixPrive + "Veuillez vous rendre dans la base " + ChatColor.RED + "ROUGE");
                joueur.sendMessage(mineralcontest.prefixPrive + "Et effectuez un clic droit sur le bloc où les joueurs apparaitrons");

                break;
            case 2:
                //mineralcontest.getPlayerGame(joueur).getRedHouse().setHouseLocation(getEmplacementTemporaire());
                joueur.sendMessage("======================");
                joueur.sendMessage(mineralcontest.prefixPrive + "Veuillez selectionner l'emplacement du coffre de l'équipe " +  ChatColor.RED + "ROUGE");
                break;

            case 3:
                //mineralcontest.getPlayerGame(joueur).getRedHouse().setCoffreEquipe(getEmplacementTemporaire());
                joueur.sendMessage(mineralcontest.prefixPrive + "Veuillez vous rendre dans la base " + ChatColor.YELLOW + "JAUNE");
                joueur.sendMessage(mineralcontest.prefixPrive + "Et effectuez un clic droit sur le bloc où les joueurs apparaitrons");
                break;

            case 4:
                //mineralcontest.getPlayerGame(joueur).getYellowHouse().setHouseLocation(getEmplacementTemporaire());
                joueur.sendMessage(mineralcontest.prefixPrive + "Veuillez selectionner l'emplacement du coffre de l'équipe " +  ChatColor.YELLOW + "JAUNE");
                break;

            case 5:
                //mineralcontest.getPlayerGame(joueur).getYellowHouse().setCoffreEquipe(getEmplacementTemporaire());
                joueur.sendMessage(mineralcontest.prefixPrive + "Veuillez vous rendre dans la base " + ChatColor.BLUE + "BLEU");
                joueur.sendMessage(mineralcontest.prefixPrive + "Et effectuez un clic droit sur le bloc où les joueurs apparaitrons");
                break;

            case 6:
                //mineralcontest.getPlayerGame(joueur).getBlueHouse().setHouseLocation(getEmplacementTemporaire());
                joueur.sendMessage(mineralcontest.prefixPrive + "Veuillez selectionner l'emplacement du coffre de l'équipe " +  ChatColor.BLUE + "BLEU");
                break;

            case 7:
                //mineralcontest.getPlayerGame(joueur).getBlueHouse().setCoffreEquipe(getEmplacementTemporaire());
                joueur.sendMessage(mineralcontest.prefixPrive + "Veuillez vous rendre dans l'arène et cliquer où le coffre d'arène doit apparaitre");
                break;

            case 8:
                mineralcontest.getPlayerGame(joueur).getArene().setCoffre(getEmplacementTemporaire());
                joueur.sendMessage(mineralcontest.prefixPrive + "Veuillez vous rendre dans l'arène et cliquer où le /arene teleportera les gens");
                break;

            case 9:
                mineralcontest.getPlayerGame(joueur).getArene().setTeleportSpawn(getEmplacementTemporaire());
                joueur.sendMessage(mineralcontest.prefixPrive + "Veuillez vous rendre où les joueurs apparaitront lorsque la map sera chargée et cliquer où les gens doivent apparaitre.");
                break;

            case 10:
                mineralcontest.getPlayerGame(joueur).getArene().getDeathZone().setSpawnLocation(getEmplacementTemporaire());
                joueur.sendMessage(mineralcontest.prefixPrive + "Le setup est  presque terminé !");
                joueur.sendMessage(mineralcontest.prefixPrive + "Veuillez cliquer sur les blocs de la porte bleu");
                addDoors = true;
                break;

            case 11:
                joueur.sendMessage(mineralcontest.prefixPrive + "Le setup est  presque terminé !");
                joueur.sendMessage(mineralcontest.prefixPrive + "Veuillez cliquer sur les blocs de la porte rouge");
                addDoors = true;
                break;

            case 12:
                joueur.sendMessage(mineralcontest.prefixPrive + "Le setup est  presque terminé !");
                joueur.sendMessage(mineralcontest.prefixPrive + "Veuillez cliquer sur les blocs de la porte jaune");
                addDoors = true;
                break;

            case 13:
                joueur.sendMessage(mineralcontest.prefixPrive + "Le setup est terminé ! Vous pouvez faire /saveworld");
                mineralcontest.debug = false;
                addDoors = true;
                break;
        }

    }


    public static void addBlockToPorte(String team, Block b) {
        AutomaticDoors porte = null;
        if (team.equalsIgnoreCase("bleu")) {
            //porte = mineralcontest.getPlayerGame(joueur).getBlueHouse().getPorte();
        }

        if (team.equalsIgnoreCase("rouge")) {
            //porte = mineralcontest.getPlayerGame(joueur).getRedHouse().getPorte();
        }

        if (team.equalsIgnoreCase("jaune")) {
            //porte = mineralcontest.getPlayerGame(joueur).getYellowHouse().getPorte();
        }

        if (porte == null) return;

        if (!porte.addToDoor(b)) {
            Joueur.sendMessage(mineralcontest.prefixPrive + "Les portes de l'équipe " + team + " sont bien enregistrées. Faites /valider pour valider votre choix.");
        }
    }

    public static void terminer() {
        Setup.premierLancement = false;
    }

    public static void setEtape(int e) { etape=e;}
    public static int getEtape() { return etape; }
    public static Location getEmplacementTemporaire() { return emplacementTemporaire;}
    public static void setEmplacementTemporaire(Location e) { emplacementTemporaire = e; sendDetailsToPlayer();}

    public static void sendDetailsToPlayer() {
        Joueur.sendMessage(mineralcontest.prefixPrive + "Vous avez selectionner les coordonnées X:" + emplacementTemporaire.getX() + ", Y: " + emplacementTemporaire.getY() + ", Z: " + emplacementTemporaire.getZ());
        Joueur.sendMessage(mineralcontest.prefixPrive + "Faites /valider pour valider votre choix.");
    }

    public static void validerChoix() {

        if (emplacementTemporaire == null) {
            Joueur.sendMessage("Commande indisponible pour le moment, veuillez sélectionner un block");
            return;
        }

        etape++;
        Joueur.sendMessage(mineralcontest.prefixPrive + "Votre choix a été validé");
        emplacementTemporaire.setY(emplacementTemporaire.getY()+1);
        displayInfos(Joueur);
    }




}
