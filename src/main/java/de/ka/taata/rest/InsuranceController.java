package de.ka.taata.rest;

import de.ka.taata.persistence.Insurance;
import de.ka.taata.persistence.InsuranceDAO;
import de.ka.taata.rest.mapper.InsuranceMapper;
import de.ka.taata.rest.misc.InsuranceNotFoundException;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 *
 */
@RestController
@RequestMapping(value = "/insurances")
public class InsuranceController {

    private InsuranceDAO insuranceDAO;
    private InsuranceMapper insuranceMapper;

    //--------------------------------------
    // Constructors
    //--------------------------------------

    public InsuranceController(InsuranceDAO insuranceDAO, InsuranceMapper insuranceMapper) {
        this.insuranceDAO = insuranceDAO;
        this.insuranceMapper = insuranceMapper;
    }

    //--------------------------------------
    // Methods
    //--------------------------------------

    @GetMapping
    public Resources<Resource<Insurance>> all() {
        List<Resource<Insurance>> Insurances = insuranceDAO.findAll().stream()
                .map(insuranceMapper::toResource)
                .collect(Collectors.toList());
        return new Resources<>(Insurances, linkTo(InsuranceController.class).withSelfRel());
    }

    @PostMapping
    public Insurance create(@RequestBody Insurance insurance) {
        return insuranceDAO.save(insurance);
        /*
        Resource<Insurance> resource = insuranceMapper.toResource(insuranceDAO.save(insurance));
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
        */
    }

    @GetMapping("/{id}")
    public Resource<Insurance> read(@PathVariable long id) throws InsuranceNotFoundException {
        return insuranceMapper.toResource(
                insuranceDAO.findById(id).orElseThrow(() -> new InsuranceNotFoundException(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Insurance newInsurance, @PathVariable long id) throws URISyntaxException {
        Insurance updatedInsurance = insuranceDAO.findById(id)
                .map(insurance -> {
                    insurance.updateFrom(newInsurance);
                    return insuranceDAO.save(insurance);
                })
                .orElseGet(() -> {
                    //newInsurance.setId(id);
                    return insuranceDAO.save(newInsurance);
                });
        Resource<Insurance> resource = insuranceMapper.toResource(updatedInsurance);
        return ResponseEntity.created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        if (insuranceDAO.existsById(id))
            insuranceDAO.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
