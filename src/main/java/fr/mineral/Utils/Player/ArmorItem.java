package fr.mineral.Utils.Player;

import org.bukkit.Material;

public class ArmorItem {
    private int type;
    private String auto_equip;
    private String when;
    private Material item_name;

    public void setType(int type) {
        this.type = type;
    }

    public void setAuto_equip(String auto_equip) {
        this.auto_equip = auto_equip;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public void setItem_name(Material item_name) {
        this.item_name = item_name;
    }

    public int getType() {
        return type;
    }

    public String getAuto_equip() {
        return auto_equip;
    }

    public String getWhen() {
        return when;
    }

    public Material getItem_name() {
        return item_name;
    }
}
