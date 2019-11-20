package fr.mineral.Utils.Player;

import fr.mineral.Core.Arena.Zones.DeathZone;
import fr.mineral.Translation.Lang;
import fr.mineral.Scoreboard.ScoreboardUtil;
import fr.mineral.Core.Equipe;
import fr.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class PlayerUtils {

    public static void setFirework(Player joueur, Color couleur) {
        Firework firework = (Firework) joueur.getWorld().spawn(joueur.getLocation(), Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        // On ajoute un effet
        fireworkMeta.addEffect(FireworkEffect.builder()
                .flicker(true)
                .trail(true)
                .withColor(couleur)
                .withFade(Color.WHITE)
                .build()

        );

        fireworkMeta.setPower(0);
        firework.setFireworkMeta(fireworkMeta);


    }

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
                ScoreboardUtil.unrankedSidebarDisplay(online, ChatColor.GOLD + (String) mineralcontest.LANG.get("vote_title"), " " ,
                        "0 - " + (String) mineralcontest.LANG.get("vote_snow") + " (" + mineralcontest.plugin.getGame().votemap.voteNeige + " "+ (String) mineralcontest.LANG.get("vote_count"),
                        "1 - "+ (String) mineralcontest.LANG.get("vote_desert") +" (" + mineralcontest.plugin.getGame().votemap.voteDesert + " "+ (String) mineralcontest.LANG.get("vote_count"),
                        "2 - "+ (String) mineralcontest.LANG.get("vote_forest") +" (" + mineralcontest.plugin.getGame().votemap.voteForet + (String) mineralcontest.LANG.get("vote_count"),
                        "3 - "+ (String) mineralcontest.LANG.get("vote_plain") +" (" + mineralcontest.plugin.getGame().votemap.votePlaine + (String) mineralcontest.LANG.get("vote_count"),
                        "4 - "+ (String) mineralcontest.LANG.get("vote_mountain") +" (" + mineralcontest.plugin.getGame().votemap.voteMontagne + (String) mineralcontest.LANG.get("vote_count"),
                        "5 - "+ (String) mineralcontest.LANG.get("vote_swamp") +" (" + mineralcontest.plugin.getGame().votemap.voteMarecage + (String) mineralcontest.LANG.get("vote_count"));

            } else {
                Equipe team = mineralcontest.plugin.getGame().getPlayerTeam(online);

                if (!gameStarted || isPreGame) {
                    // Si le joueur n'a pas d'équipe
                    if (team == null) {
                        ScoreboardUtil.unrankedSidebarDisplay(online, "   " + Lang.title.toString() + "   ", " ", Lang.hud_game_waiting_start.toString(), "", Lang.hud_you_are_not_in_team.toString());
                    } else {
                        // Le joueur a une équipe
                        ScoreboardUtil.unrankedSidebarDisplay(online, "   " + (String) mineralcontest.LANG.get("title") + "   ", " ", (String) mineralcontest.LANG.get("hud_game_waiting_start"), "", Lang.translate((String) mineralcontest.LANG.get("hud_team_name_no_score"), team));
                    }

                } else {
                    // Si la game est en pause
                    if (gamePaused) {
                        // Pas de team
                        if (team == null) {
                            ScoreboardUtil.unrankedSidebarDisplay(online,"   " + (String) mineralcontest.LANG.get("title") + "   ", " ", (String) mineralcontest.LANG.get("hud_game_paused"), "", "Vous n'êtes pas dans une " + ChatColor.RED + "équipe");
                        } else {
                            // Le joueur a une équipe
                            ScoreboardUtil.unrankedSidebarDisplay(online,"   " + (String) mineralcontest.LANG.get("title") + "   ", " ", (String) mineralcontest.LANG.get("hud_game_paused"), "", Lang.translate((String) mineralcontest.LANG.get("hud_team_score"), team));
                        }
                    } else {
                        // Game pas en pause
                        ScoreboardUtil.unrankedSidebarDisplay(online, "   " + (String) mineralcontest.LANG.get("title") + "   ", " ", (String) mineralcontest.LANG.get("hud_time_left"), "", Lang.translate((String) mineralcontest.LANG.get("hud_team_score"), team));
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


    // Appelé lorsqu'un joueur meurs et qu'il était dans la deathzone par exemple
    // On commence par le supprimé
    // Et on le remet
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


    public static void killPlayer(Player player) {

        // On récupère l'inventaire du joueur
        List<ItemStack> inventaire = new LinkedList<>();
        for(ItemStack item : player.getInventory().getContents())
            if(item != null)
                inventaire.add(item);

        // On définit un itérateur pour parcourir la liste des items à drop
        ListIterator<ItemStack> iterateur = inventaire.listIterator();

        while(iterateur.hasNext()) {
            ItemStack item = iterateur.next();

            // Liste des items à drop
            LinkedList<Material> item_a_drop = new LinkedList<Material>();
            item_a_drop.add(Material.IRON_INGOT);
            item_a_drop.add(Material.GOLD_INGOT);
            item_a_drop.add(Material.DIAMOND);
            item_a_drop.add(Material.EMERALD);

            // Si l'item actuelle n'est pas dans la liste, on le supprime de la liste de drop
            if(!item_a_drop.contains(item.getType())){
                iterateur.remove();
            }
        }

        // On l'ajoute à la deathzone
        mineralcontest.plugin.getGame().getArene().getDeathZone().add(player);

    }
}
