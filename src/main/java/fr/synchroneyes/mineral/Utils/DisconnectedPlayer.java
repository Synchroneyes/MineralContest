package fr.synchroneyes.mineral.Utils;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Utils.Player.CouplePlayer;
import org.bukkit.Location;

import java.util.UUID;

/**
 * Classe permettant de gérer les joueurs se déconnectant
 */
public class DisconnectedPlayer {
    private UUID playerUUID;
    private Equipe oldPlayerTeam;
    private Groupe oldPlayerGroupe;
    private CouplePlayer oldPlayerDeathTime;
    private Location oldPlayerLocation;

    public DisconnectedPlayer(UUID playerUUID, Equipe oldPlayerTeam, Groupe oldPlayerGroupe, CouplePlayer oldPlayerDeathTime, Location oldPlayerLocation) {
        this.playerUUID = playerUUID;
        this.oldPlayerTeam = oldPlayerTeam;
        this.oldPlayerGroupe = oldPlayerGroupe;
        this.oldPlayerDeathTime = oldPlayerDeathTime;
        this.oldPlayerLocation = oldPlayerLocation;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public Equipe getOldPlayerTeam() {
        return oldPlayerTeam;
    }

    public Groupe getOldPlayerGroupe() {
        return oldPlayerGroupe;
    }

    public CouplePlayer getOldPlayerDeathTime() {
        return oldPlayerDeathTime;
    }

    public Location getOldPlayerLocation() {
        return oldPlayerLocation;
    }

    public boolean wasPlayerDead() {
        return (oldPlayerDeathTime != null && oldPlayerDeathTime.getValeur() > 0);
    }
}
