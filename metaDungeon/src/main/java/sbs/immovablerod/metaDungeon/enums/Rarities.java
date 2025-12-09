package sbs.immovablerod.metaDungeon.enums;


import net.kyori.adventure.text.format.NamedTextColor;

public enum Rarities {
    COMMON (1) {
        @Override
        public NamedTextColor color() { return NamedTextColor.DARK_GREEN; }
    },
    UNCOMMON (2) {
        @Override
        public NamedTextColor color() {
            return NamedTextColor.YELLOW;
        }
    },
    RARE (3) {
        @Override
        public NamedTextColor color() {
            return NamedTextColor.BLUE;
        }
    },
    LEGENDARY (5) {
        @Override
        public NamedTextColor color() {
            return NamedTextColor.GOLD;
        }
    },
    MYTHIC (6) {
        @Override
        public NamedTextColor color() {
            return NamedTextColor.LIGHT_PURPLE;
        }
    };

    final private int id;

    public abstract NamedTextColor color();

    Rarities(int id) {
        this.id = id;
    }
    public static Rarities get(int abbr){
        for(Rarities v : values()){
            if(v.id == abbr){
                return v;
            }
        }
        return null;
    }

}