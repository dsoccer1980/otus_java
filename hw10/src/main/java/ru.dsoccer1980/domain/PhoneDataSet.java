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
public class PhoneDataSet {

    @Id
    @GeneratedValue
    private long id;

    private String number;

    public PhoneDataSet(String number) {
        this.number = number;
    }
}
