package fr.synchroneyes.mineral.Kits.Classes;

import fr.synchroneyes.custom_events.MCGameStartedEvent;
import fr.synchroneyes.custom_events.MCPlayerRespawnEvent;
import fr.synchroneyes.custom_events.PlayerKitSelectedEvent;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Kits.KitAbstract;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.List;
import java.util.Random;

/**
 * Spawn avec 15 niveau d'experience, 32 lapis lazuli, et 3 livre d'enchantement
 * Il reçoit ses bonus uniquement au démarrage de la partie
 */
public class Enchanteur extends KitAbstract {


    // Nombre de niveau au respawn
    private int niveauxExpRespawn = 15;

    // Nombre de lapis au respawn
    private int nombreLapisRespawn = 32;

    // Nombre de livre au respawn
    private int nombreLivreEnchant = 3;

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


    /**
     * Fonction appelé lors de la selection de ce kit par le joueur
     *
     * @param event
     */
    @EventHandler
    public void onKitSelected(PlayerKitSelectedEvent event) {

        //if (!isPlayerUsingThisKit(event.getPlayer())) return;
        //applyKitEffectToPlayer(event.getPlayer());

    }


    /**
     * Fonction appelé lors de la selection de ce kit par le joueur
     *
     * @param event
     */
    @EventHandler
    public void onPlayerRespawn(MCPlayerRespawnEvent event) {
        //if (!isPlayerUsingThisKit(event.getJoueur())) return;
        //applyKitEffectToPlayer(event.getJoueur());
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


    /**
     * Permet de donner les bonus de ce kit au joueur
     *
     * @param joueur
     */
    private void applyKitEffectToPlayer(Player joueur) {

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
        }


        joueur.setLevel(niveauxExpRespawn);
        joueur.getInventory().addItem(new ItemStack(Material.LAPIS_LAZULI, nombreLapisRespawn));
    }
}
