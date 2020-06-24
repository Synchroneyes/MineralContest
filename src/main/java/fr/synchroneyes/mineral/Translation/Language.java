package fr.synchroneyes.mineral.Translation;

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

    public static String getLanguageFromLocale(String locale) {
        if (locale.contains("fr_"))
            return FRENCH.getLanguageName();
        if (locale.contains("en_"))
            return ENGLISH.getLanguageName();

        return FRENCH.getLanguageName();
    }

    public static String getAvailableLanguages() {
        String result = "";
        for (Language item : Language.values()) {
            if (!item.getLanguageName().equals("default"))
                result += item.getLanguageName() + ", ";
        }

        result = result.substring(0, result.length() - 2);
        return result;
    }
}