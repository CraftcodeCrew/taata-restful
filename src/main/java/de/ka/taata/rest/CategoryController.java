package de.ka.taata.rest;

import de.ka.taata.persistence.Category;
import de.ka.taata.persistence.CategoryDAO;
import de.ka.taata.rest.mapper.CategoryMapper;
import de.ka.taata.rest.misc.CategoryNotFoundException;
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

    //--------------------------------------
    // Constructors
    //--------------------------------------

    public CategoryController(CategoryDAO categoryDAO, CategoryMapper categoryMapper) {
        this.categoryDAO = categoryDAO;
        this.categoryMapper = categoryMapper;
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

}
