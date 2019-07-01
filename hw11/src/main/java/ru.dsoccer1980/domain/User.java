package ru.dsoccer1980.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(fetch = FetchType.EAGER)
    private List<PhoneDataSet> phoneDataSets;

    public User(String name, int age, AddressDataSet addressDataSet, List<PhoneDataSet> phoneDataSets) {
        this.name = name;
        this.age = age;
        this.addressDataSet = addressDataSet;
        this.phoneDataSets = phoneDataSets;
    }
}
