package sbs.immovablerod.metaDungeon.enums;

public enum EffectsOLD
{
    HEALTH_BOOST_ADD("health_boost_add"),
    SPEED_BOOST("speed_boost"),
    DAMAGE_BOOST("damage_boost");


    final private String value;
    EffectsOLD(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
