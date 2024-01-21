package org.imsouhay.poketrainer.util;

public enum Operation {
    PLUS('+'),
    MINUS('-');

    private final char operation;

    Operation(char operation) {
        this.operation=operation;
    }

    @Override
    public String toString() {
        return switch (operation) {
            case '+' -> "increase";
            case '-' -> "decrease";
            default -> String.valueOf(operation);
        };
    }

    public char toChar() {return  this.operation;}

}
