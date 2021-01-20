package com.api.task.util;

public enum TaskEnum {

    PG("IN_PROGRESS"),
    CD("CONCLUDED");

    private final String value;

    TaskEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static TaskEnum getEnum(String value) {
        for(TaskEnum t : values()) {
            if(value.equals(t.getValue())) {
                return t;
            }
        }
        return null;
    }
}

