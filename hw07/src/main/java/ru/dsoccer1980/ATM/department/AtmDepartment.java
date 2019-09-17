package ru.dsoccer1980.ATM.department;

import ru.dsoccer1980.ATM.CashMachine;

public interface AtmDepartment {

    void addCashMachine(CashMachine cashMachine);

    void addCashMachine(CashMachine... cashMachines);

    int getRest();

    void resetState();
}
