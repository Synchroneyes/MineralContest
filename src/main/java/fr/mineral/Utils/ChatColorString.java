package fr.mineral.Utils;

import org.bukkit.ChatColor;

public enum ChatColorString {
    BLACK(ChatColor.BLACK, "noir"),
    DARK_BLUE(ChatColor.DARK_BLUE, "bleu_fonce"),
    DARK_GREEN(ChatColor.DARK_GREEN, "vert_fonce"),
    DARK_AQUA(ChatColor.DARK_AQUA, "aqua_fonce"),
    DARK_RED(ChatColor.DARK_RED, "rouge_fonce"),
    DARK_PURPLE(ChatColor.DARK_PURPLE, "violet_fonce"),
    GOLD(ChatColor.GOLD, "or"),
    GRAY(ChatColor.GRAY, "gris"),
    DARK_GRAY(ChatColor.DARK_GRAY, "gris_fonce"),
    BLUE(ChatColor.BLUE, "bleu"),
    GREEN(ChatColor.GREEN, "vert"),
    AQUA(ChatColor.AQUA, "turquoise"),
    RED(ChatColor.RED, "rouge"),
    LIGHT_PURPLE(ChatColor.LIGHT_PURPLE, "violet_clair"),
    YELLOW(ChatColor.YELLOW, "jaune"),
    WHITE(ChatColor.WHITE, "blanc");

    private String valeur;
    private ChatColor couleur;

    ChatColorString(ChatColor chatcolor, String string) {
        this.valeur = string;
        this.couleur = chatcolor;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public ChatColor getCouleur() {
        return couleur;
    }

    public void setCouleur(ChatColor couleur) {
        this.couleur = couleur;
    }


    public static String toString(ChatColor c) {
        for (ChatColorString colorString : ChatColorString.values())
            if (colorString.couleur.equals(c)) return colorString.valeur;

        return "";
    }
}
