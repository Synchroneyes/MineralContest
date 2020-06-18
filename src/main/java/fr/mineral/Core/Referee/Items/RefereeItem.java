package fr.mineral.Core.Referee.Items;


import jdk.internal.jline.internal.Nullable;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public interface RefereeItem {

    public abstract void performClick(Player joueur);

    public abstract String getNomItem();

    public abstract String getDescriptionItem();

    public abstract Material getItemMaterial();

}
