package net.ilexiconn.llibrary.common.command.builder;

public class Argument {
    private String name;
    private String value;
    private ArgumentType<?> type;

    public Argument(String name, String value, ArgumentType<?> type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public ArgumentType<?> getType() {
        return type;
    }
}
