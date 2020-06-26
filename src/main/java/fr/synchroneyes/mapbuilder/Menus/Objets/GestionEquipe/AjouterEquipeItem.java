package fr.synchroneyes.mapbuilder.Menus.Objets.GestionEquipe;

import fr.synchroneyes.mapbuilder.Menus.Objets.ObjetTemplate;
import fr.synchroneyes.mapbuilder.Menus.SousMenu.MenuTemplate;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class AjouterEquipeItem extends ObjetTemplate {

    public AjouterEquipeItem(MenuTemplate parent) {
        super(parent);
    }

    @Override
    public String getNomItem() {
        return "Ajouter une équipe";
    }

    @Override
    public List<String> getDescription() {
        LinkedList<String> t = new LinkedList<>();
        t.add("Ajouter une équipe");
        return t;
    }

    @Override
    public Material getItemMaterial() {
        return Material.BLUE_CONCRETE;
    }

    @Override
    public void onClick(Player joueur) {
        joueur.sendMessage("ok");
    }
}
