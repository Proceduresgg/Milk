package us.rengo.milk.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Dependency;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.rengo.milk.MilkPlugin;

public class ListCommand extends BaseCommand {

    @Dependency private MilkPlugin plugin;

    @CommandAlias("list")
    public void onList(Player player) {
        StringBuilder builder = new StringBuilder();
        this.plugin.getRankManager().getRanks().values().forEach(rank -> builder.append(rank.getName()).append(" ,"));

        player.sendMessage(builder.toString());
    }
}
