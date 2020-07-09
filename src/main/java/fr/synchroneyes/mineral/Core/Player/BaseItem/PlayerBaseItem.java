package fr.synchroneyes.mineral.Core.Player.BaseItem;

import fr.synchroneyes.file_manager.FileList;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;

/**
 * Classe gérant les items de base d'une partie, elle sera attachée à un groupe
 * De cette manière, chaque groupe pourra avoir des items de base différent! :)
 */
public class PlayerBaseItem {
    // Inventaire contenant les items de base de la partie
    private Inventory inventaire;
    private Groupe groupe;


    private PlayerInventory playerInventory = null;
    private Player openingPlayer = null;
    private boolean beingEdited = false;

    @Getter
    private ArrayList<ItemStack> items;

    public PlayerBaseItem(Groupe g) {
        inventaire = Bukkit.createInventory(null, InventoryType.CHEST, Lang.player_base_item_inventory_title.toString());
        this.groupe = g;
        this.items = new ArrayList<>();
        genererInventaireParDefaut();
    }

    /**
     * Permet de lire un fichier, et d'y copier son contenu dans l'inventaire
     */
    private void genererInventaireParDefaut() {
        File fichierParDefaut = new File(mineralcontest.plugin.getDataFolder(), FileList.Config_default_player_base_item.toString());
        if (!fichierParDefaut.exists()) {
            Bukkit.getLogger().severe(mineralcontest.prefix + FileList.Config_default_player_base_item.toString() + " doesnt exists");
            return;
        }

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fichierParDefaut);
        ConfigurationSection section = yamlConfiguration.getConfigurationSection("items");
        if (section != null) {
            for (String item_type : section.getKeys(false)) {
                ItemStack item = new ItemStack(Material.valueOf(item_type), Integer.parseInt(section.get(item_type).toString()));
                inventaire.addItem(item);
                items.add(item);
            }
        }


        // Item de sauvegarde du menu
        ItemStack item = new ItemStack(Material.GREEN_CONCRETE, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Lang.player_base_item_close_inventory_item_title.toString());
        item.setItemMeta(meta);
        inventaire.setItem(inventaire.getSize() - 1, item);

    }

    /**
     * Permet d'ouvrir l'inventaire des items de base à un joueur
     *
     * @param joueur
     */
    public void openInventory(Player joueur) {
        if (beingEdited) return;
        playerInventory = joueur.getInventory();
        joueur.closeInventory();
        joueur.openInventory(inventaire);
        beingEdited = true;
        openingPlayer = joueur;
    }

    public void closeInventory(Player joueur) {
        if (joueur.equals(openingPlayer)) {
            openingPlayer = null;
            beingEdited = false;
        } else {
            return;
        }


        joueur.closeInventory();
        joueur.getInventory().setContents(playerInventory.getContents());
        joueur.getInventory().setArmorContents(playerInventory.getArmorContents());
        joueur.getInventory().setItemInMainHand(playerInventory.getItemInMainHand());
        joueur.getInventory().setItemInOffHand(playerInventory.getItemInOffHand());

        items.clear();
        // On transfère le contenu de l'inventaire de base au tableau d'inventaire à donner
        for (ItemStack item : inventaire.getContents()) {
            if (item != null && item.getItemMeta() != null && !item.getItemMeta().getDisplayName().equals(Lang.player_base_item_close_inventory_item_title.toString())) {
                // On ajoute pas le livre d'arbitre
                if (item.getItemMeta().getDisplayName().equals(Lang.referee_item_name.toString())) {
                    inventaire.remove(item);
                    continue;
                }

                items.add(item);
            }
        }
    }


    public Inventory getInventory() {
        return inventaire;
    }


    public boolean isBeingEdited() {
        return beingEdited;
    }

    public void giveItemsToPlayer(Player p) {
        if (groupe.getGame().isReferee(p)) return;
        for (ItemStack item : items) {
            if (item.getType().toString().toUpperCase().contains("BOOTS")) p.getInventory().setBoots(item);
            else if (item.getType().toString().toUpperCase().contains("CHESTPLATE"))
                p.getInventory().setChestplate(item);
            else if (item.getType().toString().toUpperCase().contains("HELMET")) p.getInventory().setHelmet(item);
            else if (item.getType().toString().toUpperCase().contains("LEGGINGS")) p.getInventory().setLeggings(item);
            else if (item.getType().toString().toUpperCase().contains("SHIELD"))
                p.getInventory().setItemInOffHand(item);
            else p.getInventory().addItem(item);
        }
    }

}
