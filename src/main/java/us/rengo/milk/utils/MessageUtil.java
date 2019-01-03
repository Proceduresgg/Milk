package us.rengo.milk.utils;

import org.bukkit.ChatColor;

public class MessageUtil {

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}