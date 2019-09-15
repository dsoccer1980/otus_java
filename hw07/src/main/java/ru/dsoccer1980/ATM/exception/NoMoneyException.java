package ru.dsoccer1980.ATM.exception;

public class NoMoneyException extends RuntimeException {

    public NoMoneyException(String message) {
        super("There is not enough money in ATM");
    }
}
