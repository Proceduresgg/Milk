package us.rengo.milk;

import co.aikar.commands.PaperCommandManager;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import us.rengo.milk.commands.RankCommand;
import us.rengo.milk.listeners.PlayerListener;
import us.rengo.milk.managers.ProfileManager;
import us.rengo.milk.managers.RankManager;

import java.util.Arrays;
import java.util.Collections;

@Getter
public class MilkPlugin extends JavaPlugin {

    @Getter private static MilkPlugin instance;

    public static final ChatColor serverColorBright = ChatColor.AQUA;
    public static final ChatColor serverColorDim = ChatColor.GRAY;

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    private ProfileManager profileManager;
    private RankManager rankManager;

    private PaperCommandManager commandManager;

    public void onEnable() {
        instance = this;

        MongoCredential credential = MongoCredential.createCredential("god", "admin", "FUCK YOU".toCharArray());
        this.mongoClient = new MongoClient(new ServerAddress("127.0.0.1", 27017), Arrays.asList(credential));
        this.mongoDatabase = this.mongoClient.getDatabase("admin");

        this.profileManager = new ProfileManager();
        this.rankManager = new RankManager();

        this.registerListeners();
        this.registerCommands();
    }

    public void onDisable() {
        this.rankManager.saveRanks();
        this.profileManager.saveProfiles();
    }

    private void registerCommands() {
        this.commandManager = new PaperCommandManager(this);

        Arrays.asList(new RankCommand())
                .forEach(command -> this.commandManager.registerCommand(command));
    }

    private void registerListeners() {
        Arrays.asList(new PlayerListener(this))
                .forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }
}
