package us.rengo.milk.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.contexts.OnlinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.rengo.milk.MilkPlugin;
import us.rengo.milk.player.PlayerProfile;
import us.rengo.milk.player.handler.ProfileManager;
import us.rengo.milk.rank.Rank;
import us.rengo.milk.rank.handler.RankManager;

@CommandPermission("rengo.rank")
public class RankCommand extends BaseCommand {

    @CommandAlias("rank")
    @Syntax("<target> <rank>")
    @CommandCompletion("@players")
    public void onRank(CommandSender sender, OfflinePlayer player, Rank rank) {
        new PlayerProfile(player.getUniqueId()).load().whenComplete((profile, throwable) -> {
            if (throwable != null) {
                sender.sendMessage(ChatColor.RED + "No such player exists.");
            } else {
                profile.setRank(rank);
                profile.save();

                Bukkit.getPlayer(profile.getUuid()).sendMessage(MilkPlugin.SERVER_COLOR_BRIGHT + "Your rank has been updated to " + rank.getColor() + rank.getName() + MilkPlugin.SERVER_COLOR_BRIGHT + ".");

                sender.sendMessage(MilkPlugin.SERVER_COLOR_BRIGHT + "That player's rank has been updated to " + rank.getColor() + rank.getName() + MilkPlugin.SERVER_COLOR_BRIGHT + ".");
            }
        });
    }

    @CommandAlias("rank")
    @Syntax("<target> <rank>")
    @CommandCompletion("@players")
    public void onRank(CommandSender sender, OnlinePlayer onlinePlayer, Rank rank) {
        Player player = onlinePlayer.getPlayer();
        PlayerProfile profile = ProfileManager.INSTANCE.getProfile(player.getUniqueId());

        profile.setRank(rank);
        profile.save();

        Bukkit.getPlayer(profile.getUuid()).sendMessage(MilkPlugin.SERVER_COLOR_BRIGHT + "Your rank has been updated to " + rank.getColor() + rank.getName() + MilkPlugin.SERVER_COLOR_BRIGHT + ".");

        sender.sendMessage(MilkPlugin.SERVER_COLOR_BRIGHT + "That player's rank has been updated to " + rank.getColor() + rank.getName() + MilkPlugin.SERVER_COLOR_BRIGHT + ".");
    }

    @CommandAlias("rank")
    @Syntax("<name>")
    @Subcommand("create")
    public void onCreate(CommandSender sender, String name) {
        if (RankManager.INSTANCE.getRanks().containsKey(name)) {
            sender.sendMessage(MilkPlugin.SERVER_COLOR_BRIGHT + "That rank already exists.");
            return;
        }
        sender.sendMessage(MilkPlugin.SERVER_COLOR_BRIGHT + "The specified rank has been created.");

        Rank rank = new Rank(name);
        RankManager.INSTANCE.getRanks().put(name.toLowerCase(), rank);
    }

    @CommandAlias("rank")
    @Syntax("<rank>")
    @Subcommand("delete")
    public void onDelete(CommandSender sender, Rank rank) {
        sender.sendMessage(MilkPlugin.SERVER_COLOR_BRIGHT + "The specified rank has been deleted.");

        RankManager.INSTANCE.getRanks().remove(rank.getName());
    }

    @CommandAlias("rank")
    @Syntax("<prefix> <rank>")
    @Subcommand("setprefix")
    public void onSetPrefix(CommandSender sender, Rank rank, String prefix) {
        sender.sendMessage(MilkPlugin.SERVER_COLOR_BRIGHT + "The prefix for that rank has been updated.");

        rank.setPrefix(prefix);
        rank.save();
    }

    @CommandAlias("rank")
    @Syntax("<rank> <permission>")
    @Subcommand("addpermission")
    public void onAddPermission(Player player, Rank rank, String permission) {
        player.sendMessage(MilkPlugin.SERVER_COLOR_BRIGHT + "Permission has been added to the rank.");

        rank.getPermissions().add(permission);
    }
}