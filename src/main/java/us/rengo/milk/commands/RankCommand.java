package us.rengo.milk.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
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
    @Syntax("<target> <rank>")
    @CommandCompletion("@players")
    public void onDefault(CommandSender sender, PlayerProfile targetProfile, Rank rank) {
        targetProfile.setRank(rank);

        Bukkit.getPlayer(targetProfile.getUuid()).sendMessage(MilkPlugin.SERVER_COLOR_BRIGHT + "Your rank has been updated to " + rank.getColor() + rank.getName() + MilkPlugin.SERVER_COLOR_BRIGHT + ".");

        sender.sendMessage(MilkPlugin.SERVER_COLOR_BRIGHT + "That player's rank has been updated to " + rank.getColor() + rank.getName() + MilkPlugin.SERVER_COLOR_BRIGHT + ".");
    }

    @Syntax("<name>")
    @Subcommand("create")
    public void onCreate(CommandSender sender, String name) {
        if (this.plugin.getRankManager().getRanks().containsKey(name.toLowerCase())) {
            sender.sendMessage(MilkPlugin.SERVER_COLOR_BRIGHT + "That rank already exists.");
            return;
        }

        Rank rank = new Rank(name);
        this.plugin.getRankManager().getRanks().put(name.toLowerCase(), rank);
        sender.sendMessage(MilkPlugin.SERVER_COLOR_BRIGHT + "The specified rank has been created.");
    }

    @Syntax("<rank>")
    @Subcommand("delete")
    public void onDelete(CommandSender sender, Rank rank) {
        this.plugin.getRankManager().getRanks().remove(rank.getName());
        sender.sendMessage(MilkPlugin.SERVER_COLOR_BRIGHT + "The specified rank has been deleted.");
    }

    @Syntax("<prefix> <rank>")
    @CommandAlias("setprefix")
    public void onSetPrefix(CommandSender sender, Rank rank, String prefix) {
        rank.setPrefix(prefix);
        sender.sendMessage(MilkPlugin.SERVER_COLOR_BRIGHT + "The prefix for that rank has been updated.");
    }
}