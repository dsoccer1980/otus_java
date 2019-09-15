package ru.dsoccer1980.ATM.chain;

public abstract class Middleware {

    private Middleware next;

    public Middleware linkWith(Middleware next) {
        this.next = next;
        return next;
    }

    public abstract int getRest(int rest);

    protected int checkNext(int rest) {
        if (next == null) {
            return rest;
        }
        return next.getRest(rest);
    }
}
