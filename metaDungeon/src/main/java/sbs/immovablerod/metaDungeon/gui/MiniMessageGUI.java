package sbs.immovablerod.metaDungeon.gui;

import de.themoep.inventorygui.InventoryGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import sbs.immovablerod.metaDungeon.MetaDungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class MiniMessageGUI {
    private final static MetaDungeon plugin = MetaDungeon.getInstance();

    public static InventoryGui createMiniMessageGui(JavaPlugin plugin, InventoryHolder holder, String title, String[] guiSetup) {
        InventoryGui.InventoryCreator miniMessageInventoryCreator = new InventoryGui.InventoryCreator(
                (gui, viewer, type) ->
                        Bukkit.createInventory(new InventoryGui.Holder(gui), type, MiniMessage.miniMessage().deserialize(gui.replaceVars(viewer, gui.getTitle()))),
                (gui, viewer, size) ->
                        Bukkit.createInventory(new InventoryGui.Holder(gui), size, MiniMessage.miniMessage().deserialize(gui.replaceVars(viewer, gui.getTitle()))));

        BiConsumer<ItemMeta, String> miniMessageNameSetter = (itemMeta, name) -> {
            if (name != null) {
                itemMeta.displayName((MiniMessage.miniMessage().deserialize(name)));
            }
        };

        BiConsumer<ItemMeta, List<String>> miniMessageLoreSetter = (itemMeta, lore) -> {
            if (lore != null) {
                List<Component> loreComponents = new ArrayList<>();
                for (String line : lore) {
                    loreComponents.add(MiniMessage.miniMessage().deserialize(line));
                }
                itemMeta.lore(loreComponents);
            }
        };

        return new InventoryGui(plugin, miniMessageInventoryCreator, miniMessageNameSetter, miniMessageLoreSetter, holder, title, guiSetup);
    }
}
