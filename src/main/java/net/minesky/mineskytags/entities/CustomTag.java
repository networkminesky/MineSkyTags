package net.minesky.mineskytags.entities;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minesky.mineskytags.MineSkyTags;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public record CustomTag(String id, String name, String partPermission,
                        List<String> lore, String tagType, String tagChar,
                        String icon, ConfigurationSection section) {

    public static CustomTag serialize(String id, ConfigurationSection section) {
        return new CustomTag(
                id,
                section.getString("name", id),
                section.getString("permission", id),
                section.getStringList("lore"),
                section.getString("tag.type", "VANILLA_FONT"), section.getString("tag.char", "z"),
                section.getString("icon", "NAME_TAG"),
                section
        );
    }

    public String getPermission() {
        final String permissionPrefix = MineSkyTags.config.getString("permission-prefix", "mineskytags.tag.");
        return permissionPrefix+partPermission();
    }

    public Component buildComponent() {
        if(tagType.equalsIgnoreCase("CUSTOM_FONT")) {
            return Component.text(tagChar).font(TagHandler.getCustomFontKey()).color(NamedTextColor.WHITE);
        } else
            return Component.text(tagChar).color(NamedTextColor.WHITE);
    }
}
