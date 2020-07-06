package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.groups.Core.MapVote;
import fr.synchroneyes.groups.Menus.MenuVote;
import fr.synchroneyes.groups.Utils.Etats;
import fr.synchroneyes.mineral.Core.Arena.Coffre;
import fr.synchroneyes.mineral.Core.Coffre.AutomatedChestAnimation;
import fr.synchroneyes.mineral.Core.Coffre.Coffres.CoffreArene;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.House;
import fr.synchroneyes.mineral.Core.Referee.Items.RefereeItemTemplate;
import fr.synchroneyes.mineral.Exception.EventAlreadyHandledException;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.ErrorReporting.Error;
import fr.synchroneyes.mineral.Utils.Radius;
import fr.synchroneyes.mineral.mineralcontest;
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


            // On vérifie si le joueur ferme un coffre animé
            try {
                AnimatedChestInventoryCloseEvent(event);
            } catch (EventAlreadyHandledException e) {
                return;
            }

            try {
                MapVoteMenuInventoryCloseEvent(event);
            } catch (EventAlreadyHandledException e) {
                return;
            }


            Game partie = mineralcontest.getWorldGame(worldEvent);
            // Si la game est démarrée
            if ((partie != null) && partie.isGameStarted() && !partie.isGamePaused() && !partie.isPreGame()) {

                if (!partie.groupe.getMonde().equals(worldEvent)) {
                    Bukkit.getLogger().severe("onChestClose L40");
                    return;
                }

                AutomatedChestAnimation coffreArene = partie.getArene().getCoffre();
                Player player = (Player) event.getPlayer();


                // si l'inventaire provient d'un coffre
                if (event.getInventory().getHolder() instanceof Chest) {
                    Block openedInventoryBlock = ((Chest) event.getInventory().getHolder()).getBlock();
                    Chest openedChest = ((Chest) event.getInventory().getHolder());
                    // Si le coffre fermé est le coffre d'arène
                    if (openedInventoryBlock.getLocation().equals(coffreArene.getLocation())) {
                        coffreArene.closeInventory();
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

                AutomatedChestAnimation arenaChest = partie.getArene().getCoffre();
                if (arenaChest != null) {
                    if (event.getEntity().getItemStack().getType().equals(Material.CHEST))
                        if (Radius.isBlockInRadius(arenaChest.getLocation(), event.getEntity().getLocation(), 2))
                            event.setCancelled(true);
                }
            }
        }



    }

    @EventHandler
    public void onChestOpen(InventoryOpenEvent event) throws Exception {


        // On vérifie si c'est un event d'ouverture de coffre animé, si c'est le cas, on s'arrête
        try {
            AnimatedChestOpenInventoryEvent(event);
        } catch (EventAlreadyHandledException e) {
            return;
        }

        World world = event.getPlayer().getWorld();
        Game game = mineralcontest.getWorldGame(world);
        if (game == null) return;
        if (mineralcontest.isAMineralContestWorld(world)) {

            if (!game.groupe.getMonde().equals(world)) {
                Bukkit.getLogger().severe("InventoryOpenEvent L141");
                return;
            }

            Player player = (Player) event.getPlayer();
            AutomatedChestAnimation arenaChest = game.getArene().getCoffre();
            if (event.getInventory().getHolder() instanceof Chest) {

                Chest openedChest = (Chest) event.getInventory().getHolder();
                Block openedChestBlock = openedChest.getBlock();

                // the inventory opened comes from a chest.
                if (!game.isThisBlockAGameChest(openedChestBlock)) {
                    openedChest.getInventory().clear();
                }

            }
        }

    }


    @EventHandler
    public void onItemClick(InventoryClickEvent event) throws Exception {
        if (event.getWhoClicked() instanceof Player) {


            // Si on est dans un coffre animé
            try {
                AnimatedChestInventoryClickEvent(event);
            } catch (EventAlreadyHandledException e) {
                return;
            }

            // Si on est dans l'inventaire de choix de map

            try {
                MapVoteClickOnItem(event);
            } catch (EventAlreadyHandledException e) {
                event.setCancelled(true);
                return;
            }

            Player joueur = (Player) event.getWhoClicked();
            Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);
            if (playerGroup == null) return;
            if (playerGroup.getGame() == null) return;

            Game partie = playerGroup.getGame();
            Inventory clickedInventory = event.getInventory();

            if (partie.isGameStarted()) {
                if (partie.getArene().getCoffre().getLocation().getBlock().getState() instanceof Chest) {
                    Chest arenaChest = (Chest) (partie.getArene().getCoffre().getLocation().getBlock().getState());
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


    /**
     * Evenement appelé lors de l'ouverture d'un coffre, cet évènement sert à gérer l'ouverture des coffres d'animations
     *
     * @param event
     */
    public void AnimatedChestOpenInventoryEvent(InventoryOpenEvent event) throws EventAlreadyHandledException {
        if (event.getPlayer() instanceof Player) {
            Player joueur = (Player) event.getPlayer();
            Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);

            if (playerGroup == null) return;

            // On applique cet évenement uniquement aux joueurs du plugin
            if (mineralcontest.isInAMineralContestWorld(joueur)) {


                // On récupère la location du bloc contenant cet inventaire
                Location chestLocation = event.getInventory().getLocation();
                if (chestLocation == null) return;

                Block chest = chestLocation.getBlock();


                // Si le coffre ouvert fait parti des blocs d'animation
                if (playerGroup.getAutomatedChestManager().isThisBlockAChestAnimation(chest)) {


                    event.setCancelled(true);
                    // On récupère son instance
                    AutomatedChestAnimation automatedChestAnimation = playerGroup.getAutomatedChestManager().getChestAnomation(chest);


                    if (automatedChestAnimation == null) return;

                    if (automatedChestAnimation.isBeingOpened()) return;


                    if (automatedChestAnimation.getClass().equals(CoffreArene.class))
                        if (playerGroup.getGame().getArene().isChestSpawned())
                            automatedChestAnimation.setOpeningPlayer(joueur);
                        else return;
                    else automatedChestAnimation.setOpeningPlayer(joueur);


                    // Et on joue l'animation
                    throw new EventAlreadyHandledException();

                }
            }
        }
    }


    /**
     * Evenement appelé lors de la fermeture d'un coffre
     *
     * @param event
     */
    public void AnimatedChestInventoryCloseEvent(InventoryCloseEvent event) throws EventAlreadyHandledException {

        if (event.getPlayer() instanceof Player) {
            Player joueur = (Player) event.getPlayer();
            Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);

            if (playerGroup == null) return;

            // On applique cet évenement uniquement aux joueurs du plugin
            if (mineralcontest.isInAMineralContestWorld(joueur)) {

                // ON récupère l'inventaire fermé
                Inventory openedInventory = event.getInventory();
                if (playerGroup.getAutomatedChestManager().isThisAnAnimatedInventory(openedInventory)) {
                    // C'est un inventaire avec animation
                    AutomatedChestAnimation automatedChestAnimation = playerGroup.getAutomatedChestManager().getFromInventory(openedInventory);
                    if (automatedChestAnimation == null) return;

                    // Et on ferme l'inventaire
                    automatedChestAnimation.closeInventory();
                    throw new EventAlreadyHandledException();
                }
            }


        }
    }


    /**
     * Fonction appelée lors du clic sur un item de l'inventaire
     *
     * @param event
     * @throws EventAlreadyHandledException
     */
    public void AnimatedChestInventoryClickEvent(InventoryClickEvent event) throws EventAlreadyHandledException {
        if (event.getWhoClicked() instanceof Player) {
            Player joueur = (Player) event.getWhoClicked();
            Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);

            if (playerGroup == null) return;

            // On applique cet évenement uniquement aux joueurs du plugin
            if (mineralcontest.isInAMineralContestWorld(joueur)) {

                // ON récupère l'inventaire fermé
                Inventory openedInventory = event.getInventory();
                if (playerGroup.getAutomatedChestManager().isThisAnAnimatedInventory(openedInventory)) {
                    // C'est un inventaire avec animation
                    AutomatedChestAnimation automatedChestAnimation = playerGroup.getAutomatedChestManager().getFromInventory(openedInventory);
                    if (automatedChestAnimation == null) return;

                    if (!automatedChestAnimation.isAnimationOver()) {
                        event.setCancelled(true);
                        throw new EventAlreadyHandledException();

                    }
                }
            }


        }
    }

    /**
     * Fonction qui gère les clics sur une map lors du vote !
     *
     * @param event
     * @throws EventAlreadyHandledException
     */
    public void MapVoteClickOnItem(InventoryClickEvent event) throws EventAlreadyHandledException {
        // On ne s'occupe que des joueurs sur plugin
        if (event.getWhoClicked() instanceof Player) {
            Player joueur = (Player) event.getWhoClicked();
            Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);

            // Le joueur doit avoir un groupe !
            if (playerGroup == null) return;
            // ON récupère l'inventaire en question
            Inventory inventaire = event.getInventory();

            // On regarde si le groupe est en train de voter
            if (playerGroup.getEtatPartie() != Etats.VOTE_EN_COURS) return;

            if (event.getCurrentItem() == null) return;

            // On vérifie si c'est l'inventaire de vote
            if (inventaire.equals(playerGroup.getMapVote().getMenuVote().getInventory())) {
                // C'est l'inventaire en question
                MenuVote menuVote = playerGroup.getMapVote().getMenuVote();
                for (RefereeItemTemplate items : menuVote.getItems())
                    if (items.toItemStack().equals(event.getCurrentItem())) {
                        items.performClick(joueur);
                        joueur.closeInventory();
                        event.setCancelled(true);

                        throw new EventAlreadyHandledException();
                    }
            }
        }
    }

    /**
     * Fonction appelée lors de la fermeture d'un évenement.
     * On vérifie que le joueur a voté ou non, si c'est le cas on réouvre l'inventaire
     *
     * @param event
     */
    public void MapVoteMenuInventoryCloseEvent(InventoryCloseEvent event) throws EventAlreadyHandledException {
        if (event.getPlayer() instanceof Player) {
            Player joueur = (Player) event.getPlayer();
            Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);

            if (playerGroup == null) return;
            if (playerGroup.getEtatPartie() != Etats.VOTE_EN_COURS) return;

            MapVote mapVote = playerGroup.getMapVote();

            // On est dans l'inventaire du choix de map
            if (event.getInventory().equals(mapVote.getMenuVote().getInventory())) {
                // Si le joueur n'a pas voté
                if (!mapVote.havePlayerVoted(joueur)) {
                    // On réouvre l'inventaire

                    Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> joueur.openInventory(event.getInventory()), 1);

                    // Et on dit qu'on a traité l'event
                    throw new EventAlreadyHandledException();
                }
            }

        }
    }
}
