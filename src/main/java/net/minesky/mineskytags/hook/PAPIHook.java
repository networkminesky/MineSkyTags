package net.minesky.mineskytags.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minesky.mineskytags.entities.CustomTag;
import net.minesky.mineskytags.entities.TagHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PAPIHook extends PlaceholderExpansion {

    @Override
    @NotNull
    public String getAuthor() {
        return "Drawn";
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "mineskytags"; //
    }

    @Override
    @NotNull
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        Player online = Bukkit.getPlayer(player.getUniqueId());
        return online == null ? "" : onPlaceholderRequest(online, params);
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        return List.of("serialized", "char");
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        final CustomTag tag = TagHandler.getEquippedCustomTag(player);

        if(tag == null)
            return "";

        if(params.equals("serialized")) {
            return MiniMessage.miniMessage().serialize(tag.buildComponent().append(Component.text(" ").font(Style.DEFAULT_FONT)));
        }

        if(params.equals("char")) {
            return tag.tagChar();
        }

        return "";
    }
}
