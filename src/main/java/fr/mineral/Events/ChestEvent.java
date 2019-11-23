package fr.mineral.Events;

import fr.mineral.Core.Arena.CoffreAvecCooldown;
import fr.mineral.Core.Game;
import fr.mineral.Teams.Equipe;
import fr.mineral.Utils.Radius;
import fr.mineral.mineralcontest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class ChestEvent implements Listener {

    // Lorsqu'on ferme un inventaire
    @EventHandler
    public void onChestClose(InventoryCloseEvent event) throws Exception {


        // Si l'inventaire est un coffre
        if(mineralcontest.plugin.getGame().isGameStarted() && !mineralcontest.plugin.getGame().isGamePaused() && !mineralcontest.plugin.getGame().isPreGame()) {

            if(mineralcontest.plugin.getGame().getArene().getCoffre().coffre != null && mineralcontest.plugin.getGame().getArene().getCoffre().opened) {
                Player p = (Player) event.getPlayer();

                if(mineralcontest.plugin.getGame().getArene().getCoffre().openingPlayer.equals(p)) {
                    mineralcontest.plugin.getGame().getArene().getCoffre().isCancelled = true;
                    mineralcontest.plugin.getGame().getArene().getCoffre().close();
                }
            }

            if(event.getInventory().getHolder() instanceof Chest) {

                Chest c = (Chest) event.getInventory().getHolder();
                Location coffreArene = mineralcontest.plugin.getGame().getArene().getCoffre().getPosition();

                // Si le coffre est celui de l'arene
                if(Radius.isBlockInRadius(coffreArene, c.getBlock().getLocation(), 1)) {
                    // On le casse
                    c.getBlock().breakNaturally();
                }

                // Si c'est un coffre d'équipe
                // On récupere le joueur en question
                Player joueur = (Player) event.getPlayer();
                Equipe team = mineralcontest.plugin.getGame().getPlayerTeam(joueur);
                // Ne devrait pas arriver, mais sait-on jamais
                if(team != null) {
                    // On regarde si le coffre de l'équipe correspond à celui qui a été ouvert
                    Location emplacementCoffreEquipe = team.getCoffreEquipeLocation();
                    if(Radius.isBlockInRadius(c.getBlock().getLocation(), emplacementCoffreEquipe, 1)) {
                        int score = 0;
                        // C'est un coffre d'équipe
                        try {
                            ItemStack[] items = c.getInventory().getContents();
                            for(ItemStack item : items) {

                                if(item != null) {
                                    if(item.isSimilar(new ItemStack(Material.IRON_INGOT, 1))) {
                                        score += Game.SCORE_IRON*item.getAmount();
                                    }

                                    if(item.isSimilar(new ItemStack(Material.GOLD_INGOT, 1))) {
                                        score += Game.SCORE_GOLD*item.getAmount();
                                    }

                                    if(item.isSimilar(new ItemStack(Material.DIAMOND, 1))) {
                                        score += Game.SCORE_DIAMOND*item.getAmount();
                                    }

                                    if(item.isSimilar(new ItemStack(Material.EMERALD, 1))) {
                                        score += Game.SCORE_EMERALD*item.getAmount();
                                    }
                                }


                            }

                            team.setScore(score);

                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }


            }
        }

    }


    @EventHandler
    public void onChestBreaked(ItemSpawnEvent event) throws Exception {


        if(mineralcontest.plugin.getGame().isGameStarted()) {
            Location coffreArene = mineralcontest.plugin.getGame().getArene().getCoffre().getPosition();

            // On regarde si les items qui ont spawn sont proche du coffre de l'arene
            if(Radius.isBlockInRadius(coffreArene,event.getLocation(), 5)){
                // Si l'item est un coffre
                if(event.getEntity().getItemStack().equals(new ItemStack(Material.CHEST, 1)))
                    // On ne le fait pas apparaitre
                    event.setCancelled(true);
                    // On marque le coffre comme usé
                    mineralcontest.plugin.getGame().getArene().CHEST_USED = true;
            }



        }


    }

    @EventHandler
    public void onChestOpen(InventoryOpenEvent event) throws Exception {
        Player joueur = (Player) event.getPlayer();
        if(CoffreAvecCooldown.coffre != null && Radius.isBlockInRadius(CoffreAvecCooldown.coffre.getPosition(), joueur.getLocation(), 5)) {
            CoffreAvecCooldown.coffre.open((Player) event.getPlayer());
        }
    }
}
