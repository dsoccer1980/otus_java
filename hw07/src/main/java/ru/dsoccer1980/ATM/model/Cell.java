package ru.dsoccer1980.ATM.model;

public class Cell {

    private Banknote banknote;
    private int count;

    public Cell(Banknote banknote, int count) {
        this.banknote = banknote;
        this.count = count;
    }

    public Banknote getBanknote() {
        return banknote;
    }

    public void setBanknote(Banknote banknote) {
        this.banknote = banknote;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void increase() {
        count++;
    }

    public void decrease() {
        count--;
    }

    public int getBanknoteValue() {
        return banknote.getValue();
    }

    @Override
    public String toString() {
        return "Cell{" +
                "banknote=" + banknote +
                ", count=" + count +
                '}';
    }
}
