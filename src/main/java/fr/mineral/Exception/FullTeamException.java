package fr.mineral.Exception;

public class FullTeamException extends Exception {
    public FullTeamException(String nomEquipe) {
        super("L'equipe " + nomEquipe + " est pleine.");
    }
}
