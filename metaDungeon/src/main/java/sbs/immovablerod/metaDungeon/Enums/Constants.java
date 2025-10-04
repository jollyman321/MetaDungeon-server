package sbs.immovablerod.metaDungeon.Enums;


public enum Constants {
    PLAYER_SPEED_MODIFIER (50),
    MONSTER_SPEED_MODIFIER (40);


    final private int value;

    Constants(int value) {

        this.value = value;
    }
    public int value() {
        return value;
    }
}