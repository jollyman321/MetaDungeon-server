package sbs.immovablerod.metaDungeon.elements.items;


import com.fasterxml.jackson.databind.JsonNode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonItem;
import sbs.immovablerod.metaDungeon.elements.ItemInterface;
import sbs.immovablerod.metaDungeon.enums.Signal;
import sbs.immovablerod.metaDungeon.game.GConfig;
import sbs.immovablerod.metaDungeon.gui.EventMenu;
import sbs.immovablerod.metaDungeon.signals.BaseSignal;
import sbs.immovablerod.metaDungeon.signals.NewQuestSignal;
import sbs.immovablerod.metaDungeon.signals.PlayerRightClickSignal;
import sbs.immovablerod.metaDungeon.signals.QuestCompletedSignal;

import java.util.HashMap;
import java.util.UUID;

public class EventCompass extends ItemInterface {
    private final MetaDungeon plugin = MetaDungeon.getInstance();

    private final MetaDungeonItem root;
    private final EventMenu gui;
    private Player player;
    private final HashMap<UUID, UUID> idMapper;
    public EventCompass(MetaDungeonItem root) {
        this.root = root;
        this.gui = new EventMenu();
        this.player = null;
        if (root.getOwner() != null) {
            this.player = root.getOwner().getPlayer();
        }

        this.idMapper = new HashMap<>();
    }
    @Override
    public void onCreated() {
        super.onCreated();

        GConfig.signalListeners.get(Signal.PLAYER_RIGHT_CLICK).registerTask(this::onPlayerRightClick);
        GConfig.signalListeners.get(Signal.NEW_QUEST).registerTask(this::onNewQuestEvent);
        GConfig.signalListeners.get(Signal.QUEST_COMPLETED).registerTask(this::onQuestCompleted);
    }

    public void onPlayerRightClick(BaseSignal event) {
        if (((PlayerRightClickSignal) event).getPlayer() == this.player) {
            System.out.println(((PlayerRightClickSignal) event).getPlayer().getInventory().getItemInMainHand().getType());
            System.out.println(this.player.getPlayer().getInventory().getItemInMainHand().getType());
            if (((PlayerRightClickSignal) event).getPlayer().getInventory().getItemInMainHand().getType() == Material.COMPASS) {
                this.gui.show(((PlayerRightClickSignal) event).getPlayer());
            }
        }
    }

    public void onNewQuestEvent(BaseSignal event) {
        System.out.println("check");

        JsonNode guiTemplate = ((NewQuestSignal) event).getEvent().getTemplate().path("gui");
        this.idMapper.put(
                ((NewQuestSignal) event).getEvent().getId(),
                this.gui.registerMenuItem(guiTemplate, () -> this.setCompassLocation(((NewQuestSignal) event).getEvent().getQuestLocation()))
        );
    }
    public void onQuestCompleted(BaseSignal event) {
        this.gui.unregisterMenuItem(this.idMapper.get(((QuestCompletedSignal) event).getQuest().getId()));
        this.setCompassLocation(plugin.game.getSpawnLocation());
    }
    public void setCompassLocation(Location loc) {
        this.player.setCompassTarget(loc);
    }

}
