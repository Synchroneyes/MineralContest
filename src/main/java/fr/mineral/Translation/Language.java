package fr.mineral.Translation;

public enum Language {
    FRENCH("french"),
    ENGLISH("english");

    private String name;

    Language(String name) {
        this.name = name;
    }

    public String getLanguageName() {
        return this.name;
    }
}