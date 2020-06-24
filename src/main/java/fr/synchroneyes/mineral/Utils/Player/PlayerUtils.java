package fr.synchroneyes.mineral.Utils.Player;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.groups.GroupeExtension;
import fr.synchroneyes.groups.Utils.Etats;
import fr.synchroneyes.mineral.Core.Arena.Zones.DeathZone;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.Referee.Referee;
import fr.synchroneyes.mineral.Settings.GameSettings;
import fr.synchroneyes.mineral.Core.House;
import fr.synchroneyes.mineral.Scoreboard.ScoreboardUtil;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.ErrorReporting.Error;
import fr.synchroneyes.mineral.Utils.Log.GameLogger;
import fr.synchroneyes.mineral.Utils.Log.Log;
import fr.synchroneyes.mineral.Utils.Radius;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class PlayerUtils {

    public static int velocity_mult = 2;

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

    public static void teleportPlayer(Player p, double x, double y, double z) {
        World world = getPluginWorld();

        Location loc = new Location(world, x, y, z);
        p.teleport(loc);

    }

    public static void teleportPlayer(Player p, Location loc) {
        World world = getPluginWorld();
        Location new_loc = new Location(world, loc.getX(), loc.getY(), loc.getZ());
        p.teleport(new_loc);

    }

    public static void teleportPlayer(Player p, World w, Location loc) {
        Location new_loc = new Location(w, loc.getX(), loc.getY(), loc.getZ());
        p.teleport(new_loc);

    }

    public static World getPluginWorld() {

        String world_name = mineralcontest.getPluginConfigValue("world_name").toString();
        World world = Bukkit.getWorld(world_name);

        mineralcontest.plugin.pluginWorld = world;
        if (world != null) mineralcontest.plugin.defaultSpawn = world.getSpawnLocation();
        return world;
    }


    public static void runScoreboardManager() {
        Bukkit.getServer().getScheduler().runTaskTimer(mineralcontest.plugin, PlayerUtils::scoreboardManager, 0, 20);
    }

    private static void scoreboardManager() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (mineralcontest.isInAMineralContestWorld(onlinePlayer)) {
                Game playerGame = mineralcontest.getPlayerGame(onlinePlayer);
                if (playerGame == null || playerGame.groupe.getEtatPartie().equals(Etats.EN_ATTENTE)) {
                    onlinePlayer.setPlayerListName(onlinePlayer.getDisplayName());
                    continue;
                }
                Equipe playerTeam = mineralcontest.getPlayerGame(onlinePlayer).getPlayerTeam(onlinePlayer);
                StringBuilder playerPrefix = new StringBuilder();
                Game game = mineralcontest.getPlayerGame(onlinePlayer);

                if (game.getArene().getDeathZone().isPlayerDead(onlinePlayer)) {
                    playerPrefix.append(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "");
                } else {
                    if (game.isReferee(onlinePlayer)) playerPrefix.append(ChatColor.GOLD + "[☆] " + ChatColor.WHITE);
                    if (playerTeam != null) playerPrefix.append(playerTeam.getCouleur() + "██ " + ChatColor.WHITE);
                }

                onlinePlayer.setPlayerListName(playerPrefix.toString() + onlinePlayer.getDisplayName());
            }

        }
    }

    public static boolean isPlayerInDeathZone(Player joueur) {
        for (CouplePlayer infoJoueur : mineralcontest.getPlayerGame(joueur).getArene().getDeathZone().getPlayers())
            if (infoJoueur.getJoueur().equals(joueur)) return true;

        return false;
    }

    public static boolean isPlayerInHisBase(Player p) throws Exception {
        Game game = mineralcontest.getPlayerGame(p);
        if (game == null) return false;
        House playerHouse = game.getPlayerHouse(p);
        int house_radius = 7;
        if (playerHouse == null) return false;
        return (Radius.isBlockInRadius(playerHouse.getHouseLocation(), p.getLocation(), house_radius));
    }

    public static int getPlayerItemsCountInInventory(Player p) {
        int item_count = 0;
        for (ItemStack item : p.getInventory().getContents())
            if (item != null && item.getType().equals(Material.AIR))
                item_count++;

        return item_count;
    }


    public static void drawPlayersHUD() {

        if (mineralcontest.plugin.pluginWorld == null) {
            return;
        }
        if (Bukkit.getOnlinePlayers().size() == 0) {
            return;
        }

        if (mineralcontest.plugin.mapBuilderInstance != null && mineralcontest.plugin.mapBuilderInstance.isBuilderModeEnabled) {
            mineralcontest.plugin.mapBuilderInstance.sendPlayersHUD();
            return;
        }


        ArrayList<String> elementsADisplay = new ArrayList<>();
        // Si la game n'a pas démarré
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!mineralcontest.isInAMineralContestWorld(online)) continue;
            Game playergame = mineralcontest.getPlayerGame(online);

            if (GroupeExtension.enabled) {
                if (mineralcontest.isInAMineralContestWorld(online)) {

                /*
                        2 cas, le joueur à un groupe, il n'en a pas
                 */
                    Groupe playerGroup = mineralcontest.getPlayerGroupe(online);
                    elementsADisplay.add("=========");
                    if (playerGroup == null) {
                        // Le joueur ne possède pas de groupe !
                        elementsADisplay.add(ChatColor.RED + "Vous n'avez pas de groupe!");
                        elementsADisplay.add("Vous pouvez en créer un avec /creergroupe <nom>");
                        elementsADisplay.add("Ou en rejoindre un avec /joingroupe <nom>");
                        elementsADisplay.add("========= ");
                    } else {

                        if (playerGroup.getEtatPartie().equals(Etats.EN_ATTENTE)) {
                            // Le joueur possède un groupe !
                            elementsADisplay.add(ChatColor.GOLD + "Groupe: " + ChatColor.WHITE + playerGroup.getNom());
                            elementsADisplay.add(ChatColor.GOLD + "Joueurs: " + playerGroup.getPlayerCount() + "");
                            elementsADisplay.add(ChatColor.GOLD + "Etat: " + ChatColor.RED + playerGroup.getEtatPartie().getNom());
                            elementsADisplay.add("========= ");
                            elementsADisplay.add("Admins: ");
                            for (Player admin : playerGroup.getAdmins()) {
                                elementsADisplay.add(admin.getDisplayName());
                            }
                        }
                        if (playerGroup.getEtatPartie().equals(Etats.VOTE_EN_COURS)) {
                            elementsADisplay.clear();
                            ArrayList<String> maps = playerGroup.getMapVote().getMaps();
                            int index = 0;
                            for (String map : maps) {
                                elementsADisplay.add(index + " - " + map + " " + playerGroup.getMapVote().getMapVoteCount(map) + " " + Lang.vote_count.toString());
                                index++;
                            }
                        }

                        if (playerGroup.getEtatPartie().equals(Etats.ATTENTE_DEBUT_PARTIE) ||
                                playerGroup.getEtatPartie().equals(Etats.PREGAME) ||
                                playerGroup.getEtatPartie().equals(Etats.GAME_EN_COURS)) {
                            sendPlayerInGameHUD(online);
                            continue;
                        }
                    }

                    String[] elements = new String[elementsADisplay.size() + 1];
                    int index = 1;
                    for (String element : elementsADisplay) {
                        elements[index] = element;
                        index++;
                    }

                    elements[0] = Lang.title.toString() + " - " + ChatColor.GREEN + mineralcontest.plugin.getDescription().getVersion();

                    ScoreboardUtil.unrankedSidebarDisplay(online, elements);

                    elementsADisplay.clear();
                }
            }
        }
    }


    /**
     * Envoie du HUD à un joueur lorsqu'il est dans une partie
     */
    private static void sendPlayerInGameHUD(Player player) {

        Groupe playerGroup = mineralcontest.getPlayerGroupe(player);
        if (playerGroup == null) {
            Bukkit.getLogger().info("Player group is null, sendPlayerInGameHUD, PlayerUtils");
            return;
        }

        Equipe playerTeam = playerGroup.getPlayerTeam(player);
        Game playerGame = playerGroup.getGame();
        ArrayList<String> elementsADisplay = new ArrayList<>();


        //elementsADisplay.add(Lang.translate(Lang.hud_map_name.toString(), playerGroup));
        //elementsADisplay.add("                      ");

        if (playerGame.isPreGame()) {
            elementsADisplay.add(Lang.translate(Lang.hud_game_starting.toString(), playerGame));
            getPlayerTeamHUDContent(player, playerTeam, playerGame, elementsADisplay);
        } else if (playerGame.isGameStarted()) {
            elementsADisplay.add(Lang.translate(Lang.hud_time_left.toString(), playerGame));
            elementsADisplay.add("                  ");

            // Player team null = arbitre
            getPlayerTeamHUDContent(player, playerTeam, playerGame, elementsADisplay);


        } else if (playerGame.isGamePaused()) {
            elementsADisplay.add(Lang.hud_game_paused.toString());
            elementsADisplay.add("        ");
            // Player team null = arbitre
            getPlayerTeamHUDContent(player, playerTeam, playerGame, elementsADisplay);

            // game en attente
        } else {
            elementsADisplay.add(Lang.hud_game_waiting_start.toString());
            elementsADisplay.add("                 ");
            if (playerTeam == null) {
                elementsADisplay.add(Lang.hud_you_are_not_in_team.toString());
            } else {
                elementsADisplay.add(Lang.translate(Lang.hud_team_name_no_score.toString(), playerTeam));
            }
        }


        String[] elements = new String[elementsADisplay.size() + 1];
        int index = 1;
        for (String element : elementsADisplay) {
            elements[index] = element;
            index++;
        }

        elements[0] = Lang.title.toString() + " - " + ChatColor.GREEN + mineralcontest.plugin.getDescription().getVersion();

        ScoreboardUtil.unrankedSidebarDisplay(player, elements);

    }

    private static boolean getPlayerTeamHUDContent(Player player, Equipe playerTeam, Game playerGame, ArrayList<String> elementsADisplay) {
        if (playerTeam == null) {
            if (playerGame.isReferee(player)) {
                int addedTeams = 0;
                elementsADisplay.add(Lang.hud_referee_text.toString());
                for (House equipes : playerGame.getHouses()) {
                    if (!equipes.getTeam().getJoueurs().isEmpty()) {
                        elementsADisplay.add(Lang.translate(Lang.hud_team_name_score.toString(), equipes.getTeam()));
                        addedTeams++;
                    }
                }

                if (addedTeams == 0) {
                    elementsADisplay.add(Lang.error_no_teams_with_player.toString());
                }


            } else {
                Bukkit.getLogger().severe("Player have no team and is not referee !!!!");
                return true;
            }
        } else {
            // Joueur dans une équipe
            elementsADisplay.add(Lang.translate(Lang.hud_team_name_score.toString(), playerTeam));
        }
        return false;
    }

    // Seulement appelé lors du scoreboard
    private static int getDeathZoneTime(Player online) {
        DeathZone zone = mineralcontest.getPlayerGame(online).getArene().getDeathZone();
        for (CouplePlayer couple : zone.getPlayers())
            if (couple.getJoueur().equals(online))
                return couple.getValeur();

        return 0;
    }


    // Appelé lorsqu'un joueur meurs et qu'il était dans la deathzone par exemple
    // On commence par le supprimé
    // Et on le remet
    public static void resetPlayerDeathZone(Player joueur) {

        for (CouplePlayer infoJoueur : mineralcontest.getPlayerGame(joueur).getArene().getDeathZone().getPlayers()) {
            if (infoJoueur.getJoueur().equals(joueur))
                mineralcontest.getPlayerGame(joueur).getArene().getDeathZone().getPlayers().remove(infoJoueur);
        }


        // On ajoute un delai pour remettre en deathzone
        mineralcontest.plugin.getServer().getScheduler().runTaskLater(mineralcontest.plugin, new Runnable() {
            public void run() {
                try {
                    mineralcontest.getPlayerGame(joueur).getArene().getDeathZone().add(joueur);
                } catch (Exception e) {
                    e.printStackTrace();
                    Error.Report(e, mineralcontest.getPlayerGame(joueur));
                }
            }
        }, 20);
    }

    public static void clearPlayer(Player joueur) {
        joueur.getInventory().clear();
        Bukkit.getLogger().info("Player " + joueur.getDisplayName() + " has been cleared");
    }


    public static void equipReferee(Player joueur) {

        if (mineralcontest.getPlayerGame(joueur).isReferee(joueur)) {
            joueur.getInventory().setItemInMainHand(Referee.getRefereeItem());
            joueur.setGameMode(GameMode.CREATIVE);
        }

        /*if (mineralcontest.getPlayerGame(joueur).isReferee(joueur)) {
            joueur.getInventory().setItemInMainHand(new RefereeItem().getItem());
            joueur.setGameMode(GameMode.CREATIVE);
        }*/
    }


    public static boolean isPlayerHidden(Player p) {
        for (Player online : Bukkit.getOnlinePlayers())
            if (!online.canSee(p)) return true;
        return false;
    }


    public static void killPlayer(Player player) {

        // On récupère l'inventaire du joueur
        List<ItemStack> inventaire = new LinkedList<>();
        for (ItemStack item : player.getInventory().getContents())
            if (item != null)
                inventaire.add(item);

        // On définit un itérateur pour parcourir la liste des items à drop
        ListIterator<ItemStack> iterateur = inventaire.listIterator();

        while (iterateur.hasNext()) {
            ItemStack item = iterateur.next();

            // Liste des items à drop
            LinkedList<Material> item_a_drop = new LinkedList<Material>();
            item_a_drop.add(Material.IRON_INGOT);
            item_a_drop.add(Material.GOLD_INGOT);
            item_a_drop.add(Material.DIAMOND);
            item_a_drop.add(Material.EMERALD);
            item_a_drop.add(Material.IRON_ORE);
            item_a_drop.add(Material.GOLD_ORE);
            item_a_drop.add(Material.DIAMOND_ORE);
            item_a_drop.add(Material.EMERALD_ORE);

            Groupe playerGroup = mineralcontest.getPlayerGroupe(player);
            GameSettings settings = playerGroup.getParametresPartie();
            int mp_enable_item_drop = 0;
            try {
                mp_enable_item_drop = settings.getCVAR("mp_enable_item_drop").getValeurNumerique();
            } catch (Exception e) {
                e.printStackTrace();
                Error.Report(e, playerGroup.getGame());
            }

            // DROP ONLY INGOTS
            if (mp_enable_item_drop == 1) {
                // IF ITEM IS NOT TO DROP
                if (!item_a_drop.contains(item.getType())) {
                    iterateur.remove();
                }
            }

            // IF NO DROP AT ALL
            if (mp_enable_item_drop == 0) {
                iterateur.remove();
            }
        }


        // Drop items
        for (ItemStack item : inventaire) {
            if (!item.getType().equals(Material.AIR))
                player.getWorld().dropItemNaturally(player.getLocation(), item);
        }

        // On l'ajoute à la deathzone
        try {
            mineralcontest.getPlayerGame(player).getArene().getDeathZone().add(player);
            GameLogger.addLog(new Log("PlayerUtils_Dead", "Player " + player.getDisplayName() + " died", "death"));

        } catch (Exception e) {
            e.printStackTrace();
            Error.Report(e, mineralcontest.getPlayerGame(player));
        }
    }


    public static void setMaxHealth(Player p) {
        p.setHealth(20);
        p.setFoodLevel(25);
    }
}
