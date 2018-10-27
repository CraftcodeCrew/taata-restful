package de.ka.taata.rest.misc;

/**
 *
 */
public class InsuranceNotFoundException extends Exception {

    public InsuranceNotFoundException(long id) {
        super(String.format("Could not find insurance with id '%s'.", id));
    }

}
