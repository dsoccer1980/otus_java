package ru.dsoccer1980.ATM;

import ru.dsoccer1980.ATM.chain.Middleware;
import ru.dsoccer1980.ATM.memento.Memento;
import ru.dsoccer1980.ATM.memento.MementoStorable;
import ru.dsoccer1980.ATM.model.Banknote;
import ru.dsoccer1980.ATM.model.Cell;

import java.util.*;

public class CashMachineImpl extends Middleware implements CashMachine, MementoStorable {

    private Set<Cell> cells = new HashSet<>();
    private Memento initialState;

    public CashMachineImpl() {
        createDefaultCells();
        initialState = saveState();
    }

    public CashMachineImpl(Set<Cell> cells) {
        this.cells = cells;
        initialState = saveState();
    }

    @Override
    public void put(Banknote banknote) {
        cells.stream().filter(cell -> cell.getBanknote() == banknote).forEach(Cell::increase);
    }

    @Override
    public Map<Banknote, Integer> give(int sum) throws IllegalArgumentException {
        Map<Banknote, Integer> banknotesForDelivery = new HashMap<>();

        if (sum > rest()) {
            throw new IllegalArgumentException("There is not enough money in ATM");
        }

        Memento memento = saveState();
        try {
            while (sum > 0) {
                Banknote banknote = giveNextBanknote(sum);
                banknotesForDelivery.merge(banknote, 1, (o, n) -> o + 1);
                sum -= banknote.getValue();
            }
        } catch (IllegalArgumentException e) {
            restoreState(memento);
            e.printStackTrace();
        }

        return banknotesForDelivery;
    }

    @Override
    public int rest() {
        return cells.stream()
                .mapToInt(cell -> cell.getBanknoteValue() * cell.getCount())
                .sum();
    }

    @Override
    public Memento saveState() {
        return new Memento(cells);
    }

    @Override
    public void restoreState(Memento memento) {
        this.cells = memento.getState();

    }

    @Override
    public void resetState() {
        restoreState(initialState);
    }

    @Override
    public int getRest(int rest) {
        return checkNext(rest + rest());
    }

    private Banknote giveNextBanknote(int rest) {
        Cell rightCell = cells.stream()
                .filter(cell -> cell.getCount() != 0 && cell.getBanknoteValue() <= rest)
                .max(Comparator.comparingInt(Cell::getBanknoteValue))
                .orElseThrow(() -> new IllegalArgumentException("ATM cannot give this amount"));
        rightCell.decrease();
        return rightCell.getBanknote();
    }

    private void createDefaultCells() {
        for (Banknote banknote : Banknote.values()) {
            cells.add(new Cell(banknote, 0));
        }
    }


}
