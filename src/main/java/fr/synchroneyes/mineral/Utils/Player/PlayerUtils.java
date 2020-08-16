package fr.synchroneyes.mineral.Utils.Player;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.groups.GroupeExtension;
import fr.synchroneyes.groups.Utils.Etats;
import fr.synchroneyes.mineral.Core.Arena.Zones.DeathZone;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.House;
import fr.synchroneyes.mineral.Core.Referee.Referee;
import fr.synchroneyes.mineral.Kits.KitAbstract;
import fr.synchroneyes.mineral.Scoreboard.ScoreboardUtil;
import fr.synchroneyes.mineral.Settings.GameSettings;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.ErrorReporting.Error;
import fr.synchroneyes.mineral.Utils.Log.GameLogger;
import fr.synchroneyes.mineral.Utils.Log.Log;
import fr.synchroneyes.mineral.Utils.Radius;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;

import java.util.*;

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

    public static void setFirework(Player joueur, Color couleur, int puissance) {
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

        fireworkMeta.setPower(puissance);
        firework.setFireworkMeta(fireworkMeta);
    }

    /**
     * Permet d'appliquer ou non l'ancien système de pvp
     *
     * @param joueur
     */
    public static void applyPVPtoPlayer(Player joueur) {
        Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);

        if (playerGroup == null) {
            joueur.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(1024d);
            return;
        }

        if (playerGroup.getParametresPartie().getCVAR("mp_enable_old_pvp").getValeurNumerique() == 1)
            joueur.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(1024d);
        else joueur.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);

    }

    public static void teleportPlayer(Player p, double x, double y, double z) {
        World world = getPluginWorld();

        Location loc = new Location(world, x, y, z);
        p.teleport(loc);

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

                if (mineralcontest.communityVersion)
                    playerPrefix.append(ChatColor.GOLD + "<" + playerGame.groupe.getNom() + "> " + ChatColor.RESET);

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
                            if (mineralcontest.communityVersion)
                                elementsADisplay.add(ChatColor.GOLD + "Groupe: " + ChatColor.WHITE + playerGroup.getNom());
                            elementsADisplay.add(ChatColor.GOLD + Lang.hud_players_count.toString() + playerGroup.getPlayerCount() + "");
                            elementsADisplay.add(ChatColor.GOLD + Lang.hud_current_game_state.toString() + ChatColor.RED + Lang.hud_currently_waiting_game_start.toString());
                            elementsADisplay.add("========= ");
                            if (playerGroup.getNomsJoueurNonPret().length() > 0)
                                elementsADisplay.add(Lang.non_ready_hud.toString() + playerGroup.getNomsJoueurNonPret());
                            elementsADisplay.add("Admins: ");
                            for (Player admin : playerGroup.getAdmins()) {
                                elementsADisplay.add(admin.getDisplayName());
                            }
                        }
                        if (playerGroup.getEtatPartie().equals(Etats.VOTE_EN_COURS)) {
                            elementsADisplay.clear();

                            StringBuilder joueurSansVote = new StringBuilder();
                            for (Player joueur_sans_vote : playerGroup.getMapVote().joueurAyantNonVote())
                                joueurSansVote.append(joueur_sans_vote.getDisplayName() + " ");

                            if (joueurSansVote.toString().length() > 0)
                                elementsADisplay.add(Lang.non_voted_hud.toString() + joueurSansVote.toString());

                            Map<String, Integer> maps_vote = playerGroup.getMapVote().getMapVotes(true);

                            if (maps_vote.isEmpty()) {
                                elementsADisplay.add(Lang.currently_no_vote.toString());
                            } else {
                                for (Map.Entry<String, Integer> vote : maps_vote.entrySet())
                                    elementsADisplay.add(vote.getValue() + " - " + vote.getKey());
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

        elementsADisplay.add(ChatColor.GREEN + "v" + mineralcontest.plugin.getDescription().getVersion());
        elementsADisplay.add("                ");



        //elementsADisplay.add(Lang.translate(Lang.hud_map_name.toString(), playerGroup));
        //elementsADisplay.add("                      ");

        if (playerGame.isPreGame()) {
            elementsADisplay.add(Lang.translate(Lang.hud_game_starting.toString(), playerGame));
            getPlayerTeamHUDContent(player, playerTeam, playerGame, elementsADisplay);
        } else if (playerGame.isGameStarted()) {

            ChatColor couleur = null;
            if (playerTeam == null) couleur = ChatColor.GOLD;
            else couleur = playerTeam.getCouleur();


            elementsADisplay.add(couleur + Lang.translate(Lang.hud_timeleft_text.toString(), playerGame));
            elementsADisplay.add(Lang.translate(Lang.hud_timeleft_value.toString(), playerGame));

            // Si le joueur n'est pas spectateur de la partie
            if (player.getGameMode() != GameMode.SPECTATOR) {
                // Player team null = arbitre
                getPlayerTeamHUDContent(player, playerTeam, playerGame, elementsADisplay);
            } else {
                // Si le joueur est spectateur, on doit vérifier si il est mort ou non
                // On regarde si il a une équipe ou non
                if (playerTeam != null) {
                    // Il peut-être spectateur
                    if (playerGroup.getSpectatorManager().isPlayerSpectator(player)) {
                        // On récu
                        elementsADisplay.add("                  ");
                        elementsADisplay.add(playerTeam.getCouleur() + Lang.hud_currently_spectating.toString() + ChatColor.WHITE + playerGroup.getSpectatorManager().getSpectator(player).getCurrent_spectated_player().getDisplayName());

                        getPlayerTeamHUDContent(player, playerTeam, playerGame, elementsADisplay);
                    }
                }
            }




        } else if (playerGame.isGamePaused()) {
            elementsADisplay.add(Lang.hud_game_paused.toString());
            elementsADisplay.add("        ");
            // Player team null = arbitre
            getPlayerTeamHUDContent(player, playerTeam, playerGame, elementsADisplay);

            // game en attente
        } else {
            elementsADisplay.add(Lang.hud_game_waiting_start.toString());
            if (playerGroup.getNomsJoueurNonPret().length() > 0)
                elementsADisplay.add(Lang.non_ready_hud.toString() + playerGroup.getNomsJoueurNonPret());

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

        if (playerGame.isGameStarted() && playerTeam != null)
            elements[0] = playerTeam.getCouleur() + Lang.hud_title_text1.toString() + ChatColor.WHITE + Lang.hud_title_text2.toString();
        else
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

            elementsADisplay.add("            ");
            // Joueur dans une équipe
            elementsADisplay.add(playerTeam.getCouleur() + Lang.translate(Lang.hud_team_text.toString()));
            elementsADisplay.add(playerTeam.getNomEquipe());

            elementsADisplay.add("  ");
            elementsADisplay.add(playerTeam.getCouleur() + Lang.translate(Lang.hud_score_text.toString()));
            if (playerTeam.getScore() >= 0) elementsADisplay.add("" + ChatColor.GREEN + playerTeam.getScore() + "");
            else elementsADisplay.add("" + ChatColor.RED + playerTeam.getScore() + "");

            KitAbstract playerKit = playerGame.groupe.getKitManager().getPlayerKit(player);
            if (playerKit != null) elementsADisplay.add("       ");
            if (playerKit != null)
                elementsADisplay.add(playerTeam.getCouleur() + "Kit: " + ChatColor.WHITE + playerKit.getNom());

            elementsADisplay.add("    ");
            elementsADisplay.add(playerTeam.getCouleur() + "X: " + ChatColor.WHITE + player.getLocation().getBlockX() + playerTeam.getCouleur() + " Y: " + ChatColor.WHITE + player.getLocation().getBlockY() + playerTeam.getCouleur() + " Z: " + ChatColor.WHITE + player.getLocation().getBlockZ());


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

    public static void clearPlayer(Player joueur, boolean clearInventory) {
        if (clearInventory) joueur.getInventory().clear();
        for (PotionEffect potion : joueur.getActivePotionEffects())
            joueur.removePotionEffect(potion.getType());

        // On remet la vitesse de base
        joueur.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.1d);
        joueur.setWalkSpeed(0.2f);

        // On remet les dégats de base
        joueur.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1.0);

        // On remet la vie de base
        joueur.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);


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
        player.setLevel(0);
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
            item_a_drop.add(Material.IRON_ORE);
            item_a_drop.add(Material.GOLD_ORE);
            item_a_drop.add(Material.EMERALD_ORE);
            item_a_drop.add(Material.DIAMOND_ORE);

            item_a_drop.add(Material.POTION);
            item_a_drop.add(Material.REDSTONE);
            item_a_drop.add(Material.ENCHANTED_BOOK);


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
        p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        p.setFoodLevel(30);

        p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
    }

}
