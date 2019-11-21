package fr.mineral.Core;

import fr.mineral.Core.Arena.Coffre;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.Door.AutomaticDoors;
import fr.mineral.mineralcontest;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class Equipe {
    private LinkedList<Player> joueurs;
    private String nomEquipe;
    private ChatColor couleur;
    private Location houseLocation;
    private Coffre coffre;
    private int score = 0;
    private AutomaticDoors porte;


    public Equipe(String nom, ChatColor c) {
        this.joueurs = new LinkedList<Player>();
        this.nomEquipe = nom;
        this.couleur = c;
        this.porte = new AutomaticDoors(this);
    }

    public void setNomEquipe(String nom) {
        this.nomEquipe = nom;
    }

    public void setCoffreEquipe(Location loc) {
        this.coffre = new Coffre();
        this.coffre.setPosition(loc);

        // On reset le coffre actuel si il existe déjà
        if(loc.getBlock().getType().toString().equalsIgnoreCase("chest")) {
            Chest coffre = (Chest) loc.getBlock().getState();
            coffre.getInventory().clear();
        }

        mineralcontest.plugin.getLogger().info(mineralcontest.prefixGlobal + Lang.translate(Lang.team_chest_added.toString(), this));

    }

    public Location getCoffreEquipeLocation() throws Exception {
        if(this.coffre.getPosition() == null)
            throw new Exception("TeamChest " + nomEquipe + " NotDefined");
        return coffre.getPosition();
    }

    public void spawnCoffreEquipe() throws Exception {
        Location loc = coffre.getPosition();
        Block block = loc.getBlock();
        loc.getBlock().setType(Material.CHEST);

        if(loc.getBlock() instanceof Chest) {
            ((Chest)block).getInventory().clear();
        }
     }


    public int getScore() { return this.score; }
    public void setScore(int score) {
        for(Player online : joueurs)
            online.sendMessage(mineralcontest.prefixPrive + " " +  Lang.translate(Lang.team_score_now.toString(), this));
        this.score = score;
    }


    // Retourne true si la team est pleine, false si non
    public boolean isTeamFull() {
        if(this.joueurs.size() >= mineralcontest.teamMaxPlayers)
            return true;
        return false;
    }

    public boolean addPlayerToTeam(Player p) throws Exception {
        if(!isTeamFull()) {
            this.joueurs.add(p);

            p.sendMessage(mineralcontest.prefix + " " + Lang.translate(Lang.team_welcome.toString(), this));

            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + " " +  Lang.translate(Lang.team_player_joined.toString(), this, p));
            return true;
        }
        throw new Exception("L'equipe est pleine.");
    }

    public boolean removePlayer(Player p) {
        if(isPlayerInTeam(p)) {
            this.joueurs.remove(p);
            p.sendMessage(mineralcontest.prefix + " " + Lang.team_kicked.toString());
            return true;
        }
        return false;
    }

    public boolean isPlayerInTeam(Player p) {
        return this.joueurs.contains(p);
    }

    public LinkedList<Player> getJoueurs() {
        return this.joueurs;
    }

    public String toString() {
        String joueurs = "Team " + this.getCouleur() + this.nomEquipe + ChatColor.WHITE + ": ";
        for(int i = 0; i < this.joueurs.size(); i++) {
            joueurs += this.joueurs.get(i).getDisplayName() + " ";
        }
        return joueurs;
    }

    public String getNomEquipe() {
        return this.nomEquipe;
    }


    public ChatColor getCouleur() {
        return this.couleur;
    }

    public Color toColor() {
        switch(couleur) {
            case RED:
                return Color.RED;
            case BLUE:
                return Color.BLUE;
            case YELLOW:
                return Color.YELLOW;
        }

        return Color.WHITE;
    }

    public void setHouseLocation(Location houseLocation){
        mineralcontest.plugin.getLogger().info(Lang.translate(Lang.team_house_location_added.toString(), this));
        this.houseLocation = houseLocation;
    }

    public Location getHouseLocation() throws Exception {
        if(this.houseLocation == null)
            throw new Exception(Lang.translate(Lang.team_house_location_not_added.toString(), this));
        return houseLocation;
    }


    public AutomaticDoors getPorte() {
        return porte;
    }
}
