package fr.mineral.Utils;

import fr.mineral.Core.Arena.Zones.DeathZone;
import fr.mineral.Scoreboard.ScoreboardUtil;
import fr.mineral.Core.Equipe;
import fr.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class PlayerUtils {

    public static boolean isPlayerInDeathZone(Player joueur) {
        for(CouplePlayer infoJoueur : mineralcontest.plugin.getGame().getArene().getDeathZone().getPlayers())
            if(infoJoueur.getJoueur().equals(joueur)) return true;

        return false;
    }

    public static void givePlayerBaseItems(Player joueur) {
            /*
            On donne au joueur:
                - Arc
                - 64 fleches
                - Epée en fer
                -
             */

        joueur.getInventory().addItem(new ItemStack(Material.BOW, 1));
        joueur.getInventory().addItem(new ItemStack(Material.ARROW, 64));
        joueur.getInventory().addItem(new ItemStack(Material.IRON_SWORD, 1));
        joueur.getInventory().addItem(new ItemStack(Material.COOKED_BEEF,30));

        // On protege le joueur
        ItemStack[] armure = new ItemStack[4];
        armure[0] = new ItemStack(Material.IRON_BOOTS, 1);
        armure[1] = new ItemStack(Material.IRON_LEGGINGS, 1);
        armure[2] = new ItemStack(Material.IRON_CHESTPLATE, 1);
        armure[3] = new ItemStack(Material.IRON_HELMET, 1);

        joueur.getInventory().setArmorContents(armure);
    }

    public static void drawPlayersHUD(boolean gameStarted, boolean gamePaused, boolean isPreGame, boolean voteMapEnabled) {
        Collection<? extends Player> onlinePlayers = mineralcontest.plugin.getServer().getOnlinePlayers();
        // Si la game n'a pas démarré
        for(Player online : onlinePlayers) {

            // Si on vote
            if(voteMapEnabled) {
                ScoreboardUtil.unrankedSidebarDisplay(online, ChatColor.GOLD + "Vote pour le biome à jouer", " " ,
                        "0 - Neige (" + mineralcontest.plugin.getGame().votemap.voteNeige + " vote(s))",
                        "1 - Desert (" + mineralcontest.plugin.getGame().votemap.voteDesert + " vote(s))",
                        "2 - Foret (" + mineralcontest.plugin.getGame().votemap.voteForet + " vote(s))",
                        "3 - Plaine (" + mineralcontest.plugin.getGame().votemap.votePlaine + " vote(s))",
                        "4 - Montagne (" + mineralcontest.plugin.getGame().votemap.voteMontagne + " vote(s))",
                        "5 - Marecage (" + mineralcontest.plugin.getGame().votemap.voteMarecage + " vote(s))");

            } else {
                Equipe team = mineralcontest.plugin.getGame().getPlayerTeam(online);

                if (!gameStarted || isPreGame) {
                    // Si le joueur n'a pas d'équipe
                    if (team == null) {
                        ScoreboardUtil.unrankedSidebarDisplay(online, "   " + ChatColor.GOLD + "Mineral" + ChatColor.BLUE + "Contest   ", " ", mineralcontest.GAME_WAITING_START, "", "Vous n'êtes pas dans une " + ChatColor.RED + "équipe");
                    } else {
                        // Le joueur a une équipe
                        ScoreboardUtil.unrankedSidebarDisplay(online, "   " + ChatColor.GOLD + "Mineral" + ChatColor.BLUE + "Contest   ", " ", mineralcontest.GAME_WAITING_START, "", team.getCouleur() + "Equipe " + team.getNomEquipe());
                    }

                } else {
                    // Si la game est en pause
                    if (gamePaused) {
                        // Pas de team
                        if (team == null) {
                            ScoreboardUtil.unrankedSidebarDisplay(online, "   " + ChatColor.GOLD + "Mineral" + ChatColor.BLUE + "Contest   ", " ", mineralcontest.GAME_PAUSED, "", "Vous n'êtes pas dans une " + ChatColor.RED + "équipe");
                        } else {
                            // Le joueur a une équipe
                            ScoreboardUtil.unrankedSidebarDisplay(online, "   " + ChatColor.GOLD + "Mineral" + ChatColor.BLUE + "Contest   ", " ", mineralcontest.GAME_PAUSED, "", team.getCouleur() + "Equipe " + team.getNomEquipe(), " ", "Score: " + team.getScore() + " points");
                        }
                    } else {
                        // Game pas en pause
                        // Si le joueur est mort
                        if(PlayerUtils.isPlayerInDeathZone(online)) {
                            ScoreboardUtil.unrankedSidebarDisplay(online, "   " + ChatColor.GOLD + "Mineral" + ChatColor.BLUE + "Contest   ", " ", "Temps restant: " + mineralcontest.plugin.getGame().getTempsRestant(), "", team.getCouleur() + "Equipe " + team.getNomEquipe(), " ", "Score: " + team.getScore() + " points", " ", ChatColor.GOLD + "Vous allez réapparaitre dans " + PlayerUtils.getDeathZoneTime(online) + " secondes");

                        } else {
                            // joueur pas mort
                            // Le joueur a une équipe
                            ScoreboardUtil.unrankedSidebarDisplay(online, "   " + ChatColor.GOLD + "Mineral" + ChatColor.BLUE + "Contest   ", " ", "Temps restant: " + mineralcontest.plugin.getGame().getTempsRestant(), "", team.getCouleur() + "Equipe " + team.getNomEquipe(), " ", "Score: " + team.getScore() + " points");

                        }
                    }
                }
            }


        }
    }

    // Seulement appelé lors du scoreboard
    private static int getDeathZoneTime(Player online) {
        DeathZone zone = mineralcontest.plugin.getGame().getArene().getDeathZone();
        for(CouplePlayer couple : zone.getPlayers())
            if(couple.getJoueur().equals(online))
                return couple.getValeur();

        return 0;
    }

    public static void resetPlayerDeathZone(Player joueur) {

        for(CouplePlayer infoJoueur : mineralcontest.plugin.getGame().getArene().getDeathZone().getPlayers()) {
            if(infoJoueur.getJoueur().equals(joueur))
                mineralcontest.plugin.getGame().getArene().getDeathZone().getPlayers().remove(infoJoueur);
        }


        // On ajoute un delai pour remettre en deathzone
        mineralcontest.plugin.getServer().getScheduler().runTaskLater(mineralcontest.plugin, new Runnable() {
            public void run() {
                mineralcontest.plugin.getGame().getArene().getDeathZone().add(joueur);
            }
        }, 20);
    }


}
