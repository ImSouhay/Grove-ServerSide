package org.imsouhay.poketrainer.util;

public enum vType {
    IV("IV"),
    EV("EV");

    private final String name;

    vType(String name) {
        this.name=name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
