package de.ka.taata.rest;

import de.ka.taata.persistence.Insurable;
import de.ka.taata.persistence.InsurableDAO;
import de.ka.taata.persistence.Insurance;
import de.ka.taata.persistence.InsuranceDAO;
import de.ka.taata.rest.mapper.InsurableMapper;
import de.ka.taata.rest.misc.InsurableNotFoundException;
import de.ka.taata.rest.misc.InsuranceNotFoundException;
import de.ka.taata.rest.model.InsurableCreate;
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
@RequestMapping(value = "/insurables")
public class InsurableController {

    private InsurableDAO insurableDAO;
    private InsurableMapper insurableMapper;

    private InsuranceDAO insuranceDAO;

    //--------------------------------------
    // Constructors
    //--------------------------------------

    public InsurableController(InsurableDAO insurableDAO, InsurableMapper insurableMapper,
                               InsuranceDAO insuranceDAO) {
        this.insurableDAO = insurableDAO;
        this.insurableMapper = insurableMapper;
        this.insuranceDAO = insuranceDAO;
    }

    //--------------------------------------
    // Methods
    //--------------------------------------

    @GetMapping
    public Resources<Resource<Insurable>> all() {
        List<Resource<Insurable>> insurables = insurableDAO.findAll().stream()
                .map(insurableMapper::toResource)
                .collect(Collectors.toList());
        return new Resources<>(insurables, linkTo(InsurableController.class).withSelfRel());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody InsurableCreate insurableCreate)
            throws URISyntaxException, InsuranceNotFoundException {
        Insurable insurable = new Insurable();
        insurable.setTitle(insurableCreate.getTitle());
        insurable.setDescription(insurableCreate.getDescription());
        insurable.setImageId(insurableCreate.getImageId());
        insurable.setProbability(insurableCreate.getProbability());

        Insurance insurance = insuranceDAO.findById(insurableCreate.getInsuranceId())
                .orElseThrow(() -> new InsuranceNotFoundException(insurableCreate.getInsuranceId()));
        insurable.setInsurance(insurance);
        Resource<Insurable> resource = insurableMapper.toResource(insurableDAO.save(insurable));
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @GetMapping("/{id}")
    public Resource<Insurable> read(@PathVariable long id) throws InsurableNotFoundException {
        return insurableMapper.toResource(
                insurableDAO.findById(id).orElseThrow(() -> new InsurableNotFoundException(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Insurable newInsurable, @PathVariable long id) throws URISyntaxException {
        Insurable updatedInsurable = insurableDAO.findById(id)
                .map(insurable -> {
                    insurable.updateFrom(newInsurable);
                    return insurableDAO.save(insurable);
                })
                .orElseGet(() -> {
                    //newInsurable.setId(id);
                    return insurableDAO.save(newInsurable);
                });
        Resource<Insurable> resource = insurableMapper.toResource(updatedInsurable);
        return ResponseEntity.created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        if (insurableDAO.existsById(id))
            insurableDAO.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
