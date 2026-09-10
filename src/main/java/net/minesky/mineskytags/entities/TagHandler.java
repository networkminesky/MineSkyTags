package net.minesky.mineskytags.entities;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minesky.mineskytags.MineSkyTags;
import net.minesky.mineskytags.hook.MainframeHook;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TagHandler {

    public static final List<CustomTag> registeredTags = new ArrayList<>();
    public static final HashMap<UUID, CustomTag> CACHED_TAGS = new HashMap<>();

    public static Key getCustomFontKey() {
        return Key.key(MineSkyTags.config.getString("custom-font.key", "tags"));
    }

    public static void registerAll() {
        registeredTags.clear();

        final ConfigurationSection tagsSection = MineSkyTags.config.getConfigurationSection("tags");
        if(tagsSection == null)
            return;

        for(String idKey : tagsSection.getKeys(false)) {
            final ConfigurationSection section = tagsSection.getConfigurationSection(idKey);
            if(section == null) continue;

            registeredTags.add(CustomTag.serialize(idKey, section));
        }
    }

    public static void clearEquippedTag(final Player player) {
        CACHED_TAGS.remove(player.getUniqueId());
        MainframeHook.setDatabaseEquippedTag(player, "");
    }

    public static void setEquippedTag(final Player player, final CustomTag customTag) {
        MainframeHook.setDatabaseEquippedTag(player, customTag.id());
        CACHED_TAGS.put(player.getUniqueId(), customTag);
    }

    public static Optional<CustomTag> getById(final String id) {
        return registeredTags.stream().filter(tag -> tag.id().equalsIgnoreCase(id)).findAny();
    }

    public static Component getTag(final Player player) {
        CustomTag tag = getEquippedCustomTag(player);
        if(tag == null)
            return Component.empty();
        else
            return tag.buildComponent();
    }

    public static @Nullable CustomTag getEquippedCustomTag(final Player player) {
        return CACHED_TAGS.getOrDefault(player.getUniqueId(), null);
    }

    public static void refreshFromDatabase(Player player) {
        MainframeHook.getDatabaseCurrentTag(player, tag -> {
            getById(tag).ifPresent(customTag -> {
                if(!player.hasPermission(customTag.getPermission())) {
                    clearEquippedTag(player);
                } else {
                    CACHED_TAGS.put(player.getUniqueId(), customTag);
                }
            });
        });
    }
}
