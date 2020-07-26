package fr.synchroneyes.mapbuilder.Blocks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum BlocksDataColor {

    white("white", 0, ChatColor.WHITE),
    orange("orange", 1, ChatColor.GOLD),
    magenta("magenta", 2, ChatColor.LIGHT_PURPLE),
    light_blue("light_blue", 3, ChatColor.AQUA),
    yellow("yellow", 4, ChatColor.YELLOW),
    lime("lime", 5, ChatColor.GREEN),
    pink("pink", 6, ChatColor.LIGHT_PURPLE),
    gray("gray", 7, ChatColor.DARK_GRAY),
    light_gray("light_gray", 8, ChatColor.GRAY),
    cyan("cyan", 9, ChatColor.BLUE),
    purple("purple", 10, ChatColor.DARK_PURPLE),
    blue("blue", 11, ChatColor.BLUE),
    brown("brown", 12, ChatColor.DARK_RED),
    green("green", 13, ChatColor.GREEN),
    red("red", 14, ChatColor.RED),
    black("black", 15, ChatColor.BLACK);

    public String color;
    public int blockDataColor;
    public ChatColor chatColor;

    BlocksDataColor(String color, int blockDataColor, ChatColor chatColor) {
        this.blockDataColor = blockDataColor;
        this.color = color;
        this.chatColor = chatColor;
    }

    public ItemStack toItemStack() {
        String itemStackName = color + "_concrete";
        Material materialName = Material.valueOf(itemStackName.toUpperCase());

        return new ItemStack(materialName, 1);
    }


    public static BlocksDataColor fromItemName(String itemName) {

        for (BlocksDataColor couleur : values()) {
            if (itemName.contains(couleur.color))
                return couleur;
        }
        return black;
    }


}
