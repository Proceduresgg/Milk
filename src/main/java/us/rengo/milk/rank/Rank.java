package us.rengo.milk.rank;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.ChatColor;
import us.rengo.milk.MilkPlugin;
import us.rengo.milk.utils.MessageUtil;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Rank {

    private List<String> permissions = new ArrayList<>();

    private String name;

    private String prefix = "";
    private String suffix = "";

    private int hierarchy;

    private ChatColor color = ChatColor.WHITE;

    public Rank(String name) {
        this.name = name.toLowerCase();
    }

    public List<String> getAllPermissions() {
        permissions.forEach(perm -> System.out.println("SPEC PERMS + " + perm));

        List<String> permissions = new ArrayList<>(this.permissions);

        permissions.forEach(perm -> System.out.println("GETTING ALL PERMS + " + perm));

//        MilkPlugin.getInstance().getRankManager().getRanks().values().stream()
//                .filter(rank -> rank.getHierarchy() < this.hierarchy)
//                .forEach(rank -> permissions.addAll(rank.getPermissions()));

        return permissions;
    }

    public void load(Document document) {
        this.prefix = ChatColor.translateAlternateColorCodes('&', document.getString("prefix"));
        this.suffix = ChatColor.translateAlternateColorCodes('&', document.getString("suffix"));
        this.hierarchy = document.getInteger("hierarchy");
        this.color = ChatColor.valueOf(document.getString("color"));

        List<String> permissions = new ArrayList<>();

        for (JsonElement element : new JsonParser().parse(document.getString("permissions")).getAsJsonArray()) {
            permissions.add(element.getAsString());
        }

        this.permissions = permissions;

        this.permissions.forEach(perm -> System.out.println("LOADING PERM: " + perm));
        this.getAllPermissions().forEach(perm -> System.out.println("ALL PERM: " + perm));
    }

    public void save() {
        Document document = new Document();

        document.put("name", this.name);
        document.put("hierarchy", this.hierarchy);
        document.put("color", this.color.name());
        document.put("prefix", this.prefix.replace(ChatColor.COLOR_CHAR + "", "&"));
        document.put("suffix", this.suffix.replace(ChatColor.COLOR_CHAR + "", "&"));

        JsonArray permissions = new JsonArray();
        this.permissions.forEach(perm -> permissions.add(new JsonPrimitive(perm)));

        document.put("permissions", permissions.toString());

        MilkPlugin.getInstance().getRankManager().getCollection().replaceOne(Filters.eq("name", this.name), document, new ReplaceOptions().upsert(true));
    }

    public String getName() {
        return MessageUtil.color(this.name);
    }
}
