package fr.synchroneyes.special_events.halloween2024.utils;

import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HalloweenTitle {

    /**
     * Send a title to a player
     * @param player Player to send the title to
     * @param title Title of the title
     * @param content Text of the message
     * @param fadeInDuration Duration in seconds of the fade in
     * @param stayDuration Duration in seconds of the message staying on screen
     * @param fadeOutDuration Duration in seconds of the fade out
     */
    public static void sendTitle(Player player, String title, String content, int fadeInDuration, int stayDuration, int fadeOutDuration) {
        player.sendTitle(format(title), content, fadeInDuration*20, stayDuration*20, fadeOutDuration*20);
    }

    /**
     * Send a title to a player
     * @param player Player to send the title to
     * @param title Title of the title
     * @param content Text of the message
     * @param fadeInDuration Duration in seconds of the fade in
     * @param stayDuration Duration in seconds of the message staying on screen
     * @param fadeOutDuration Duration in seconds of the fade out
     */
    public static void sendTitle(Player player, String title, String content, int fadeInDuration, int stayDuration, int fadeOutDuration, boolean sendTextMessage) {
        player.sendTitle(format(title), content, fadeInDuration*20, stayDuration*20, fadeOutDuration*20);
        if(sendTextMessage) player.sendMessage(mineralcontest.prefixPrive + "[" + format(title) + ChatColor.RESET + "] " + content);
    }


    /**
     * Format a message to have a orange beginning text and a black end text
     * @param message Message to format
     * @return Formatted message
     */
    private static String format(String message) {
        int messageLength = message.length();
        StringBuilder newMessageBuilder = new StringBuilder();

        newMessageBuilder.append(ChatColor.GOLD + "");

        int middleString = (int)(messageLength / 2);
        for(int i = 0; i < messageLength; i++) {
            newMessageBuilder.append(message.charAt(i));
            if( middleString == i) {
                newMessageBuilder.append(ChatColor.BLUE + "");
            }
        }

        return newMessageBuilder.toString();
    }
}
