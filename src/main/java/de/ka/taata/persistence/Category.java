package de.ka.taata.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

/**
 *
 */
@Data
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "category")
public class Category {

    // microInsurance
    // - price
    // - id
    // - name

    // Insurable
    // - id
    // - objective
    // - microInsurance

    // possibility

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String description;
    private String imageId;

    //--------------------------------------
    // Constructors
    //--------------------------------------

    public Category() {}

    public Category(String title, String description) {
        this.title = title;
        this.description = description;
    }

    //--------------------------------------
    // Methods
    //--------------------------------------

    public void updateFrom(Category category) {
        this.title = category.title;
        this.description = category.description;
    }

    //--------------------------------------
    // General
    //--------------------------------------

}
