package sbs.immovablerod.metaDungeon.elements.items;

import com.fasterxml.jackson.databind.JsonNode;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonEffect;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonEntity;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonItem;
import sbs.immovablerod.metaDungeon.elements.ItemInterface;
import sbs.immovablerod.metaDungeon.enums.Effects;

import java.util.ArrayList;
import java.util.Objects;

public class EffectWeapon extends ItemInterface {
    private final MetaDungeonItem root;
    private final ArrayList<MetaDungeonEffect> effects;


    public EffectWeapon(MetaDungeonItem root) {
        this.root = root;
        this.effects = new ArrayList<MetaDungeonEffect>();
    }

    @Override
    public void onCreated() {
        super.onCreated();

    }

    @Override
    public void onCommitAttack(MetaDungeonEntity attacker, MetaDungeonEntity victim) {
        super.onCommitAttack(attacker, victim);
        final JsonNode arrNode = this.root.getTemplate().get("onCommitAttack").get("applyEffects");
        if (arrNode.isArray()) {
            arrNode.forEach(node -> {
                MetaDungeonEffect effect = new MetaDungeonEffect(
                        Effects.valueOf(node.path("name").asText().toUpperCase()),
                        node.path("duration").asInt(1),
                        node.path("level").asInt(0));

                if (Objects.equals(node.path("target").asText(), "self")) {
                    effect.addTarget(attacker);
                } else if (Objects.equals(node.path("target").asText(), "victim")) {
                    effect.addTarget(victim);
                }

                effect.trigger();
            });
        }

        //this.effects.forEach(effect -> {
        //    victim.addEffect(effect, true);
        //});
    }

    @Override
    public void onReceiveAttack(MetaDungeonEntity attacker, MetaDungeonEntity receiver) {
        super.onReceiveAttack(attacker, receiver);
        final JsonNode arrNode = this.root.getTemplate().get("onReceiveAttack").get("applyEffects");
        if (arrNode.isArray()) {
            arrNode.forEach(node -> {
                MetaDungeonEffect effect = new MetaDungeonEffect(
                        Effects.valueOf(node.path("name").asText().toUpperCase()),
                        node.path("duration").asInt(1),
                        node.path("level").asInt(0));
                if (Objects.equals(node.path("target").asText(), "self")) {
                    effect.addTarget(receiver);
                } else if (Objects.equals(node.path("target").asText(), "victim")) {
                    effect.addTarget(attacker);
                }

                effect.trigger();
            });
        }

        //this.effects.forEach(effect -> {
        //    receiver.addEffect(effect, true);
        //});
    }
}
