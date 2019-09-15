package ru.dsoccer1980.ATM.memento;

import ru.dsoccer1980.ATM.model.Cell;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Memento {

    private Set<Cell> cells = new HashSet<>();

    public Memento(Set<Cell> cells) {
        this.cells = cells.stream().map(cell -> new Cell(cell.getBanknote(), cell.getCount())).collect(Collectors.toSet());
    }

    public Set<Cell> getState() {
        return cells;
    }
}
