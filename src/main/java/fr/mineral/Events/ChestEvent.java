package fr.mineral.Events;

import fr.mineral.Core.Arena.Coffre;
import fr.mineral.Core.Arena.CoffreAvecCooldown;
import fr.mineral.Core.Game;
import fr.mineral.Core.GameSettingsCvar;
import fr.mineral.Core.House;
import fr.mineral.Teams.Equipe;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.Utils.Radius;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
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
        World worldEvent = event.getPlayer().getWorld();
        if(worldEvent.equals(mineralcontest.plugin.pluginWorld)) {
            // Si la game est démarrée
            if (mineralcontest.plugin.getGame().isGameStarted() && !mineralcontest.plugin.getGame().isGamePaused() && !mineralcontest.plugin.getGame().isPreGame()) {
                CoffreAvecCooldown coffreArene = mineralcontest.plugin.getGame().getArene().getCoffre();
                Player player = (Player) event.getPlayer();


                // si l'inventaire provient d'un coffre
                if (event.getInventory().getHolder() instanceof Chest) {
                    Block openedInventoryBlock = ((Chest) event.getInventory().getHolder()).getBlock();
                    Chest openedChest = ((Chest) event.getInventory().getHolder());
                    // Si le coffre fermé est le coffre d'arène
                    if (openedInventoryBlock.getLocation().equals(coffreArene.getPosition())) {
                        coffreArene.close();
                        return;
                    }

                    House playerHouse = mineralcontest.plugin.getGame().getPlayerHouse(player);
                    if(playerHouse == null) {
                        return;
                    }

                    Coffre teamChest = playerHouse.getCoffre();
                    // Si le coffre fermé est celui de son équipe
                    if (openedInventoryBlock.getLocation().equals(teamChest.getPosition())) {
                        // Team Chest
                        int score = 0;
                        try {
                            ItemStack[] items = openedChest.getInventory().getContents();
                            for (ItemStack item : items) {

                                if (item != null) {
                                    if (item.isSimilar(new ItemStack(Material.IRON_INGOT, 1))) {
                                        score += (int) GameSettingsCvar.SCORE_IRON.getValue() * item.getAmount();
                                    }

                                    if (item.isSimilar(new ItemStack(Material.GOLD_INGOT, 1))) {
                                        score += (int) GameSettingsCvar.SCORE_GOLD.getValue() * item.getAmount();
                                    }

                                    if (item.isSimilar(new ItemStack(Material.DIAMOND, 1))) {
                                        score += (int) GameSettingsCvar.SCORE_DIAMOND.getValue() * item.getAmount();
                                    }

                                    if (item.isSimilar(new ItemStack(Material.EMERALD, 1))) {
                                        score += (int) GameSettingsCvar.SCORE_EMERALD.getValue() * item.getAmount();
                                    }
                                }
                            }

                            playerHouse.getTeam().setScore(score);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }
            }

        }
    }



    @EventHandler
    public void onChestBreaked(ItemSpawnEvent event) throws Exception {

        World world = event.getEntity().getWorld();
        if(world.equals(mineralcontest.plugin.pluginWorld)) {
            if(mineralcontest.plugin.getGame().isGameStarted()) {
                CoffreAvecCooldown arenaChest = mineralcontest.plugin.getGame().getArene().getCoffre();
                if(arenaChest != null) {
                    if(event.getEntity().getItemStack().getType().equals(Material.CHEST))
                        if(Radius.isBlockInRadius(arenaChest.getPosition(), event.getEntity().getLocation(), 2))
                            event.setCancelled(true);
                }
            }
        }



    }

    @EventHandler
    public void onChestOpen(InventoryOpenEvent event) throws Exception {

        World world = event.getPlayer().getWorld();
        if(world.equals(mineralcontest.plugin.pluginWorld)) {
            Player player = (Player) event.getPlayer();
            CoffreAvecCooldown arenaChest = mineralcontest.plugin.getGame().getArene().getCoffre();
            if(event.getInventory().getHolder() instanceof Chest) {
                Chest openedChest = (Chest) event.getInventory().getHolder();
                if(arenaChest != null) {
                    if(arenaChest.getPosition().equals(openedChest.getLocation())) {
                        if(arenaChest.openingPlayer == null) {
                            arenaChest.openChest(player);
                        }else{
                            player.sendMessage(mineralcontest.prefixErreur + Lang.arena_chest_being_opened.toString());
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }

    }
}
