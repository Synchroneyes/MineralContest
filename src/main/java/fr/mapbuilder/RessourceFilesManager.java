package fr.mapbuilder;

import fr.mineral.mineralcontest;

import java.io.File;

public class RessourceFilesManager {
    private static String[] files_names = {"houses/e.yml", "houses/n.yml", "houses/w.yml", "houses/s.yml", "arene.yml"};
    public static String datafolder_name = "custom-maps-models";

    public static void copyFilesToPluginFolder() {
        mineralcontest plugin = mineralcontest.plugin;
        for(String fileName : files_names) {
            File ressourceFile = new File(plugin.getDataFolder(), fileName);
            if(!ressourceFile.exists())
                plugin.saveResource(datafolder_name + File.separator + fileName, true);
        }

        MapBuilder.getInstance().printToConsole("Successfully copied files to plugin folder");
    }
}
