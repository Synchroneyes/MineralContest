package fr.mineral.Utils.Save;

import fr.mineral.mineralcontest;

import java.io.File;

public class MapFileHandler {
    private static int number_of_biomes = 6;
    public static String biome_data_folderPath = "biome";
    /*
        Copy the maps inside the .jar into the plugin data folder
     */
    public static void copyMapFileToPluginRessourceFolder() {
        mineralcontest plugin = mineralcontest.plugin;
        for(int indexBiome = 0; indexBiome < number_of_biomes; ++indexBiome) {
            String biomeFileName = indexBiome + ".json";
            File biomeFile = new File(plugin.getDataFolder() + File.separator + biome_data_folderPath, biomeFileName);
            if(!biomeFile.exists()) {
                plugin.saveResource(biome_data_folderPath + File.separator + biomeFileName, false);
            }
        }
    }
}
