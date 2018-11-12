package us.rengo.milk.listeners;

import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import us.rengo.milk.MilkPlugin;
import us.rengo.milk.player.Profile;

@AllArgsConstructor
public class PlayerListener implements Listener {

    private final MilkPlugin plugin;

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        Profile profile = new Profile(event.getUniqueId());

        try {
            profile.load();
        } catch (Exception e) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(ChatColor.RED + "Your player data did not load properly, please relog. \nIf this continues to happen, please join ts.rengo.us.");
        }

        this.plugin.getProfileManager().getPlayerData().put(event.getUniqueId(), profile);

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.plugin.getProfileManager().getProfile(player);

        profile.setupPermissionsAttachment();
    }


}