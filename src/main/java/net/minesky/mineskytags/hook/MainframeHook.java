package net.minesky.mineskytags.hook;

import net.minesky.api.database.PlayerDatabase;
import net.minesky.api.database.UpdatedData;
import net.minesky.api.database.ValueType;
import net.minesky.core.databridge.callbacks.ErrorType;
import net.minesky.core.databridge.callbacks.FindValueCallback;
import net.minesky.core.databridge.callbacks.SetOneCallback;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MainframeHook {

    public interface TagCallback {
        public void onDone(@Nullable String tag);
    }

    public static void setDatabaseEquippedTag(Player player, String tag) {
        PlayerDatabase.setPlayerData(player.getUniqueId().toString(), new UpdatedData("equipped-tag", tag),
                new SetOneCallback() {
            @Override public void onSetDone() {}
            @Override public void onSetError(ErrorType errorType) {}
        });
    }

    public static void getDatabaseCurrentTag(Player player, TagCallback callback) {
        PlayerDatabase.getPlayerSpecificDataAsync(player.getUniqueId().toString(), ValueType.STRING, "equipped-tag",
                new FindValueCallback() {
                    @Override
                    public void onQueryDone(Document document, Object o, boolean b) {
                        callback.onDone((String)o);
                    }
                    @Override
                    public void onQueryError(ErrorType errorType) {
                        callback.onDone(null);
                    }
                });
    }

}
