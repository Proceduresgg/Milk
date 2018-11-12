package us.rengo.milk.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import us.rengo.milk.MilkPlugin;

@CommandAlias("rank")
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


    }
}
