package fr.synchroneyes.mineral.Utils;

import org.bukkit.ChatColor;

public enum ChatColorString {
    BLACK(ChatColor.BLACK, "noir", "BLACK"),
    DARK_BLUE(ChatColor.DARK_BLUE, "bleu_fonce", "BLUE"),
    DARK_GREEN(ChatColor.DARK_GREEN, "vert_fonce", "GREEN"),
    DARK_AQUA(ChatColor.DARK_AQUA, "aqua_fonce", "LIGHT_BLUE"),
    DARK_RED(ChatColor.DARK_RED, "rouge_fonce", "RED"),
    DARK_PURPLE(ChatColor.DARK_PURPLE, "violet_fonce", "PURPLE"),
    GOLD(ChatColor.GOLD, "or", "GOLD"),
    GRAY(ChatColor.GRAY, "gris", "GRAY"),
    DARK_GRAY(ChatColor.DARK_GRAY, "gris_fonce", "GRAY"),
    BLUE(ChatColor.BLUE, "bleu", "BLUE"),
    GREEN(ChatColor.GREEN, "vert", "GREEN"),
    AQUA(ChatColor.AQUA, "turquoise", "AQUA"),
    RED(ChatColor.RED, "rouge", "RED"),
    LIGHT_PURPLE(ChatColor.LIGHT_PURPLE, "violet_clair", "PURPLE"),
    YELLOW(ChatColor.YELLOW, "jaune", "YELLOW"),
    WHITE(ChatColor.WHITE, "blanc", "WHITE");

    private String valeur;
    private String valeurEN;
    private ChatColor couleur;

    ChatColorString(ChatColor chatcolor, String string, String valeurEN) {
        this.valeur = string;
        this.couleur = chatcolor;
        this.valeurEN = valeurEN;
    }

    public static String toString(ChatColor c) {
        for (ChatColorString colorString : ChatColorString.values())
            if (colorString.couleur.equals(c)) return colorString.valeur;

        return "";
    }

    public static String toStringEN(ChatColor c) {
        for (ChatColorString colorString : ChatColorString.values())
            if (colorString.couleur.equals(c)) return colorString.valeurEN;

        return "";
    }
}
