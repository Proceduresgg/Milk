package us.rengo.milk.listeners;

import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.rengo.milk.MilkPlugin;
import us.rengo.milk.player.handler.ProfileManager;
import us.rengo.milk.player.PlayerProfile;
import us.rengo.milk.utils.MessageUtil;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class PlayerListener implements Listener {

    private final MilkPlugin plugin;

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        CompletableFuture<PlayerProfile> load = new PlayerProfile(event.getUniqueId()).load();
        try {
            ProfileManager.INSTANCE.getProfiles().put(event.getUniqueId(), load.get());
        } catch (Exception e) {
            e.printStackTrace();

            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Error Loading Data Try Again");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ProfileManager.INSTANCE.getProfile(event.getPlayer()).setupPermissionsAttachment();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ProfileManager.INSTANCE.getProfiles().remove(player.getUniqueId()).save();
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = ProfileManager.INSTANCE.getProfile(player);

        event.setFormat(MessageUtil.color(playerProfile.getRank().getPrefix() + "%1$s" + ChatColor.GRAY + ": " + ChatColor.WHITE + "%2$s"));
    }
}
