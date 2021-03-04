package fr.synchroneyes.achievements;

import fr.synchroneyes.achievements.Availables.AbstractAchievement;
import fr.synchroneyes.achievements.Availables.PoserBlockTest;
import fr.synchroneyes.achievements.Availables.TuerZombie;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.Utils.Pair;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AchievementManager {

    private List<AbstractAchievement> liste_defis;

    // <MCPlayer, List<Achievement, AchievementDone> >
    // Si le booléen est vrai, alors le joueur a terminé le defis
    private HashMap<MCPlayer, List<Pair<AbstractAchievement, Boolean>>> defis_par_joueur;

    private Game game;


    public AchievementManager(Game game){
        this.game = game;
        this.liste_defis = new ArrayList<>();
        this.defis_par_joueur = new HashMap<>();

        initAchievements();
    }


    /**
     * Méthode permettant d'initialiser les achievements
     */
    private void initAchievements() {
        this.liste_defis.add(new PoserBlockTest(this));
        this.liste_defis.add(new TuerZombie(this));
    }

    public List<AbstractAchievement> getListe_defis() {
        return liste_defis;
    }

    public AbstractAchievement getAchievement(Class clazz) {
        for(AbstractAchievement achievement : liste_defis)
            if(achievement.getClass().getName().equals(clazz.getName())) return achievement;
        return null;
    }

    /**
     * Méthode permettant d'ajouter un objectif à un joueur
     * @param joueur
     * @param abstractAchievement
     */
    public void addPlayerAchievement(MCPlayer joueur, AbstractAchievement abstractAchievement){
        List<Pair<AbstractAchievement, Boolean>> defis_joueur = defis_par_joueur.computeIfAbsent(joueur, k -> new LinkedList<>());

        // On ajoute le défi au joueur
        defis_joueur.add(new Pair<>(abstractAchievement, false));

        defis_par_joueur.replace(joueur, defis_joueur);

        joueur.sendPrivateMessage("Vous avez un nouveau défi: " + abstractAchievement.getNom());
        joueur.sendPrivateMessage(abstractAchievement.getObjectifTexte());

    }

    public boolean doesPlayerHaveThisAchievement(MCPlayer mcPlayer, AbstractAchievement abstractAchievement){
        if(defis_par_joueur.get(mcPlayer) == null) return false;

        for(Pair<AbstractAchievement, Boolean> defis : defis_par_joueur.get(mcPlayer)) {
            if (defis.getKey().getNom().equals(abstractAchievement.getNom()) && !defis.getValue()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Méthode permettant de marquer un achievement comme terminé
     * @param player
     * @param abstractAchievement
     * @return
     */
    public void playerDidAchievement(MCPlayer player, AbstractAchievement abstractAchievement) {
        // On marque l'achievement comme étant terminé
        List<Pair<AbstractAchievement, Boolean>> defis_joueur = defis_par_joueur.get(player);

        if(defis_joueur == null) return;
        for(Pair<AbstractAchievement, Boolean> defis : defis_par_joueur.get(player)) {
            if (defis.getKey().getNom().equals(abstractAchievement.getNom()) && !defis.getValue()) {
                defis.setValue(true);
                abstractAchievement.setAchievementCompleted(player);
                return;
            }
        }

    }

    public void unloadAchievementManager() {
        for(AbstractAchievement achievement : liste_defis)
            HandlerList.unregisterAll(achievement);
    }

    public void init(){
        for(AbstractAchievement achievement : liste_defis) {
            Bukkit.getLogger().info("Registered achievement: " + achievement.getNom());
            Bukkit.getPluginManager().registerEvents(achievement, mineralcontest.plugin);
        }

        // On récupère les membres de la partie
        for(Player joueur : game.groupe.getPlayers()) {
            MCPlayer mcPlayer = mineralcontest.plugin.getMCPlayer(joueur);
            addPlayerAchievement(mcPlayer, getAchievement(PoserBlockTest.class));
            addPlayerAchievement(mcPlayer, getAchievement(TuerZombie.class));
        }
    }


}
