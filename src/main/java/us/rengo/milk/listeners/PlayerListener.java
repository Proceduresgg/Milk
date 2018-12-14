package us.rengo.milk.listeners;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.rengo.milk.MilkPlugin;
import us.rengo.milk.player.PlayerProfile;
import us.rengo.milk.utils.MessageUtil;

import java.util.stream.Collectors;

@AllArgsConstructor
public class PlayerListener implements Listener {

    private final MilkPlugin plugin;

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (Bukkit.getOnlinePlayers().stream().map(Entity::getUniqueId).collect(Collectors.toList()).contains(event.getUniqueId())) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(ChatColor.RED + "Your player data did not load properly, please relog. \nIf this continues to happen, please join ts.rengo.us.");

            Bukkit.getPlayer(event.getUniqueId()).kickPlayer("wtf? idiot");
        }

        PlayerProfile playerProfile = new PlayerProfile(event.getUniqueId());

        try {
            playerProfile.load();
        } catch (Exception e) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(ChatColor.RED + "Your player data did not load properly, please relog. \nIf this continues to happen, please join ts.rengo.us.");
        }

        this.plugin.getProfileManager().getProfiles().put(event.getUniqueId(), playerProfile);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = this.plugin.getProfileManager().getProfile(player);

        if (playerProfile == null) {
            player.kickPlayer(ChatColor.RED + "Your player data did not load properly, please relog. \nIf this continues to happen, please join ts.rengo.us.");
            return;
        }

        playerProfile.setupPermissionsAttachment();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = this.plugin.getProfileManager().getProfiles().remove(player.getUniqueId());

        playerProfile.save();
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = this.plugin.getProfileManager().getProfile(player);

        event.setFormat(MessageUtil.color( playerProfile.getRank().getPrefix() + "%1$s" + ChatColor.GRAY + ": " + ChatColor.WHITE + "%2$s"));
    }
}