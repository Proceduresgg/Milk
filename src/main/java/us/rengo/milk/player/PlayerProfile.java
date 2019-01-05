package us.rengo.milk.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import us.rengo.milk.MilkPlugin;
import us.rengo.milk.player.handler.ProfileManager;
import us.rengo.milk.rank.handler.RankManager;
import us.rengo.milk.rank.Rank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter @Setter
@RequiredArgsConstructor
public class PlayerProfile {

    private final UUID uuid;

    private final List<String> permissions = new ArrayList<>();
    
    private Rank rank = RankManager.INSTANCE.getRanks().get("default");

    public Player toPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public void setupPermissionsAttachment() {
        Player player = this.toPlayer();

        if (player != null) {
            for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
                if (attachmentInfo.getAttachment() == null) {
                    continue;
                }

                attachmentInfo.getAttachment().getPermissions()
                        .forEach((permission, value) -> attachmentInfo.getAttachment().unsetPermission(permission));
            }

            PermissionAttachment attachment = player.addAttachment(MilkPlugin.getInstance());

            this.getAllPermissions().forEach(perm -> attachment.setPermission(perm, true));

            player.recalculatePermissions();
        }
    }

    private List<String> getAllPermissions() {
        List<String> permissions = new ArrayList<>(this.permissions);
        permissions.addAll(this.rank.getAllPermissions());

        return permissions;
    }

    public CompletableFuture<PlayerProfile> load() {
        return CompletableFuture.supplyAsync(() -> {
            Document document = ProfileManager.INSTANCE.getCollection()
                    .find(Filters.eq("uuid", this.uuid.toString())).first();

            if (document != null) {
                String name = document.getString("rank");

                if (RankManager.INSTANCE.isRank(name)) {
                    this.rank = RankManager.INSTANCE.getRank(name);
                }
            }
            return PlayerProfile.this;
        });
    }

    public void save() {
        Document document = new Document("uuid", this.uuid.toString());

        if (this.rank != null) {
            document.append("rank", this.rank.getName());
        }

        JsonArray permissions = new JsonArray();

        this.permissions.forEach(perm -> permissions.add(new JsonPrimitive(perm)));

        document.put("permissions", permissions.toString());

        ProfileManager.INSTANCE.getCollection()
                .replaceOne(Filters.eq("uuid", this.uuid.toString()), document, new ReplaceOptions().upsert(true));
    }
}
