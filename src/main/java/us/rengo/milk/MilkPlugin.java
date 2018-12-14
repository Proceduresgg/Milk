package us.rengo.milk;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import us.rengo.milk.commands.RankCommand;
import us.rengo.milk.listeners.PlayerListener;
import us.rengo.milk.managers.ProfileManager;
import us.rengo.milk.managers.RankManager;
import us.rengo.milk.player.PlayerProfile;
import us.rengo.milk.rank.Rank;

import java.util.Arrays;

@Getter
public class MilkPlugin extends JavaPlugin {

    @Getter private static MilkPlugin instance;

    public static final ChatColor SERVER_COLOR_BRIGHT = ChatColor.AQUA;
    public static final ChatColor SERVER_COLOR_DIM = ChatColor.GRAY;

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    private ProfileManager profileManager;
    private RankManager rankManager;

    public void onEnable() {
        instance = this;

        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(50).build();
        MongoCredential credential = MongoCredential.createCredential("Shyon", "practice", "fuckk".toCharArray());
        this.mongoClient = new MongoClient(new ServerAddress("127.0.0.1", 27017), credential, options);
        this.mongoDatabase = this.mongoClient.getDatabase("milk");

        this.profileManager = new ProfileManager();
        this.rankManager = new RankManager();

        this.registerListeners();
        this.registerCommands(new PaperCommandManager(this));
    }

    public void onDisable() {
        this.rankManager.saveRanks();
        this.profileManager.saveProfiles();
    }

    private void registerListeners() {
        Arrays.asList(new PlayerListener(this)).forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }

    private void registerCommands(PaperCommandManager commandManager) {
        this.registerContexts(commandManager);

        Arrays.asList(new RankCommand()).forEach(commandManager::registerCommand);
    }

    private void registerContexts(PaperCommandManager commandManager) {
        commandManager.getCommandContexts().registerContext(Rank.class, c -> {
            String arg = c.popFirstArg();

            if (!rankManager.getRanks().containsKey(arg.toLowerCase())) {
                c.getSender().sendMessage(SERVER_COLOR_BRIGHT + "The specified rank does not exist.");
                throw new InvalidCommandArgument(true);
            }

            return rankManager.getRanks().get(arg);
        });

        commandManager.getCommandContexts().registerContext(PlayerProfile.class, c -> {
            String arg = c.popFirstArg();

            if (Bukkit.getPlayer(arg) == null) {
                c.getSender().sendMessage(SERVER_COLOR_BRIGHT + "The specified player is not online.");
                throw new InvalidCommandArgument(true);
            }

            return profileManager.getProfile(Bukkit.getPlayer(arg));
        });
    }
}
