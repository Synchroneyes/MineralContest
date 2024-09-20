package fr.synchroneyes.special_events.halloween2024.game_events;

import fr.synchroneyes.custom_events.MCAutomatedChestTimeOverEvent;
import fr.synchroneyes.custom_events.MCPlayerOpenChestEvent;
import fr.synchroneyes.custom_events.MCPlayerRespawnEvent;
import fr.synchroneyes.custom_events.PlayerDeathByPlayerEvent;
import fr.synchroneyes.file_manager.FileList;
import fr.synchroneyes.mineral.Core.Coffre.AutomatedChestAnimation;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.mineralcontest;
import fr.synchroneyes.special_events.halloween2024.chests.ParkourChest;
import fr.synchroneyes.special_events.halloween2024.utils.ClonedInventory;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HellParkourEvent extends HalloweenEvent implements Listener {

    @Getter
    private Game game;

    private List<Block> blocks;

    private Location parkourSpawnLocation;

    private Location parkourChestLocation;

    private boolean isEnabled = false;

    private int lavaHeight = 252;

    private BukkitTask lavaLoop;

    private List<Player> playersAlive;

    private HashMap<Player, ClonedInventory> playersInventory;

    private List<Player> playerWithoutInventory;

    private ParkourChest parkourChest;


    public HellParkourEvent(Game partie) {
        super(partie);
        this.game = partie;
        this.blocks = new ArrayList<>();
        this.playersAlive = new ArrayList<>();
        this.playersInventory = new HashMap<>();
        this.playerWithoutInventory = new ArrayList<>();
        this.parkourChest = new ParkourChest(54, partie.groupe.getAutomatedChestManager());
        Bukkit.getPluginManager().registerEvents(this, mineralcontest.plugin);
    }


    @Override
    public String getEventName() {
        return "HellParkour";
    }

    @Override
    public void executionContent() {
        for(Player player : this.game.groupe.getPlayers()) {
            player.teleport(this.parkourSpawnLocation);
        }
    }

    @Override
    public void beforeExecute() {

        for(Player player: this.game.groupe.getPlayers()) {
            this.playersInventory.put(player, new ClonedInventory(player.getInventory()));
            this.playerWithoutInventory.add(player);
            player.getInventory().clear();
            player.sendMessage(mineralcontest.prefixPrive + "[???] J'ai ENCORE appuyé sur ce bouton, cette fois-ci ça va être un vrai massacre... Des lags sont à prévoir pendant un instant...");

        }

        this.playersAlive.addAll(this.getPartie().groupe.getPlayers());

        World world = this.getPartie().groupe.getMonde();
        int xLocation = 20000;
        int yLocation = 250;
        int zLocation = 20000;

        File parkourContent = new File(mineralcontest.plugin.getDataFolder(), FileList.Halloween_Parkour.toString());
        YamlConfiguration parkourContentConfig = YamlConfiguration.loadConfiguration(parkourContent);
        ConfigurationSection blocksSection = parkourContentConfig.getConfigurationSection("blocks");
        for(String blockId : blocksSection.getKeys(false)) {
            //arenaContentConfig.get("blocks." + blockId + ".x").toString());
            int x = Integer.parseInt(parkourContentConfig.get("blocks." + blockId + ".x").toString())+10;
            int y = Integer.parseInt(parkourContentConfig.get("blocks." + blockId + ".y").toString())+150;
            int z = Integer.parseInt(parkourContentConfig.get("blocks." + blockId + ".z").toString())+10;
            String itemType = parkourContentConfig.get("blocks." + blockId + ".type").toString();


            Material itemTypeMaterial = Material.valueOf(itemType);
            world.getBlockAt(xLocation + x, yLocation + y, zLocation + z).setType(itemTypeMaterial);
            this.blocks.add(world.getBlockAt(xLocation + x, yLocation + y, zLocation + z));


            if(itemTypeMaterial == Material.OAK_PLANKS) {
                this.parkourSpawnLocation = new Location(world, xLocation + x, yLocation + y+2, zLocation + z);
            }

            if(itemTypeMaterial == Material.CHEST) {
                this.parkourChestLocation = new Location(world, xLocation + x, yLocation + y, zLocation + z);
                this.parkourChest.setChestLocation(this.parkourChestLocation);
                this.parkourChest.spawn();
            }



        }
    }

    @Override
    public void afterExecute() {
        this.isEnabled = true;

        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
            this.lavaLoop = Bukkit.getScheduler().runTaskTimer(mineralcontest.plugin, () -> {
                if(this.isEnabled) {
                    addLava();
                    this.lavaHeight++;
                }
            }, 0, 5*20);
        }, 8*20);

    }

    @Override
    public String getEventTitle() {
        return "Parkour de l'enfer";
    }

    @Override
    public String getEventDescription() {
        return "Atteindrez-vous le bout du parcours ?";
    }

    @Override
    public boolean isTextMessageNotificationEnabled() {
        return false;
    }

    public boolean isPlayerOnParkour(Player player) {
        Location playerLocation = player.getLocation();
        int xLocation = 20000;
        int yLocation = 250;
        int zLocation = 20000;

        return (playerLocation.getBlockX() >= xLocation && playerLocation.getBlockX() <= xLocation+20 &&
                playerLocation.getBlockZ() >= zLocation && playerLocation.getBlockZ() <= zLocation+20 &&
                playerLocation.getBlockY() >= yLocation && playerLocation.getBlockY() <= yLocation+25);
    }

    private void addLava() {

        if(this.lavaHeight > 275) {
            this.lavaLoop.cancel();
            return;
        }

        World world = this.getPartie().groupe.getMonde();
        int xLocation = 20001;
        int zLocation = 20001;

        for(int x = xLocation; x < xLocation + 19; x++) {
            for(int z = zLocation; z < zLocation + 19; z++) {
                world.getBlockAt(x, lavaHeight, z).setType(Material.LAVA);
                this.blocks.add(world.getBlockAt(x, lavaHeight, z));
            }
        }
    }


    private void cleanParkour() {
        this.lavaLoop.cancel();
        for(Block block : this.blocks) {
            block.setType(Material.AIR);
        }

        for(Player player : this.playersAlive) {
            MCPlayer mcPlayer = mineralcontest.plugin.getMCPlayer(player);
            player.teleport(mcPlayer.getEquipe().getMaison().getHouseLocation());

            resetPlayerInventory(player);
            this.playerWithoutInventory.remove(player);
        }

        this.playersAlive.clear();
        this.isEnabled = false;
    }

    private void resetPlayerInventory(Player player){
        PlayerInventory inventory = this.playersInventory.get(player).reset();
        player.getInventory().setArmorContents(inventory.getArmorContents());
        player.getInventory().setContents(inventory.getContents());
        player.getInventory().setExtraContents(inventory.getExtraContents());

        this.playersInventory.remove(player);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event){
        if(!this.isEnabled) return;

        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        if(!isPlayerOnParkour(player)) return;

        if(!event.getCause().equals(EntityDamageEvent.DamageCause.FALL) && !event.getCause().equals(EntityDamageEvent.DamageCause.LAVA)) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathByPlayerEvent event){
        if(!this.isEnabled) return;

        if(!isPlayerOnParkour(event.getPlayerDead())) return;

        Player player = event.getPlayerDead();
        MCPlayer mcPlayer = mineralcontest.plugin.getMCPlayer(player);
        Equipe equipe = mcPlayer.getEquipe();
        equipe.retirerPoints(100);
        player.sendMessage(mineralcontest.prefixPrive + "[???] J'ai triomphé. Vous n'êtes qu'une bande de nullard sans talent.");
        player.sendMessage(mineralcontest.prefixPrive + "Vous avez fait perdre" + ChatColor.RED + " -100 points" + ChatColor.RESET + " à votre équipe.");

        this.playersAlive.remove(player);
        if(this.playersAlive.isEmpty()) cleanParkour();
    }

    @EventHandler
    public void onPlayerSpawn(MCPlayerRespawnEvent event) {

        if(this.playersAlive.contains(event.getJoueur())) {
            event.getJoueur().teleport(this.parkourSpawnLocation);
            this.playerWithoutInventory.remove(event.getJoueur());
            return;
        }



        if(playerWithoutInventory.contains(event.getJoueur())) {
            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
                event.getJoueur().sendMessage(mineralcontest.prefixPrive + "Vous avez récupéré votre inventaire.");
                event.getJoueur().sendMessage(mineralcontest.prefixPrive + ChatColor.RED + "Si vous n'avez aucun item, suicidez-vous dans la lave de l'arène.");
                resetPlayerInventory(event.getJoueur());
                playerWithoutInventory.remove(event.getJoueur());

            }, 20*2);
        }
    }

    @EventHandler
    public void onChestOpenEvent(MCPlayerOpenChestEvent event){
        if(! event.getCoffre().equals(this.parkourChest)) return;
        cleanParkour();
        this.parkourChest.remove();
    }

}
