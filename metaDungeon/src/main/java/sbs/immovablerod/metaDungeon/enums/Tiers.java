package sbs.immovablerod.metaDungeon.enums;


import net.kyori.adventure.text.format.NamedTextColor;

public enum Tiers {
    ONE (1) {
        @Override
        public NamedTextColor color() {
            return NamedTextColor.DARK_GREEN;
        }
    },
    TWO (2) {
        @Override
        public NamedTextColor color() {
            return NamedTextColor.BLUE;
        }
    },
    THREE (3) {
        @Override
        public NamedTextColor color() {
            return NamedTextColor.GOLD;
        }
    },
    FOUR (4) {
        @Override
        public NamedTextColor color() {
            return NamedTextColor.LIGHT_PURPLE;
        }
    },
    TEN (10) {
        @Override
        public NamedTextColor color() {
            return NamedTextColor.LIGHT_PURPLE;
        }
    };

    final private int id;
    public abstract NamedTextColor color();

    Tiers(int id) {
        this.id = id;
    }
    public static Tiers get(int abbr){
        for(Tiers v : values()){
            if(v.id == abbr){
                return v;
            }
        }
        return null;
    }
}