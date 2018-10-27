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
@Table(name = "insurable")
public class Insurable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String description;
    private String imageId;

    private boolean isInsured;
    private double probability;

    @OneToOne(fetch = FetchType.EAGER)
    private Insurance insurance;

    //--------------------------------------
    // Constructors
    //--------------------------------------

    public Insurable() {

    }

    public Insurable(String title, String description) {
        this.title = title;
        this.description = description;
    }

    //--------------------------------------
    // Methods
    //--------------------------------------

    public void updateFrom(Insurable newInsurable) {
        title = newInsurable.title;
        description = newInsurable.description;
        imageId = newInsurable.imageId;
        isInsured = newInsurable.isInsured;
        probability = newInsurable.probability;
    }

}
