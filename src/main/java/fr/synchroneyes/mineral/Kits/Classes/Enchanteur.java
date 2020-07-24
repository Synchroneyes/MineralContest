package fr.synchroneyes.mineral.Kits.Classes;

import fr.synchroneyes.custom_events.MCPlayerRespawnEvent;
import fr.synchroneyes.custom_events.PlayerKitSelectedEvent;
import fr.synchroneyes.mineral.Kits.KitAbstract;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Random;

/**
 * Spawn avec 15 niveau d'experience, 32 lapis lazuli, et 3 livre d'enchantement
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
        return "Enchanteur";
    }

    @Override
    public String getDescription() {
        return "Vous réapparaissez avec 15 niveaux d'experience, 32 lapis lazuli, et 3 livres d'enchantement";
    }


    /**
     * Fonction appelé lors de la selection de ce kit par le joueur
     *
     * @param event
     */
    @EventHandler
    public void onKitSelected(PlayerKitSelectedEvent event) {

        if (!isPlayerUsingThisKit(event.getPlayer())) return;
        applyKitEffectToPlayer(event.getPlayer());

    }


    /**
     * Fonction appelé lors de la selection de ce kit par le joueur
     *
     * @param event
     */
    @EventHandler
    public void onPlayerRespawn(MCPlayerRespawnEvent event) {

        Bukkit.getLogger().info("Called MCPlayerRespawnEvent");
        if (!isPlayerUsingThisKit(event.getJoueur())) return;
        applyKitEffectToPlayer(event.getJoueur());
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

        joueur.sendMessage("APPLIED");
    }
}
