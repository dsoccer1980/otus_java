package ru.dsoccer1980.ATM;

import ru.dsoccer1980.ATM.model.Banknote;

import java.util.Map;

public interface CashMachine {

    void put(Banknote banknote);

    Map<Banknote, Integer> give(int sum) throws IllegalArgumentException;

    int rest();

    void resetState();
}
