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
        CompletableFuture<PlayerProfile> load = new PlayerProfile(this.plugin, event.getUniqueId()).load();
        try {
            ProfileManager.INSTANCE.getProfiles().put(event.getUniqueId(), load.get());
        } catch (Exception e) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                    ChatColor.RED + "Failed to load your data, please try to rejoin.");

            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ProfileManager.INSTANCE.getProfile(this.plugin, event.getPlayer()).setupPermissionsAttachment();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ProfileManager.INSTANCE.getProfiles().remove(event.getPlayer().getUniqueId()).save();
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        PlayerProfile playerProfile = ProfileManager.INSTANCE.getProfile(this.plugin, event.getPlayer());

        event.setFormat(MessageUtil.color(playerProfile.getRank().getPrefix() + "%1$s" + ChatColor.GRAY + ": " + ChatColor.WHITE + "%2$s"));
    }
}
