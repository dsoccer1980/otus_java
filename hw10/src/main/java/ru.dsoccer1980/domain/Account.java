package ru.dsoccer1980.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Account {

    @Id
    @GeneratedValue
    private long no;

    private String type;

    private Number rest;

    public Account(long no, String type, Number rest) {
        this.no = no;
        this.type = type;
        this.rest = rest;
    }

    public Account() {
    }

    @Override
    public String toString() {
        return "Account{" +
                "no=" + no +
                ", type='" + type + '\'' +
                ", rest=" + rest +
                '}';
    }
}
