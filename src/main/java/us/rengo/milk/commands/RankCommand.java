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

    @Syntax("<target> <rank>")
    @CommandCompletion("@players")
    public void onDefault(CommandSender sender, PlayerProfile targetProfile, Rank rank) {
        targetProfile.setRank(rank);

        Bukkit.getPlayer(targetProfile.getUuid()).sendMessage(MilkPlugin.serverColorBright + "Your rank has been updated to " + rank.getColor() + rank.getName() + MilkPlugin.serverColorBright + ".");

        sender.sendMessage(MilkPlugin.serverColorBright + "That player's rank has been updated to " + rank.getColor() + rank.getName() + MilkPlugin.serverColorBright + ".");
    }

    @Syntax("<name>")
    @Subcommand("create")
    public void onCreate(CommandSender sender, String name) {
        if (this.plugin.getRankManager().getRanks().containsKey(name)) {
            sender.sendMessage(MilkPlugin.serverColorBright + "That rank already exists.");
            return;
        }

        Rank rank = new Rank(name);
        this.plugin.getRankManager().getRanks().put(name, rank);
        sender.sendMessage(MilkPlugin.serverColorBright + "The specified rank has been created.");
    }

    @Syntax("<rank>")
    @Subcommand("delete")
    public void onDelete(CommandSender sender, Rank rank) {
        this.plugin.getRankManager().getRanks().remove(rank.getName());
        sender.sendMessage(MilkPlugin.serverColorBright + "The specified rank has been deleted.");
    }

    @Syntax("<prefix> <rank>")
    @CommandAlias("setprefix")
    public void onSetPrefix(CommandSender sender, String prefix, Rank rank) {
        rank.setPrefix(prefix);
        sender.sendMessage(MilkPlugin.serverColorBright + "The prefix for that rank has been updated.");
    }
}
