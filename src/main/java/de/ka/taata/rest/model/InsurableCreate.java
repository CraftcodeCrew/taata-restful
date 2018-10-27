package de.ka.taata.rest.model;

import lombok.Data;

/**
 *
 */
@Data
public class InsurableCreate {

    private String title;
    private String description;
    private String imageId;
    private double probability;
    private long insuranceId;

    //--------------------------------------
    // Constructors
    //--------------------------------------

    public InsurableCreate() {

    }

}
