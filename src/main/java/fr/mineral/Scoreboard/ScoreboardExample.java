package fr.mineral.Scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardExample
{
	public void displayYourGameBoardWithoutScoreboardUtil(Player p)
	{
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		p.setScoreboard(scoreboard);
		
		Objective randomObjective = scoreboard.registerNewObjective("example", "dummy");
		randomObjective.setDisplayName("§c§lMy stats");
		randomObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		randomObjective.getScore(" ").setScore(15);
		randomObjective.getScore("Your kills: " + 30).setScore(14);
		randomObjective.getScore("Your deaths: " + 20).setScore(13);
		randomObjective.getScore("Your ration: " + (30d / 20d)).setScore(12);
		randomObjective.getScore("  ").setScore(11);
	}
	
	public void displayYourGameBoardWithScoreboardUtil(Player p)
	{
		ScoreboardUtil.unrankedSidebarDisplay(p, new String[]{"§c§lMy stats", 
																" ", 
																"Your kills: " + 30, 
																"Your deaths: " + 20, 
																"Your ration: " + (30d / 20d), 
																"  "});
	}
	
	
	
}
