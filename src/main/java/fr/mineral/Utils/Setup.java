package fr.mineral.Utils;

import fr.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Setup {

    // Utile pour savoir si il faut relancer un setup ou non
    public static boolean premierLancement = true;
    public static int etape = 0;
    public static Setup instance;
    public static Location emplacementTemporaire;
    public static Player Joueur;
    public Setup() {
        Setup.instance = this;
    }

    public static void displayInfos(Player joueur) {

        switch(etape) {
            case 0:
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
                mineralcontest.plugin.getGame().getTeamRouge().setHouseLocation(getEmplacementTemporaire());
                joueur.sendMessage("======================");
                joueur.sendMessage(mineralcontest.prefixPrive + "Veuillez selectionner l'emplacement du coffre de l'équipe " +  ChatColor.RED + "ROUGE");
                break;

            case 3:
                mineralcontest.plugin.getGame().getTeamRouge().setCoffreEquipe(getEmplacementTemporaire());
                joueur.sendMessage(mineralcontest.prefixPrive + "Veuillez vous rendre dans la base " + ChatColor.YELLOW + "JAUNE");
                joueur.sendMessage(mineralcontest.prefixPrive + "Et effectuez un clic droit sur le bloc où les joueurs apparaitrons");
                break;

            case 4:
                mineralcontest.plugin.getGame().getTeamJaune().setHouseLocation(getEmplacementTemporaire());
                joueur.sendMessage(mineralcontest.prefixPrive + "Veuillez selectionner l'emplacement du coffre de l'équipe " +  ChatColor.YELLOW + "JAUNE");
                break;

            case 5:
                mineralcontest.plugin.getGame().getTeamJaune().setCoffreEquipe(getEmplacementTemporaire());
                joueur.sendMessage(mineralcontest.prefixPrive + "Veuillez vous rendre dans la base " + ChatColor.BLUE + "BLEU");
                joueur.sendMessage(mineralcontest.prefixPrive + "Et effectuez un clic droit sur le bloc où les joueurs apparaitrons");
                break;

            case 6:
                mineralcontest.plugin.getGame().getTeamBleu().setHouseLocation(getEmplacementTemporaire());
                joueur.sendMessage(mineralcontest.prefixPrive + "Veuillez selectionner l'emplacement du coffre de l'équipe " +  ChatColor.BLUE + "BLEU");
                break;

            case 7:
                mineralcontest.plugin.getGame().getTeamBleu().setCoffreEquipe(getEmplacementTemporaire());
                joueur.sendMessage(mineralcontest.prefixPrive + "Veuillez vous rendre dans l'arène et cliquer où le coffre d'arène doit apparaitre");
                break;

            case 8:
                mineralcontest.plugin.getGame().getArene().setCoffre(getEmplacementTemporaire());
                joueur.sendMessage(mineralcontest.prefixPrive + "Veuillez vous rendre dans l'arène et cliquer où le /arene teleportera les gens");
                break;

            case 9:
                mineralcontest.plugin.getGame().getArene().setTeleportSpawn(getEmplacementTemporaire());
                joueur.sendMessage(mineralcontest.prefixPrive + "Veuillez vous rendre où sera la Deathzone et cliquer où les gens doivent apparaitre.");
                break;

            case 10:
                mineralcontest.plugin.getGame().getArene().getDeathZone().setSpawnLocation(getEmplacementTemporaire());
                joueur.sendMessage(mineralcontest.prefixPrive + "Le setup est terminé !");
                joueur.sendMessage(mineralcontest.prefixPrive + "La partie peut être démarré grâce à la commande /start");
                premierLancement = false;
                break;



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
        etape++;
        Joueur.sendMessage(mineralcontest.prefixPrive + "Votre choix a été validé");
        emplacementTemporaire.setY(emplacementTemporaire.getY()+1);
        displayInfos(Joueur);
    }




}
