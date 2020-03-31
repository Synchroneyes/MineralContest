package fr.mineral.Teams;

import fr.mineral.Core.GameSettingsCvar;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.Player.PlayerBaseItem;
import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class Equipe {
    private LinkedList<Player> joueurs;
    private String nomEquipe;
    private ChatColor couleur;
    private int score = 0;
    private int penalty = 0;


    public Equipe(String nom, ChatColor c) {
        this.joueurs = new LinkedList<Player>();
        this.nomEquipe = nom;
        this.couleur = c;
    }

    public void clear() {
        this.joueurs.clear();
        this.score = 0;
        this.penalty = 0;
    }

    public int getPenalty() { return this.penalty; }

    public void updateScore() {

    }

    public void sendMessage(String message, Player sender) {
        if(this.joueurs.contains(sender)) {
            for(Player member : joueurs)
                member.sendMessage(mineralcontest.prefixTeamChat + sender.getDisplayName() + ": " + message);

        }
    }

    public void addPenalty(int penalty){
        this.penalty += penalty;
        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_got_penality.toString(), this));
    }

    public void resetPenalty() {
        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_got_penality_reseted.toString(), this));

        this.penalty = 0;
    }

    public void setNomEquipe(String n ) { this.nomEquipe = n;}

    public int getScore() { return this.score - this.penalty; }
    public void setScore(int score) {
        this.score = score;
        for(Player online : joueurs)
            online.sendMessage(mineralcontest.prefixPrive + Lang.translate(Lang.team_score_now.toString(), this));
    }


    // Retourne true si la team est pleine, false si non
    public boolean isTeamFull() {
        if(this.joueurs.size() >= (int) GameSettingsCvar.mp_team_max_player.getValue())
            return true;
        return false;
    }

    public boolean addPlayerToTeam(Player p, boolean switched) throws Exception {
        if(!isTeamFull() || switched || mineralcontest.plugin.getGame().isReferee(p)) {

            Equipe team = mineralcontest.plugin.getGame().getPlayerTeam(p);
            if(team != null) team.removePlayer(p);
            if(mineralcontest.plugin.getGame().isReferee(p)) mineralcontest.plugin.getGame().removeReferee(p);

            this.joueurs.add(p);

            p.setGameMode(GameMode.SURVIVAL);


            if(PlayerUtils.getPlayerItemsCountInInventory(p) == 0 && mineralcontest.plugin.getGame().isGameInitialized) {
                PlayerBaseItem.givePlayerItems(p, PlayerBaseItem.onFirstSpawnName);
                p.teleport(mineralcontest.plugin.getGame().getPlayerHouse(p).getHouseLocation());
            }

            p.sendMessage(mineralcontest.prefix + Lang.translate(Lang.team_welcome.toString(), this));

            mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_player_joined.toString(), this, p));
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

    public Color toColor() {
        if(this.nomEquipe.equals(Lang.red_team.toString())) return Color.RED;
        if(this.nomEquipe.equals(Lang.yellow_team.toString())) return Color.YELLOW;
        if(this.nomEquipe.equals(Lang.blue_team.toString())) return Color.BLUE;

        return Color.WHITE;
    }
}
