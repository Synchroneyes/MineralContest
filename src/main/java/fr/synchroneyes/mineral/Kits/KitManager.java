package fr.synchroneyes.mineral.Kits;

import fr.synchroneyes.custom_events.PlayerKitSelectedEvent;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Kits.Classes.Soutien;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe permettant de gérer les kits!
 */
public class KitManager {

    // Liste des kits disponible
    private List<KitAbstract> kitsDisponible;

    // Liste des joueurs avec leurs kits
    private Map<Player, KitAbstract> kits_joueurs;

    private boolean areKitsEnabled = false;

    // Groupe où les kits doivent être gérés
    private Groupe groupe;

    // Partie où les kits doivent être gérés
    private Game partie;

    private BukkitTask boucleGestionKits;


    /**
     * Constructeur, permettant d'instancier les classes
     *
     * @param groupe
     */
    public KitManager(Groupe groupe) {
        this.kitsDisponible = new ArrayList<>();
        this.kits_joueurs = new HashMap<>();

        this.groupe = groupe;
        this.partie = groupe.getGame();

        // On y ajoute les classes disponibles
        //this.kitsDisponible.add(new Guerrier());
    }


    /**
     * Permet d'attribuer un kit à un joueur
     *
     * @param joueur
     * @param kit
     */
    public void setPlayerKit(Player joueur, KitAbstract kit) {
        if (kits_joueurs.containsKey(joueur)) kits_joueurs.replace(joueur, kit);
        else kits_joueurs.put(joueur, kit);

        PlayerKitSelectedEvent event = new PlayerKitSelectedEvent(joueur, kit);
        Bukkit.getPluginManager().callEvent(event);

        joueur.sendMessage("Vous êtes maintenant: " + kit.getNom());
    }

    /**
     * Permet de retourner le kit d'un joueur
     *
     * @param joueur
     * @return
     */
    public Class<? extends KitAbstract> getPlayerKit(Player joueur) {
        if (!kits_joueurs.containsKey(joueur)) return null;
        return kits_joueurs.get(joueur).getClass();
    }

    /**
     * Fonction permettant d'effectuer un tour de boucle, de gestion des kits
     * Exemple, pour le kit soutien, on utilisera la fonction de heal
     */
    private void kitLoop() {

        // On regarde le kit de chaque joueur
        for (Player joueur : groupe.getPlayers()) {

            // On regarde si le joueur possède un kit
            if (kits_joueurs.containsKey(joueur)) {

                // Si le joueur possède le kit soutien
                KitAbstract kit_joueur = kits_joueurs.get(joueur);
                if (kit_joueur instanceof Soutien) ((Soutien) kit_joueur).healAroundPlayer(joueur);

            }

        }
    }

    /**
     * FOnction permettant de démarrer la boucle de gestion des kits
     *
     * @param delay
     */
    public void startKitLoop(int delay) {
        if (boucleGestionKits == null) {
            boucleGestionKits = Bukkit.getScheduler().runTaskTimer(mineralcontest.plugin, this::kitLoop, 0, delay);
        }

    }
}
