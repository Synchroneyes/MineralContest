package fr.mapbuilder.Core;

import fr.mineral.Core.Arena.Arene;
import fr.mineral.Core.House;
import fr.mineral.Teams.Equipe;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.LinkedList;

public class Monde {

    private LinkedList<House> equipes;
    private Arene arene;
    private Location spawnDepart;
    private String nom;

    public Monde() {
        equipes = new LinkedList<>();
        arene = new Arene();
    }

    public Arene getArene() {
        return arene;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LinkedList<String> getNomsEquipe() {
        LinkedList<String> noms = new LinkedList<>();
        for (House e : equipes)
            noms.add(e.getTeam().getNomEquipe());
        return noms;
    }

    public LinkedList<House> getHouses() {
        return equipes;
    }

    public void ajouterEquipe(String nom, ChatColor couleur) {
        if (isTeamCree(nom)) {
            Bukkit.broadcastMessage(mineralcontest.prefixGlobal + "L'équipe " + nom + " existe déjà");
            return;
        }
        this.equipes.add(new House(nom, couleur));
        Bukkit.broadcastMessage(mineralcontest.prefixGlobal + "L'équipe " + couleur + nom + ChatColor.WHITE + " a été crée avec succès");
    }

    public void supprimerEquipe(String nom) {
        House maison;
        if ((maison = getHouseFromNom(nom)) == null) {
            // Equipe n'existe pas
            return;
        }

        equipes.remove(maison);

    }

    public boolean isTeamCree(String nom) {
        return (getHouseFromNom(nom) != null);
    }

    public House getHouseFromNom(String nom) {
        for (House equipe : equipes) {
            if (equipe.getTeam().getNomEquipe().equalsIgnoreCase(nom)) return equipe;
        }

        return null;
    }
}
