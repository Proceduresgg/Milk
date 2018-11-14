package us.rengo.milk.managers;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.entity.Player;
import us.rengo.milk.MilkPlugin;
import us.rengo.milk.player.PlayerProfile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class ProfileManager {

    private Map<UUID, PlayerProfile> profiles = new HashMap<>();
    private MongoCollection<Document> collection = MilkPlugin.getInstance().getMongoDatabase().getCollection("ranks");

    public PlayerProfile getProfile(UUID uuid) {
        return this.profiles.computeIfAbsent(uuid, k -> new PlayerProfile(uuid));
    }

    public PlayerProfile getProfile(Player player) {
        return this.getProfile(player.getUniqueId());
    }

    public void saveProfiles() {
        for (PlayerProfile profile : this.profiles.values()) {
            profile.save();
        }
    }
}
