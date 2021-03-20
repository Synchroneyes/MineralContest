package fr.synchroneyes.challenges;

import fr.synchroneyes.challenges.Availables.AbstractChallenge;
import fr.synchroneyes.challenges.Availables.AbstractRepeatableChallenge;
import fr.synchroneyes.challenges.Availables.PoserBlockTest;
import fr.synchroneyes.challenges.Availables.TuerZombie;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.Utils.Pair;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ChallengeManager {

    private List<AbstractChallenge> liste_defis;

    private int CHALLENGE_COMPLETED = -1;

    // <MCPlayer, List<Achievement, AchievementDone> >
    // Si le booléen est vrai, alors le joueur a terminé le defis
    private HashMap<MCPlayer, List<Pair<AbstractChallenge, Integer>>> defis_par_joueur;

    private Game game;


    public ChallengeManager(Game game){
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

    public List<AbstractChallenge> getListe_defis() {
        return liste_defis;
    }

    public AbstractChallenge getAchievement(Class clazz) {
        for(AbstractChallenge achievement : liste_defis)
            if(achievement.getClass().getName().equals(clazz.getName())) return achievement;
        return null;
    }

    /**
     * Méthode permettant d'ajouter un objectif à un joueur
     * @param joueur
     * @param abstractChallenge
     */
    public void addPlayerAchievement(MCPlayer joueur, AbstractChallenge abstractChallenge){
        List<Pair<AbstractChallenge, Integer>> defis_joueur = defis_par_joueur.computeIfAbsent(joueur, k -> new LinkedList<>());

        // On ajoute le défi au joueur
        defis_joueur.add(new Pair<>(abstractChallenge, 0));

        defis_par_joueur.replace(joueur, defis_joueur);

        joueur.sendPrivateMessage("Vous avez un nouveau défi: " + abstractChallenge.getNom());
        joueur.sendPrivateMessage(abstractChallenge.getObjectifTexte());

    }

    public boolean doesPlayerHaveThisAchievement(MCPlayer mcPlayer, AbstractChallenge abstractChallenge){
        if(defis_par_joueur.get(mcPlayer) == null) return false;

        for(Pair<AbstractChallenge, Integer> defis : defis_par_joueur.get(mcPlayer)) {
            if (defis.getKey().getNom().equals(abstractChallenge.getNom()) && defis.getValue() != CHALLENGE_COMPLETED) {
                return true;
            }
        }
        return false;
    }

    /**
     * Méthode permettant de marquer un achievement comme terminé
     * @param player
     * @param abstractChallenge
     * @return
     */
    public void playerDidAchievement(MCPlayer player, AbstractChallenge abstractChallenge) {
        // On marque l'achievement comme étant terminé
        List<Pair<AbstractChallenge, Integer>> defis_joueur = defis_par_joueur.get(player);

        if(defis_joueur == null) return;
        for(Pair<AbstractChallenge, Integer> defis : defis_par_joueur.get(player)) {

            AbstractChallenge challenge = defis.getKey();

            if(doesPlayerHaveThisAchievement(player, challenge)) {
                // Dans le cas où il faut repeter le challenge X fois pour le marquer comme gagné
                if(challenge instanceof AbstractRepeatableChallenge){
                    AbstractRepeatableChallenge abstractRepeatableChallenge = (AbstractRepeatableChallenge) challenge;
                    // On récupère le nombre de fois déjà fait, qu'on incrémente
                    int nb_realisation = defis.getValue() + 1;

                    if(abstractRepeatableChallenge.repetitionNeeded() == nb_realisation) {
                        defis.setValue(CHALLENGE_COMPLETED);
                        challenge.setAchievementCompleted(player);
                    } else {
                        defis.setValue(nb_realisation);

                    }
                    return;
                }

                // On reward le joueur
                defis.setValue(CHALLENGE_COMPLETED);
                challenge.setAchievementCompleted(player);
                return;
            }

        }

    }

    public void unloadAchievementManager() {
        for(AbstractChallenge achievement : liste_defis)
            HandlerList.unregisterAll(achievement);
    }

    public void init(){
        for(AbstractChallenge achievement : liste_defis) {
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
