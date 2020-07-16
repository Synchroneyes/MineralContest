package fr.synchroneyes.mineral.Shop.Items.Items;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Exception.EventAlreadyHandledException;
import fr.synchroneyes.mineral.Shop.Items.Abstract.ConsumableItem;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;

public class BouleDeFeu extends ConsumableItem {

    public static ItemStack toInventoryItem() {

        BouleDeFeu instance = new BouleDeFeu();
        ItemStack item = new ItemStack(Material.FIRE_CHARGE);
        ItemMeta meta = item.getItemMeta();
        List<String> description = new LinkedList<>();

        description.add(Lang.translate(instance.getDescriptionItem()[0]));

        meta.setDisplayName(Lang.translate(instance.getNomItem()));
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public String getNomItem() {
        return "%gold%Boule de feu";
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{"Cet item vous permet de lancer une boule de feu!"};
    }

    @Override
    public Material getItemMaterial() {
        return Material.FIRE_CHARGE;
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


        joueur.getInventory().addItem(toInventoryItem());
    }

    @Override
    public int getPrice() {
        return 250;
    }


    public static void FireballPlayerInteractEvent(PlayerInteractEvent event) throws EventAlreadyHandledException {
        Player joueur = event.getPlayer();

        ItemStack itemBouleDeFeu = BouleDeFeu.toInventoryItem();

        if (mineralcontest.isInAMineralContestWorld(joueur)) {
            Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);
            if (playerGroup == null) return;


            if (event.getItem() != null && event.getItem().getItemMeta() != null && event.getItem().getItemMeta().getDisplayName().equals(itemBouleDeFeu.getItemMeta().getDisplayName())) {
                event.getItem().setAmount(event.getItem().getAmount() - 1);
                joueur.launchProjectile(Fireball.class);
                throw new EventAlreadyHandledException();
            }
        }
    }
}
