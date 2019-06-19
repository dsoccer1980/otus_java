package ru.dsoccer1980;

import java.util.*;

public class CashMachine {
    private Set<Cell> cells = new HashSet<>();

    {
        for (Banknote banknote : Banknote.values()) {
            cells.add(new Cell(banknote, 0));
        }
    }

    public void put(Banknote banknote) {
        cells.stream().filter(cell -> cell.getBanknote() == banknote).forEach(Cell::increase);
    }

    public Map<Banknote, Integer> give(int sum) throws IllegalArgumentException {
        Map<Banknote, Integer> banknotesForDelivery = new HashMap<>();

        if (sum > rest()) {
            throw new IllegalArgumentException("There is not enough money in ATM");
        }

        Set<Cell> cellsCopy = createBackup();
        try {
            while (sum > 0) {
                Banknote banknote = giveNextBanknote(sum);
                banknotesForDelivery.merge(banknote, 1, (o, n) -> o + 1);
                sum -= banknote.getValue();
            }
        } catch (IllegalArgumentException e) {
            restoreBackup(cellsCopy);
            e.printStackTrace();
        }

        return banknotesForDelivery;
    }

    public int rest() {
        return cells.stream()
                .mapToInt(cell -> cell.getBanknoteValue() * cell.getCount())
                .sum();
    }

    private Banknote giveNextBanknote(int rest) {
        Cell rightCell = cells.stream()
                .filter(cell -> cell.getCount() != 0 && cell.getBanknoteValue() <= rest)
                .max(Comparator.comparingInt(Cell::getBanknoteValue))
                .orElseThrow(() -> new IllegalArgumentException("ATM cannot give this amount"));
        rightCell.decrease();
        return rightCell.getBanknote();
    }

    private Set<Cell> createBackup() {
        Set<Cell> cellsCopy = new HashSet<>();
        cells.forEach(cell -> cellsCopy.add(new Cell(cell.getBanknote(), cell.getCount())));
        return cellsCopy;
    }

    private void restoreBackup(Set<Cell> cellsCopy) {
        cells = cellsCopy;
    }


}
