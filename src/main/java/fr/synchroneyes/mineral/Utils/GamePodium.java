package fr.synchroneyes.mineral.Utils;

import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.Player.PlayerUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;

import java.util.List;

public class GamePodium {

    private static Location getCenter(Location loc) {
        return new Location(loc.getWorld(),
                getRelativeCoord(loc.getBlockX()),
                getRelativeCoord(loc.getBlockY()),
                getRelativeCoord(loc.getBlockZ()));
    }

    private static double getRelativeCoord(int i) {
        double d = i;
        //d = d < 0 ? d - .5 : d + .5;
        return d + .5;
    }


    /**
     * Méthode permettant de faire apparaitre un podium
     * La liste des équipes doit être triée
     * @param position
     * @param equipes - liste triée d'équipe
     */
    public static void spawn(Location position, List<Equipe> equipes) {

        // ON commence par spawn le podium
        // Le bloc centrale est un block de quartz, avec au dessus une demi dalle de quartz
        Location centrePodium = position.clone();

        // On monte le podium d'un block
        //centrePodium.setY(centrePodium.getBlockY()+1);

        Location premierePlace, secondePlace, troisiemePlace;

        premierePlace = centrePodium.getBlock().getRelative(BlockFace.UP).getLocation();
        secondePlace = centrePodium.getBlock().getRelative(BlockFace.WEST).getLocation();
        troisiemePlace = centrePodium.getBlock().getRelative(BlockFace.EAST).getLocation();

        Bukkit.broadcastMessage(centrePodium + "");


        // On défini la base du podium
        centrePodium.getBlock().setType(Material.QUARTZ_BLOCK);
        premierePlace.getBlock().setType(Material.QUARTZ_SLAB);
        secondePlace.getBlock().setType(Material.QUARTZ_BLOCK);
        troisiemePlace.getBlock().setType(Material.QUARTZ_BLOCK);



        Location premierArmorLocation, secondArmorLocation, troisiemeArmorLocation;

        premierePlace.setY(premierePlace.getBlockY() + 1);
        premierArmorLocation = premierePlace.clone();



        secondePlace.setY(secondePlace.getBlockY() + 1);
        secondArmorLocation = secondePlace.clone();

        troisiemePlace.setY(troisiemePlace.getBlockY() + 1);
        troisiemeArmorLocation = troisiemePlace.clone();



        if(equipes.get(0) != null){
            ArmorStand premierArmorStand =  ArmorStandUtility.createArmorStandWithColoredLeather(getCenter(premierePlace.getBlock().getLocation()), equipes.get(0).getCouleur() + equipes.get(0).getNomEquipe(), equipes.get(0).getBukkitColor(), Material.GOLDEN_SWORD);
            setSignWithTeamScore(premierePlace.getBlock(), equipes.get(0));
            PlayerUtils.setFirework(getCenter(premierArmorStand.getLocation()), equipes.get(0).getBukkitColor(), 2);

            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "" + (getCenter(premierePlace.getBlock().getLocation())) + " - " + ChatColor.RED + "" + premierePlace.getBlock().getLocation());
        }
        if(equipes.size() >= 2 && equipes.get(1) != null) {
            ArmorStand secondArmorStand = ArmorStandUtility.createArmorStandWithColoredLeather(getCenter(secondArmorLocation), equipes.get(1).getCouleur() +equipes.get(1).getNomEquipe(), equipes.get(1).getBukkitColor(), Material.DIAMOND_SWORD);
            setSignWithTeamScore(secondePlace.getBlock(), equipes.get(1));
        }

        if(equipes.size() >= 3 && equipes.get(2) != null) {
            ArmorStand troisiemeArmorStand = ArmorStandUtility.createArmorStandWithColoredLeather(getCenter(troisiemeArmorLocation), equipes.get(2).getCouleur() + equipes.get(2).getNomEquipe(), equipes.get(2).getBukkitColor(), Material.WOODEN_SWORD);
            setSignWithTeamScore(troisiemePlace.getBlock(), equipes.get(2));
        }


    }

    private static void setSignWithTeamScore(Block blockSupport, Equipe equipe) {
        Block panneauPremier = blockSupport.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN);
        panneauPremier.setType(Material.ACACIA_WALL_SIGN);

        Sign sign = (Sign) panneauPremier.getState();
        sign.setEditable(true);
        sign.setLine(1, equipe.getCouleur() + equipe.getNomEquipe());
        sign.setLine(2, Lang.hud_score_text.toString());
        sign.setLine(3, equipe.getScore() + "");

        sign.update();
        WallSign wallSign = (WallSign) panneauPremier.getBlockData();
        wallSign.setFacing(BlockFace.SOUTH);
        panneauPremier.setBlockData(wallSign);
    }

}
