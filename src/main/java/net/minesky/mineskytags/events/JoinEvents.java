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
        TagHandler.refreshFromDatabase(e.getPlayer());
    }

}