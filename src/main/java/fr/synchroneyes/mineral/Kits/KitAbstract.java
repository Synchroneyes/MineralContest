package fr.synchroneyes.mineral.Kits;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Classe abstraite permettant de gérer des classes
 * On implémente l'interface Listener afin de pouvoir gérer des évènements directement depuis les classes
 */
public abstract class KitAbstract implements Listener {

    /**
     * Permet de récuperer le nom du kit
     *
     * @return
     */
    public abstract String getNom();

    /**
     * Permet de récupérer la description du kit
     *
     * @return
     */
    public abstract String getDescription();


    public KitAbstract() {
        // Afin de pouvoir gérer les évènements
        Bukkit.getPluginManager().registerEvents(this, mineralcontest.plugin);
    }


    /**
     * Définir l'item qu'il faut afficher dans le menu
     *
     * @return
     */
    public abstract Material getRepresentationMaterialForSelectionMenu();


    /**
     * Fonction permettant de vérifier si un utilisateur utilise ce kit ou non
     * Cette fonction sera appelé dans les évènements afin de vérifier si il peut bénéficier des avantages du kit ou non
     *
     * @return
     */
    public boolean isPlayerUsingThisKit(Player joueur) {
        Groupe groupe = mineralcontest.getPlayerGroupe(joueur);

        // Si le groupe est nulle, il n'a pas de kit
        if (groupe == null) return false;

        // Si la partie n'est pas démarré, il n'a pas de kit non plus, ou alors il ne faut pas les activer
        if (!groupe.getGame().isGameStarted()) return false;

        if (groupe.getKitManager().getPlayerKit(joueur) == null) return false;

        // On vérifie maintenant si l'utilisateur possède ce kit
        return groupe.getKitManager().getPlayerKit(joueur).equals(groupe.getKitManager().getKitFromClass(this.getClass()));

    }


}
