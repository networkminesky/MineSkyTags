package net.minesky.mineskytags.events;

import net.minesky.mineskytags.entities.CustomTag;
import net.minesky.mineskytags.entities.TagHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvents implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final Player p = e.getPlayer();

        CustomTag tag = TagHandler.getEquippedCustomTag(p);
        if(tag != null && !p.hasPermission(tag.getPermission())) {
            TagHandler.clearEquippedTag(p);
        }
    }
}