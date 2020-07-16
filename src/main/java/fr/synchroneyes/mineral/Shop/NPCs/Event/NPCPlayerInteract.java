package fr.synchroneyes.mineral.Shop.NPCs.Event;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Exception.EventAlreadyHandledException;
import fr.synchroneyes.mineral.Shop.NPCs.NPCTemplate;
import fr.synchroneyes.mineral.Shop.ShopManager;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class NPCPlayerInteract {


    /**
     * Fonction permettant de vérifier l'interaction d'un joueur avec les NPC
     *
     * @param entityEvent
     */
    public static void OnPlayerRightClick(PlayerInteractAtEntityEvent entityEvent) throws EventAlreadyHandledException {
        Player joueur = entityEvent.getPlayer();
        if (mineralcontest.isInAMineralContestWorld(joueur)) {
            Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);

            if (playerGroup == null) return;
            Game partie = playerGroup.getGame();
            ShopManager shopManager = partie.getShopManager();

            if (entityEvent.getRightClicked() instanceof Villager) {
                Villager clickedEntity = (Villager) entityEvent.getRightClicked();

                for (NPCTemplate npc : shopManager.getListe_pnj()) {
                    if (npc.getEmplacement().equals(clickedEntity.getLocation())) {
                        entityEvent.setCancelled(true);
                        joueur.closeInventory();
                        npc.onNPCRightClick(joueur);

                        throw new EventAlreadyHandledException();
                    }
                }
            }


        }
    }
}
