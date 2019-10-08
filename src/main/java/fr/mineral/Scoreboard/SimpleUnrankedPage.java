package fr.mineral.Scoreboard;

import org.bukkit.entity.Player;

public class SimpleUnrankedPage implements BoardPage
{
    private String[] content;
        
    public SimpleUnrankedPage(String[] content)
    {
        this.content = content;
    }
    
    @Override
    public void update(Player p)
    {
        ScoreboardUtil.unrankedSidebarDisplay(p, content);   
    }
}