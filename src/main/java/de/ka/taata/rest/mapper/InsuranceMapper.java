package de.ka.taata.rest.mapper;

import de.ka.taata.persistence.Insurance;
import de.ka.taata.rest.CategoryController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 *
 */
@Component
public class InsuranceMapper implements ResourceAssembler<Insurance, Resource<Insurance>> {

    @Override
    public Resource<Insurance> toResource(Insurance insurance) {
        Resource<Insurance> resource = new Resource<>(insurance);
        resource.add(linkTo(CategoryController.class).slash(insurance.getId()).withSelfRel());
        resource.add(linkTo(CategoryController.class).withRel("insurances"));
        return resource;
    }

}
