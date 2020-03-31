package fr.mineral.Scoreboard;

import org.bukkit.entity.Player;

import java.util.Collection;

public abstract class Board
{
	public abstract void startDisplay(Player p);
	public abstract void stopDisplay(Player p);
	
	public void startDisplay(Collection<Player> players)
	{
		for(Player p : players)
			this.startDisplay(p);
	}
}
