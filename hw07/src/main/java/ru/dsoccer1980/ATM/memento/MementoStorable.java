package ru.dsoccer1980.ATM.memento;

public interface MementoStorable {

    Memento saveState();

    void restoreState(Memento memento);

}
