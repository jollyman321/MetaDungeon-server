package sbs.immovablerod.metaDungeon.Enums;

public enum Colors {
    BLACK("black") {
        @Override
        public String code() {
            return "§0";
        }
    },
    DARK_BLUE("dark_blue") {
        @Override
        public String code() {
            return "§1";
        }
    },
    DARK_GREEN("dark_green"){
        @Override
        public String code() {
            return "§2";
        }
    },
    DARK_AQUA("dark_aqua"){
        @Override
        public String code() {
            return "§3";
        }
    },
    DARK_RED("dark_red"){
        @Override
        public String code() {
            return "§4";
        }
    },
    DARK_PURPLE("dark_purple"){
        @Override
        public String code() {
            return "§5";
        }
    },
    GOLD("gold"){
        @Override
        public String code() {
            return "§6";
        }
    },
    GRAY("gray") {
        @Override
        public String code() {
            return "§7";
        }
    },
    DARK_GRAY("dark_gray"){
        @Override
        public String code() {
            return "§8";
        }
    },
    BLUE("blue"){
        @Override
        public String code() {
            return "§9";
        }
    },
    GREEN("green"){
        @Override
        public String code() {
            return "§a";
        }
    },
    AQUA("aqua"){
        @Override
        public String code() {
            return "§b";
        }
    },
    RED("red"){
        @Override
        public String code() {
            return "§c";
        }
    },
    LIGHT_PURPLE("light_purple"){
        @Override
        public String code() {return "§d";}
    },
    YELLOW("yellow"){
        @Override
        public String code() {return "§e";}
    },
    WHITE("white"){
        @Override
        public String code() {return "§f";}
    };
    final private String value;
    public abstract String code();

    Colors(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
