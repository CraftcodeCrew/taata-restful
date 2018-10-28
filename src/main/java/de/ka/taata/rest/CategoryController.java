package de.ka.taata.rest;

import de.ka.taata.persistence.*;
import de.ka.taata.rest.mapper.CategoryMapper;
import de.ka.taata.rest.misc.CategoryNotFoundException;
import de.ka.taata.rest.misc.InsurableNotFoundException;
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
@RequestMapping(value = "/categories")
public class CategoryController {

    private CategoryDAO categoryDAO;
    private CategoryMapper categoryMapper;

    private InsuranceDAO insuranceDAO;
    private InsurableDAO insurableDAO;

    //--------------------------------------
    // Constructors
    //--------------------------------------

    public CategoryController(CategoryDAO categoryDAO, CategoryMapper categoryMapper,
                              InsuranceDAO insuranceDAO, InsurableDAO insurableDAO) {
        this.categoryDAO = categoryDAO;
        this.categoryMapper = categoryMapper;
        this.insuranceDAO = insuranceDAO;
        this.insurableDAO = insurableDAO;

        String loremIpsum = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam";

        Insurance liabilityInsurance = insuranceDAO.save(new Insurance("Liability insurance", 14));
        Insurance homeInsurance = insuranceDAO.save(new Insurance("Home content insurance", 14));
        Insurance healthInsurance = insuranceDAO.save(new Insurance("Health insurance", 11));
        Insurance incomeInsurance = insuranceDAO.save(new Insurance("Income protection", 8));
        Insurance lifeInsurance = insuranceDAO.save(new Insurance("Life assurance", 5));
        Insurance carInsurance = insuranceDAO.save(new Insurance("Comprehensive insurance", 6));
        Insurance carPartialInsurance = insuranceDAO.save(new Insurance("Partial coverage insurance", 13));

        Insurance mobilePhoneInsurance = insuranceDAO.save(new Insurance("Mobile phone insurance", 5));
        Insurance guitarInsurance = insuranceDAO.save(new Insurance("Guitar insurance", 5));
        Insurance laptopInsurance = insuranceDAO.save(new Insurance("Laptop insurance", 5));
        Insurance voiceInsurance = insuranceDAO.save(new Insurance("Voice insurance", 6));
        Insurance bikeInsurance = insuranceDAO.save(new Insurance("Bike insurance", 4));
        Insurance eyesInsurance = insuranceDAO.saveAndFlush(new Insurance("Eyes insurance", 7));

        Insurable mobilePhoneInsurable = new Insurable("Mobile phone", loremIpsum);
        mobilePhoneInsurable.setImageId("mobile_alt_solid");
        Insurable guitarInsurable = new Insurable("Guitar", loremIpsum);
        guitarInsurable.setImageId("music_solid");
        Insurable laptopInsurable = new Insurable("Laptop", loremIpsum);
        laptopInsurable.setImageId("laptop_solid");
        Insurable voiceInsurable = new Insurable("Voice", loremIpsum);
        voiceInsurable.setImageId("microphone_solid");
        Insurable bikeInsurable = new Insurable("Bike", loremIpsum);
        bikeInsurable.setImageId("running_solid");
        Insurable eyesInsurable = new Insurable("Eyes", loremIpsum);
        eyesInsurable.setImageId("eye_solid");

        mobilePhoneInsurable.setInsurance(mobilePhoneInsurance);
        guitarInsurable.setInsurance(guitarInsurance);
        laptopInsurable.setInsurance(laptopInsurance);
        voiceInsurable.setInsurance(voiceInsurance);
        bikeInsurable.setInsurance(bikeInsurance);
        eyesInsurable.setInsurance(eyesInsurance);

        insurableDAO.save(mobilePhoneInsurable);
        insurableDAO.save(guitarInsurable);
        insurableDAO.save(laptopInsurable);
        insurableDAO.save(voiceInsurable);
        insurableDAO.save(bikeInsurable);
        insurableDAO.saveAndFlush(eyesInsurable);

        Category belongings = new Category("Belongings", loremIpsum);
        belongings.setImageId("box_open_solid");
        belongings.addInsurable(mobilePhoneInsurable);
        belongings.addInsurable(guitarInsurable);
        belongings.addInsurable(laptopInsurable);
        belongings.addInsurable(bikeInsurable);

        Category accidents = new Category("Accidents", loremIpsum);
        accidents.setImageId("car_crash_solid");

        Category activities = new Category("Activities", loremIpsum);
        activities.setImageId("bicycle_solid");

        Category catastrophes = new Category("Catastrophes", loremIpsum);
        catastrophes.setImageId("fire_solid");

        Category law = new Category("Law", loremIpsum);
        law.setImageId("gavel_solid");

        Category health = new Category("Health", loremIpsum);
        health.setImageId("heartbeat_solid");
        health.addInsurable(voiceInsurable);
        health.addInsurable(eyesInsurable);

        Category abilities = new Category("Abilities", loremIpsum);
        abilities.setImageId("toolbox_solid");

        categoryDAO.save(belongings);
        categoryDAO.save(accidents);
        categoryDAO.save(activities);
        categoryDAO.save(catastrophes);
        categoryDAO.save(law);
        categoryDAO.save(health);
        categoryDAO.saveAndFlush(abilities);
    }

    //--------------------------------------
    // Methods
    //--------------------------------------

    @GetMapping
    public Resources<Resource<Category>> all() {
        List<Resource<Category>> recipes = categoryDAO.findAll().stream()
                .map(categoryMapper::toResource)
                .collect(Collectors.toList());
        return new Resources<>(recipes, linkTo(CategoryController.class).withSelfRel());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Category category) throws URISyntaxException {
        Resource<Category> resource = categoryMapper.toResource(categoryDAO.save(category));
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @GetMapping("/{id}")
    public Resource<Category> read(@PathVariable long id) throws CategoryNotFoundException {
        return categoryMapper.toResource(
                categoryDAO.findById(id).orElseThrow(() -> new CategoryNotFoundException(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Category newCategory, @PathVariable long id) throws URISyntaxException {
        Category updatedCategory = categoryDAO.findById(id)
                .map(category -> {
                    category.updateFrom(newCategory);
                    return categoryDAO.save(category);
                })
                .orElseGet(() -> {
                    //newCategory.setId(id);
                    return categoryDAO.save(newCategory);
                });
        Resource<Category> resource = categoryMapper.toResource(updatedCategory);
        return ResponseEntity.created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        if (categoryDAO.existsById(id))
            categoryDAO.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{category}/insure/{insurable}")
    public Resource<Category> insure(
            @PathVariable(name = "category") long categoryId,
            @PathVariable(name = "insurable") long insurableId)
            throws Exception {
        Insurable insurable = insurableDAO.findById(insurableId)
                .orElseThrow(() -> new InsurableNotFoundException(insurableId));
        Category category = categoryDAO.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        if (!category.getInsurables().contains(insurable))
            category.addInsurable(insurable);
        try {
            return categoryMapper.toResource(categoryDAO.save(category));
        } catch (Exception e) {
            throw new Exception(String.format(
                    "Insurable (%s, %s) already belongs to an another category",
                    insurable.getTitle(), insurable.getId()));
        }
    }

}
