package fr.synchroneyes.mineral.Utils.Save;

import org.json.JSONObject;

import java.io.IOException;

public class FileToGame {

    public boolean readFile(String worldName) throws IOException {
    /*
        try {
            mineralcontest plugin = mineralcontest.plugin;
            InputStream worldStream = new FileInputStream(plugin.getDataFolder() + File.separator + MapFileHandler.biome_data_folderPath + File.separator + worldName + ".json");
            JSONTokener tokener = new JSONTokener(worldStream);
            JSONObject monde = new JSONObject(tokener).getJSONObject(worldName);

            setHousesLocation(monde.getJSONObject("teams"));
            setDoors(monde.getJSONObject("teams"));
            setTeamChestLocation(monde.getJSONObject("teams"));
            setArenaLocation(monde.getJSONObject("arene"));

            for(Player online : mineralcontest.plugin.pluginWorld.getPlayers()) {
                try {
                    //online.sendMessage(mineralcontest.prefixGlobal + "Configuration du monde chargée avec succès");
                    PlayerUtils.teleportPlayer(online, mineralcontest.getPlayerGame(online).getArene().getDeathZone().getSpawnLocation());
                }catch(Exception e) {
                    mineralcontest.broadcastMessage(mineralcontest.prefixErreur + "Error while loading world");
                    mineralcontest.broadcastMessage(mineralcontest.prefixErreur + "This error usually happens when you reload the plugin. please dont, " + ChatColor.RED + "restart server instead");
                    e.printStackTrace();
                    Error.Report(e, mineralcontest.getPlayerGame(online));
                }

            }

            //mineralcontest.getPlayerGame().isGameInitialized = true;
        }catch (Exception e) {
            mineralcontest.broadcastMessage(mineralcontest.prefixErreur + "Error while loading world");
            mineralcontest.broadcastMessage(mineralcontest.prefixErreur + "This error usually happens when you reload the plugin. please dont, " + ChatColor.RED + "restart server instead");
            e.printStackTrace();
            Error.Report(e);
        }


        return true;

    }

    public void setDoors(JSONObject teams) {
        World pluginDoors = PlayerUtils.getPluginWorld();
        JSONArray portes;

        // Team Rouge
        portes = teams.getJSONObject("rouge").getJSONArray("porte");
        for(int i = 0; i < portes.length(); i++) {
            Location emplacementCoordonne = new Location(pluginDoors, 0d, 0d, 0d);
            JSONObject objet = portes.getJSONObject(i);
            emplacementCoordonne.setX(objet.getDouble("x"));
            emplacementCoordonne.setY(objet.getDouble("y"));
            emplacementCoordonne.setZ(objet.getDouble("z"));
            //mineralcontest.getPlayerGame(joueur).getRedHouse().getPorte().addToDoor(emplacementCoordonne.getBlock());

        }

        // Team Jaune
        portes = teams.getJSONObject("jaune").getJSONArray("porte");
        for(int i = 0; i < portes.length(); i++) {
            Location emplacementCoordonne = new Location(pluginDoors, 0d, 0d, 0d);
            JSONObject objet = portes.getJSONObject(i);
            emplacementCoordonne.setX(objet.getDouble("x"));
            emplacementCoordonne.setY(objet.getDouble("y"));
            emplacementCoordonne.setZ(objet.getDouble("z"));


           // mineralcontest.getPlayerGame(joueur).getYellowHouse().getPorte().addToDoor(emplacementCoordonne.getBlock());

        }

        // Team Bleu
        portes = teams.getJSONObject("bleu").getJSONArray("porte");
        for(int i = 0; i < portes.length(); i++) {
            Location emplacementCoordonne = new Location(pluginDoors, 0d, 0d, 0d);
            JSONObject objet = portes.getJSONObject(i);
            emplacementCoordonne.setX(objet.getDouble("x"));
            emplacementCoordonne.setY(objet.getDouble("y"));
            emplacementCoordonne.setZ(objet.getDouble("z"));
            //mineralcontest.getPlayerGame(joueur).getBlueHouse().getPorte().addToDoor(emplacementCoordonne.getBlock());

        }
    }

    public void setHousesLocation(JSONObject teams) {
        World pluginDoors = PlayerUtils.getPluginWorld();

        Location emplacementCoordonne = new Location(pluginDoors, 0d, 0d, 0d);

        // On récupère les coordonnées du spawn rouge
        emplacementCoordonne.setX(teams.getJSONObject("rouge").getJSONObject("spawn").getDouble("x"));
        emplacementCoordonne.setY(teams.getJSONObject("rouge").getJSONObject("spawn").getDouble("y"));
        emplacementCoordonne.setZ(teams.getJSONObject("rouge").getJSONObject("spawn").getDouble("z"));
        //mineralcontest.getPlayerGame(joueur).getRedHouse().setHouseLocation(emplacementCoordonne);


        // On récupère les coordonnées du spawn bleu
        emplacementCoordonne = new Location(pluginDoors, 0d, 0d, 0d);
        emplacementCoordonne.setX(teams.getJSONObject("bleu").getJSONObject("spawn").getDouble("x"));
        emplacementCoordonne.setY(teams.getJSONObject("bleu").getJSONObject("spawn").getDouble("y"));
        emplacementCoordonne.setZ(teams.getJSONObject("bleu").getJSONObject("spawn").getDouble("z"));
        //mineralcontest.getPlayerGame(joueur).getBlueHouse().setHouseLocation(emplacementCoordonne);


        // On récupère les coordonnées du spawn rouge
        emplacementCoordonne = new Location(pluginDoors, 0d, 0d, 0d);
        emplacementCoordonne.setX(teams.getJSONObject("jaune").getJSONObject("spawn").getDouble("x"));
        emplacementCoordonne.setY(teams.getJSONObject("jaune").getJSONObject("spawn").getDouble("y"));
        emplacementCoordonne.setZ(teams.getJSONObject("jaune").getJSONObject("spawn").getDouble("z"));

        //mineralcontest.getPlayerGame(joueur).getYellowHouse().setHouseLocation(emplacementCoordonne);

     */
        return true;
    }

    public void setTeamChestLocation(JSONObject teams) {

        /*World pluginDoors = PlayerUtils.getPluginWorld();

        Location emplacementCoordonne = new Location(pluginDoors, 0d, 0d, 0d);

        // On récupère les coordonnées du spawn rouge
        emplacementCoordonne.setX(teams.getJSONObject("rouge").getJSONObject("coffre").getDouble("x"));
        emplacementCoordonne.setY(teams.getJSONObject("rouge").getJSONObject("coffre").getDouble("y"));
        emplacementCoordonne.setZ(teams.getJSONObject("rouge").getJSONObject("coffre").getDouble("z"));
        //mineralcontest.getPlayerGame(joueur).getRedHouse().setCoffreEquipe(emplacementCoordonne);

        emplacementCoordonne = new Location(pluginDoors, 0d, 0d, 0d);

        // On récupère les coordonnées du spawn bleu
        emplacementCoordonne.setX(teams.getJSONObject("bleu").getJSONObject("coffre").getDouble("x"));
        emplacementCoordonne.setY(teams.getJSONObject("bleu").getJSONObject("coffre").getDouble("y"));
        emplacementCoordonne.setZ(teams.getJSONObject("bleu").getJSONObject("coffre").getDouble("z"));
        //mineralcontest.getPlayerGame(joueur).getBlueHouse().setCoffreEquipe(emplacementCoordonne);

        emplacementCoordonne = new Location(pluginDoors, 0d, 0d, 0d);
        // On récupère les coordonnées du spawn rouge
        emplacementCoordonne.setX(teams.getJSONObject("jaune").getJSONObject("coffre").getDouble("x"));
        emplacementCoordonne.setY(teams.getJSONObject("jaune").getJSONObject("coffre").getDouble("y"));
        emplacementCoordonne.setZ(teams.getJSONObject("jaune").getJSONObject("coffre").getDouble("z"));

        //mineralcontest.getPlayerGame(joueur).getYellowHouse().setCoffreEquipe(emplacementCoordonne);*/
    }

    public void setArenaLocation(JSONObject arene) {
        /*World pluginDoors = PlayerUtils.getPluginWorld();
        Location emplacementCoordonne = new Location(pluginDoors, 0d, 0d, 0d);
        // On récupère le coffre
        emplacementCoordonne.setX(arene.getJSONObject("coffre").getDouble("x"));
        emplacementCoordonne.setY(arene.getJSONObject("coffre").getDouble("y"));
        emplacementCoordonne.setZ(arene.getJSONObject("coffre").getDouble("z"));
        mineralcontest.getPlayerGame(joueur).getArene().setCoffre(emplacementCoordonne);


        // On récupère le spawn
        emplacementCoordonne = new Location(pluginDoors, 0d, 0d, 0d);
        emplacementCoordonne.setX(arene.getJSONObject("spawn").getDouble("x"));
        emplacementCoordonne.setY(arene.getJSONObject("spawn").getDouble("y"));
        emplacementCoordonne.setZ(arene.getJSONObject("spawn").getDouble("z"));
        mineralcontest.getPlayerGame(joueur).getArene().setTeleportSpawn(emplacementCoordonne);

        // Deathzone
        emplacementCoordonne = new Location(pluginDoors, 0d, 0d, 0d);
        emplacementCoordonne.setX(arene.getJSONObject("deathZone").getDouble("x"));
        emplacementCoordonne.setY(arene.getJSONObject("deathZone").getDouble("y"));
        emplacementCoordonne.setZ(arene.getJSONObject("deathZone").getDouble("z"));
        mineralcontest.getPlayerGame(joueur).getArene().getDeathZone().setSpawnLocation(emplacementCoordonne);*/
    }
}
