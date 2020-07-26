package fr.synchroneyes.mineral.Kits;

import fr.synchroneyes.custom_events.PlayerKitSelectedEvent;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Kits.Classes.*;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.TextUtils;
import fr.synchroneyes.mineral.mineralcontest;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe permettant de gérer les kits!
 * On utilise l'interface Listener afin de pouvoir gérer le menu de selection de kit
 */
public class KitManager implements Listener {

    // Liste des kits disponible
    private List<KitAbstract> kitsDisponible;

    // Liste des joueurs avec leurs kits
    private Map<Player, KitAbstract> kits_joueurs;

    @Getter
    @Setter
    private boolean kitsEnabled = true;

    @Getter
    @Setter
    private boolean kitSelectionOver = false;

    // Groupe où les kits doivent être gérés
    private Groupe groupe;

    // Partie où les kits doivent être gérés
    private Game partie;

    // Boucle gérant la boucle pour les action des kits (ex soutien auto heal)
    private BukkitTask boucleGestionKits;

    // Variable contenant le selecteur de kit
    private Inventory kitSelection = null;



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

        // On enregistre les events de cette classe
        Bukkit.getPluginManager().registerEvents(this, mineralcontest.plugin);

        // On y ajoute les classes disponibles
        this.kitsDisponible.add(new Agile());
        this.kitsDisponible.add(new Enchanteur());
        this.kitsDisponible.add(new Guerrier());
        this.kitsDisponible.add(new Mineur());
        this.kitsDisponible.add(new Parieur());
        this.kitsDisponible.add(new Robuste());
        this.kitsDisponible.add(new Soutien());
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

        Bukkit.getLogger().info(joueur.getDisplayName() + " -> " + kit.getNom());


        Equipe playerTeam = groupe.getPlayerTeam(joueur);

        // On averti le serveur que le joueur a sélectionner un kit
        if (playerTeam != null && !groupe.getGame().isGameStarted())
            groupe.sendToEveryone(mineralcontest.prefixGlobal + Lang.translate(Lang.kitmanager_player_selected_kit.toString(), joueur));
        if (playerTeam != null && !groupe.getGame().isGameStarted())
            playerTeam.sendMessage(mineralcontest.prefixTeamChat + Lang.translate(Lang.kitmanager_player_selected_kit_team.toString(), joueur).replace("%k", kit.getNom()));


        String separateur = ChatColor.GOLD + "----------";
        StringBuilder liste_pseudo_sans_team = new StringBuilder();

        List<Player> liste_joueur_sans_kits = getPlayerWithoutKits(false);

        // Si on a des joueurs sans kits
        if (!liste_joueur_sans_kits.isEmpty()) {

            // Pour chaque joueur sans kit
            for (Player joueur_sans_kit : liste_joueur_sans_kits)
                liste_pseudo_sans_team.append(joueur_sans_kit.getDisplayName()).append(", ");

            String liste_joueur = liste_pseudo_sans_team.toString();
            // ON retire la dernière virgule
            liste_joueur = liste_joueur.substring(0, liste_joueur.length() - 2);

            // On informe le chat
            groupe.sendToEveryone(separateur);
            groupe.sendToEveryone(mineralcontest.prefixGlobal + Lang.kitmanager_player_list_without_kits.toString() + liste_joueur);
            groupe.sendToEveryone(separateur);
        }


        // On regarde si on peut démarrer la partie
        if (doesAllPlayerHaveAKit(false)) {
            try {
                if (!groupe.getGame().isGameStarted()) groupe.getGame().demarrerPartie(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Permet de retourner le kit d'un joueur
     *
     * @param joueur
     * @return
     */
    public KitAbstract getPlayerKit(Player joueur) {
        if (!kits_joueurs.containsKey(joueur)) return null;
        return kits_joueurs.get(joueur);
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

    /**
     * Permet de récuperer l'instance d'une classe par le biais d'une classe
     *
     * @param classe
     * @return
     */
    public KitAbstract getKitFromClass(Class classe) {
        for (KitAbstract kit : kitsDisponible)
            if (kit.getClass().equals(classe)) return kit;
        return null;
    }

    public KitAbstract getKitFromString(String nomClasse) {
        for (KitAbstract kit : kitsDisponible)
            if (kit.getNom().contains(nomClasse)) return kit;
        return null;
    }


    /**
     * Permet de récuperer le menu de selection de kit
     *
     * @return menu selection de kit
     */
    public Inventory getKitSelectionInventory() {

        // On crée un inventaire d'une ligne
        if (kitSelection == null) {
            this.kitSelection = Bukkit.createInventory(null, 9, Lang.kitmanager_inventory_kitSelectionTitle.toString());

            // On veut ne veut pas de stack d'item
            this.kitSelection.setMaxStackSize(1);

            // On ajoute les kits disponibles
            for (KitAbstract kit : kitsDisponible) {
                ItemStack itemKit = new ItemStack(kit.getRepresentationMaterialForSelectionMenu());
                ItemMeta kitMeta = itemKit.getItemMeta();

                kitMeta.setDisplayName(kit.getNom());

                kitMeta.setLore(TextUtils.textToLore(kit.getDescription(), 50));

                itemKit.setItemMeta(kitMeta);

                // ON ajoute le kit
                this.kitSelection.addItem(itemKit);
            }
        }

        // ON retourne le kit
        return this.kitSelection;
    }

    /**
     * Ouvre l'inventaire pour un joueur
     *
     * @param joueur
     */
    public void openInventoryToPlayer(Player joueur) {
        joueur.openInventory(getKitSelectionInventory());
    }


    /**
     * Méthode appelé lors de la fermeture du menu par un joueur
     *
     * @param event
     */
    @EventHandler
    public void onKitSelectionMenuClosed(InventoryCloseEvent event) {

        if (kitSelectionOver) return;

        // On récupère l'inventaire fermé
        Inventory menu = event.getInventory();

        // On vérifie que ça provient d'un joueur
        if (event.getPlayer() instanceof Player) {
            Player joueur = (Player) event.getPlayer();

            // On vérifie que c'est un joueur du plugin
            if (mineralcontest.isInAMineralContestWorld(joueur)) {

                // On vérifie que c'est l'inventaire de selection de kit
                if (menu.equals(getKitSelectionInventory())) {

                    // Si le joueur n'a pas selectionner de kit, on réouvre l'inventaire
                    if (!kits_joueurs.containsKey(joueur)) {
                        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> joueur.openInventory(getKitSelectionInventory()), 1);
                    }
                }
            }
        }
    }

    /**
     * Méthode appelé lors de la selection d'un kit
     *
     * @param event
     */
    @EventHandler
    public void onKitSelected(InventoryClickEvent event) {


        if (kitSelectionOver) return;

        // On récupère l'inventaire
        Inventory inventory = event.getClickedInventory();
        if (inventory == null) return;

        // On récupère le joueur
        if (event.getWhoClicked() instanceof Player) {
            Player joueur = (Player) event.getWhoClicked();

            // On regarde si c'est l'inventaire de selection de kit
            if (inventory.equals(getKitSelectionInventory())) {
                // On récupère l'item cliqué


                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem == null || clickedItem.getItemMeta() == null) {
                    event.setCancelled(true);
                    return;
                }

                // On récupère le kit
                KitAbstract selectedKit = getKitFromString(clickedItem.getItemMeta().getDisplayName());
                if (selectedKit == null) {
                    event.setCancelled(true);
                    return;
                }

                // On possède le kit du joueur
                setPlayerKit(joueur, selectedKit);
                event.setCancelled(true);
                joueur.closeInventory();
            }
        }

    }


    /**
     * Permet d'ouvrir le mnu à tous les joueurs
     *
     * @param openToReferee - Si vrai, on ouvrira également le menu aux arbitres
     */
    public void openMenuToEveryone(boolean openToReferee) {

        // Pour chaque jouuer de la partie
        for (Player joueur : groupe.getPlayers()) {

            if (groupe.getGame().isReferee(joueur)) {
                if (openToReferee) {
                    joueur.openInventory(getKitSelectionInventory());
                }
                continue;
            }

            // Le jouuer n'est pas arbitre
            joueur.openInventory(getKitSelectionInventory());

        }
    }


    /**
     * Fonction retournant vrai si tous les joueurs ont un kit
     *
     * @param includeReferee - Inclure ou non les arbitre dans les joueurs qui ont besoin d'avoir un kit
     * @return boolean
     */
    public boolean doesAllPlayerHaveAKit(boolean includeReferee) {

        // On regarde pour tous les joueurs du groupe
        for (Player joueur : groupe.getPlayers()) {

            if (groupe.getGame().isReferee(joueur)) {
                if (includeReferee) {
                    // Si on ne contient pas le joueur dans la liste d'association de kit <-> joueur, on retourne faux
                    if (!kits_joueurs.containsKey(joueur)) return false;
                }
                continue;
            }


            // Si on ne contient pas le joueur dans la liste d'association de kit <-> joueur, on retourne faux
            if (!kits_joueurs.containsKey(joueur)) return false;

        }

        // Tous les joueurs ont un kit, on retourne vrai
        return true;
    }


    /**
     * Récupère une liste de joueurs sans kit
     *
     * @param includeReferee - Inclure ou non les arbitre dans les joueurs qui ont besoin d'avoir un kit
     * @return
     */
    public List<Player> getPlayerWithoutKits(boolean includeReferee) {
        List<Player> joueurs_sans_kits = new ArrayList<>();

        // On regarde pour tous les joueurs du groupe
        for (Player joueur : groupe.getPlayers()) {

            if (groupe.getGame().isReferee(joueur)) {
                if (includeReferee) {
                    // Si on ne contient pas le joueur dans la liste d'association de kit <-> joueur, on retourne faux
                    if (!kits_joueurs.containsKey(joueur)) joueurs_sans_kits.add(joueur);
                }
                continue;
            }


            // Si on ne contient pas le joueur dans la liste d'association de kit <-> joueur, on retourne faux
            if (!kits_joueurs.containsKey(joueur)) joueurs_sans_kits.add(joueur);

        }

        return joueurs_sans_kits;
    }
}
