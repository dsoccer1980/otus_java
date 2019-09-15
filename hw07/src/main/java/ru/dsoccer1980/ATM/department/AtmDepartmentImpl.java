package ru.dsoccer1980.ATM.department;

import ru.dsoccer1980.ATM.CashMachine;
import ru.dsoccer1980.ATM.chain.Middleware;
import ru.dsoccer1980.ATM.exception.UnSupportedOperationException;

import java.util.ArrayList;
import java.util.List;

public class AtmDepartmentImpl implements AtmDepartment {
    private List<CashMachine> cashMachineList = new ArrayList<>();


    @Override
    public void addCashMachine(CashMachine cashMachine) {
        if (cashMachine instanceof Middleware) {
            if (!cashMachineList.isEmpty()) {
                ((Middleware) cashMachineList.get(cashMachineList.size() - 1)).linkWith((Middleware) cashMachine);
            }
        } else {
            throw new UnSupportedOperationException("Not supported");
        }

        cashMachineList.add(cashMachine);
    }

    @Override
    public void addCashMachine(CashMachine... cashMachines) {
        for (CashMachine cashMachine : cashMachines) {
            addCashMachine(cashMachine);
        }
    }

    @Override
    public int getRest() {
        if (cashMachineList.isEmpty()) {
            return 0;
        }
        return ((Middleware) cashMachineList.get(0)).getRest(0);
    }

    @Override
    public void resetState() {
        cashMachineList.forEach(CashMachine::resetState);
    }
}
