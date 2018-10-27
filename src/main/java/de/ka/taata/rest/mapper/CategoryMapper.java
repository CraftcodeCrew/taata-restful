package de.ka.taata.rest.mapper;

import de.ka.taata.persistence.Category;
import de.ka.taata.rest.CategoryController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

/**
 *
 */
@Component
public class CategoryMapper implements ResourceAssembler<Category, Resource<Category>> {

    @Override
    public Resource<Category> toResource(Category category) {
        Resource<Category> resource = new Resource<>(category);
        resource.add(linkTo(CategoryController.class).slash(category.getId()).withSelfRel());
        resource.add(linkTo(CategoryController.class).withRel("categories"));
        return resource;
    }

}
