package fr.synchroneyes.mineral.Core.Game.JoinTeam.Inventories;

import fr.synchroneyes.mineral.Core.Game.JoinTeam.Items.JoinTeamItem;
import fr.synchroneyes.mineral.Core.House;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.Material;

import java.util.List;

public class SelectionEquipeInventory extends InventoryInterface {

    private List<House> maisons;

    public SelectionEquipeInventory() {
        // On veut 3 lignes, donc 3*9 items
        super(true, 3 * 9);
    }

    public SelectionEquipeInventory(List<House> maisons) {
        // On veut 3 lignes, donc 3*9 items
        super(true, 3 * 9);
        this.maisons = maisons;
    }


    @Override
    public void setInventoryItems() {
        inventaire.setMaxStackSize(1);


        /**
         * Pour styliser, le mieux serait de faire:
         *  - 1 bloc sur 2 entre chaque équipe
         *  - Centrer les blocs
         *
         *  Pour centrer les blocs, on prend le nombre d'espace avant un bloc + sa taille
         *  On multiplie ce résultat par le nombre d'équipe, et on divise le 9/nombre equipe
         *  On arrondi au supérieur, pour avoir le nombre de block à sauter avant de commencer a afficher
         */


        // Position de départ dans l'inventaire
        int positionItem = 8;


        // Nombre de case avant un bloc
        int espaceAvantBloc = 1;
        // Nombre de case pour un bloc
        int tailleBloc = 1;
        if (maisons != null) {
            // On récupère le nombre d'équipe
            int nombreEquipe = maisons.size();
            int resultatOperationNombreEquipe = nombreEquipe * (tailleBloc + espaceAvantBloc);

            int nombreItemsParLigne = 9;

            positionItem += Math.ceil(nombreItemsParLigne / nombreEquipe);

            for (House maison : maisons) {
                JoinTeamItem item = new JoinTeamItem(maison.getTeam());
                inventaire.setItem(positionItem, item.toItemStack());
                items.add(item);
                positionItem += (espaceAvantBloc + tailleBloc);
            }
        }
    }

    @Override
    public Material getItemMaterial() {
        return Material.BOOK;
    }

    @Override
    public String getNomInventaire() {
        return Lang.item_team_selection_title.toString();
    }

    @Override
    public String getDescriptionInventaire() {
        return Lang.item_team_selection_title.toString();
    }
}
