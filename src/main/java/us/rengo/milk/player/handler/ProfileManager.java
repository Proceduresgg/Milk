package us.rengo.milk.player.handler;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.entity.Player;
import us.rengo.milk.MilkPlugin;
import us.rengo.milk.player.PlayerProfile;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public enum ProfileManager {

    INSTANCE;

    private final Map<UUID, PlayerProfile> profiles = new ConcurrentHashMap<>();

    public PlayerProfile getProfile(MilkPlugin plugin, UUID uuid) {
        return this.profiles.computeIfAbsent(uuid, k -> new PlayerProfile(plugin, uuid));
    }

    public PlayerProfile getProfile(MilkPlugin plugin, Player player) {
        return this.getProfile(plugin, player.getUniqueId());
    }

    public void saveProfiles() {
        this.profiles.values().forEach(PlayerProfile::save);
    }

    public MongoCollection<Document> getCollection(MilkPlugin plugin) {
        return plugin.getMongoDatabase().getCollection("player-ranks");
    }
}
