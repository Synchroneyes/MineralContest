package fr.synchroneyes.mineral.Utils.Player.HUD;

import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.ChatColor;

public abstract class PlayerHUD {

    private String PluginVersion;

    private ChatColor hudColor;

    private String HUDTitle;

    private MCPlayer player;

    public PlayerHUD(MCPlayer mcPlayer) {
        this.player = mcPlayer;

        HUDTitle = Lang.title.toString();
        PluginVersion = mineralcontest.plugin.getDescription().getVersion();
    }


    /**
     * Méthode permettant de mettre à jour un HUD
     */
    public abstract void update();

    /**
     * Méthode permettant d'afficher le HUD à un joueur
     */
    public abstract void draw();


    public String getPluginVersion() {
        return PluginVersion;
    }

    public ChatColor getHudColor() {
        return hudColor;
    }

    public void setHudColor(ChatColor hudColor) {
        this.hudColor = hudColor;
    }

    public String getHUDTitle() {
        return HUDTitle;
    }


    public MCPlayer getPlayer() {
        return player;
    }

}
