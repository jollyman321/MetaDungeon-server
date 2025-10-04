package sbs.immovablerod.metaDungeon.Enums;


public enum Tiers {
    ONE (1) {
        @Override
        public Colors color() {
            return Colors.DARK_GREEN;
        }
    },
    TWO (2) {
        @Override
        public Colors color() {
            return Colors.BLUE;
        }
    },
    THREE (3) {
        @Override
        public Colors color() {
            return Colors.GOLD;
        }
    },
    FOUR (4) {
        @Override
        public Colors color() {
            return Colors.LIGHT_PURPLE;
        }
    };

    final private int id;

    public abstract Colors color();

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