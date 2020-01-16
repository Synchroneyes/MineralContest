package fr.mineral.Utils.Save;

import fr.mineral.Core.Arena.Arene;
import fr.mineral.Teams.Equipe;
import fr.mineral.mineralcontest;
import org.json.JSONArray;
import org.json.JSONObject;


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


        mineralcontest.plugin.getServer().broadcastMessage(json.toString());
        mineralcontest.plugin.getServer().getLogger().info(json.toString());
        return true;
    }


}
