package ru.dsoccer1980.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    private int age;

    @OneToOne
    private AddressDataSet addressDataSet;

    public User(String name, int age, AddressDataSet addressDataSet) {
        this.name = name;
        this.age = age;
        this.addressDataSet = addressDataSet;
    }
}
