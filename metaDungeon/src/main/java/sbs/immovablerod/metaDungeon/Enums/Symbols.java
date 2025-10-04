package sbs.immovablerod.metaDungeon.Enums;

public enum Symbols {
    HEART("❤") {
        @Override
        public Colors color() {
            return Colors.RED;}
    },
    LIFE("✝"){
        @Override
        public Colors color() {return Colors.DARK_AQUA;}
    },
    DEFENCE("✠"){
        @Override
        public Colors color() {return Colors.BLUE;}
    },
    DAMAGE("❉"){
        @Override
        public Colors color() {return Colors.RED;}
    },
    STAMINA("❖"){
        @Override
        public Colors color() {return Colors.YELLOW;}
    },
    HEAL_STAMINA("+"){
        @Override
        public Colors color() {return Colors.GREEN;}
    },
    HEAL_LIFE("+"){
        @Override
        public Colors color() {return Colors.GREEN;}
    },
    STUNNED("✨"){
        @Override
        public Colors color() {return Colors.YELLOW;}
    };

    final private String value;
    public abstract Colors color();

    Symbols(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
