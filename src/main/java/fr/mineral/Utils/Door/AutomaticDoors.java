package fr.mineral.Utils.Door;

import fr.mineral.Teams.Equipe;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.LinkedList;


/*
    On ne traite que des "portes" carrées

    TODO: sauvegarder lla couleur des portes
 */
public class AutomaticDoors {

    // Le bloc du centre

    // Une porte contient 9 blocs
    public static final int maxDoorSize = 9;
    Equipe proprietaire;

    // On a une Liste de block
    private LinkedList<DisplayBlock> porte;

    LinkedList<Player> playerNearDoor;

    private boolean estOuvert = false;


    // Prend un bloc, et un rayon
    public AutomaticDoors(Equipe equipe) {
        this.porte = new LinkedList<DisplayBlock>();
        this.proprietaire = equipe;
        this.playerNearDoor = new LinkedList<Player>();
    }

    public void clear() {
        this.forceCloseDoor();
        this.porte.clear();
        this.playerNearDoor.clear();
    }

    // On initialise la pile de blocs
    public boolean addToDoor(Block b) {
        // Pour chaque bloc de la porte
        boolean ajouter = false;
        if(porte.size() == 0) {
            porte.add(new DisplayBlock(b));
            if (mineralcontest.debug)
                mineralcontest.broadcastMessage(ChatColor.GREEN + "+ Le bloc selectionné a été ajouté");
        } else {
            for(DisplayBlock db : porte) {
                if(db.getBlock().equals(b)) {
                    porte.remove(db);
                    if (mineralcontest.debug)
                        mineralcontest.broadcastMessage(ChatColor.YELLOW + "- Le bloc selectionné a été supprimé");
                } else {
                    if(porte.size() >= maxDoorSize) {
                        if (mineralcontest.debug) mineralcontest.broadcastMessage(ChatColor.RED + "# - Porte pleine");
                        return false;
                    } else {
                        ajouter = true;
                    }

                }
            }
            if(ajouter) {
                porte.add(new DisplayBlock(b));
                if (mineralcontest.debug)
                    mineralcontest.broadcastMessage(ChatColor.GREEN + "+ Le bloc selectionné a été ajouté à la porte");
                return true;
            }

            return true;
        }

        return true;


    }

    // On initialise la pile de blocs
    public void openDoor() {
        // Pour chaque bloc de la porte
        if(playerNearDoor.size() > 0) {
            // Pour chaque bloc de la porte

            for(DisplayBlock db : porte) {
                db.hide();
            }
            this.estOuvert = true;
        }
    }


    public void forceCloseDoor() {
        playerNearDoor.clear();
        closeDoor();
    }

    public void closeDoor() {

        // Si on a personne pres de la porte
        if(playerNearDoor.size() == 0) {
            // Pour chaque bloc de la porte

            for(DisplayBlock db : porte) {
                db.display();
            }
            this.estOuvert = false;
        }

    }

    public LinkedList<DisplayBlock> getPorte() {
        return porte;
    }

    public Location getMiddleBlockLocation() {
        return this.porte.get(4).getBlock().getLocation();
    }

    public boolean isOpened() { return estOuvert; }


    public boolean isSet() {
        return (porte.size() == maxDoorSize);
    }

    public void playerIsNearDoor(Player joueur) {
        if(!this.playerNearDoor.contains(joueur) && !mineralcontest.plugin.getGame().getArene().getDeathZone().isPlayerDead(joueur)) {
            this.playerNearDoor.add(joueur);
            openDoor();
        }
    }

    public void playerIsNotNearDoor(Player joueur) {
        if(this.playerNearDoor.contains(joueur)) {
            this.playerNearDoor.remove(joueur);
            closeDoor();
        }
    }
}
