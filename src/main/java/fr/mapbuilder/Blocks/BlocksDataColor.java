package fr.mapbuilder.Blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;

enum BlocksDataColor {

    white("white", 0),
    orange("orange", 1),
    magenta("magenta", 2),
    light_blue("light_blue", 3),
    yellow("yellow", 4),
    lime("lime", 5),
    pink("pink", 6),
    gray("gray", 7),
    light_gray("light_gray", 8),
    cyan("cyan", 9),
    purple("purple", 10),
    blue("blue", 11),
    brown("brown", 12),
    green("green", 13),
    red("red", 14),
    black("black", 15);

    public String color;
    public int blockDataColor;

    BlocksDataColor(String color, int blockDataColor) {
        this.blockDataColor = blockDataColor;
        this.color = color;
    }


}
