package us.rengo.milk.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import org.bukkit.entity.Player;
import us.rengo.milk.rank.Rank;

public class ListCommand extends BaseCommand {

    @CommandAlias("list")
    public void onList(Player player) {
        StringBuilder builder = new StringBuilder();

        Rank.getRanks().values().forEach(rank -> builder.append(rank.getName()).append(" ,"));

        player.sendMessage(builder.toString());
    }
}
