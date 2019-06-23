package ru.dsoccer1980;

public enum Banknote {

    FIVE(5),
    TEN(10),
    FIFTY(50),
    HUNDRED(100),
    FIVEHUNDRED(500),
    THOUSAND(1000),
    FIVETHOUSAND(5000);

    private int value;

    Banknote(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
