package ru.dsoccer1980.ATM;

import ru.dsoccer1980.ATM.department.AtmDepartment;
import ru.dsoccer1980.ATM.department.AtmDepartmentImpl;
import ru.dsoccer1980.ATM.model.Banknote;

public class Main {

    public static void main(String[] args) {

        CashMachine cashMachine = new CashMachineImpl();
        cashMachine.put(Banknote.HUNDRED);
        cashMachine.put(Banknote.THOUSAND);
        System.out.println("Остаток в банкомате1: " + cashMachine.rest());

        CashMachine cashMachine2 = new CashMachineImpl();
        cashMachine2.put(Banknote.FIFTY);
        System.out.println("Остаток в банкомате2: " + cashMachine2.rest());

        CashMachine cashMachine3 = new CashMachineImpl();
        cashMachine3.put(Banknote.FIVE);
        cashMachine3.put(Banknote.TEN);
        System.out.println("Остаток в банкомате3: " + cashMachine3.rest());

        AtmDepartment atmDepartment = new AtmDepartmentImpl();
        atmDepartment.addCashMachine(cashMachine, cashMachine2, cashMachine3);
        System.out.println("Остаток в департаменте: " + atmDepartment.getRest());

        atmDepartment.resetState();
        System.out.println("Остаток в департаменте: " + atmDepartment.getRest());

    }
}
