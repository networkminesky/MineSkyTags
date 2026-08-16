package net.minesky.mineskytags;

import net.minesky.mineskytags.commands.CommandHandler;
import net.minesky.mineskytags.entities.TagHandler;
import net.minesky.mineskytags.gui.TagsGUI;
import net.minesky.mineskytags.hook.PAPIHook;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class MineSkyTags extends JavaPlugin {

    public static FileConfiguration config;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        config = this.getConfig();

        getLogger().info("Plugin carregado! Registrando tags...");
        TagHandler.registerAll();

        getLogger().info("Tags registradas: "+TagHandler.registeredTags.size());

        this.getCommand("tags").setExecutor(new CommandHandler());
        this.getCommand("mineskytags").setExecutor(new CommandHandler());

        if(getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PAPIHook().register();
        }

        for(Player player : Bukkit.getOnlinePlayers()) {
            TagHandler.refreshFromDatabase(player);
        }

        this.getServer().getPluginManager().registerEvents(new TagsGUI(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
