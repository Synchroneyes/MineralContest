package fr.synchroneyes.mineral.Kits.Classes;

import fr.synchroneyes.custom_events.MCGameStartedEvent;
import fr.synchroneyes.custom_events.MCPlayerRespawnEvent;
import fr.synchroneyes.custom_events.PlayerDeathByPlayerEvent;
import fr.synchroneyes.file_manager.FileList;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Events.PlayerKilledByPlayer;
import fr.synchroneyes.mineral.Kits.KitAbstract;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.io.File;
import java.util.*;

/**
 * Spawn avec 15 niveau d'experience, 32 lapis lazuli, et 3 livre d'enchantement
 * Il reçoit ses bonus uniquement au démarrage de la partie
 */
public class Enchanteur extends KitAbstract {

    // On doit stocker les niveaux de chaque enchanteur
    private HashMap<Player, Integer> niveaux_joueurs;

    // Nombre de niveau au respawn
    private int niveauxExpRespawn = 15;

    // Nombre de lapis au respawn
    private int nombreLapisRespawn = 32;

    // Nombre de livre au respawn
    private int nombreLivreEnchant = 5;

    public Enchanteur() {
        this.niveaux_joueurs = new HashMap<>();
    }

    @Override
    public String getNom() {
        return Lang.kit_wizard_title.toString();
    }

    @Override
    public String getDescription() {
        return Lang.kit_wizard_description.toString();
    }

    @Override
    public Material getRepresentationMaterialForSelectionMenu() {
        return Material.ENCHANTING_TABLE;
    }



    @EventHandler
    public void OnGameStarted(MCGameStartedEvent event) {
        Game partie = event.getGame();

        // On récupère les joueurs
        List<Player> joueurs = partie.groupe.getPlayers();

        // On regarde pour chaque joueur si ils ont ce kit
        for (Player joueur : joueurs)
            // Si le joueur possède le kit
            if (isPlayerUsingThisKit(joueur))
                // On lui applique les effets!
                applyKitEffectToPlayer(joueur);

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathByPlayerEvent event) {
        Player deadPlayer = event.getPlayerDead();

        if(!isPlayerUsingThisKit(deadPlayer)) return;

        if(niveaux_joueurs.containsKey(deadPlayer)) niveaux_joueurs.replace(deadPlayer, deadPlayer.getLevel());
        else niveaux_joueurs.put(deadPlayer, deadPlayer.getLevel());
    }

    @EventHandler
    public void onPlayerRespawn(MCPlayerRespawnEvent event) {
        if(!isPlayerUsingThisKit(event.getJoueur())) return;

        if(!niveaux_joueurs.containsKey(event.getJoueur())) return;

        event.getJoueur().setLevel(niveaux_joueurs.get(event.getJoueur()));
    }


    /**
     * Permet de donner les bonus de ce kit au joueur
     *
     * @param joueur
     */
    private void applyKitEffectToPlayer(Player joueur) {
        /*Enchantment[] enchantments = {Enchantment.DURABILITY, Enchantment.DAMAGE_ALL, Enchantment.PROTECTION_FIRE, Enchantment.PROTECTION_PROJECTILE, Enchantment.KNOCKBACK, Enchantment.LOOT_BONUS_BLOCKS};

        // On va généré nos livres
        ItemStack[] items = new ItemStack[nombreLivreEnchant];

        for (int indexLivre = 0; indexLivre < nombreLivreEnchant; ++indexLivre) {
            ItemStack livre = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) livre.getItemMeta();

            Random random = new Random();
            int niveauGenere = 0;
            Enchantment enchantmentGenere = enchantments[random.nextInt(enchantments.length)];

            niveauGenere = random.nextInt((niveamMaxEnchantement - niveauMinEnchantement) + 1) + niveauMinEnchantement;

            meta.addStoredEnchant(enchantmentGenere, niveauGenere, true);
            livre.setItemMeta(meta);


            joueur.getInventory().addItem(livre);
        }*/

        // On charge le fichier de config
        File fichierConfig = new File(mineralcontest.plugin.getDataFolder(), FileList.Kit_Enchanteur_ConfigFile.toString());

        // On vérifie si il existe pas
        // Si il n'existe pas, on charge les valeurs par défaut
        if(!fichierConfig.exists()) {
            applyDefaultEffects(joueur);
            return;
        }

        // Le fichier existe, on va donc le lire
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(fichierConfig);

        // On charge les valeurs
        int book_count = -1, book_min_level = -1, book_max_level = -1, lapis_count = -1, level_on_spawn = -1;
        lapis_count = Integer.parseInt(Objects.requireNonNull(configuration.get("lapis_on_game_start")).toString());
        book_count = Integer.parseInt(Objects.requireNonNull(configuration.get("enchantment_book_count")).toString());
        book_min_level = Integer.parseInt(Objects.requireNonNull(configuration.get("enchantment_book_min_level")).toString());
        book_max_level = Integer.parseInt(Objects.requireNonNull(configuration.get("enchantment_book_max_level")).toString());
        level_on_spawn = Integer.parseInt(Objects.requireNonNull(configuration.get("level_on_game_start")).toString());

        // Si une des valeurs n'est pas valide, on applique les effets par défauts
        if(book_count == -1 || book_min_level <= 0 || book_max_level <= 0 || lapis_count == -1 || level_on_spawn <= 0) {
            applyDefaultEffects(joueur);
            return;
        }

        // On interverti le min et le max (juste au cas où)
        int min = Math.min(book_max_level, book_min_level);
        int max = Math.max(book_max_level, book_min_level);

        book_max_level = max;
        book_min_level = min;

        // On lit les enchantments dispo
        List<String> enchantments = configuration.getStringList("enchantment_available");
        List<Enchantment> enchantements_list = new ArrayList<>();

        for(String enchant : enchantments) {
            enchantements_list.add(new EnchantmentWrapper(enchant));
        }

        // On applique les valeurs
        // On donne le niveau au joueur
        joueur.setLevel(level_on_spawn);

        // ON lui donne le lapis
        joueur.getInventory().addItem(new ItemStack(Material.LAPIS_LAZULI, lapis_count));

        // On lui donne les livres
        for (int indexLivre = 0; indexLivre < book_count; ++indexLivre) {
            ItemStack livre = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) livre.getItemMeta();

            Random random = new Random();
            int niveauGenere = 0;

            niveauGenere = random.nextInt((book_max_level - book_min_level) + 1) + book_min_level;

            Enchantment enchantment_genere = enchantements_list.get(new Random().nextInt(enchantements_list.size()));

            // On ajoute un enchantement
            meta.addStoredEnchant(enchantment_genere, niveauGenere, true);
            livre.setItemMeta(meta);


            joueur.getInventory().addItem(livre);
        }


    }


    /**
     * Cette méthode permet d'appliquer les effets de base du kit enchanteur, lorsque le fichier de configuration n'existe pas
     * @param joueur
     */
    private void applyDefaultEffects(Player joueur) {
        // Dans le cas où il n'existe pas, on prend les valeurs par défauts
        int niveauMinEnchantement = 1;
        int niveamMaxEnchantement = 3;
        Enchantment[] enchantments = {Enchantment.DURABILITY, Enchantment.DAMAGE_ALL, Enchantment.PROTECTION_FIRE, Enchantment.PROTECTION_PROJECTILE, Enchantment.KNOCKBACK, Enchantment.LOOT_BONUS_BLOCKS};

        // On va généré nos livres
        ItemStack[] items = new ItemStack[nombreLivreEnchant];

        for (int indexLivre = 0; indexLivre < nombreLivreEnchant; ++indexLivre) {
            ItemStack livre = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) livre.getItemMeta();

            Random random = new Random();
            int niveauGenere = 0;
            Enchantment enchantmentGenere = enchantments[random.nextInt(enchantments.length)];

            niveauGenere = random.nextInt((niveamMaxEnchantement - niveauMinEnchantement) + 1) + niveauMinEnchantement;

            meta.addStoredEnchant(enchantmentGenere, niveauGenere, true);
            livre.setItemMeta(meta);


            joueur.getInventory().addItem(livre);
            joueur.setLevel(niveauxExpRespawn);
            joueur.getInventory().addItem(new ItemStack(Material.LAPIS_LAZULI, nombreLapisRespawn));
            return;
        }
    }
}
