package fr.synchroneyes.mineral.Utils;

public class TimeConverter {
    public static String intToString(int valeur) {
        int minutes, secondes;
        minutes = (valeur % 3600) / 60;
        secondes = valeur % 60;
        return String.format("%02d:%02d", minutes, secondes);

    }
}
