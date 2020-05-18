package fr.mineral.Utils.Save;

import fr.mineral.Core.Arena.Arene;
import fr.mineral.Teams.Equipe;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;


public class GameToFile {
    public String worldName;
    public Equipe teamRouge, teamJaune, teamBleu;
    public Arene arene;

    public GameToFile(String nomMonde) {
        this.worldName = nomMonde;
        // On récupère les valeurs du monde actuel
        this.teamBleu = mineralcontest.plugin.getGame().getBlueHouse().getTeam();
        this.teamRouge = mineralcontest.plugin.getGame().getRedHouse().getTeam();
        this.teamJaune = mineralcontest.plugin.getGame().getYellowHouse().getTeam();
        this.arene = mineralcontest.plugin.getGame().getArene();
    }

    public boolean saveToFile() throws Exception {

        JSONObject data = new JSONObject();
        JSONArray mondes = new JSONArray();
        JSONObject monde_actuel = new JSONObject();

        JSONArray porteRouge = new JSONArray();

        for(int i = 0; i < mineralcontest.plugin.getGame().getRedHouse().getPorte().getPorte().size(); i++) {
            JSONObject tempBlock = new JSONObject();
            tempBlock.put("x", mineralcontest.plugin.getGame().getRedHouse().getPorte().getPorte().get(i).getBlock().getLocation().getX());
            tempBlock.put("y", mineralcontest.plugin.getGame().getRedHouse().getPorte().getPorte().get(i).getBlock().getLocation().getY());
            tempBlock.put("z", mineralcontest.plugin.getGame().getRedHouse().getPorte().getPorte().get(i).getBlock().getLocation().getZ());
            porteRouge.put(tempBlock);
        }


        JSONArray porteJaune = new JSONArray();
        for(int i = 0; i < mineralcontest.plugin.getGame().getYellowHouse().getPorte().getPorte().size(); i++) {
            JSONObject tempBlock = new JSONObject();
            tempBlock.put("x", mineralcontest.plugin.getGame().getYellowHouse().getPorte().getPorte().get(i).getBlock().getLocation().getX());
            tempBlock.put("y", mineralcontest.plugin.getGame().getYellowHouse().getPorte().getPorte().get(i).getBlock().getLocation().getY());
            tempBlock.put("z", mineralcontest.plugin.getGame().getYellowHouse().getPorte().getPorte().get(i).getBlock().getLocation().getZ());
            porteJaune.put(tempBlock);

        }

        JSONArray porteBleu = new JSONArray();
        for(int i = 0; i < mineralcontest.plugin.getGame().getBlueHouse().getPorte().getPorte().size(); i++) {
            JSONObject tempBlock = new JSONObject();
            tempBlock.put("x", mineralcontest.plugin.getGame().getBlueHouse().getPorte().getPorte().get(i).getBlock().getLocation().getX());
            tempBlock.put("y", mineralcontest.plugin.getGame().getBlueHouse().getPorte().getPorte().get(i).getBlock().getLocation().getY());
            tempBlock.put("z", mineralcontest.plugin.getGame().getBlueHouse().getPorte().getPorte().get(i).getBlock().getLocation().getZ());
            porteBleu.put(tempBlock);

        }



        JSONObject json = new JSONObject()
                .put(worldName, new JSONObject()
                                .put("teams", new JSONObject()
                                            .put("rouge", new JSONObject()

                                                    .put("spawn", new JSONObject()
                                                                .put("x", mineralcontest.plugin.getGame().getRedHouse().getHouseLocation().getX())
                                                                .put("y", mineralcontest.plugin.getGame().getRedHouse().getHouseLocation().getY())
                                                                .put("z", mineralcontest.plugin.getGame().getRedHouse().getHouseLocation().getZ())
                                                    )
                                                    .put("coffre", new JSONObject()
                                                            .put("x", mineralcontest.plugin.getGame().getRedHouse().getCoffreEquipeLocation().getX())
                                                            .put("y", mineralcontest.plugin.getGame().getRedHouse().getCoffreEquipeLocation().getY())
                                                            .put("z", mineralcontest.plugin.getGame().getRedHouse().getCoffreEquipeLocation().getZ())
                                                    )
                                                    .put("porte", porteRouge)

                                            )
                                            .put("jaune", new JSONObject()

                                                    .put("spawn", new JSONObject()
                                                            .put("x", mineralcontest.plugin.getGame().getYellowHouse().getHouseLocation().getX())
                                                            .put("y", mineralcontest.plugin.getGame().getYellowHouse().getHouseLocation().getY())
                                                            .put("z", mineralcontest.plugin.getGame().getYellowHouse().getHouseLocation().getZ())
                                                    )
                                                    .put("coffre", new JSONObject()
                                                            .put("x", mineralcontest.plugin.getGame().getYellowHouse().getCoffreEquipeLocation().getX())
                                                            .put("y", mineralcontest.plugin.getGame().getYellowHouse().getCoffreEquipeLocation().getY())
                                                            .put("z", mineralcontest.plugin.getGame().getYellowHouse().getCoffreEquipeLocation().getZ())
                                                    )
                                                    .put("porte", porteJaune)

                                            )
                                            .put("bleu", new JSONObject()

                                                    .put("spawn", new JSONObject()
                                                            .put("x", mineralcontest.plugin.getGame().getBlueHouse().getHouseLocation().getX())
                                                            .put("y", mineralcontest.plugin.getGame().getBlueHouse().getHouseLocation().getY())
                                                            .put("z", mineralcontest.plugin.getGame().getBlueHouse().getHouseLocation().getZ())
                                                    )
                                                    .put("coffre", new JSONObject()
                                                            .put("x", mineralcontest.plugin.getGame().getBlueHouse().getCoffreEquipeLocation().getX())
                                                            .put("y", mineralcontest.plugin.getGame().getBlueHouse().getCoffreEquipeLocation().getY())
                                                            .put("z", mineralcontest.plugin.getGame().getBlueHouse().getCoffreEquipeLocation().getZ())
                                                    )
                                                    .put("porte", porteBleu)

                                            )
                                )
                                .put("arene", new JSONObject()
                                            .put("spawn", new JSONObject()
                                                    .put("x", mineralcontest.plugin.getGame().getArene().getTeleportSpawn().getX())
                                                    .put("y", mineralcontest.plugin.getGame().getArene().getTeleportSpawn().getY())
                                                    .put("z", mineralcontest.plugin.getGame().getArene().getTeleportSpawn().getZ()))

                                            .put("deathZone", new JSONObject()
                                                    .put("x", mineralcontest.plugin.getGame().getArene().getDeathZone().getSpawnLocation().getX())
                                                    .put("y", mineralcontest.plugin.getGame().getArene().getDeathZone().getSpawnLocation().getY())
                                                    .put("z", mineralcontest.plugin.getGame().getArene().getDeathZone().getSpawnLocation().getZ()))

                                            .put("coffre", new JSONObject()
                                                    .put("x", mineralcontest.plugin.getGame().getArene().getCoffre().getPosition().getX())
                                                    .put("y", mineralcontest.plugin.getGame().getArene().getCoffre().getPosition().getY())
                                                    .put("z", mineralcontest.plugin.getGame().getArene().getCoffre().getPosition().getZ()))
                                )

                );


        //mineralcontest.broadcastMessage(json.toString());
        //mineralcontest.plugin.getServer().getLogger().info(json.toString());

        String biomefolder = mineralcontest.plugin.getDataFolder() + File.separator + "biome" + File.separator;
        FileWriter nouveau_biome = new FileWriter(biomefolder + "nouveau_biome.json");
        nouveau_biome.write(json.toString());
        nouveau_biome.close();
        Bukkit.getLogger().info("======================================");
        mineralcontest.broadcastMessage("Un nouveau biome a été ajouté. Pour pouvoir l'utiliser, il faut remplacer un biome existant par celui ci." + ChatColor.RED + ChatColor.BOLD + " MERCI DE LIRE LA CONSOLE !!!!!!!!!");
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
