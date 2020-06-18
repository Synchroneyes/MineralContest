package fr.mineral.Core;

import fr.groups.Core.Groupe;
import fr.mineral.Core.Arena.Coffre;
import fr.mineral.Teams.Equipe;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.Door.AutomaticDoors;
import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import java.util.LinkedHashMap;
import java.util.Map;

public class House {
    private Equipe team;
    private AutomaticDoors doors;
    private LinkedHashMap<Block, MaterialData> blocks;
    private Coffre coffre;
    private Location spawnLocation;
    private String teamName;
    private ChatColor color;
    private Groupe groupe;


    public House(String nomEquipe, ChatColor couleur, Groupe g) {
        this.teamName = nomEquipe;
        this.color = couleur;
        this.team = new Equipe(this.teamName, this.color, g, this);
        this.doors = new AutomaticDoors(team, g);
        this.blocks = new LinkedHashMap<>();
        this.groupe = g;
    }

    public void clearHouse() {

        /*if(spawnLocation != null)
            for(Player teamMember : team.getJoueurs())
                teamMember.teleport(getHouseLocation());*/

        this.doors.clear();
        this.blocks.clear();
        this.team.clear();
        if(this.coffre != null) this.coffre.clear();
    }

    public Coffre getCoffre() { return this.coffre;}

    /*
            Used to save house blocks
     */

    public Equipe getTeam() {
        return this.team;
    }

    public void addBlock(Location location) throws Exception{
        MaterialData materialData;
        Block block = location.getBlock();

        if(block.getType().equals(Material.AIR)) {
            throw new Exception("Impossible d'ajouter de l'air comme block");
        }
        materialData = block.getState().getData();
        this.blocks.put(block, materialData);
        mineralcontest.log.info(mineralcontest.prefix + ChatColor.GOLD + "Block " + block.getType().toString() + " successfully added");
    }


    public void removeBlock(Location location) throws Exception {
        MaterialData materialData;
        Block block = location.getBlock();

        if(block.getType().equals(Material.AIR)) {
            throw new Exception("Impossible d'ajouter de l'air comme block");
        }
        materialData = block.getState().getData();
        /* For each "value<block, materialdata> in saved blocks*/
        for (Map.Entry<Block, MaterialData> data : blocks.entrySet()) {
            Block foreachBlock;
            MaterialData foreachMaterialData;
            foreachBlock = data.getKey();
            foreachMaterialData = data.getValue();

            if(foreachBlock.getLocation().equals(location) && foreachMaterialData.equals(location.getBlock().getState().getData())) {
                this.blocks.remove(foreachBlock, foreachMaterialData);
                mineralcontest.log.info(mineralcontest.prefix + ChatColor.RED + "Block " + foreachBlock.getType().toString() + " successfully removed");
                return;
            }
        }
    }

    /*
    -------------------------------------------
     */


    public void setCoffreEquipe(Location loc) {
        this.coffre = new Coffre(groupe);
        this.coffre.setPosition(loc);
        if(mineralcontest.debug) mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_chest_added.toString(), team), groupe);

    }

    public Location getCoffreEquipeLocation() throws Exception {
        if(this.coffre.getPosition() == null)
            throw new Exception(Lang.translate(Lang.chest_not_defined.toString(), team));
        return coffre.getPosition();
    }

    public void spawnCoffreEquipe() throws Exception {
        Location loc = coffre.getPosition();
        coffre.clear();
        loc.getBlock().setType(Material.CHEST);
        this.groupe.getGame().addAChest(loc.getBlock());
    }

    public void setHouseLocation(Location houseLocation){
        if(mineralcontest.debug)  mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_house_location_added.toString(), team), groupe);
        this.spawnLocation = houseLocation;
    }

    public Location getHouseLocation() throws Exception {
        if(this.spawnLocation == null)
            throw new Exception(Lang.translate(Lang.team_house_location_not_added.toString(), team));
        return spawnLocation;
    }


    public AutomaticDoors getPorte() {
        return doors;
    }
}
