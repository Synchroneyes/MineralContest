package fr.mineral.Events;

import fr.mineral.Core.Arena.Coffre;
import fr.mineral.Core.Arena.CoffreAvecCooldown;
import fr.mineral.Core.Game.Game;
import fr.mineral.Settings.GameSettingsCvarOLD;
import fr.mineral.Core.House;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.Utils.Radius;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
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
        if (mineralcontest.isAMineralContestWorld(worldEvent)) {
            Game partie = mineralcontest.getWorldGame(worldEvent);
            // Si la game est démarrée
            if ((partie != null) && partie.isGameStarted() && !partie.isGamePaused() && !partie.isPreGame()) {

                if (!partie.groupe.getMonde().equals(worldEvent)) {
                    Bukkit.getLogger().severe("onChestClose L40");
                    return;
                }

                CoffreAvecCooldown coffreArene = partie.getArene().getCoffre();
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

                    House playerHouse = partie.getPlayerHouse(player);
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
                                        score += (int) GameSettingsCvarOLD.SCORE_IRON.getValue() * item.getAmount();
                                    }

                                    if (item.isSimilar(new ItemStack(Material.GOLD_INGOT, 1))) {
                                        score += (int) GameSettingsCvarOLD.SCORE_GOLD.getValue() * item.getAmount();
                                    }

                                    if (item.isSimilar(new ItemStack(Material.DIAMOND, 1))) {
                                        score += (int) GameSettingsCvarOLD.SCORE_DIAMOND.getValue() * item.getAmount();
                                    }

                                    if (item.isSimilar(new ItemStack(Material.EMERALD, 1))) {
                                        score += (int) GameSettingsCvarOLD.SCORE_EMERALD.getValue() * item.getAmount();
                                    }
                                }
                            }

                            playerHouse.getTeam().setScore(score);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Error.Report(e, partie);
                        }
                    }


                }
            }

        }
    }



    @EventHandler
    public void onChestBreaked(ItemSpawnEvent event) throws Exception {
        World world = event.getEntity().getWorld();
        if (mineralcontest.isAMineralContestWorld(world)) {
            Game partie = mineralcontest.getWorldGame(world);
            if (partie != null && partie.isGameStarted()) {

                if (!partie.groupe.getMonde().equals(world)) {
                    Bukkit.getLogger().severe("onChestBReaked L110");
                    return;
                }

                CoffreAvecCooldown arenaChest = partie.getArene().getCoffre();
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
        Game game = mineralcontest.getWorldGame(world);
        if (game == null) return;
        if (mineralcontest.isAMineralContestWorld(world)) {

            if (!game.groupe.getMonde().equals(world)) {
                Bukkit.getLogger().severe("InventoryOpenEvent L141");
                return;
            }

            Player player = (Player) event.getPlayer();
            CoffreAvecCooldown arenaChest = game.getArene().getCoffre();
            if(event.getInventory().getHolder() instanceof Chest) {

                Chest openedChest = (Chest) event.getInventory().getHolder();
                Block openedChestBlock = openedChest.getBlock();

                // the inventory opened comes from a chest.

                if (!game.isThisBlockAGameChest(openedChestBlock)) {
                    openedChest.getInventory().clear();
                }

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
