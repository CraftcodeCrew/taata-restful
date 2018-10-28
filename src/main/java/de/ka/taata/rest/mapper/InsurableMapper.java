package de.ka.taata.rest.mapper;

import de.ka.taata.persistence.Insurable;
import de.ka.taata.rest.InsurableController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 *
 */
@Component
public class InsurableMapper implements ResourceAssembler<Insurable, Resource<Insurable>> {

    @Override
    public Resource<Insurable> toResource(Insurable insurable) {
        Resource<Insurable> resource = new Resource<>(insurable);
        resource.add(linkTo(InsurableController.class).slash(insurable.getId()).withSelfRel());
        resource.add(linkTo(InsurableController.class).withRel("insurables"));
        return resource;
    }

}
