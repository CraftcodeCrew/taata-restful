package de.ka.taata.rest.misc;

/**
 *
 */
public class InsurableNotFoundException extends Exception {

    public InsurableNotFoundException(long id) {
        super(String.format("Could not find insurable with id '%s'.", id));    }

}
