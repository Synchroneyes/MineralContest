package fr.synchroneyes.mineral.DeathAnimations;

import fr.synchroneyes.custom_events.MCGameStartedEvent;
import fr.synchroneyes.custom_events.PlayerDeathByPlayerEvent;
import fr.synchroneyes.file_manager.FileList;
import fr.synchroneyes.mineral.DeathAnimations.Animations.*;
import fr.synchroneyes.mineral.Events.PlayerKilledByPlayer;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Menu permettant de gérer les animations de mort
 * Classe globale au plugin, pas besoin de spécifier un groupe/game
 */
public class DeathAnimationManager implements Listener {

    // Liste des animations disponible
    private List<DeathAnimation> liste_animations;


    // Liste des animations par joueurs
    private HashMap<Player, DeathAnimation> animation_par_joueur;


    private Inventory inventaireSelectionAnimation;

    private File fichier_data;


    public DeathAnimationManager() {

        Bukkit.getLogger().info(mineralcontest.prefix + "Enabling death animation manager");
        this.liste_animations = new LinkedList<>();
        this.animation_par_joueur = new HashMap<>();

        this.initAnimations();

        this.inventaireSelectionAnimation = Bukkit.createInventory(null, 9, "Selection d'une animation de mort");

        for(DeathAnimation animation : liste_animations)
            inventaireSelectionAnimation.addItem(animation.toItemStack());

        Bukkit.getPluginManager().registerEvents(this, mineralcontest.plugin);

        // Vérification si le fichier data existe ou non
        fichier_data = new File(mineralcontest.plugin.getDataFolder(), FileList.DeathAnimation_DataFile.toString());

    }

    /**
     * Méthode ajoutant les animations dispos;
     */
    private void initAnimations() {
        liste_animations.add(new EnderDomeAnimation());
        liste_animations.add(new HalloweenHurricaneAnimation());
        liste_animations.add(new LavaSpiderAnimation());
        liste_animations.add(new WaterSpiderAnimation());
        liste_animations.add(new SmokeAnimation());
    }

    /**
     * Méthode permettant d'affecter à un joueur, une animation
     * @param player
     * @param animation
     */
    private void setPlayerAnimation(Player player, DeathAnimation animation) {
        if(animation_par_joueur.containsKey(player)) animation_par_joueur.replace(player, animation);
        else animation_par_joueur.put(player, animation);

        savePlayerAnimation(player, animation);
    }

    /**
     * Méthode permettant de récupérer l'animation d'un joueur
     * @param p
     * @return
     */
    private DeathAnimation getPlayerAnimation(Player p){
        if(animation_par_joueur.containsKey(p)) return animation_par_joueur.get(p);
        return null;
    }


    /**
     * Menu permettant d'ouvrir le menu de selection d'animation
     * @param p
     */
    public void openMenuSelection(Player p) {
        p.openInventory(inventaireSelectionAnimation);
    }


    public void loadAnimationData() {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(fichier_data);
        for(String player_uid : configuration.getKeys(false)){
            if(Bukkit.getPlayer(UUID.fromString(player_uid)) != null){
                DeathAnimation animation = getAnimationFromString(configuration.get(player_uid).toString());
                if(animation == null) continue;


                setPlayerAnimation(Bukkit.getPlayer(UUID.fromString(player_uid)), animation);
            }
        }
    }

    private DeathAnimation getAnimationFromString(String name) {
        for(DeathAnimation animation : liste_animations)
            if(animation.getClass().getSimpleName().equals(name)) return animation;

        return null;
    }


    public void savePlayerAnimation(Player player, DeathAnimation animation){

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(fichier_data);
        configuration.set(player.getUniqueId().toString(), animation.getClass().getSimpleName());


        try {
            configuration.save(fichier_data);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @EventHandler
    public void onGameStart(MCGameStartedEvent event) {
        loadAnimationData();
    }

    @EventHandler
    public void onPlayerAnimationSelected(InventoryClickEvent event) {

        if(!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player joueur = (Player) event.getWhoClicked();

        // Vérification du bon inventaire
        if(event.getInventory().equals(inventaireSelectionAnimation)) {
            // On récupère l'animation
            if(event.getCurrentItem() == null) return;

            for(DeathAnimation animation : liste_animations) {
                if (animation.toItemStack().equals(event.getCurrentItem())) {
                    // ON affecte l'animation au joueur
                    setPlayerAnimation(joueur, animation);
                    joueur.sendMessage(mineralcontest.prefixPrive + "Vous avez sélectionné l'animation: " + animation.getAnimationName());
                    joueur.closeInventory();
                    return;
                }
            }

        }
    }

    @EventHandler
    public void onPlayerKilled(PlayerDeathByPlayerEvent playerEvent) {
        if(getPlayerAnimation(playerEvent.getKiller()) == null) return;

        // On joue l'animation de mort
        DeathAnimation animation = getPlayerAnimation(playerEvent.getKiller());
        animation.playAnimation(playerEvent.getPlayerDead());
    }








}
