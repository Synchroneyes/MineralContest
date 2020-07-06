package fr.synchroneyes.mineral.Core.Coffre;

import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AutomatedChestAnimation {

    // Variable pour controler si le coffre a été ouvert ou non
    protected boolean isChestContentGenerated = false;

    // variable pour enregistrer le joueur ouvrant le coffre
    protected Player openingPlayer = null;

    // Variable contenant le contenu de l'inventaire du coffre
    private Inventory inventaireCoffre;

    // Variable contenant la position du coffre
    private Location chestLocation;

    protected boolean isAnimationOver = false;

    private int tailleInventaire;

    private BukkitTask tacheOuverture = null;


    // Permet de vérifier si le coffre est apparu ou non
    private boolean isChestSpawned = false;

    // Utile afin de mettre à jour le coffre dans la liste
    private AutomatedChestManager manager;


    /**
     * Constructeur, permet de donner en paramètre le nom de l'inventaire ainsi que la taille
     *
     * @param tailleInventaire - Taille de l'inventaire, doit-être un multiple de 7
     */
    public AutomatedChestAnimation(int tailleInventaire, AutomatedChestManager manager) {
        this.tailleInventaire = tailleInventaire;
        this.inventaireCoffre = Bukkit.createInventory(null, tailleInventaire, getOpeningChestTitle());
        this.manager = manager;
    }


    public void setChestLocation(Location chestLocation) {
        this.chestLocation = chestLocation;
        updateManager();
    }

    public Player getOpeningPlayer() {
        return openingPlayer;
    }

    public Inventory getInventory() {
        return inventaireCoffre;
    }

    public boolean isBeingOpened() {
        return openingPlayer != null;
    }

    public boolean isAnimationOver() {
        return isAnimationOver;
    }


    /**
     * Fonction appelée avant de faire apparaitre le coffre
     */
    public abstract void actionToPerformBeforeSpawn();

    /**
     * Fonction appelée lorsque le coffre a été ouvert
     */
    public abstract void actionToPerformAfterAnimationOver();

    /**
     * Fonction permettant d'afficher ou non les items d'attente
     *
     * @return
     */
    public abstract boolean displayWaitingItems();

    /**
     * Récupère le nom du coffre lors de l'ouverture
     *
     * @return
     */
    public abstract String getOpeningChestTitle();


    /**
     * Récupère le nom du coffre lorsqu'il est ouvert
     *
     * @return
     */
    public abstract String getOpenedChestTitle();


    public Location getLocation() {
        return chestLocation;
    }

    /**
     * Le WaitingItemMaterial est l'item qui sera affiché en tout premier
     *
     * @return ItemStack
     */
    public abstract ItemStack getWaitingItemMaterial();

    /**
     * Le UsedItemMaterial est l'item qui sera affiché lorsque l'animation sera en cours
     *
     * @return ItemStack
     */
    public abstract ItemStack getUsedItemMaterial();

    /**
     * Récupère la séquence d'ouverture du coffre (pour l'animation, les numéros de slots, ex: 27;26;25;24;10;11;12 ...)
     *
     * @return Liste ordonnée
     */
    public abstract LinkedList<Integer> getOpeningSequence();

    /**
     * Retourne le type de coffre à ouvrir
     *
     * @return
     */
    public abstract Material getChestMaterial();

    /**
     * Retourne le temps en seconde d'ouverture d'un coffre
     *
     * @return
     */
    public abstract int getAnimationTime();

    /**
     * Retourne si oui ou non le coffre peut être ouvert pas plusieurs joueurs
     *
     * @return
     */
    public abstract boolean canChestBeOpenedByMultiplePlayers();

    /**
     * Fonction permettant de générer le contenu du coffre après ouverture
     *
     * @return
     */
    public abstract List<ItemStack> genererContenuCoffre();


    /**
     * Fonction permettant de définir si oui ou non l'utilisateur doit récuperer le contenu du coffre automatiquement après l'ouverture
     *
     * @return
     */
    public abstract boolean automaticallyGiveItemsToPlayer();


    /**
     * Fonction permettant d'executer l'animation
     */
    public void performAnimation() {

        // Si l'animation est terminée, on s'arrête tout de suite
        if (isAnimationOver) return;

        // On clear l'inventaire
        this.inventaireCoffre.clear();

        // On récupère la séquence et le nombre d'item
        int nombreItemSequence = getOpeningSequence().size();

        // On récupère le temps d'execution
        double tempsExecution = getAnimationTime();

        // Variable permettant de mettre en pause l'animation avant de passer au prochain tour de boucle
        // On multiplie le tempsExecution afin d'avoir un résultat en millisecondes
        double tempsPauseEntreChaqueTour = (tempsExecution * 1000) / nombreItemSequence;

        double intervalTimer = (tempsPauseEntreChaqueTour * 20) / 1000;

        // On commence par remplir l'inventaire avec les items en attente
        // Seulement si l'option est activée
        if (displayWaitingItems())
            for (int slot : getOpeningSequence())
                inventaireCoffre.setItem(slot, getWaitingItemMaterial());


        // Variable pour stocker la séquence actuelle afin de savoir quelle bloc il faut changer
        AtomicInteger indexSequence = new AtomicInteger(0);

        // On lance la boucle d'animation
        tacheOuverture = new BukkitRunnable() {
            @Override
            public void run() {

                // Dans le cas où le joueur qui a ouvert le coffre s'est fait attaqué ou a fermer le coffre
                if (openingPlayer == null) {
                    this.cancel();
                    return;
                }

                if (indexSequence.get() > nombreItemSequence - 1) {
                    inventaireCoffre.clear();
                    inventaireCoffre = Bukkit.createInventory(null, tailleInventaire, getOpenedChestTitle());

                    // On rempli l'inventaire du coffre
                    List<ItemStack> itemsGenere = genererContenuCoffre();
                    for (ItemStack item : itemsGenere)
                        inventaireCoffre.addItem(item);

                    // On marque le coffre comme ayant été généré
                    isChestContentGenerated = true;

                    // On dit que l'animation du coffre est terminée
                    isAnimationOver = true;

                    openInventoryToPlayer(openingPlayer);

                    actionToPerformAfterAnimationOver();


                    // On ferme le coffre en question
                    closeInventory();



                    this.cancel();


                    return;
                }

                // On remplace l'index actuel par le bloc "utilisé"
                int slot = getOpeningSequence().get(indexSequence.get());
                inventaireCoffre.setItem(slot, getUsedItemMaterial());

                // On incrémente le compteur
                indexSequence.incrementAndGet();


            }
        }.runTaskTimer(mineralcontest.plugin, 0, (long) intervalTimer);

    }


    /**
     * Fonction permettant de mettre le joueur ouvrant le coffre
     *
     * @param p
     */
    public void setOpeningPlayer(Player p) {
        this.openingPlayer = p;
        performAnimation();
        p.openInventory(inventaireCoffre);
    }

    public void closeInventory() {
        this.openingPlayer = null;
        if (tacheOuverture != null) tacheOuverture.cancel();
    }

    public void spawn() {

        actionToPerformBeforeSpawn();

        isChestSpawned = true;
        isAnimationOver = false;
        isChestContentGenerated = false;
        inventaireCoffre = Bukkit.createInventory(null, tailleInventaire, getOpeningChestTitle());
        getLocation().getBlock().setType(getChestMaterial());
    }

    /**
     * Fonction appelée lorsqu'il faut donner le contenu d'un inventaire à un joueur
     *
     * @param p
     */
    public void openInventoryToPlayer(Player p) {
        // On vérifie si le contenu du coffre  a été généré
        if (isChestContentGenerated) {
            if (automaticallyGiveItemsToPlayer()) {

                for (ItemStack item : inventaireCoffre.getContents())
                    if (item != null) p.getInventory().addItem(item);

                inventaireCoffre.clear();
                // On supprime le bloc
                getLocation().getBlock().setType(Material.AIR);
                openingPlayer.closeInventory();

            } else {
                p.openInventory(inventaireCoffre);
            }
        }
    }

    public boolean isChestSpawned() {
        return isChestSpawned;
    }

    public void updateManager() {
        this.manager.replace(this.getClass(), this);
    }
}
