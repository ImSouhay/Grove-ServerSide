package org.imsouhay.Grove.util;

public class DexRange {
    private final int max;
    private final int min;

    public static final int ABSOLUTE_MAX=-2;
    public static final int ABSOLUTE_MIN=-3;

    public DexRange(int max, int min) {
        this.max = max;
        this.min = min;
    }

    public DexRange(int singleValue) {
        this.max = singleValue;
        this.min = singleValue;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public int isSingle() {
        if(max==min) return max;
        return -1;
    }
}
