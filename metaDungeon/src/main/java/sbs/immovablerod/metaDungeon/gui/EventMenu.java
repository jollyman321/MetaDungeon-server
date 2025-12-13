package sbs.immovablerod.metaDungeon.gui;

import com.fasterxml.jackson.databind.JsonNode;
import de.themoep.inventorygui.GuiElement;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sbs.immovablerod.metaDungeon.MetaDungeon;

import java.util.HashMap;
import java.util.UUID;

import static sbs.immovablerod.metaDungeon.gui.MiniMessageGUI.createMiniMessageGui;

public class EventMenu {
    private final MetaDungeon plugin = MetaDungeon.getInstance();
    private final GuiElementGroup elementGroup;
    private final InventoryGui gui;

    private HashMap<UUID, GuiElement> activeQuests;

    public EventMenu () {
        this.activeQuests = new HashMap<>();

        String[] guiSetup = {
                "         ",
                " ggggggg ",
                "         "
        };
        this.elementGroup = new GuiElementGroup('g');
        this.gui = new InventoryGui(plugin, "Events", guiSetup); //createMiniMessageGui(plugin, null, "Quests", guiSetup);

        this.gui.addElement(this.elementGroup);
        this.gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
    }

    public UUID registerMenuItem(JsonNode template, Runnable func) {
            UUID id = UUID.randomUUID();
        activeQuests.put(id, (
                    new StaticGuiElement('e',
                            new ItemStack(Material.valueOf(template.path("item").asText().toUpperCase())),
                            click -> {
                                func.run(); // Update the GUI
                                return true;
                            },
                            template.path("name").asText(),
                            template.path("desc").asText()
                    )
            ));
            this.elementGroup.addElement(activeQuests.get(id));
            return id;
    }

    public void unregisterMenuItem(UUID id) {
        this.elementGroup.clearElements();
        this.activeQuests.remove(id);
        this.activeQuests.values().forEach(this.elementGroup::addElement);
    }


    public void show(Player target) {

        this.gui.show(target);
    }
}
