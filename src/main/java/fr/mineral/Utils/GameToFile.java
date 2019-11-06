package fr.mineral.Utils;


/*
TODO:
    Verif les portes
    Selectionner les portes
 */

import fr.mineral.Core.Arena;
import fr.mineral.Teams.Equipe;
import fr.mineral.mineralcontest;
import org.bukkit.Location;
import org.json.JSONArray;
import org.json.JSONObject;


import java.util.Locale;

public class GameToFile {
    public String worldName;
    public Equipe teamRouge, teamJaune, teamBleu;
    public Arena arene;

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
        JSONObject tempBlock = new JSONObject();

        JSONArray porteRouge = new JSONArray();
        for(DisplayBlock block : mineralcontest.plugin.getGame().getTeamRouge().getPorte().getPorte()) {
            tempBlock.put("x", block.getBlock().getLocation().getX());
            tempBlock.put("y", block.getBlock().getLocation().getY());
            tempBlock.put("z", block.getBlock().getLocation().getZ());
            porteRouge.put(tempBlock);
        }


        JSONArray porteJaune = new JSONArray();
        for(DisplayBlock block : mineralcontest.plugin.getGame().getTeamJaune().getPorte().getPorte()) {
            tempBlock.put("x", block.getBlock().getLocation().getX());
            tempBlock.put("y", block.getBlock().getLocation().getY());
            tempBlock.put("z", block.getBlock().getLocation().getZ());
            porteJaune.put(tempBlock);

        }

        JSONArray porteBleu = new JSONArray();
        for(DisplayBlock block : mineralcontest.plugin.getGame().getTeamBleu().getPorte().getPorte()) {
            tempBlock.put("x", block.getBlock().getLocation().getX());
            tempBlock.put("y", block.getBlock().getLocation().getY());
            tempBlock.put("z", block.getBlock().getLocation().getZ());
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
                                                    .put("porte", porteJaune)

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

    public Object getWorldObject() throws Exception {
        class Obj {
            Object teamRouge, teamJaune, teamBleu;
            Object arene;
        }

        Obj obj = new Obj();
        obj.teamRouge = TeamToObj(mineralcontest.plugin.getGame().getTeamRouge());
        obj.teamJaune = TeamToObj(mineralcontest.plugin.getGame().getTeamJaune());
        obj.teamBleu = TeamToObj(mineralcontest.plugin.getGame().getTeamBleu());

        obj.arene = ArenaToObj(mineralcontest.plugin.getGame().getArene());
        return obj;

    }

    public Object TeamToObj(Equipe team) throws Exception {
        class Obj {
            Object positionCoffre;
            Object positionSpawn;
        }

        Obj objet = new Obj();
        objet.positionCoffre = LocationToObj(team.getCoffreEquipeLocation());
        objet.positionSpawn = LocationToObj(team.getHouseLocation());

        return objet;
    }

    public Object ArenaToObj(Arena arene) throws Exception {
        class Obj {
            Object positionSpawnTP;
            Object positionCoffre;
            Object positionDeathZone;
        }
        Obj objet = new Obj();
        objet.positionCoffre = LocationToObj(arene.getCoffre().getPosition());
        objet.positionSpawnTP = LocationToObj(arene.getTeleportSpawn());
        objet.positionDeathZone = LocationToObj(arene.getDeathZone().getSpawnLocation());
        return objet;


    }

    public Object LocationToObj(Location l) {
        class Obj {
            double x, y, z;
        }

        Obj position = new Obj();
        position.x = l.getX();
        position.y = l.getY();
        position.z = l.getZ();

        return position;
    }


}
