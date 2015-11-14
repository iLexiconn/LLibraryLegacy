package net.ilexiconn.llibrary.common.command;

import java.util.Map;

/**
 * @see net.ilexiconn.llibrary.common.command.CommandBuilder
 * @author iLexiconn
 * @since 0.6.0
 */
public class CommandArguments {
    private Map<String, String> arguments;

    public CommandArguments(Map<String, String> arguments) {
        this.arguments = arguments;
    }

    public boolean has(String argument) {
        for (Map.Entry<String, String> entry : arguments.entrySet()) {
            if (entry.getKey().equals(argument)) {
                return true;
            }
        }
        return false;
    }

    public String get(String argument) {
        for (Map.Entry<String, String> entry : arguments.entrySet()) {
            if (entry.getKey().equals(argument)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
