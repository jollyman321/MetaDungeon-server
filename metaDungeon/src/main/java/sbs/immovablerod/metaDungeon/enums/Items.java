package sbs.immovablerod.metaDungeon.enums;

import sbs.immovablerod.metaDungeon.classes.MetaDungeonItem;
import sbs.immovablerod.metaDungeon.elements.ItemInterface;
import sbs.immovablerod.metaDungeon.elements.items.Dummy;
import sbs.immovablerod.metaDungeon.elements.items.SmallHealthCrystal;
import sbs.immovablerod.metaDungeon.elements.items.SmallStaminaCrystal;

import java.util.Objects;

public enum Items
{
    DUMMY("dummy") {
        @Override
        public ItemInterface itemInterface(MetaDungeonItem root) {
            return new Dummy(root);
        }
    },
    SMALL_HEALTH_CRYSTAL("small_health_crystal") {
        @Override
        public ItemInterface itemInterface(MetaDungeonItem root) {
            return new SmallHealthCrystal(root);
        }
    },
    SMALL_STAMINA_CRYSTAL("small_stamina_crystal") {
        @Override
        public ItemInterface itemInterface(MetaDungeonItem root) {
            return new SmallStaminaCrystal(root);
        }
    };


    final private String value;
    Items(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public abstract ItemInterface itemInterface(MetaDungeonItem root);

    public static ItemInterface get(String abbr, MetaDungeonItem root){
    for(Items v : values()){
        if(Objects.equals(v.value, abbr)){
            return v.itemInterface(root);
        }
    }
    return Items.DUMMY.itemInterface(root);
}
}
