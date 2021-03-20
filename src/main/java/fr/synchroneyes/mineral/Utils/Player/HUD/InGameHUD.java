package fr.synchroneyes.mineral.Utils.Player.HUD;

import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class InGameHUD extends PlayerHUD{

    private String teamName;

    private String teamScore;

    private String playerLocation;

    private String timeLeft;

    public InGameHUD(MCPlayer player) {
        super(player);


        update();
    }

    @Override
    public void update() {
        MCPlayer player = getPlayer();

        Equipe equipe = player.getEquipe();

        if(equipe != null){
            setHudColor(equipe.getCouleur());
            teamName = equipe.getNomEquipe();

            if(equipe.getScore() > 0) teamScore = ChatColor.GREEN + "" + equipe.getScore();
            else teamScore = ChatColor.RED + "" + equipe.getScore();

            timeLeft = Lang.translate(Lang.hud_time_left.toString(), player.getGroupe().getGame());

            Location location = player.getJoueur().getLocation();
            playerLocation = location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ();

        }
    }

    @Override
    public void draw() {
        Scoreboard playerScoreboard = getPlayer().getJoueur().getScoreboard();




    }


}
