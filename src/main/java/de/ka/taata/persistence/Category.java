package de.ka.taata.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
@Data
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "category")
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String description;
    private String imageId;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Insurable> insurables;

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

    public void addInsurable(Insurable insurable) {
        if (insurables == null)
            insurables = new LinkedList<>();
        insurables.add(insurable);
    }

    public void updateFrom(Category category) {
        this.title = category.title;
        this.description = category.description;
    }

}
