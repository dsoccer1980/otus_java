package ru.dsoccer1980.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class AddressDataSet {

    @Id
    @GeneratedValue
    private long Id;
    private String street;

    public AddressDataSet() {
    }

    public AddressDataSet(long id, String street) {
        Id = id;
        this.street = street;
    }

    public AddressDataSet(String street) {
        this.street = street;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "AddressDataSet{" +
                "Id=" + Id +
                ", street='" + street + '\'' +
                '}';
    }
}
