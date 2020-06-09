package fr.mineral.Core.Referee;


import fr.mineral.Core.Game.Game;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RefereeInventory {
    private Inventory inventory;

    public static RefereeInventory instance;

    public RefereeInventory() {
        this.inventory = Bukkit.createInventory(null, 9, "Menu Arbitrage");
        RefereeInventory.instance = this;
        fillInventory();
    }

    private void fillInventory() {

        // TODO

        /*Game game = mineralcontest.getPlayerGame(joueur);
        inventory.clear();
        /*
                        GREEN_CONCRETE : START / RESUME
                        YELLOW CONCRETE: PAUSE
                        RED CONCRETE: STOP
                        BLUE CONCRETE: LEADERBOARD
                        PINK CONCRETE: SPAWN ARENA CHEST
                        BROWN CONCRETE: START VOTE

                     */
        /*ItemStack StartOrResume = new ItemStack(Material.GREEN_CONCRETE, 1);
        ItemStack Pause = new ItemStack(Material.YELLOW_CONCRETE, 1);
        //ItemStack Stop = new ItemStack(Material.RED_CONCRETE, 1);
        ItemStack LeaderBoard = new ItemStack(Material.BLUE_CONCRETE, 1);
        ItemStack ArenaChest = new ItemStack(Material.PINK_CONCRETE, 1);
        ItemStack StartVote = new ItemStack(Material.BROWN_CONCRETE, 1);
        ItemStack ForceVote = new ItemStack(Material.BLACK_CONCRETE, 1);

        ItemMeta meta;
        meta = StartOrResume.getItemMeta();

        if(game.isGameStarted()) meta.setDisplayName(Lang.referee_item_resume.toString());
        else meta.setDisplayName(Lang.referee_item_start.toString());
        StartOrResume.setItemMeta(meta);

        meta = Pause.getItemMeta();
        meta.setDisplayName(Lang.referee_item_pause.toString());
        Pause.setItemMeta(meta);

        /*meta = Stop.getItemMeta();
        meta.setDisplayName("Stop game");
        Stop.setItemMeta(meta);*/

        /*meta = LeaderBoard.getItemMeta();
        meta.setDisplayName(Lang.referee_item_leaderboard.toString());
        LeaderBoard.setItemMeta(meta);

        meta = ArenaChest.getItemMeta();
        meta.setDisplayName(Lang.referee_item_spawn_arena_chest.toString());
        ArenaChest.setItemMeta(meta);


        meta = StartVote.getItemMeta();
        meta.setDisplayName(Lang.referee_item_start_vote.toString());
        StartVote.setItemMeta(meta);

        meta = ForceVote.getItemMeta();
        meta.setDisplayName(Lang.referee_item_force_biome.toString());
        ForceVote.setItemMeta(meta);

        if(mineralcontest.getPlayerGame(joueur).isGameStarted()) {
            inventory.addItem(StartOrResume, Pause,
                    //Stop,
                    ArenaChest,
                    LeaderBoard);
        } else {
            inventory.addItem(StartOrResume, StartVote, ForceVote);
        }*/

    }



    public static Inventory getInventory() {
        if(instance == null) {
            new RefereeInventory();
        }

        RefereeInventory refereeInventory = RefereeInventory.instance;
        refereeInventory.fillInventory();

        return refereeInventory.inventory;
    }
}
