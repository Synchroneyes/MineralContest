package fr.mineral.Core.Referee;

import fr.mineral.Core.Game;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RefereeEvent implements Listener {

    @EventHandler
    public void OnPlayerRightClick(PlayerInteractEvent event) {
        Player joueur = event.getPlayer();

        if(mineralcontest.plugin.getGame().isReferee(joueur)) {
            if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                // On doit récup l'item
                ItemStack itemEnMain = joueur.getInventory().getItemInMainHand();
                ItemMeta itemEnMainMeta = itemEnMain.getItemMeta();

                if(!itemEnMain.getType().equals(Material.AIR) && itemEnMainMeta.getDisplayName().equals(RefereeItem.nomItem)) {
                    // C'est le livre d'arbitrage
                    joueur.openInventory(RefereeInventory.getInventory());
                }
            }
        }
    }

    @EventHandler
    public void OnInventoryItemClicked(InventoryClickEvent event) {
        Player joueur = (Player) event.getWhoClicked();
        if(mineralcontest.plugin.getGame().isReferee(joueur)) {
            Inventory inventaire = event.getClickedInventory();
            if(inventaire.equals(RefereeInventory.getInventory())) {
                if(event.getCurrentItem() != null) {
                    ItemStack item = event.getCurrentItem();

                    /*
                        GREEN_CONCRETE : START / RESUME
                        YELLOW CONCRETE: PAUSE
                        RED CONCRETE: STOP
                        BLUE CONCRETE: LEADERBOARD
                        PINK CONCRETE: ALL TEAM SCORE
                        BROWN CONCRETE: START VOTE
                        BLACK: REFEREE SELECT BIOME

                     */
                    Game game = mineralcontest.plugin.getGame();
                    // GREEN CONCRETE : START
                    Material itemType = item.getType();
                    if(itemType.equals(Material.GREEN_CONCRETE)) {
                        if(!game.isGameStarted()) {
                            try {
                                game.demarrerPartie(true);
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else if(game.isGameStarted() && game.isGamePaused()) {
                            game.resumeGame();
                        } else {
                            sendActionUnavailable(joueur);
                        }
                    }

                    // YELLOW = PAUSE / RESUME
                    if(itemType.equals(Material.YELLOW_CONCRETE)) {
                        if(game.isGameStarted() && !game.isGamePaused()) game.pauseGame();
                        else if(game.isGameStarted() && game.isGamePaused()) game.resumeGame();
                        else sendActionUnavailable(joueur);

                    }

                    // RED = STOP
                    if(itemType.equals(Material.RED_CONCRETE)) {
                        if(game.isGameStarted()) {
                            try {
                                game.terminerPartie();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else sendActionUnavailable(joueur);
                    }

                    // BLUE = leaderboard
                    if(itemType.equals(Material.BLUE_CONCRETE)) {
                        if(game.isGameStarted()) Referee.displayLeaderboard();
                        else sendActionUnavailable(joueur);
                    }

                    // brown = start vote
                    if(itemType.equals(Material.BROWN_CONCRETE)) {
                        if(!game.isGameStarted() && !game.votemap.voteEnabled) game.votemap.enableVote(true);
                        else sendActionUnavailable(joueur);
                    }

                    // pink = spawn arena chest
                    if(itemType.equals(Material.PINK_CONCRETE)) {
                        if(game.isGameStarted()) {
                            try {
                                game.getArene().getCoffre().spawn();
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else sendActionUnavailable(joueur);
                    }

                    // BLACK = Force a biome
                    if(itemType.equals(Material.BLACK_CONCRETE)) {
                        if(!game.isGameStarted()) {
                            Referee.forceVote(joueur);
                            game.votemap.enableVote(true);
                            mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.referee_will_now_select_biome.toString());
                        }
                    }

                    joueur.closeInventory();
                    event.setCancelled(true);
                    return;


                }
            }
        }
    }

    private void sendActionUnavailable(Player p) {
        p.sendMessage(mineralcontest.prefixErreur + Lang.referee_action_not_available.toString());
    }
}
