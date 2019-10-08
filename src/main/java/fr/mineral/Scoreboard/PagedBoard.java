package fr.mineral.Scoreboard;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

public class PagedBoard extends AutomaticBoard
{
    private HashMap<BoardPage, Integer> pages;
    private int count, currentPageId;
        
    public PagedBoard()
    {
        super(1);
        this.pages = new HashMap<>();
        this.count = 0;
    }
    
    public void addPage(BoardPage page, int ticks)
    {
        this.pages.put(page, ticks);
    }
    
    public void removePage(BoardPage page)
    {
        this.pages.remove(page);
    }
    
    @Override
    public void update(Player p)
    {
        getPage().update(p);
    }
    
    public BoardPage getPage()
    {
        return new ArrayList<>(pages.keySet()).get(currentPageId);
    }
    
    @Override
    public void run()
    {
        super.run();
        if(++this.count >= pages.get(getPage()))
        {
            this.count = 0;
            this.currentPageId++;
            
            if(this.currentPageId >= this.pages.size())
                this.currentPageId = 0;
        }
    }
}