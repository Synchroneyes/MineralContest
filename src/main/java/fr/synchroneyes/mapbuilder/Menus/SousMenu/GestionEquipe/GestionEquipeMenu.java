package fr.synchroneyes.mapbuilder.Menus.SousMenu.GestionEquipe;

import fr.synchroneyes.mapbuilder.Menus.Objets.GestionEquipe.AjouterEquipeItem;
import fr.synchroneyes.mapbuilder.Menus.SousMenu.MenuTemplate;
import org.bukkit.Material;

import java.util.LinkedList;
import java.util.List;

public class GestionEquipeMenu extends MenuTemplate {

    public GestionEquipeMenu() {
        super(true);
        ajouterItem(new AjouterEquipeItem(this));
    }

    @Override
    public String getNomInventaire() {
        return "Gestion des équipes";
    }

    @Override
    public List<String> getDescription() {
        LinkedList<String> desc = new LinkedList<>();
        desc.add("Menu de gestion des équipes");
        return null;
    }


    @Override
    public Material getItemMaterial() {
        return Material.GREEN_CONCRETE;
    }
}
