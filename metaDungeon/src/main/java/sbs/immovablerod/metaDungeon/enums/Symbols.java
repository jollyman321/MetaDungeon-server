package sbs.immovablerod.metaDungeon.enums;

import net.kyori.adventure.text.format.NamedTextColor;

public enum Symbols {
    HEART("❤") {
        @Override
        public NamedTextColor color() {return NamedTextColor.RED;}
    },
    LIFE("✝"){
        @Override
        public NamedTextColor color() {return NamedTextColor.DARK_AQUA;}
    },
    DEFENCE("✠"){
        @Override
        public NamedTextColor color() {return NamedTextColor.BLUE;}
    },
    DAMAGE("❉"){
        @Override
        public NamedTextColor color() {return NamedTextColor.RED;}
    },
    STAMINA("❖"){
        @Override
        public NamedTextColor color() {return NamedTextColor.YELLOW;}
    },
    HEAL_STAMINA("+"){
        @Override
        public NamedTextColor color() {return NamedTextColor.GREEN;}
    },
    HEAL_LIFE("+"){
        @Override
        public NamedTextColor color() {return NamedTextColor.GREEN;}
    },
    STUNNED("✨"){
        @Override
        public NamedTextColor color() {return NamedTextColor.YELLOW;}
    };

    final private String value;
    public abstract NamedTextColor color();

    Symbols(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
