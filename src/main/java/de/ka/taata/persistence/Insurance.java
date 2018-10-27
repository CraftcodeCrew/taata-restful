package de.ka.taata.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 *
 */
@Data
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "insurance")
public class Insurance {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private double pricePerMonth;

    //--------------------------------------
    // Constructors
    //--------------------------------------

    public Insurance() {

    }

    public Insurance(String name, double pricePerMonth) {
        this.name = name;
        this.pricePerMonth = pricePerMonth;
    }

    //--------------------------------------
    // Methods
    //--------------------------------------

    public void updateFrom(Insurance newInsurance) {
        name = newInsurance.name;
        pricePerMonth = newInsurance.pricePerMonth;
    }

}
