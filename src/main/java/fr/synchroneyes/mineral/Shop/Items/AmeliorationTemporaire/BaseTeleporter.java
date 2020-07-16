package fr.synchroneyes.mineral.Shop.Items.AmeliorationTemporaire;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Exception.EventAlreadyHandledException;
import fr.synchroneyes.mineral.Shop.Items.Abstract.ConsumableItem;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Utils.Player.PlayerUtils;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;


public class BaseTeleporter extends ConsumableItem {

    public ItemStack toInventoryItem() {
        ItemStack item = toItemStack();
        ItemMeta meta = item.getItemMeta();

        List<String> description = meta.getLore();


        // On retire les deux dernières lignes
        description.remove(description.size() - 1);
        description.remove(description.size() - 1);


        meta.setLore(description);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public String getNomItem() {
        return "Téléphone, Maison";
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{"Cet item vous permet de vous %green%téléporter à votre base", "Vous obtiendrez un item"};
    }

    @Override
    public Material getItemMaterial() {
        return Material.MAP;
    }

    @Override
    public boolean isEnabledOnRespawn() {
        return false;
    }

    @Override
    public boolean isEnabledOnPurchase() {
        return true;
    }

    @Override
    public int getNombreUtilisations() {
        return 1;
    }

    @Override
    public void onItemUse() {
        ItemStack teleport = toInventoryItem();

        joueur.getInventory().addItem(teleport);
    }

    @Override
    public int getPrice() {
        return 5;
    }


    /**
     * Fonction utilisée pour traiter l'utilisation de l'item
     *
     * @param event
     */
    public static void TeleportItemUseEvent(PlayerInteractEvent event) throws EventAlreadyHandledException {
        Player joueur = event.getPlayer();

        ItemStack itemTeleportation = new BaseTeleporter().toInventoryItem();

        if (mineralcontest.isInAMineralContestWorld(joueur)) {
            Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);
            if (playerGroup == null) return;

            Equipe playerTeam = playerGroup.getPlayerTeam(joueur);

            if (event.getItem() != null && event.getItem().equals(itemTeleportation)) {
                if (playerTeam == null) {
                    joueur.sendMessage("Vous devez être dans une équipe !");
                    event.setCancelled(true);
                } else {
                    joueur.sendMessage("Vous avez été téléporter à votre base");
                    PlayerUtils.teleportPlayer(joueur, joueur.getLocation().getWorld(), playerTeam.getMaison().getHouseLocation());
                    joueur.getInventory().remove(itemTeleportation);
                    event.setCancelled(true);
                }

                throw new EventAlreadyHandledException();

            }
        }
    }
}
