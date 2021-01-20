package com.api.task.util;

public enum PriorityEnum {

    HI("HIGH"),
    MI("MID"),
    LO("LOW");

    private final String value;

    PriorityEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static PriorityEnum getEnum(String value) {
        for(PriorityEnum t : values()) {
            if(value.equals(t.getValue())) {
                return t;
            }
        }
        return null;
    }
}

