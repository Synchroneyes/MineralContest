package fr.mapbuilder;

import fr.mineral.mineralcontest;

import java.io.File;

public class RessourceFilesManager {
    private static String[] files_names = {"houses/east.yml", "houses/north.yml", "houses/west.yml", "houses/south.yml"};
    private static String datafolder_name = "custom-maps-models";

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
