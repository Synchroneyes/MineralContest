package fr.mineral.Events;

import fr.mineral.Core.House;
import fr.mineral.Core.MapBuilder.Item.HouseEgg;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.HouseSetup;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Ladder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerInteract implements Listener {


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) throws Exception {
        Player joueur = (Player) event.getPlayer();
        mineralcontest plugin = mineralcontest.plugin;
        House bleu = plugin.getGame().getBlueHouse();

        Block b = event.getClickedBlock();

        //Block b1 = new Location(b.getWorld(), b.getLocation().getX(), b.getLocation().getY(), b.getLocation().getZ()-1).getBlock();

        if(HouseSetup.addBlock && event.getClickedBlock() != null && !event.getClickedBlock().getType().equals(Material.AIR)) {
            bleu.addBlock(event.getClickedBlock().getLocation());
            joueur.sendMessage("Block de maison ajouté");

            event.getClickedBlock().setType(Material.AIR);
        }

        if(HouseSetup.addDoors && event.getClickedBlock() != null && !event.getClickedBlock().getType().equals(Material.AIR)) {
            bleu.getPorte().addToDoor(event.getClickedBlock());
            joueur.sendMessage("Block de porte ajouté");
            event.getClickedBlock().setType(Material.AIR);


        }

        if(HouseSetup.addSpawn) {
            Location l = event.getClickedBlock().getLocation();
            l.setY(l.getY()+1);
            bleu.setHouseLocation(l);
        }

        if(HouseSetup.addChest) {
            bleu.setCoffreEquipe(event.getClickedBlock().getLocation());
        }

        /*if(Setup.addDoors) {
            if(mineralcontest.plugin.getGame().getBlueHouse().getPorte().getPorte().size() < AutomaticDoors.maxDoorSize) {
                mineralcontest.plugin.getGame().getBlueHouse().getPorte().addToDoor(event.getClickedBlock());
                joueur.sendMessage("porte bleu added");
                event.setCancelled(true);

            } else if(mineralcontest.plugin.getGame().getYellowHouse().getPorte().getPorte().size() < AutomaticDoors.maxDoorSize){
                mineralcontest.plugin.getGame().getYellowHouse().getPorte().addToDoor(event.getClickedBlock());
                joueur.sendMessage("porte jaune added");

                event.setCancelled(true);
            } else if(mineralcontest.plugin.getGame().getRedHouse().getPorte().getPorte().size() < AutomaticDoors.maxDoorSize) {
                mineralcontest.plugin.getGame().getRedHouse().getPorte().addToDoor(event.getClickedBlock());
                joueur.sendMessage("porte rouge added");

                event.setCancelled(true);
            } else {
                mineralcontest.plugin.getServer().broadcastMessage("DONE");
            }
        }

        if(!mineralcontest.plugin.getGame().isGameStarted() && (event.getAction().equals(Action.LEFT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && !Setup.premierLancement) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(mineralcontest.prefixPrive + Lang.cant_interact_block_pre_game.toString());
        }


        if(Setup.getEtape() > 0 && Setup.premierLancement) {
            if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                event.setCancelled(true);
                Setup.setEmplacementTemporaire(event.getClickedBlock().getLocation());
            }
        }
        */








    }
}
