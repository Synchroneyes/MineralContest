package fr.synchroneyes.special_events.halloween2022;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.mineralcontest;
import fr.synchroneyes.special_events.SpecialEvent;
import fr.synchroneyes.special_events.halloween2022.events.ArenaEvent;
import fr.synchroneyes.special_events.halloween2022.events.PlayerDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class HalloweenEvent extends SpecialEvent {
    @Override
    public String getEventName() {
        return ChatColor.RED + "Halloween 2022" + ChatColor.RESET;
    }

    @Override
    public String getDescription() {
        return "Cet évenement est prévu spécialement pour Halloween. Il sera actif entre le 28 octobre et le 2 novembre.";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void startEvent(Game partie) {
        // register events
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDeathEvent(), mineralcontest.plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaEvent(), mineralcontest.plugin);

        // register boss


        // plugin logic
    }
}
