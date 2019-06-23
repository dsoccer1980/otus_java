package ru.dsoccer1980;

public class Main {

    public static void main(String[] args) {
        CashMachine cashMachine = new CashMachine();
        cashMachine.put(Banknote.HUNDRED);
        cashMachine.put(Banknote.HUNDRED);
        cashMachine.put(Banknote.HUNDRED);
        cashMachine.put(Banknote.FIFTY);
        cashMachine.put(Banknote.TEN);
        cashMachine.put(Banknote.FIVE);

        System.out.println("Остаток в банкомате: " + cashMachine.rest());
        System.out.println("Выдать из банкомата 215");
        System.out.println("Банкомат выдал:");
        cashMachine.give(215).forEach((k, v) -> System.out.println(k + " " + v));
        System.out.println("Остаток в банкомате: " + cashMachine.rest());
        System.out.println("Внести в банкомат 500");
        cashMachine.put(Banknote.FIVEHUNDRED);
        System.out.println("Остаток в банкомате: " + cashMachine.rest());

    }
}
