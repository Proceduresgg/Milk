package us.rengo.milk.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import us.rengo.milk.MilkPlugin;
import us.rengo.milk.rank.Rank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PlayerProfile {

    private final UUID uuid;

    private List<String> permissions = new ArrayList<>();

    private Rank rank;

    public PlayerProfile(UUID uuid) {
        this.uuid = uuid;
    }

    public Player toPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void setupPermissionsAttachment() {
        Player player = this.toPlayer();

        if (player != null) {
            for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
                if (attachmentInfo.getAttachment() == null) {
                    continue;
                }

                attachmentInfo.getAttachment().getPermissions().forEach((permission, value) -> {
                    attachmentInfo.getAttachment().unsetPermission(permission);
                });
            }

            PermissionAttachment attachment = player.addAttachment(MilkPlugin.getInstance());

            this.getAllPermissions().forEach(permission -> attachment.setPermission(permission, true));

            player.recalculatePermissions();
        }
    }

    private List<String> getAllPermissions() {
        List<String> permissions = new ArrayList<>(this.permissions);
        permissions.addAll(this.rank.getAllPermissions());
        return permissions;
    }

    public void load() throws Exception {
        Document document = MilkPlugin.getInstance().getProfileManager().getCollection().find(Filters.eq("uuid", this.uuid.toString())).first();

        if (document != null) {
            List<String> permissions = new ArrayList<>();

            for (JsonElement element : new JsonParser().parse(document.getString("permissions")).getAsJsonArray()) {
                permissions.add(element.getAsString());
            }

            this.permissions = permissions;

            for (Rank rank : MilkPlugin.getInstance().getRankManager().getRanks().values()) {
                if (rank.getName().equals(document.getString("rank"))) {
                    this.rank = rank;
                    break;
                }
            }
        }
    }

    public void save() {
        Document document = new Document();

        document.append("rank", this.rank.getName());

        JsonArray permissions = new JsonArray();
        this.permissions.forEach(perm -> permissions.add(new JsonPrimitive(perm)));

        document.put("permissions", permissions.toString());

        MilkPlugin.getInstance().getProfileManager().getCollection().replaceOne(Filters.eq("uuid", this.uuid.toString()), document, new ReplaceOptions().upsert(true));
    }
}
