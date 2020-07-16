package fr.synchroneyes.mineral.Shop.Items.AmeliorationTemporaire;

import fr.synchroneyes.mineral.Shop.Items.Abstract.ConsumableItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.TNTPrimed;

public class DerniereChance extends ConsumableItem {
    @Override
    public String getNomItem() {
        return "%red%Dernière chance";
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{"Fait apparaitre de la tnt autour de votre mort", "Ce bonus s'active lors que vous êtes tué par un autre joueur"};
    }

    @Override
    public Material getItemMaterial() {
        return Material.TNT;
    }

    @Override
    public boolean isEnabledOnRespawn() {
        return false;
    }

    @Override
    public boolean isEnabledOnPurchase() {
        return false;
    }

    @Override
    public int getNombreUtilisations() {
        return 1;
    }

    @Override
    public void onItemUse() {
        Location playerDeathLocation = this.joueur.getLocation();

        int rayon_block_tnt = 1;
        int temps_avant_explosion = 3;

        int defaultX, defaultY, defaultZ;
        defaultX = playerDeathLocation.getBlockX();
        defaultY = playerDeathLocation.getBlockY();
        defaultZ = playerDeathLocation.getBlockZ();


        for (int x = defaultX - rayon_block_tnt; x < defaultX + rayon_block_tnt; ++x)
            for (int z = defaultZ - rayon_block_tnt; z < defaultZ + rayon_block_tnt; ++z)
                playerDeathLocation.getWorld().spawn(new Location(playerDeathLocation.getWorld(), x, defaultY, z), TNTPrimed.class).setFuseTicks(20 * temps_avant_explosion);

    }

    @Override
    public int getPrice() {
        return 20;
    }


    @Override
    public boolean isEnabledOnDeathByAnotherPlayer() {
        return true;
    }
}
