package fr.mineral.Teams;

import fr.mineral.Core.Arena.Coffre;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.Door.AutomaticDoors;
import fr.mineral.mineralcontest;
import org.bukkit.*;
import org.bukkit.block.Block;
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

    public void setNomEquipe(String n ) { this.nomEquipe = n;}

    public void setCoffreEquipe(Location loc) {
        this.coffre = new Coffre();
        this.coffre.setPosition(loc);
        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_chest_added.toString(), this));

    }

    public Location getCoffreEquipeLocation() throws Exception {
        if(this.coffre.getPosition() == null)
            throw new Exception(Lang.translate(Lang.chest_not_defined.toString(), this));
        return coffre.getPosition();
    }

    public void spawnCoffreEquipe() throws Exception {
        Location loc = coffre.getPosition();
        Block block = loc.getBlock();
        loc.getBlock().setType(Material.CHEST);
     }


    public int getScore() { return this.score; }
    public void setScore(int score) {
        for(Player online : joueurs)
            online.sendMessage(mineralcontest.prefixPrive + Lang.translate(Lang.team_score_now.toString(), this));
        this.score = score;
    }


    // Retourne true si la team est pleine, false si non
    public boolean isTeamFull() {
        if(this.joueurs.size() >= mineralcontest.teamMaxPlayers)
            return true;
        return false;
    }

    public boolean addPlayerToTeam(Player p) {
        if(!isTeamFull()) {
            this.joueurs.add(p);

            p.sendMessage(mineralcontest.prefix + Lang.translate(Lang.team_welcome.toString(), this));

            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_player_joined.toString(), this));
            return true;
        }

        p.sendMessage(mineralcontest.prefixPrive + Lang.translate(Lang.team_is_full.toString(), this));
        return false;
    }

    public boolean removePlayer(Player p) {
        if(isPlayerInTeam(p)) {
            this.joueurs.remove(p);
            p.sendMessage(mineralcontest.prefix + Lang.translate(Lang.team_kicked.toString(), this));
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

    public void setHouseLocation(Location houseLocation){
        Bukkit.getServer().broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_house_location_added.toString(), this));
        this.houseLocation = houseLocation;
    }

    public Location getHouseLocation() {
        if(this.houseLocation == null)
            Bukkit.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_house_location_not_added.toString(), this));
        return houseLocation;
    }


    public AutomaticDoors getPorte() {
        return porte;
    }

    public Color toColor() {
        if(this.nomEquipe.equals(Lang.red_team.toString())) return Color.RED;
        if(this.nomEquipe.equals(Lang.yellow_team.toString())) return Color.YELLOW;
        if(this.nomEquipe.equals(Lang.blue_team.toString())) return Color.BLUE;

        return Color.WHITE;
    }
}
