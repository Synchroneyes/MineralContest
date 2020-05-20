package fr.mineral.Utils.Player;

import fr.groups.Core.Groupe;
import fr.groups.GroupeExtension;
import fr.groups.Utils.Etats;
import fr.mineral.Core.Arena.Zones.DeathZone;
import fr.mineral.Core.Game.Game;
import fr.mineral.Settings.GameSettingsCvar;
import fr.mineral.Core.House;
import fr.mineral.Core.Referee.RefereeItem;
import fr.mineral.Scoreboard.ScoreboardUtil;
import fr.mineral.Teams.Equipe;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.Utils.Radius;
import fr.mineral.mineralcontest;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scoreboard.Score;
import org.bukkit.util.Vector;

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

    public static World getPluginWorld() {
        String world_name = (String) GameSettingsCvar.getValueFromCVARName("world_name");
        World world = Bukkit.getWorld(world_name);


        return world;
    }


    public static void runScoreboardManager() {
        Bukkit.getServer().getScheduler().runTaskTimer(mineralcontest.plugin, PlayerUtils::scoreboardManager, 0, 20);
    }

    private static void scoreboardManager() {

        for(Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
            if(onlinePlayer.getWorld().equals(mineralcontest.plugin.pluginWorld)) {
                Equipe playerTeam = mineralcontest.plugin.getGame().getPlayerTeam(onlinePlayer);
                StringBuilder playerPrefix = new StringBuilder();
                Game game = mineralcontest.plugin.getGame();

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
        for(CouplePlayer infoJoueur : mineralcontest.plugin.getGame().getArene().getDeathZone().getPlayers())
            if(infoJoueur.getJoueur().equals(joueur)) return true;

        return false;
    }

    public static boolean isPlayerInHisBase(Player p) throws Exception {
        Game game = mineralcontest.plugin.getGame();
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

        if(mineralcontest.plugin.pluginWorld == null) {
            return;
        }
        List<Player> onlinePlayers = mineralcontest.plugin.pluginWorld.getPlayers();
        if(onlinePlayers.size() == 0) {
            return;
        }

        if(mineralcontest.plugin.mapBuilderInstance != null && mineralcontest.plugin.mapBuilderInstance.isBuilderModeEnabled){
            mineralcontest.plugin.mapBuilderInstance.sendPlayersHUD();
            return;
        }

        boolean gameStarted = mineralcontest.plugin.getGame().isGameStarted();
        boolean gamePaused = mineralcontest.plugin.getGame().isGamePaused();
        boolean voteMapEnabled = mineralcontest.plugin.getGame().votemap.voteEnabled;
        boolean isPreGame = mineralcontest.plugin.getGame().isPreGame();

        ArrayList<String> elementsADisplay = new ArrayList<>();
        // Si la game n'a pas démarré
        for(Player online : onlinePlayers) {


            if (GroupeExtension.enabled) {
                if (online.getWorld().equals(mineralcontest.plugin.pluginWorld)) {
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
                        // Le joueur possède un groupe !
                        elementsADisplay.add(ChatColor.GOLD + "Groupe: " + ChatColor.WHITE + playerGroup.getNom());
                        elementsADisplay.add(ChatColor.GOLD + "Joueurs: " + playerGroup.getPlayerCount() + "/" + playerGroup.getPlayerCountRequired());
                        elementsADisplay.add(ChatColor.GOLD + "Etat: " + ChatColor.RED + playerGroup.getEtatPartie().getNom());
                        elementsADisplay.add("========= ");
                        elementsADisplay.add("Admins: ");
                        for (Player admin : playerGroup.getAdmins()) {
                            elementsADisplay.add(admin.getDisplayName());
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
                    }

                    String[] elements = new String[elementsADisplay.size() + 1];
                    int index = 1;
                    for (String element : elementsADisplay) {
                        elements[index] = element;
                        index++;
                    }

                    elements[0] = Lang.title.toString();

                    ScoreboardUtil.unrankedSidebarDisplay(online, elements);

                    elementsADisplay.clear();
                }
            } else {
                // Groupe Extention non chargé
                if (online.getWorld().equals(mineralcontest.plugin.pluginWorld)) {
                    // Si on vote
                    if (voteMapEnabled) {
                        ScoreboardUtil.unrankedSidebarDisplay(online, Lang.vote_title.toString(), " ",
                                "0 - " + Lang.vote_snow.toString() + " (" + mineralcontest.plugin.getGame().votemap.voteNeige + " " + Lang.vote_count.toString(),
                                "1 - " + Lang.vote_desert.toString() + " (" + mineralcontest.plugin.getGame().votemap.voteDesert + " " + Lang.vote_count.toString(),
                                "2 - " + Lang.vote_forest.toString() + " (" + mineralcontest.plugin.getGame().votemap.voteForet + " " + Lang.vote_count.toString(),
                                "3 - " + Lang.vote_plain.toString() + " (" + mineralcontest.plugin.getGame().votemap.votePlaine + " " + Lang.vote_count.toString(),
                                "4 - " + Lang.vote_mountain.toString() + " (" + mineralcontest.plugin.getGame().votemap.voteMontagne + " " + Lang.vote_count.toString(),
                                "5 - " + Lang.vote_swamp.toString() + " (" + mineralcontest.plugin.getGame().votemap.voteMarecage + " " + Lang.vote_count.toString());

                    } else {
                        Equipe team = mineralcontest.plugin.getGame().getPlayerTeam(online);

                        if (!gameStarted || isPreGame) {
                            if (team == null)
                                ScoreboardUtil.unrankedSidebarDisplay(online, "   " + Lang.title.toString() + "   ", " ", Lang.hud_game_waiting_start.toString(), "", Lang.hud_awaiting_players.toString(), Lang.hud_you_are_not_in_team.toString());
                            else
                                ScoreboardUtil.unrankedSidebarDisplay(online, "   " + Lang.title.toString() + "   ", " ", Lang.hud_game_waiting_start.toString(), "", Lang.hud_awaiting_players.toString(), Lang.translate(Lang.hud_team_name_no_score.toString(), team, online));
                        } else {
                            // Si la game est en pause
                            if (gamePaused) {
                                // Pas de team
                                if (team == null) {
                                    ScoreboardUtil.unrankedSidebarDisplay(online, "   " + Lang.title.toString() + "   ", " ", Lang.hud_game_paused.toString(), "", Lang.hud_you_are_not_in_team.toString());
                                } else {

                                    if (mineralcontest.plugin.getGame().isReferee(online)) {
                                        ScoreboardUtil.unrankedSidebarDisplay(online, "   " + Lang.title.toString() + "   ", " ", Lang.hud_time_left.toString(), "", "Referee", "", "Red team: " + mineralcontest.plugin.getGame().getRedHouse().getTeam().getScore() + " point(s)",
                                                "", "Blue team: " + mineralcontest.plugin.getGame().getBlueHouse().getTeam().getScore() + " point(s)",
                                                "", "Yellow team: " + mineralcontest.plugin.getGame().getYellowHouse().getTeam().getScore() + " point(s)");
                                    } else {
                                        // Le joueur a une équipe
                                        ScoreboardUtil.unrankedSidebarDisplay(online, "   " + Lang.title.toString() + "   ", " ", Lang.hud_game_paused.toString(), "", Lang.translate(Lang.hud_team_name_score.toString(), team));
                                    }
                                }

                            } else {
                                // Game pas en pause
                                if (mineralcontest.plugin.getGame().isReferee(online)) {
                                    ScoreboardUtil.unrankedSidebarDisplay(online, "   " + Lang.title.toString() + "   ", " ", Lang.hud_time_left.toString(), "", "Referee", "", "Red team: " + mineralcontest.plugin.getGame().getRedHouse().getTeam().getScore() + " point(s)",
                                            "", "Blue team: " + mineralcontest.plugin.getGame().getBlueHouse().getTeam().getScore() + " point(s)",
                                            "", "Yellow team: " + mineralcontest.plugin.getGame().getYellowHouse().getTeam().getScore() + " point(s)");
                                } else {
                                    ScoreboardUtil.unrankedSidebarDisplay(online, "   " + Lang.title.toString() + "   ", " ", Lang.hud_time_left.toString(), "", Lang.translate(Lang.hud_team_name_score.toString(), team));
                                }
                            }
                        }
                    }
                }
            }


            /*if(online.getWorld().equals(mineralcontest.plugin.pluginWorld)) {
                // Si on vote
                if(voteMapEnabled) {
                    ScoreboardUtil.unrankedSidebarDisplay(online, Lang.vote_title.toString(), " " ,
                            "0 - " + Lang.vote_snow.toString() + " (" + mineralcontest.plugin.getGame().votemap.voteNeige + " " + Lang.vote_count.toString(),
                            "1 - "+ Lang.vote_desert.toString() +" (" + mineralcontest.plugin.getGame().votemap.voteDesert + " "+ Lang.vote_count.toString(),
                            "2 - "+ Lang.vote_forest.toString() +" (" + mineralcontest.plugin.getGame().votemap.voteForet + " "+ Lang.vote_count.toString(),
                            "3 - "+ Lang.vote_plain.toString() +" (" + mineralcontest.plugin.getGame().votemap.votePlaine + " "+ Lang.vote_count.toString(),
                            "4 - "+ Lang.vote_mountain.toString() +" (" + mineralcontest.plugin.getGame().votemap.voteMontagne +" "+  Lang.vote_count.toString(),
                            "5 - "+ Lang.vote_swamp.toString() +" (" + mineralcontest.plugin.getGame().votemap.voteMarecage +" "+  Lang.vote_count.toString());

                } else {
                    Equipe team = mineralcontest.plugin.getGame().getPlayerTeam(online);

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

                                if(mineralcontest.plugin.getGame().isReferee(online)) {
                                    ScoreboardUtil.unrankedSidebarDisplay(online, "   " + Lang.title.toString() + "   ", " ", Lang.hud_time_left.toString(),"", "Referee","", "Red team: " + mineralcontest.plugin.getGame().getRedHouse().getTeam().getScore() + " point(s)",
                                            "", "Blue team: " + mineralcontest.plugin.getGame().getBlueHouse().getTeam().getScore() + " point(s)",
                                            "", "Yellow team: " + mineralcontest.plugin.getGame().getYellowHouse().getTeam().getScore() + " point(s)");
                                }else {
                                    // Le joueur a une équipe
                                    ScoreboardUtil.unrankedSidebarDisplay(online, "   " + Lang.title.toString() + "   ", " ", Lang.hud_game_paused.toString(), "", Lang.translate(Lang.hud_team_name_score.toString(), team));
                                }
                            }

                        } else {
                            // Game pas en pause
                            if(mineralcontest.plugin.getGame().isReferee(online)) {
                                ScoreboardUtil.unrankedSidebarDisplay(online, "   " + Lang.title.toString() + "   ", " ", Lang.hud_time_left.toString(),"", "Referee","", "Red team: " + mineralcontest.plugin.getGame().getRedHouse().getTeam().getScore() + " point(s)",
                                        "", "Blue team: " + mineralcontest.plugin.getGame().getBlueHouse().getTeam().getScore() + " point(s)",
                                        "", "Yellow team: " + mineralcontest.plugin.getGame().getYellowHouse().getTeam().getScore() + " point(s)");
                            } else {
                                ScoreboardUtil.unrankedSidebarDisplay(online, "   " + Lang.title.toString() + "   ", " ", Lang.hud_time_left.toString(), "", Lang.translate(Lang.hud_team_name_score.toString(), team));
                            }
                        }
                    }
                }
            }*/
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
                try {
                    mineralcontest.plugin.getGame().getArene().getDeathZone().add(joueur);
                } catch (Exception e) {
                    e.printStackTrace();
                    Error.Report(e);
                }
            }
        }, 20);
    }

    public static void clearPlayer(Player joueur) {
        joueur.getInventory().clear();
        Bukkit.getLogger().info("Player " + joueur.getDisplayName() + " has been cleared");
    }


    public static void equipReferee(Player joueur) {
        if(mineralcontest.plugin.getGame().isReferee(joueur)) {
            joueur.getInventory().setItemInMainHand(new RefereeItem().getItem());
            joueur.setGameMode(GameMode.CREATIVE);
        }
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
            item_a_drop.add(Material.IRON_ORE);
            item_a_drop.add(Material.GOLD_ORE);
            item_a_drop.add(Material.DIAMOND_ORE);
            item_a_drop.add(Material.EMERALD_ORE);

            int mp_enable_item_drop = (int) GameSettingsCvar.mp_enable_item_drop.getValue();

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
                Bukkit.getWorld((String) GameSettingsCvar.getValueFromCVARName("world_name")).dropItemNaturally(player.getLocation(), item);
        }

        // On l'ajoute à la deathzone
        try {
            mineralcontest.plugin.getGame().getArene().getDeathZone().add(player);

        }catch (Exception e) {
            e.printStackTrace();
            Error.Report(e);
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
    }


    public static void setMaxHealth(Player p) {
        p.setHealth(20);
        p.setFoodLevel(25);
    }
}
