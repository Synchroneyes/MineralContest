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
        this.teamBleu = mineralcontest.plugin.getGame().getTeamBleu();
        this.teamRouge = mineralcontest.plugin.getGame().getTeamRouge();
        this.teamJaune = mineralcontest.plugin.getGame().getTeamJaune();
        this.arene = mineralcontest.plugin.getGame().getArene();
    }

    public boolean saveToFile() throws Exception {

        JSONObject data = new JSONObject();
        JSONArray mondes = new JSONArray();
        JSONObject monde_actuel = new JSONObject();

        JSONArray porteRouge = new JSONArray();

        for(int i = 0; i < mineralcontest.plugin.getGame().getTeamRouge().getPorte().getPorte().size(); i++) {
            JSONObject tempBlock = new JSONObject();
            tempBlock.put("x", mineralcontest.plugin.getGame().getTeamRouge().getPorte().getPorte().get(i).getBlock().getLocation().getX());
            tempBlock.put("y", mineralcontest.plugin.getGame().getTeamRouge().getPorte().getPorte().get(i).getBlock().getLocation().getY());
            tempBlock.put("z", mineralcontest.plugin.getGame().getTeamRouge().getPorte().getPorte().get(i).getBlock().getLocation().getZ());
            porteRouge.put(tempBlock);
        }


        JSONArray porteJaune = new JSONArray();
        for(int i = 0; i < mineralcontest.plugin.getGame().getTeamJaune().getPorte().getPorte().size(); i++) {
            JSONObject tempBlock = new JSONObject();
            tempBlock.put("x", mineralcontest.plugin.getGame().getTeamJaune().getPorte().getPorte().get(i).getBlock().getLocation().getX());
            tempBlock.put("y", mineralcontest.plugin.getGame().getTeamJaune().getPorte().getPorte().get(i).getBlock().getLocation().getY());
            tempBlock.put("z", mineralcontest.plugin.getGame().getTeamJaune().getPorte().getPorte().get(i).getBlock().getLocation().getZ());
            porteJaune.put(tempBlock);

        }

        JSONArray porteBleu = new JSONArray();
        for(int i = 0; i < mineralcontest.plugin.getGame().getTeamBleu().getPorte().getPorte().size(); i++) {
            JSONObject tempBlock = new JSONObject();
            tempBlock.put("x", mineralcontest.plugin.getGame().getTeamBleu().getPorte().getPorte().get(i).getBlock().getLocation().getX());
            tempBlock.put("y", mineralcontest.plugin.getGame().getTeamBleu().getPorte().getPorte().get(i).getBlock().getLocation().getY());
            tempBlock.put("z", mineralcontest.plugin.getGame().getTeamBleu().getPorte().getPorte().get(i).getBlock().getLocation().getZ());
            porteBleu.put(tempBlock);

        }



        JSONObject json = new JSONObject()
                .put(worldName, new JSONObject()
                                .put("teams", new JSONObject()
                                            .put("rouge", new JSONObject()

                                                    .put("spawn", new JSONObject()
                                                                .put("x", mineralcontest.plugin.getGame().getTeamRouge().getHouseLocation().getX())
                                                                .put("y", mineralcontest.plugin.getGame().getTeamRouge().getHouseLocation().getY())
                                                                .put("z", mineralcontest.plugin.getGame().getTeamRouge().getHouseLocation().getZ())
                                                    )
                                                    .put("coffre", new JSONObject()
                                                            .put("x", mineralcontest.plugin.getGame().getTeamRouge().getCoffreEquipeLocation().getX())
                                                            .put("y", mineralcontest.plugin.getGame().getTeamRouge().getCoffreEquipeLocation().getY())
                                                            .put("z", mineralcontest.plugin.getGame().getTeamRouge().getCoffreEquipeLocation().getZ())
                                                    )
                                                    .put("porte", porteRouge)

                                            )
                                            .put("jaune", new JSONObject()

                                                    .put("spawn", new JSONObject()
                                                            .put("x", mineralcontest.plugin.getGame().getTeamJaune().getHouseLocation().getX())
                                                            .put("y", mineralcontest.plugin.getGame().getTeamJaune().getHouseLocation().getY())
                                                            .put("z", mineralcontest.plugin.getGame().getTeamJaune().getHouseLocation().getZ())
                                                    )
                                                    .put("coffre", new JSONObject()
                                                            .put("x", mineralcontest.plugin.getGame().getTeamJaune().getCoffreEquipeLocation().getX())
                                                            .put("y", mineralcontest.plugin.getGame().getTeamJaune().getCoffreEquipeLocation().getY())
                                                            .put("z", mineralcontest.plugin.getGame().getTeamJaune().getCoffreEquipeLocation().getZ())
                                                    )
                                                    .put("porte", porteJaune)

                                            )
                                            .put("bleu", new JSONObject()

                                                    .put("spawn", new JSONObject()
                                                            .put("x", mineralcontest.plugin.getGame().getTeamBleu().getHouseLocation().getX())
                                                            .put("y", mineralcontest.plugin.getGame().getTeamBleu().getHouseLocation().getY())
                                                            .put("z", mineralcontest.plugin.getGame().getTeamBleu().getHouseLocation().getZ())
                                                    )
                                                    .put("coffre", new JSONObject()
                                                            .put("x", mineralcontest.plugin.getGame().getTeamBleu().getCoffreEquipeLocation().getX())
                                                            .put("y", mineralcontest.plugin.getGame().getTeamBleu().getCoffreEquipeLocation().getY())
                                                            .put("z", mineralcontest.plugin.getGame().getTeamBleu().getCoffreEquipeLocation().getZ())
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
