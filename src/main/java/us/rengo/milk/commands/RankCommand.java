package us.rengo.milk.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.rengo.milk.MilkPlugin;
import us.rengo.milk.player.PlayerProfile;
import us.rengo.milk.rank.Rank;

@CommandAlias("rank")
@CommandPermission("rengo.rank")
public class RankCommand extends BaseCommand {

    @Dependency
    private MilkPlugin plugin;

    @Default
    @CatchUnknown
    @CommandCompletion("@players")
    public void onDefault(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(MilkPlugin.serverColorBright + "Usage: /rank [player] [rank]");
            return;

        } else if (!this.plugin.getRankManager().getRanks().containsKey(args[1])) {
            sender.sendMessage(MilkPlugin.serverColorBright + "That rank does not exist.");
            return;

        } else if (Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage(MilkPlugin.serverColorBright + "The specified player does not exist.");
            return;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);
        PlayerProfile targetProfile = this.plugin.getProfileManager().getProfile(target);
        Rank rank = this.plugin.getRankManager().getRanks().get(args[1]);

        targetProfile.setRank(rank);

        sender.sendMessage(MilkPlugin.serverColorBright + "That player's rank has been updated to " + rank.getColor() + rank.getName() + MilkPlugin.serverColorBright + ".");
        target.sendMessage(MilkPlugin.serverColorBright + "Your rank has been updated to " + rank.getColor() + rank.getName() + MilkPlugin.serverColorBright + ".");
    }

    @Subcommand("create")
    public void onCreate(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(MilkPlugin.serverColorBright + "Usage: /rank create [name]");
            return;

        } else if (this.plugin.getRankManager().getRanks().containsKey(args[0])) {
            sender.sendMessage(MilkPlugin.serverColorBright + "That rank already exists.");
            return;
        }

        Rank rank = new Rank(args[0]);
        this.plugin.getRankManager().getRanks().put(args[0], rank);
        sender.sendMessage(MilkPlugin.serverColorBright + "The specified rank has been created.");
    }

    @Subcommand("delete")
    public void onDelete(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(MilkPlugin.serverColorBright + "Usage: /rank delete [name]");
            return;

        } else if (!this.plugin.getRankManager().getRanks().containsKey(args[0])) {
            sender.sendMessage(MilkPlugin.serverColorBright + "That rank does not exist.");
            return;
        }

        this.plugin.getRankManager().getRanks().remove(args[0]);
        sender.sendMessage(MilkPlugin.serverColorBright + "The specified rank has been deleted.");
    }
}
