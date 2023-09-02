package fr.synchroneyes.mineral.Utils;

import org.bukkit.Bukkit;

public class TimeConverter {
    public static String intToString(int valeurEnSecondes) {
        int minutes, secondes;
        minutes = valeurEnSecondes / 60;
        secondes = valeurEnSecondes % 60;
        return String.format("%02d:%02d", minutes, secondes);
    }

}
