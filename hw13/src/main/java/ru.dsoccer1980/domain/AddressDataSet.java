package ru.dsoccer1980.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDataSet {

    @Id
    @GeneratedValue
    private long Id;

    private String street;

    public AddressDataSet(String street) {
        this.street = street;
    }
}
