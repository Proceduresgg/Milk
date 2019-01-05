package us.rengo.milk.rank.handler;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import lombok.Getter;
import org.bson.Document;
import us.rengo.milk.MilkPlugin;
import us.rengo.milk.rank.Rank;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum RankManager {

    INSTANCE;

    private final Map<String, Rank> ranks = new HashMap<>();

    public void loadRanks(MilkPlugin plugin) {
        try (MongoCursor<Document> cursor = this.getCollection(plugin).find().iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();

                if (document.getString("name").contains(" ")) {
                    this.getCollection(plugin).deleteOne(document);
                    continue;
                }

                Rank rank = new Rank(plugin, document.getString("name").toLowerCase());
                rank.load(document);

                this.ranks.put(rank.getName(), rank);
            }
        }
    }

    public boolean isRank(String name) {
        return this.ranks.containsKey(name);
    }

    public Rank getRank(String rank) {
        return this.ranks.get(rank);
    }

    public void saveRanks() {
        this.ranks.values().forEach(Rank::save);
    }

    public MongoCollection<Document> getCollection(MilkPlugin plugin) {
        return plugin.getMongoDatabase().getCollection("ranks");
    }
}
