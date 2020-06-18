package fr.mineral.Utils.Player;

import fr.groups.Core.Groupe;
import fr.groups.GroupeExtension;
import fr.groups.Utils.Etats;
import fr.mineral.Core.Arena.Zones.DeathZone;
import fr.mineral.Core.Game.Game;
import fr.mineral.Core.Referee.Items.RefereeItem;
import fr.mineral.Core.Referee.Referee;
import fr.mineral.Settings.GameSettings;
import fr.mineral.Core.House;
import fr.mineral.Scoreboard.ScoreboardUtil;
import fr.mineral.Teams.Equipe;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.Utils.Log.GameLogger;
import fr.mineral.Utils.Log.Log;
import fr.mineral.Utils.Radius;
import fr.mineral.mineralcontest;
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

        Location loc = new Location(world, x,y,z);
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
        if(world != null) mineralcontest.plugin.defaultSpawn = world.getSpawnLocation();
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

                if(game.getArene().getDeathZone().isPlayerDead(onlinePlayer)) {
                    playerPrefix.append(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "");
                }else {
                    if(game.isReferee(onlinePlayer)) playerPrefix.append(ChatColor.GOLD + "[☆] " + ChatColor.WHITE);
                    if(playerTeam != null) playerPrefix.append(playerTeam.getCouleur() + "██ " + ChatColor.WHITE);
                }

                onlinePlayer.setPlayerListName(playerPrefix.toString() + onlinePlayer.getDisplayName());
            }

        }
    }

    public static boolean isPlayerInDeathZone(Player joueur) {
        for (CouplePlayer infoJoueur : mineralcontest.getPlayerGame(joueur).getArene().getDeathZone().getPlayers())
            if(infoJoueur.getJoueur().equals(joueur)) return true;

        return false;
    }

    public static boolean isPlayerInHisBase(Player p) throws Exception {
        Game game = mineralcontest.getPlayerGame(p);
        if (game == null) return false;
        House playerHouse = game.getPlayerHouse(p);
        int house_radius = 7;
        if(playerHouse == null) return false;
        return (Radius.isBlockInRadius(playerHouse.getHouseLocation(), p.getLocation(), house_radius));
    }

    public static int getPlayerItemsCountInInventory(Player p) {
        int item_count = 0;
        for(ItemStack item : p.getInventory().getContents())
            if(item != null && item.getType().equals(Material.AIR))
                item_count++;

        return item_count;
    }

    /*public static void givePlayerBaseItems(Player joueur) {

            On donne au joueur:
                - Arc
                - 64 fleches
                - Epée en fer
                -


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
    }*/

    public static void drawPlayersHUD() {

        //Bukkit.broadcastMessage("Nombre de joueur en ligne: " + Bukkit.getOnlinePlayers().size());
        if(mineralcontest.plugin.pluginWorld == null) {
            return;
        }
        if (Bukkit.getOnlinePlayers().size() == 0) {
            return;
        }

        if(mineralcontest.plugin.mapBuilderInstance != null && mineralcontest.plugin.mapBuilderInstance.isBuilderModeEnabled){
            mineralcontest.plugin.mapBuilderInstance.sendPlayersHUD();
            return;
        }


        ArrayList<String> elementsADisplay = new ArrayList<>();
        // Si la game n'a pas démarré
        for (Player online : Bukkit.getOnlinePlayers()) {
            //Bukkit.getLogger().info("playerHud " + online.getDisplayName() + "=> isInAWorld" + mineralcontest.isInAMineralContestWorld(online));
            if (!mineralcontest.isInAMineralContestWorld(online)) continue;
            Game playergame = mineralcontest.getPlayerGame(online);
            //Bukkit.getLogger().info(online.getDisplayName() + " => playergame==null:" + (playergame == null));

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
            } else {
                // Groupe Extention non chargé
                if (mineralcontest.isInAMineralContestWorld(online)) {
                    // Si on vote

                    if (playergame == null) continue;
                    boolean gameStarted = playergame.isGameStarted();
                    boolean gamePaused = playergame.isGamePaused();
                    boolean isPreGame = playergame.isPreGame();

                    Equipe team = playergame.getPlayerTeam(online);

                    if (!gameStarted || isPreGame) {
                        if (team == null)
                            ScoreboardUtil.unrankedSidebarDisplay(online, "   " + Lang.title.toString() + "   ", " ", Lang.hud_game_waiting_start.toString(), "", Lang.translate(Lang.hud_awaiting_players.toString(), playergame), Lang.hud_you_are_not_in_team.toString());
                        else
                            ScoreboardUtil.unrankedSidebarDisplay(online, "   " + Lang.title.toString() + "   ", " ", Lang.hud_game_waiting_start.toString(), "", Lang.translate(Lang.hud_awaiting_players.toString(), playergame), Lang.translate(Lang.hud_team_name_no_score.toString(), team, online));
                    } else {
                        // Si la game est en pause
                        if (gamePaused) {
                            // Pas de team
                            if (team == null) {
                                ScoreboardUtil.unrankedSidebarDisplay(online, "   " + Lang.title.toString() + "   ", " ", Lang.hud_game_paused.toString(), "", Lang.hud_you_are_not_in_team.toString());
                            } else {

                                if (playergame.isReferee(online)) {
                                    // TODO
                                    //ScoreboardUtil.unrankedSidebarDisplay(online, "   " + Lang.title.toString() + "   ", " ", Lang.hud_time_left.toString(), "", "Referee", "", "Red team: " + mineralcontest.getPlayerGame(joueur).getRedHouse().getTeam().getScore() + " point(s)",
                                    //      "", "Blue team: " + mineralcontest.getPlayerGame(joueur).getBlueHouse().getTeam().getScore() + " point(s)",
                                    //    "", "Yellow team: " + mineralcontest.getPlayerGame(joueur).getYellowHouse().getTeam().getScore() + " point(s)");
                                } else {
                                    // Le joueur a une équipe
                                    ScoreboardUtil.unrankedSidebarDisplay(online, "   " + Lang.title.toString() + "   ", " ", Lang.hud_game_paused.toString(), "", Lang.translate(Lang.hud_team_name_score.toString(), team));
                                }
                            }

                        } else {
                            // Game pas en pause
                            if (playergame.isReferee(online)) {
                                //ScoreboardUtil.unrankedSidebarDisplay(online, "   " + Lang.title.toString() + "   ", " ", Lang.hud_time_left.toString(), "", "Referee", "", "Red team: " + mineralcontest.getPlayerGame(joueur).getRedHouse().getTeam().getScore() + " point(s)",
                                //"", "Blue team: " + mineralcontest.getPlayerGame(joueur).getBlueHouse().getTeam().getScore() + " point(s)",
                                //"", "Yellow team: " + mineralcontest.getPlayerGame(joueur).getYellowHouse().getTeam().getScore() + " point(s)");
                                // TODO
                            } else {
                                ScoreboardUtil.unrankedSidebarDisplay(online, "   " + Lang.title.toString() + "   ", " ", Lang.hud_time_left.toString(), "", Lang.translate(Lang.hud_team_name_score.toString(), team));
                            }
                        }
                    }

                }
            }


            /*if(online.getWorld().equals(mineralcontest.plugin.pluginWorld)) {
                // Si on vote
                if(voteMapEnabled) {
                    ScoreboardUtil.unrankedSidebarDisplay(online, Lang.vote_title.toString(), " " ,
                            "0 - " + Lang.vote_snow.toString() + " (" + mineralcontest.getPlayerGame(joueur).votemap.voteNeige + " " + Lang.vote_count.toString(),
                            "1 - "+ Lang.vote_desert.toString() +" (" + mineralcontest.getPlayerGame(joueur).votemap.voteDesert + " "+ Lang.vote_count.toString(),
                            "2 - "+ Lang.vote_forest.toString() +" (" + mineralcontest.getPlayerGame(joueur).votemap.voteForet + " "+ Lang.vote_count.toString(),
                            "3 - "+ Lang.vote_plain.toString() +" (" + mineralcontest.getPlayerGame(joueur).votemap.votePlaine + " "+ Lang.vote_count.toString(),
                            "4 - "+ Lang.vote_mountain.toString() +" (" + mineralcontest.getPlayerGame(joueur).votemap.voteMontagne +" "+  Lang.vote_count.toString(),
                            "5 - "+ Lang.vote_swamp.toString() +" (" + mineralcontest.getPlayerGame(joueur).votemap.voteMarecage +" "+  Lang.vote_count.toString());

                } else {
                    Equipe team = mineralcontest.getPlayerGame(joueur).getPlayerTeam(online);

                    if (!gameStarted || isPreGame) {
                        if(team == null) ScoreboardUtil.unrankedSidebarDisplay(online, "   " + Lang.title.toString() + "   ", " ", Lang.hud_game_waiting_start.toString(), "", Lang.hud_awaiting_players.toString(), Lang.hud_you_are_not_in_team.toString());
                        else ScoreboardUtil.unrankedSidebarDisplay(online, "   " + Lang.title.toString() + "   ", " ", Lang.hud_game_waiting_start.toString(), "", Lang.hud_awaiting_players.toString(), Lang.translate(Lang.hud_team_name_no_score.toString(), team, online));
                    } else {
                        // Si la game est en pause
                        if (gamePaused) {
                            // Pas de team
                            if (team == null) {
                                ScoreboardUtil.unrankedSidebarDisplay(online,"   " + Lang.title.toString() + "   ", " ", Lang.hud_game_paused.toString(), "", Lang.hud_you_are_not_in_team.toString());
                            } else {

                                if(mineralcontest.getPlayerGame(joueur).isReferee(online)) {
                                    ScoreboardUtil.unrankedSidebarDisplay(online, "   " + Lang.title.toString() + "   ", " ", Lang.hud_time_left.toString(),"", "Referee","", "Red team: " + mineralcontest.getPlayerGame(joueur).getRedHouse().getTeam().getScore() + " point(s)",
                                            "", "Blue team: " + mineralcontest.getPlayerGame(joueur).getBlueHouse().getTeam().getScore() + " point(s)",
                                            "", "Yellow team: " + mineralcontest.getPlayerGame(joueur).getYellowHouse().getTeam().getScore() + " point(s)");
                                }else {
                                    // Le joueur a une équipe
                                    ScoreboardUtil.unrankedSidebarDisplay(online, "   " + Lang.title.toString() + "   ", " ", Lang.hud_game_paused.toString(), "", Lang.translate(Lang.hud_team_name_score.toString(), team));
                                }
                            }

                        } else {
                            // Game pas en pause
                            if(mineralcontest.getPlayerGame(joueur).isReferee(online)) {
                                ScoreboardUtil.unrankedSidebarDisplay(online, "   " + Lang.title.toString() + "   ", " ", Lang.hud_time_left.toString(),"", "Referee","", "Red team: " + mineralcontest.getPlayerGame(joueur).getRedHouse().getTeam().getScore() + " point(s)",
                                        "", "Blue team: " + mineralcontest.getPlayerGame(joueur).getBlueHouse().getTeam().getScore() + " point(s)",
                                        "", "Yellow team: " + mineralcontest.getPlayerGame(joueur).getYellowHouse().getTeam().getScore() + " point(s)");
                            } else {
                                ScoreboardUtil.unrankedSidebarDisplay(online, "   " + Lang.title.toString() + "   ", " ", Lang.hud_time_left.toString(), "", Lang.translate(Lang.hud_team_name_score.toString(), team));
                            }
                        }
                    }
                }
            }*/
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

        elementsADisplay.add(Lang.translate(Lang.hud_map_name.toString(), playerGroup));
        elementsADisplay.add("                      ");

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

        elementsADisplay.clear();


    }

    private static boolean getPlayerTeamHUDContent(Player player, Equipe playerTeam, Game playerGame, ArrayList<String> elementsADisplay) {
        if (playerTeam == null) {
            if (playerGame.isReferee(player)) {
                elementsADisplay.add(Lang.hud_referee_text.toString());
                for (House equipes : playerGame.getHouses())
                    elementsADisplay.add(Lang.translate(Lang.hud_team_name_score.toString(), equipes.getTeam()));

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
        for(CouplePlayer couple : zone.getPlayers())
            if(couple.getJoueur().equals(online))
                return couple.getValeur();

        return 0;
    }


    // Appelé lorsqu'un joueur meurs et qu'il était dans la deathzone par exemple
    // On commence par le supprimé
    // Et on le remet
    public static void resetPlayerDeathZone(Player joueur) {

        for (CouplePlayer infoJoueur : mineralcontest.getPlayerGame(joueur).getArene().getDeathZone().getPlayers()) {
            if(infoJoueur.getJoueur().equals(joueur))
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


    // TODO
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
            if(mp_enable_item_drop == 1) {
                // IF ITEM IS NOT TO DROP
                if(!item_a_drop.contains(item.getType())) {
                    iterateur.remove();
                }
            }

            // IF NO DROP AT ALL
            if(mp_enable_item_drop == 0) {
                iterateur.remove();
            }
        }


        // Drop items
        for(ItemStack item : inventaire) {
            if(!item.getType().equals(Material.AIR))
                player.getWorld().dropItemNaturally(player.getLocation(), item);
        }

        // On l'ajoute à la deathzone
        try {
            mineralcontest.getPlayerGame(player).getArene().getDeathZone().add(player);
            GameLogger.addLog(new Log("PlayerUtils_Dead", "Player " + player.getDisplayName() + " died", "death"));

        }catch (Exception e) {
            e.printStackTrace();
            Error.Report(e, mineralcontest.getPlayerGame(player));
        }
    }

    public static void sendPluginCommandsToPlayer(Player player) {
        StringBuilder commands = new StringBuilder();
        String adminPrefix = ChatColor.RED + "[ADMIN] " + ChatColor.WHITE;
        if(player.isOp()) {
            commands.append(adminPrefix).append(ChatColor.GOLD + "mp_randomize_team <1 ou 0> " + ChatColor.WHITE + "# Permet d'activer ou non les équipes aléatoires (1 = oui, 0 = non aléatoire)\n");
            commands.append(adminPrefix).append(ChatColor.GOLD + "mp_iron_score  " + ChatColor.WHITE + "# Permet de modifier les points rapporté par un lingot de fer\n");
            commands.append(adminPrefix).append(ChatColor.GOLD + "mp_gold_score  " + ChatColor.WHITE + "# Permet de modifier les points rapporté par un lingot d'or\n");
            commands.append(adminPrefix).append(ChatColor.GOLD + "mp_diamond_score  " + ChatColor.WHITE + "# Permet de modifier les points rapporté par un lingot de diamant\n");
            commands.append(adminPrefix).append(ChatColor.GOLD + "mp_emerald_score  " + ChatColor.WHITE + "# Permet de modifier les points rapporté par un lingot d'émeraude\n");
            commands.append(adminPrefix).append(ChatColor.GOLD + "mp_team_max_player  " + ChatColor.WHITE + "# Permet de modifier le nombre de joueur requis par équipe pour lancer une partie\n");
            commands.append(adminPrefix).append(ChatColor.GOLD + "mp_enable_metrics  <1 ou 0> " + ChatColor.WHITE + "# Permet d'activer ou non l'envoie de statistique\n");
            commands.append(adminPrefix).append(ChatColor.GOLD + "mp_add_team_penality   " + ChatColor.WHITE + "# Ajoute une pénalité à une équipe\n");
            commands.append(adminPrefix).append(ChatColor.GOLD + "mp_reset_team_penality  " + ChatColor.WHITE + "# Supprime une pénalité d'équipe\n");
            commands.append(adminPrefix).append(ChatColor.GOLD + "mp_set_language  " + ChatColor.WHITE + "# Choisit le langage à utiliser (french, english)\n");
            commands.append(adminPrefix).append(ChatColor.GOLD + "mp_start_vote  " + ChatColor.WHITE + "# Démarre un vote\n");
            commands.append(adminPrefix).append(ChatColor.GOLD + "mp_enable_item_drop <0 ou 1 ou 2> " + ChatColor.WHITE + "# 0 = aucun drop, 1 = drop de minerais, 2 = drop tout l'inventaire\n");
            commands.append(adminPrefix).append(ChatColor.GOLD + "start  " + ChatColor.WHITE + "# Permet de démarrer une partie, si la valeur \"force\" est ajoutée, alors la partie démarrera sans tenir compte du nombre de joueur en ligne\n");
            commands.append(adminPrefix).append(ChatColor.GOLD + "resume " + ChatColor.WHITE + "# Permet de reprendre une partie en pause\n");
            commands.append(adminPrefix).append(ChatColor.GOLD + "pause " + ChatColor.WHITE + "# permet de mettre une partie en pause\n");
            commands.append(adminPrefix).append(ChatColor.GOLD + "switch   " + ChatColor.WHITE + "# Permet de mettre un joueur dans une équipe\n");
            commands.append(adminPrefix).append(ChatColor.GOLD + "referee   " + ChatColor.WHITE + "# Permet de devenir arbitre\n");
            commands.append(adminPrefix).append(ChatColor.GOLD + "arbitre   " + ChatColor.WHITE + "# Permet de devenir arbitre\n");
            commands.append(adminPrefix).append(ChatColor.GOLD + "t <message>   " + ChatColor.WHITE + "# envoyer un message à son équipe\n");
            commands.append(adminPrefix).append(ChatColor.GOLD + "team <message>   " + ChatColor.WHITE + "# envoyer un message à sa équipe\n");
            commands.append(adminPrefix).append(ChatColor.GOLD + "allow <playername>   " + ChatColor.WHITE + "# Autoriser la connexion d'un joueur\n");
        }

        commands.append(ChatColor.GREEN + "[ALL]" + ChatColor.GOLD + " join  " + ChatColor.WHITE + "# permet de rejoindre une équipe\n");
        commands.append(ChatColor.GREEN + "[ALL]" + ChatColor.GOLD + " leaveteam  " + ChatColor.WHITE + "# Quitter son équipe\n");
        player.sendMessage(commands.toString());
        GameLogger.addLog(new Log("CommandsSentToPlayer", "Player " + player.getDisplayName() + " received all command", "PlayerInteract"));

    }


    public static void setMaxHealth(Player p) {
        p.setHealth(20);
        p.setFoodLevel(25);
    }
}
