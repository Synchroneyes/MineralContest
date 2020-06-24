package fr.synchroneyes.mineral.Utils.Save;

import fr.synchroneyes.mineral.Core.Arena.Arene;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;


public class GameToFile {
    public String worldName;
    public Arene arene;

    public GameToFile(String nomMonde) {
        this.worldName = nomMonde;
        // On récupère les valeurs du monde actuel
        /*this.teamBleu = mineralcontest.getPlayerGame(joueur).getBlueHouse().getTeam();
        this.teamRouge = mineralcontest.getPlayerGame(joueur).getRedHouse().getTeam();
        this.teamJaune = mineralcontest.getPlayerGame(joueur).getYellowHouse().getTeam();*/
        //this.arene = mineralcontest.getPlayerGame(joueur).getArene();
    }

    public boolean saveToFile() throws Exception {

        JSONObject data = new JSONObject();
        JSONArray mondes = new JSONArray();
        JSONObject monde_actuel = new JSONObject();

        JSONArray porteRouge = new JSONArray();

        // TODO TODO TODO
        /*for(int i = 0; i < mineralcontest.getPlayerGame(joueur).getRedHouse().getPorte().getPorte().size(); i++) {
            JSONObject tempBlock = new JSONObject();
            //tempBlock.put("x", mineralcontest.getPlayerGame(joueur).getRedHouse().getPorte().getPorte().get(i).getBlock().getLocation().getX());
            //tempBlock.put("y", mineralcontest.getPlayerGame(joueur).getRedHouse().getPorte().getPorte().get(i).getBlock().getLocation().getY());
            //tempBlock.put("z", mineralcontest.getPlayerGame(joueur).getRedHouse().getPorte().getPorte().get(i).getBlock().getLocation().getZ());
            porteRouge.put(tempBlock);
        }


        JSONArray porteJaune = new JSONArray();
        for(int i = 0; i < mineralcontest.getPlayerGame(joueur).getYellowHouse().getPorte().getPorte().size(); i++) {
            JSONObject tempBlock = new JSONObject();
            tempBlock.put("x", mineralcontest.getPlayerGame(joueur).getYellowHouse().getPorte().getPorte().get(i).getBlock().getLocation().getX());
            tempBlock.put("y", mineralcontest.getPlayerGame(joueur).getYellowHouse().getPorte().getPorte().get(i).getBlock().getLocation().getY());
            tempBlock.put("z", mineralcontest.getPlayerGame(joueur).getYellowHouse().getPorte().getPorte().get(i).getBlock().getLocation().getZ());
            porteJaune.put(tempBlock);

        }

        JSONArray porteBleu = new JSONArray();
        for(int i = 0; i < mineralcontest.getPlayerGame(joueur).getBlueHouse().getPorte().getPorte().size(); i++) {
            JSONObject tempBlock = new JSONObject();
            tempBlock.put("x", mineralcontest.getPlayerGame(joueur).getBlueHouse().getPorte().getPorte().get(i).getBlock().getLocation().getX());
            tempBlock.put("y", mineralcontest.getPlayerGame(joueur).getBlueHouse().getPorte().getPorte().get(i).getBlock().getLocation().getY());
            tempBlock.put("z", mineralcontest.getPlayerGame(joueur).getBlueHouse().getPorte().getPorte().get(i).getBlock().getLocation().getZ());
            porteBleu.put(tempBlock);

        }



        JSONObject json = new JSONObject()
                .put(worldName, new JSONObject()
                                .put("teams", new JSONObject()
                                            .put("rouge", new JSONObject()

                                                    .put("spawn", new JSONObject()
                                                                .put("x", mineralcontest.getPlayerGame(joueur).getRedHouse().getHouseLocation().getX())
                                                                .put("y", mineralcontest.getPlayerGame(joueur).getRedHouse().getHouseLocation().getY())
                                                                .put("z", mineralcontest.getPlayerGame(joueur).getRedHouse().getHouseLocation().getZ())
                                                    )
                                                    .put("coffre", new JSONObject()
                                                            .put("x", mineralcontest.getPlayerGame(joueur).getRedHouse().getCoffreEquipeLocation().getX())
                                                            .put("y", mineralcontest.getPlayerGame(joueur).getRedHouse().getCoffreEquipeLocation().getY())
                                                            .put("z", mineralcontest.getPlayerGame(joueur).getRedHouse().getCoffreEquipeLocation().getZ())
                                                    )
                                                    .put("porte", porteRouge)

                                            )
                                            .put("jaune", new JSONObject()

                                                    .put("spawn", new JSONObject()
                                                            .put("x", mineralcontest.getPlayerGame(joueur).getYellowHouse().getHouseLocation().getX())
                                                            .put("y", mineralcontest.getPlayerGame(joueur).getYellowHouse().getHouseLocation().getY())
                                                            .put("z", mineralcontest.getPlayerGame(joueur).getYellowHouse().getHouseLocation().getZ())
                                                    )
                                                    .put("coffre", new JSONObject()
                                                            .put("x", mineralcontest.getPlayerGame(joueur).getYellowHouse().getCoffreEquipeLocation().getX())
                                                            .put("y", mineralcontest.getPlayerGame(joueur).getYellowHouse().getCoffreEquipeLocation().getY())
                                                            .put("z", mineralcontest.getPlayerGame(joueur).getYellowHouse().getCoffreEquipeLocation().getZ())
                                                    )
                                                    .put("porte", porteJaune)

                                            )
                                            .put("bleu", new JSONObject()

                                                    .put("spawn", new JSONObject()
                                                            .put("x", mineralcontest.getPlayerGame(joueur).getBlueHouse().getHouseLocation().getX())
                                                            .put("y", mineralcontest.getPlayerGame(joueur).getBlueHouse().getHouseLocation().getY())
                                                            .put("z", mineralcontest.getPlayerGame(joueur).getBlueHouse().getHouseLocation().getZ())
                                                    )
                                                    .put("coffre", new JSONObject()
                                                            .put("x", mineralcontest.getPlayerGame(joueur).getBlueHouse().getCoffreEquipeLocation().getX())
                                                            .put("y", mineralcontest.getPlayerGame(joueur).getBlueHouse().getCoffreEquipeLocation().getY())
                                                            .put("z", mineralcontest.getPlayerGame(joueur).getBlueHouse().getCoffreEquipeLocation().getZ())
                                                    )
                                                    .put("porte", porteBleu)

                                            )
                                )
                                .put("arene", new JSONObject()
                                            .put("spawn", new JSONObject()
                                                    .put("x", mineralcontest.getPlayerGame(joueur).getArene().getTeleportSpawn().getX())
                                                    .put("y", mineralcontest.getPlayerGame(joueur).getArene().getTeleportSpawn().getY())
                                                    .put("z", mineralcontest.getPlayerGame(joueur).getArene().getTeleportSpawn().getZ()))

                                            .put("deathZone", new JSONObject()
                                                    .put("x", mineralcontest.getPlayerGame(joueur).getArene().getDeathZone().getSpawnLocation().getX())
                                                    .put("y", mineralcontest.getPlayerGame(joueur).getArene().getDeathZone().getSpawnLocation().getY())
                                                    .put("z", mineralcontest.getPlayerGame(joueur).getArene().getDeathZone().getSpawnLocation().getZ()))

                                            .put("coffre", new JSONObject()
                                                    .put("x", mineralcontest.getPlayerGame(joueur).getArene().getCoffre().getPosition().getX())
                                                    .put("y", mineralcontest.getPlayerGame(joueur).getArene().getCoffre().getPosition().getY())
                                                    .put("z", mineralcontest.getPlayerGame(joueur).getArene().getCoffre().getPosition().getZ()))
                                )

                );

         */

        //mineralcontest.broadcastMessage(json.toString());
        //mineralcontest.plugin.getServer().getLogger().info(json.toString());

        String biomefolder = mineralcontest.plugin.getDataFolder() + File.separator + "biome" + File.separator;
        FileWriter nouveau_biome = new FileWriter(biomefolder + "nouveau_biome.json");
        //nouveau_biome.write(json.toString());
        //nouveau_biome.close();
        Bukkit.getLogger().info("======================================");
        //mineralcontest.broadcastMessage("Un nouveau biome a été ajouté. Pour pouvoir l'utiliser, il faut remplacer un biome existant par celui ci." + ChatColor.RED + ChatColor.BOLD + " MERCI DE LIRE LA CONSOLE !!!!!!!!!");
        Bukkit.getLogger().info("Le nouveau biome est dispo dans le dossier /plugins/mineralcontest/biome/nouveau_biome.json");
        Bukkit.getLogger().info("Il faut renommer le fichier \"nouveau_biome.json\" en <numero>.json, exemple: 0.json");
        Bukkit.getLogger().info("Ce numéro ne doit pas dépasser 5");
        Bukkit.getLogger().info("Il faut également ouvrir le fichier avec un éditeur de texte (notepad++, ou blocnote par exemple) et au tout début du fichier; vous avez \"{\"nouveau_biome\":\". Il faut remplacer \"nouveau_biome\" par le numéro choisit plus haut.");
        Bukkit.getLogger().info("Vous allez également devoir supprimer l'ancien fichier. Pour jouer sur ce nouveau biome, voici les ID en fonction des noms de biome:");
        Bukkit.getLogger().info("0 = neige\n" +
                "1 = desert\n" +
                "2 = foret\n" +
                "3 = plaine\n" +
                "4 = montagne\n" +
                "5 = marécage");
        Bukkit.getLogger().info("======================================");


        return true;
    }


}
