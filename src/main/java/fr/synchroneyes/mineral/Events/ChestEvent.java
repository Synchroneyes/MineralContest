package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Arena.Coffre;
import fr.synchroneyes.mineral.Core.Arena.CoffreAvecCooldown;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.House;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.ErrorReporting.Error;
import fr.synchroneyes.mineral.Utils.Radius;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

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


                    // Si le joueur est un arbitre
                    if (partie.isReferee(player)) {
                        // On récupère l'équipe du coffre ouvert
                        Inventory inventaireFerme = event.getInventory();

                        // Pour chaque maison de la partie
                        // On regarde si l'inventaire fermé est le même que celui d'une équipe
                        for (House maison : partie.getHouses()) {
                            Block blockCoffreMaison = maison.getCoffreEquipeLocation().getBlock();

                            // On s'assure que c'est bien un coffre
                            if (!(blockCoffreMaison.getState() instanceof Chest)) return;
                            Chest coffre = ((Chest) blockCoffreMaison.getState());
                            if (inventaireFerme.equals(coffre.getInventory())) {
                                maison.getTeam().updateScore();
                                return;
                            }
                        }
                    }


                    House playerHouse = partie.getPlayerHouse(player);
                    Coffre teamChest = playerHouse.getCoffre();
                    // Si le coffre fermé est celui de son équipe
                    if (openedInventoryBlock.getLocation().equals(teamChest.getPosition())) {
                        // Team Chest
                        try {
                            playerHouse.getTeam().updateScore();
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
                if (arenaChest != null) {
                    if (event.getEntity().getItemStack().getType().equals(Material.CHEST))
                        if (Radius.isBlockInRadius(arenaChest.getPosition(), event.getEntity().getLocation(), 2))
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
            if (event.getInventory().getHolder() instanceof Chest) {

                Chest openedChest = (Chest) event.getInventory().getHolder();
                Block openedChestBlock = openedChest.getBlock();

                // the inventory opened comes from a chest.

                if (!game.isThisBlockAGameChest(openedChestBlock)) {
                    openedChest.getInventory().clear();
                }

                if (arenaChest != null) {
                    if (arenaChest.getPosition().equals(openedChest.getLocation()) && arenaChest.isChestSpawned()) {
                        if (arenaChest.openingPlayer == null) {
                            arenaChest.openChest(player);
                        } else {
                            player.sendMessage(mineralcontest.prefixErreur + Lang.arena_chest_being_opened.toString());
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }

    }


    @EventHandler
    public void onItemClick(InventoryClickEvent event) throws Exception {
        if (event.getWhoClicked() instanceof Player) {
            Player joueur = (Player) event.getWhoClicked();
            Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);
            if (playerGroup == null) return;
            if (playerGroup.getGame() == null) return;

            Game partie = playerGroup.getGame();
            Inventory clickedInventory = event.getInventory();

            if (partie.isGameStarted()) {
                if (partie.getArene().getCoffre().getPosition().getBlock().getState() instanceof Chest) {
                    Chest arenaChest = (Chest) (partie.getArene().getCoffre().getPosition().getBlock().getState());
                    if (arenaChest.getInventory().equals(clickedInventory)) {
                        event.setCancelled(true);
                        return;
                    }
                }

            }
        }
    }

    @EventHandler
    public void onStatItemClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player joueur = (Player) event.getWhoClicked();
            if (mineralcontest.isInAMineralContestWorld(joueur)) {
                Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);
                if (playerGroup == null) return;
                if (playerGroup.getGame() == null) return;

                if (event.getView().getTitle().equals(Lang.stats_menu_title.getDefault())) {
                    event.setCancelled(true);
                    return;
                }

            }
        }
    }
}
