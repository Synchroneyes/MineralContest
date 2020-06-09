package fr.mineral.Utils;

import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.mineralcontest;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class AutomaticSetup {
    public static Location positionSpawnArene;
    public static Location spawnRouge;
    public static Location spawnBleu;
    public static Location spawnJaune;
    public static Location positionDeathZone;


    public static void setPositionSpawnArene(Location l) {
    /*    try {
            if(positionSpawnArene == null) {
                AutomaticSetup.positionSpawnArene = l;
                double x,y,z;
                x = l.getX();
                y = l.getY();
                z = l.getZ();

                Location spawn = l;
                // JAUNE
                spawn.setX(x + 46);
                spawn.setY(y + 10);
                spawn.setZ(z + 0);
                mineralcontest.broadcastMessage(toString(spawn));
                mineralcontest.broadcastMessage(toString(l));

                mineralcontest.broadcastMessage("=============");



                // BLEU
                spawn.setX(x+ 0);
                spawn.setY(y + 10);
                spawn.setZ(z + 46);
                AutomaticSetup.spawnBleu = spawn;
                mineralcontest.broadcastMessage(toString(spawn));
                mineralcontest.broadcastMessage(toString(l));

                mineralcontest.broadcastMessage("=============");


                // ROUGE
                spawn.setX(x+ 0);
                spawn.setY(y + 10);
                spawn.setZ(z - 46);
                AutomaticSetup.spawnRouge = spawn;
                mineralcontest.broadcastMessage(toString(spawn));
                mineralcontest.broadcastMessage(toString(l));

                mineralcontest.broadcastMessage("=============");

            }
        }catch(Exception e) {
            mineralcontest.plugin.getServer().getLogger().info(e.getCause().toString());
            Error.Report(e, null);
        }

    }

    public static Location getPositionSpawnArene() { return  AutomaticSetup.positionSpawnArene; }

    public static void teleportToJaune(Player joueur) {
        if(getPositionSpawnArene() != null) {

            PlayerUtils.teleportPlayer(joueur, spawnJaune);
            joueur.sendMessage("Vous avez été téléporter chez les jaunes");
        } else {
            joueur.sendMessage("Position arene non définit");
        }
    }

    public static void teleportToBleu(Player joueur) {
        if(getPositionSpawnArene() != null) {
            PlayerUtils.teleportPlayer(joueur, spawnBleu);
            joueur.sendMessage("Vous avez été téléporter chez les bleu");
        } else {
            joueur.sendMessage("Position arene non définit");
        }
    }

    public static void teleportToRouge(Player joueur) {
        if(getPositionSpawnArene() != null) {
            PlayerUtils.teleportPlayer(joueur, spawnRouge);
            joueur.sendMessage("Vous avez été téléporter chez les rouge");
        } else {
            joueur.sendMessage("Position arene non définit");
        }
    }

    public static String toString(Location x) {
        return new String(x.getX() + " " + x.getY() + " " + x.getZ());
    */
    }
}
