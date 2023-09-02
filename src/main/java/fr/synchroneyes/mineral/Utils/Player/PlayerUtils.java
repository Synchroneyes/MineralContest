package fr.synchroneyes.mineral.Utils.Player;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.groups.Utils.Etats;
import fr.synchroneyes.mineral.Core.Arena.Zones.DeathZone;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.House;
import fr.synchroneyes.mineral.Core.MCPlayer;
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

    public static void setFirework(Player joueur, Color couleur) {
        Firework firework = joueur.getWorld().spawn(joueur.getLocation(), Firework.class);
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

    public static void setFirework(Location position, Color couleur, int puissance) {
        Firework firework = (Firework) position.getWorld().spawn(position, Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        // On ajoute un effet
        fireworkMeta.addEffect(FireworkEffect.builder()
                .flicker(true)
                .trail(true)
                .withColor(couleur)
                .withFade(couleur)
                .build()

        );

        fireworkMeta.setPower(puissance);
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


        // On regarde si on doit faire apparaitre un coffre ou non, si non on arrête là
        MCPlayer mcPlayer = mineralcontest.plugin.getMCPlayer(player);
        if(mcPlayer != null) {
            GameSettings gameSettings = mcPlayer.getGroupe().getParametresPartie();
            if(gameSettings.getCVAR("drop_chest_on_death").getValeurNumerique() == 1 && !Radius.isBlockInRadius(player.getLocation(), mcPlayer.getGroupe().getGame().getArene().getCoffre().getLocation(), gameSettings.getCVAR("protected_zone_area_radius").getValeurNumerique())){
                // On l'ajoute à la deathzone
                try {
                    mineralcontest.getPlayerGame(player).getArene().getDeathZone().add(player);
                    GameLogger.addLog(new Log("PlayerUtils_Dead", "Player " + player.getDisplayName() + " died", "death"));

                } catch (Exception e) {
                    e.printStackTrace();
                    Error.Report(e, mineralcontest.getPlayerGame(player));
                }
                return;
            }
        }




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
