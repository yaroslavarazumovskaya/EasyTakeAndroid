package com.diploma.easytake.enums;

public enum Category {

    PRODUCTS("Товари"), CLIENTS("Клієнти"), ORDERS("Замовлення");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Category getValue(String name){
        for (Category value : values()) {
            if(value.name.equals(name))
                return value;
        }
        return null;
    }
}
