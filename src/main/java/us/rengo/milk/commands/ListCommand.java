package us.rengo.milk.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Dependency;
import org.bukkit.entity.Player;
import us.rengo.milk.MilkPlugin;
import us.rengo.milk.rank.handler.RankManager;

public class ListCommand extends BaseCommand {

    @CommandAlias("list")
    public void onList(Player player) {
        StringBuilder builder = new StringBuilder();

        RankManager.INSTANCE.getRanks().values().forEach(rank -> builder.append(rank.getName()).append(" ,"));

        player.sendMessage(builder.toString());
    }
}
