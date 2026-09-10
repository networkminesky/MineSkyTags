package net.minesky.mineskytags.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minesky.mineskytags.MineSkyTags;
import net.minesky.mineskytags.entities.CustomTag;
import net.minesky.mineskytags.entities.TagHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class TagsGUI implements Listener {
    public static List<Inventory> inventories = new ArrayList<>();

    private static int CLEAR_BUTTON_SLOT = 49;

    private static final NamespacedKey TAG = new NamespacedKey("mineskytags", "tag");

    private static ItemStack buildFromTag(Player player, CustomTag tag) {
        Material icon = Material.matchMaterial(tag.icon());
        if(icon == null) {
            icon = Material.NAME_TAG;
        }

        ItemStack itemStack = new ItemStack(icon);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setTooltipStyle(NamespacedKey.fromString("raro"));
        itemMeta.itemName(LegacyComponentSerializer.legacyAmpersand().deserialize(tag.name()).color(NamedTextColor.AQUA));
        itemMeta.getPersistentDataContainer().set(TAG, PersistentDataType.STRING, tag.id());
        itemMeta.lore(buildLore(tag, player));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private static ItemStack clearTagButton() {
        ItemStack itemStack = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(8);
        itemMeta.setTooltipStyle(NamespacedKey.fromString("vermelho"));
        itemMeta.itemName(Component.text("Remover tag atual").color(NamedTextColor.RED));
        itemMeta.lore();
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private static List<Component> buildLore(CustomTag tag, Player player) {
        List<Component> components = new ArrayList<>();
        components.add(Component.text("Pré-visualização: ").color(NamedTextColor.GRAY).append(
                tag.buildComponent()).decoration(TextDecoration.ITALIC, false));

        LegacyComponentSerializer legacy = LegacyComponentSerializer.legacyAmpersand();

        if(!tag.lore().isEmpty()) {
            components.add(Component.empty());
            for(String line : tag.lore()) {
                components.add(legacy.deserialize(line).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
            }
        }

        components.add(Component.empty());

        components.add(accessComponent(player, tag).decoration(TextDecoration.ITALIC, false));

        return components;
    }

    private static Component accessComponent(Player player, CustomTag tag) {
        return player.hasPermission(tag.getPermission())
                ?
                (TagHandler.getEquippedCustomTag(player) == tag
                        ?
                        Component.text("Tag já equipada!").color(NamedTextColor.GREEN)
                        :
                        Component.text("Clique para equipar.").color(NamedTextColor.YELLOW)
                )
                :
                Component.text("Você não tem acesso.").color(NamedTextColor.RED);
    }

    public static void openInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, "Tags");

        int spot = 0;
        for(CustomTag tag : TagHandler.registeredTags) {
            inv.setItem(spot , buildFromTag(player, tag));
            spot++;
        }

        inv.setItem(CLEAR_BUTTON_SLOT, clearTagButton());

        inventories.add(inv);

        player.openInventory(inv);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        inventories.remove(e.getInventory());
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if(inventories.contains(e.getInventory()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(!inventories.contains(e.getInventory()))
            return;

        e.setCancelled(true);

        final ItemStack stack = e.getCurrentItem();
        if(stack == null || stack.getItemMeta() == null)
            return;

        Player player = (Player)e.getWhoClicked();

        if(e.getSlot() == CLEAR_BUTTON_SLOT) {
            CustomTag tag = TagHandler.getEquippedCustomTag(player);
            if(tag == null) {
                player.sendMessage(Component.text("Você não tem nenhuma tag equipada atualmente.").color(NamedTextColor.RED));
            } else {
                TagHandler.clearEquippedTag(player);
                player.sendMessage(Component.text("Sua tag foi removida com sucesso.").color(NamedTextColor.GREEN));
            }
            return;
        }

        String id = stack.getPersistentDataContainer().getOrDefault(TAG, PersistentDataType.STRING, "");
        CustomTag customTag = TagHandler.getById(id).orElse(null);

        if(customTag == null)
            return;

        player.closeInventory();

        if(!player.hasPermission(customTag.getPermission())) {
            player.sendMessage(Component.text("Você não tem acesso a essa tag.")
                    .color(NamedTextColor.RED));
        } else {
            TagHandler.setEquippedTag(player, customTag);
            player.sendMessage(Component.text("Tag ")
                    .append(LegacyComponentSerializer.legacyAmpersand().deserialize(customTag.name())
                            .append(Component.text(" equipada com sucesso!"))
                    ).color(NamedTextColor.GREEN));
        }
    }

}
