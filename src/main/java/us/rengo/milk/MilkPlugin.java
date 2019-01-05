package us.rengo.milk;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import us.rengo.milk.commands.ListCommand;
import us.rengo.milk.commands.RankCommand;
import us.rengo.milk.listeners.PlayerListener;
import us.rengo.milk.player.handler.ProfileManager;
import us.rengo.milk.rank.handler.RankManager;
import us.rengo.milk.rank.Rank;

import java.util.Arrays;

@Getter
public class MilkPlugin extends JavaPlugin {

    @Getter private static MilkPlugin instance;

    public static final ChatColor SERVER_COLOR_BRIGHT = ChatColor.DARK_AQUA;
    public static final ChatColor SERVER_COLOR_DIM = ChatColor.GRAY;

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public void onEnable() {
        instance = this;

        // This mongo shit right here is ugly as fuck, I'll fix it later though.
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(50).build();
        MongoCredential credential = MongoCredential.createCredential("Shyon", "practice", "fuckk".toCharArray());

        this.mongoClient = new MongoClient(new ServerAddress("127.0.0.1", 27017), credential, options);
        this.mongoDatabase = this.mongoClient.getDatabase("milk");

        RankManager.INSTANCE.loadRanks();

        this.registerListeners();
        this.registerCommands(new PaperCommandManager(this));
    }

    public void onDisable() {
        RankManager.INSTANCE.saveRanks();
        ProfileManager.INSTANCE.saveProfiles();
    }

    private void registerListeners() {
        Arrays.asList(new PlayerListener(this)).forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }

    private void registerCommands(PaperCommandManager commandManager) {
        this.registerContexts(commandManager);

        Arrays.asList(new RankCommand(), new ListCommand()).forEach(commandManager::registerCommand);
    }

    private void registerContexts(PaperCommandManager commandManager) {
        commandManager.getCommandContexts().registerContext(Rank.class, c -> {
            String arg = c.popFirstArg().toLowerCase();

            if (!RankManager.INSTANCE.getRanks().containsKey(arg)) {
                c.getSender().sendMessage(SERVER_COLOR_BRIGHT + "The specified rank does not exist.");
                throw new InvalidCommandArgument(true);
            }

            return RankManager.INSTANCE.getRanks().get(arg);
        });
    }
}
