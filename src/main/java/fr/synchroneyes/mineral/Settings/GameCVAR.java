package fr.synchroneyes.mineral.Settings;

public class GameCVAR {

    private String command;
    private String description;
    private String valeur;
    private String type;
    private boolean canBeReloaded;
    private boolean isNumber;
    private int valeurNumerique;

    public GameCVAR(String command, String valeur, String description, String type, boolean canBeReloadedInGame, boolean isNumber) {
        this.command = command;
        this.valeur = valeur;
        this.description = description;
        this.type = type;
        this.canBeReloaded = canBeReloadedInGame;
        this.isNumber = isNumber;

        if (isNumber) valeurNumerique = Integer.parseInt(valeur);
    }


    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
        if (isNumber) valeurNumerique = Integer.parseInt(valeur);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean canBeReloaded() {
        return canBeReloaded;
    }

    public void setCanBeReloaded(boolean canBeReloaded) {
        this.canBeReloaded = canBeReloaded;
    }

    public boolean isNumber() {
        return isNumber;
    }

    public int getValeurNumerique() {
        return valeurNumerique;
    }
}
