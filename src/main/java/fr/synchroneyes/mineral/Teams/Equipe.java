package fr.synchroneyes.mineral.Teams;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.House;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.Log.GameLogger;
import fr.synchroneyes.mineral.Utils.Log.Log;
import fr.synchroneyes.mineral.Utils.Player.PlayerUtils;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;

public class Equipe implements Comparable<Equipe> {
    private LinkedList<Player> joueurs;
    private String nomEquipe;
    private ChatColor couleur;
    private int score = 0;
    private int penalty = 0;
    private House maison;

    private Groupe groupe;


    public Equipe(String nom, ChatColor c, Groupe g, House maison) {
        this.joueurs = new LinkedList<Player>();
        this.nomEquipe = nom;
        this.couleur = c;
        this.groupe = g;
        this.maison = maison;
    }


    public House getMaison() {
        return maison;
    }

    public void clear() {
        this.joueurs.clear();
        this.score = 0;
        this.penalty = 0;
    }

    public int getPenalty() {
        return this.penalty;
    }

    public void updateScore() throws Exception {

        int _score = 0;
        Block block_coffre = maison.getCoffreEquipeLocation().getBlock();
        Chest openedChest = ((Chest) block_coffre.getState());

        ItemStack[] items = openedChest.getInventory().getContents();
        for (ItemStack item : items) {

            if (item != null) {
                if (item.isSimilar(new ItemStack(Material.IRON_INGOT, 1))) {
                    _score += groupe.getParametresPartie().getCVAR("SCORE_IRON").getValeurNumerique() * item.getAmount();
                }

                if (item.isSimilar(new ItemStack(Material.GOLD_INGOT, 1))) {
                    _score += groupe.getParametresPartie().getCVAR("SCORE_GOLD").getValeurNumerique() * item.getAmount();
                }

                if (item.isSimilar(new ItemStack(Material.DIAMOND, 1))) {
                    _score += groupe.getParametresPartie().getCVAR("SCORE_DIAMOND").getValeurNumerique() * item.getAmount();
                }

                if (item.isSimilar(new ItemStack(Material.EMERALD, 1))) {
                    _score += groupe.getParametresPartie().getCVAR("SCORE_EMERALD").getValeurNumerique() * item.getAmount();
                }
            }
        }

        setScore(_score);
    }

    public void sendMessage(String message, Player sender) {
        if (this.joueurs.contains(sender)) {
            for (Player member : joueurs)
                member.sendMessage(mineralcontest.prefixTeamChat + sender.getDisplayName() + ": " + message);

        }
    }

    public void addPenalty(int penalty) {
        this.penalty += penalty;
        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_got_penality.toString(), this), groupe);
    }

    public void resetPenalty() {
        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_got_penality_reseted.toString(), this), groupe);

        this.penalty = 0;
    }

    public void setNomEquipe(String n) {
        this.nomEquipe = n;
    }

    public int getScore() {
        return this.score - this.penalty;
    }
    public void setScore(int score) {
        this.score = score;
        GameLogger.addLog(new Log("TeamChestScoreUpdated", "The team " + getNomEquipe() + " score got updated to " + score + "", "ChestEvent"));

        for (Player online : joueurs)
            online.sendMessage(mineralcontest.prefixPrive + Lang.translate(Lang.team_score_now.toString(), this));
    }


    public boolean addPlayerToTeam(Player p, boolean switched, boolean teleportToBase) throws Exception {


        Game partie = mineralcontest.getPlayerGame(p);

        if (partie != null) {
            Equipe team = mineralcontest.getPlayerGame(p).getPlayerTeam(p);
            if (team != null) team.removePlayer(p);
            if (mineralcontest.getPlayerGame(p).isReferee(p)) mineralcontest.getPlayerGame(p).removeReferee(p);
        }


        this.joueurs.add(p);

        p.setGameMode(GameMode.SURVIVAL);


        if (PlayerUtils.getPlayerItemsCountInInventory(p) == 0 && mineralcontest.getPlayerGame(p).isGameInitialized) {
            //PlayerBaseItem.givePlayerItems(p, PlayerBaseItem.onFirstSpawnName);
            if (teleportToBase)
                PlayerUtils.teleportPlayer(p, mineralcontest.getPlayerGroupe(p).getMonde(), mineralcontest.getPlayerGame(p).getPlayerHouse(p).getHouseLocation());
        }

        p.sendMessage(mineralcontest.prefix + Lang.translate(Lang.team_welcome.toString(), this));


        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_player_joined.toString(), this, p), groupe);

        return true;

    }

    public boolean removePlayer(Player p) {
        if (isPlayerInTeam(p)) {
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
        for (int i = 0; i < this.joueurs.size(); i++) {
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
        if (this.nomEquipe.equals(Lang.red_team.toString())) return Color.RED;
        if (this.nomEquipe.equals(Lang.yellow_team.toString())) return Color.YELLOW;
        if (this.nomEquipe.equals(Lang.blue_team.toString())) return Color.BLUE;

        return Color.WHITE;
    }

    @Override
    public int compareTo(Equipe equipe) {
        return (int) (this.getScore() - equipe.getScore());
    }
}
