package com.kt.giga.home.b2b.domain;

import lombok.ToString;

/**
 * Created by cecil on 2017. 1. 15..
 */
@ToString
public enum PeriodUnit {
    YEAR('Y'),
    MONTH('M');

    private char unit;

    public char getUnit() {
        return unit;
    }

    PeriodUnit(char unit) {
        this.unit = unit;
    }

    public static PeriodUnit getPeriodUnit(char c) {
        for (PeriodUnit periodUnit : values()) {
            if (Character.toUpperCase(c) == periodUnit.unit)
                return periodUnit;
        }

        throw new IllegalArgumentException("No matching period unit for unit [" + c + "] found.");
    }
}
