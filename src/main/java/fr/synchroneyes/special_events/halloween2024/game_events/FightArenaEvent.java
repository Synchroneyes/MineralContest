package fr.synchroneyes.special_events.halloween2024.game_events;

import fr.synchroneyes.custom_events.MCBossKilledByPlayerEvent;
import fr.synchroneyes.custom_events.MCMassBlockSpawnEndedEvent;
import fr.synchroneyes.custom_events.MCPlayerRespawnEvent;
import fr.synchroneyes.custom_events.PlayerDeathByPlayerEvent;
import fr.synchroneyes.file_manager.FileList;
import fr.synchroneyes.mineral.Core.Boss.Boss;
import fr.synchroneyes.mineral.Core.Boss.BossType.AngryZombie;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Utils.MassBlockSpawner;
import fr.synchroneyes.mineral.Utils.Player.PlayerUtils;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class FightArenaEvent extends HalloweenEvent implements Listener {

    private Location arenaLocation;
    private List<Player> alivePlayers;
    private Boolean enabled = false;
    private HashMap<Equipe, Location> teamSpawnLocation;
    private Location arenaCenter;
    private Boss boss;
    private List<Block> blocks;
    private boolean isEnabled = false;
    private MassBlockSpawner blockSpawner;
    private boolean blockSpawnEnded = false;

    public FightArenaEvent(Game partie) {
        super(partie);
        this.arenaLocation = partie.getArene().getCoffre().getLocation().clone();
        this.arenaLocation.setX(18000);
        this.arenaLocation.setY(222);
        this.arenaLocation.setZ(18000);

        this.alivePlayers = new LinkedList<>();
        this.teamSpawnLocation = new HashMap<>();
        this.blocks = new ArrayList<>();
        this.blockSpawner = new MassBlockSpawner();

        Bukkit.getPluginManager().registerEvents(this, mineralcontest.plugin);
    }

    @Override
    public String getEventName() {
        return "FightArena";
    }

    @Override
    public void executionContent() {
        if(!this.blockSpawnEnded) return;
        for(Player joueur : this.alivePlayers) {

            PlayerUtils.respawnPlayer(joueur);

            MCPlayer mcPlayer = mineralcontest.plugin.getMCPlayer(joueur);
            Equipe equipe = mcPlayer.getEquipe();
            Location spawnLocation = this.teamSpawnLocation.get(equipe);
            joueur.teleport(spawnLocation);
        }

        for(Player p : this.getPartie().groupe.getPlayers()) {
            if(getPartie().isReferee(p)) {
                p.teleport(arenaLocation);
            }
        }
    }

    @Override
    public void beforeExecute() {

        for(Player p : this.getPartie().groupe.getPlayers()) {
            if(getPartie().isReferee(p)) continue;
            alivePlayers.add(p);
        }


        for(Player player : alivePlayers) {
            player.sendMessage(mineralcontest.prefixPrive + "[???] J'ai appuyé sur un bouton, ça risque de ne pas être bon... Des lags sont à prévoir pendant un instant...");

        }

        List<Equipe> teams = new ArrayList<>();
        for(Player joueur : this.alivePlayers) {
            Equipe team = this.getPartie().getPlayerTeam(joueur);
            if(!teams.contains(team)) teams.add(team);
        }

        // Spawn arena

        World world = this.getPartie().groupe.getMonde();
        int xLocation = 18000;
        int yLocation = 250;
        int zLocation = 18000;

        File arenaContent = new File(mineralcontest.plugin.getDataFolder(), FileList.Halloween_Arena.toString());
        YamlConfiguration arenaContentConfig = YamlConfiguration.loadConfiguration(arenaContent);
        ConfigurationSection blocksSection = arenaContentConfig.getConfigurationSection("blocks");
        for(String blockId : blocksSection.getKeys(false)) {
            //arenaContentConfig.get("blocks." + blockId + ".x").toString());
            int x = Integer.parseInt(arenaContentConfig.get("blocks." + blockId + ".x").toString());
            int y = Integer.parseInt(arenaContentConfig.get("blocks." + blockId + ".y").toString());
            int z = Integer.parseInt(arenaContentConfig.get("blocks." + blockId + ".z").toString());
            String itemType = arenaContentConfig.get("blocks." + blockId + ".type").toString();

            Material itemTypeMaterial = Material.valueOf(itemType);
            this.blockSpawner.addBlock(new Location(world, xLocation + x, yLocation + y, zLocation + z), itemTypeMaterial);

            // If material is YELLOW WOOL, team spawn location
            if(itemTypeMaterial == Material.YELLOW_WOOL) {
                if(teams.isEmpty()) continue;
                Equipe _team = teams.get(0);
                teams.remove(_team);
                this.teamSpawnLocation.put(_team, new Location(world, xLocation + x, yLocation + y+1, zLocation + z));
            }

            if(itemTypeMaterial == Material.REDSTONE_BLOCK) {
                this.arenaCenter = new Location(world, xLocation + x, yLocation + y+2, zLocation + z);
            }




        }


        this.blockSpawner.setBlockPerBatch(500);
        this.blockSpawner.spawnBlocks();

    }

    @Override
    public void afterExecute() {
        // Spawn Boss

        if(!this.blockSpawnEnded) return;

        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
            AngryZombie boss = new AngryZombie();
            this.boss = boss;
            getPartie().getBossManager().spawnNewBoss(this.arenaCenter, boss);
        }, 20 * 4);

        this.isEnabled = true;


    }

    @Override
    public String getEventTitle() {
        return "Arène maudite...";
    }

    @Override
    public String getEventDescription() {
        return "Vous avez été envoyé dans l'arène maudite, combattez pour votre survie!";
    }

    @Override
    public boolean isTextMessageNotificationEnabled() {
        return true;
    }

    @Override
    public boolean isNotificationDelayed() {
        return true;
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathByPlayerEvent event){
        if(!this.isEnabled) return;

        this.alivePlayers.remove(event.getPlayerDead());

        if(this.getTeamLeftCount() == 0) {
            punishPlayers();
        }

        if(this.getTeamLeftCount() == 1 && !this.boss.isAlive()) {
            // Il ne reste plus qu'une équipe, on téléporte les joueurs en vie dans leur base et on leur offre 10 redstone, 4 fer, 3 or, 2 diamants et 1 emeraude
            sendRewardToPlayers();
        }


    }

    private int getTeamLeftCount() {
        int teamCount = 0;
        Set<String> teams = new HashSet<>();
        for(Player joueur : this.alivePlayers) {
            MCPlayer mcPlayer = mineralcontest.plugin.getMCPlayer(joueur);
            if(!teams.contains(mcPlayer.getEquipe().getNomEquipe())) {
                teams.add(mcPlayer.getEquipe().getNomEquipe());
                teamCount++;
            }
        }

        return teamCount;
    }

    @EventHandler
    public void onBossDeath(MCBossKilledByPlayerEvent event){
        if(!this.isEnabled) return;
        if(getTeamLeftCount() == 1) {
            if(this.boss.equals(event.getBoss())) {
                sendRewardToPlayers();
            }
        }
    }

    private void sendRewardToPlayers() {
        for(Player joueur : this.alivePlayers) {
            MCPlayer mcPlayer = mineralcontest.plugin.getMCPlayer(joueur);
            Equipe equipe = mcPlayer.getEquipe();
            joueur.teleport(equipe.getMaison().getHouseLocation());

            joueur.sendMessage(mineralcontest.prefixPrive + "Vous avez survécu à l'arène maudite! Vous avez été téléporté dans votre base. Vous avez également reçu une récompense.");

            joueur.getInventory().addItem(new ItemStack(Material.REDSTONE, 10));
            joueur.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 3));
            joueur.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 2));
            joueur.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));

        }

        cleanArena();
        this.boss.remove();

    }

    private void punishPlayers() {
        for(Player joueur : this.getPartie().groupe.getPlayers()) {
            if(getPartie().isReferee(joueur)) continue;

            MCPlayer mcPlayer = mineralcontest.plugin.getMCPlayer(joueur);
            Equipe equipe = mcPlayer.getEquipe();
            equipe.retirerPoints(100);

            joueur.sendMessage(mineralcontest.prefixPrive + "[???] J'ai triomphé. Vous n'êtes qu'une bande de nullard sans talent.");
            joueur.sendMessage(mineralcontest.prefixPrive + "Vous avez fait perdre " + ChatColor.RED + " -100 points" + ChatColor.RESET + " à votre équipe.");
        }

        cleanArena();
        this.boss.remove();

    }

    private void cleanArena() {
        this.blockSpawner.removeSpawnedBlocks();

        this.isEnabled = false;

        for(Player p: getPartie().groupe.getPlayers()) {
            this.boss.removePlayerBossBar(p);

            if(getPartie().isReferee(p)) {
                p.teleport(getPartie().getArene().getCoffre().getLocation());
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(MCPlayerRespawnEvent event){
        if(!this.enabled) return;
        if(this.alivePlayers.contains(event.getJoueur())) {
            event.getJoueur().teleport(this.teamSpawnLocation.get(mineralcontest.plugin.getMCPlayer(event.getJoueur()).getEquipe()));
        }
    }

    @EventHandler
    public void onBlockSpawnEnd(MCMassBlockSpawnEndedEvent event){
        if(event.getSpawner().equals(this.blockSpawner)) {
            this.blockSpawnEnded = true;
            sendEventNotification();
            executionContent();
            afterExecute();
        }

    }

}
