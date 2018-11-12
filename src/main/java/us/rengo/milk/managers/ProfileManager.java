package us.rengo.milk.managers;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.entity.Player;
import us.rengo.milk.MilkPlugin;
import us.rengo.milk.player.Profile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class ProfileManager {

    private Map<UUID, Profile> playerData = new HashMap<>();
    private MongoCollection<Document> collection = MilkPlugin.getInstance().getMongoDatabase().getCollection("ranks");

    public Profile getProfile(UUID uuid) {
        return this.playerData.computeIfAbsent(uuid, k -> new Profile(uuid));
    }

    public Profile getProfile(Player player) {
        return this.getProfile(player.getUniqueId());
    }
}
