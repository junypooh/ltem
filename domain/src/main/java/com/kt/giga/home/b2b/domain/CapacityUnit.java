package com.kt.giga.home.b2b.domain;

import lombok.ToString;

/**
 * Created by cecil on 2017. 1. 15..
 */
@ToString
public enum CapacityUnit {
    GIGABYTE('G', 1024 * 1024 * 1024),
    MEGABYTE('M', 1024 * 1024),
    PACKET('P', 512);

    private char unit;
    private int  inBytes;

    public char getUnit() {
        return unit;
    }

    public int getBytes() {
        return this.inBytes;
    }

    CapacityUnit(char unit, int inBytes) {
        this.unit = unit;
        this.inBytes = inBytes;
    }

    public static CapacityUnit getCapacityUnit(char c) {
        for (CapacityUnit capacityUnit : values()) {
            if (Character.toUpperCase(c) == capacityUnit.unit)
                return capacityUnit;
        }

        throw new IllegalArgumentException("No matching capacity unit for unit [" + c + "] found.");
    }
}
