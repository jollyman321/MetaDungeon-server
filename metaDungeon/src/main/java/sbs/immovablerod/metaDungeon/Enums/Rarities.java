package sbs.immovablerod.metaDungeon.Enums;


public enum Rarities {
    COMMON (1) {
        @Override
        public Colors color() { return Colors.DARK_GREEN; }
    },
    RARE (3) {
        @Override
        public Colors color() {
            return Colors.BLUE;
        }
    },
    LEGENDARY (5) {
        @Override
        public Colors color() {
            return Colors.GOLD;
        }
    },
    MYTHIC (6) {
        @Override
        public Colors color() {
            return Colors.LIGHT_PURPLE;
        }
    };

    final private int id;

    public abstract Colors color();

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