package net.minesky.mineskytags.commands;

import net.minesky.mineskytags.gui.TagsGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandHandler implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player p)) {
            sender.sendMessage("Você deve ser um jogador.");
            return true;
        }

        if(command.getName().equalsIgnoreCase("tags")) {
            TagsGUI.openInventory(p);
        }

        return false;
    }
}
