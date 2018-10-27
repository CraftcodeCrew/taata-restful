package de.ka.taata.rest.misc;

/**
 *
 */
public class CategoryNotFoundException extends Exception {

    public CategoryNotFoundException(long id) {
        super(String.format("Could not find category with id '%s'.", id));
    }

}
