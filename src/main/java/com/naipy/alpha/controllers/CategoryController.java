package com.naipy.alpha.controllers;

import com.naipy.alpha.entities.Category;
import com.naipy.alpha.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    @Autowired
    private final CategoryService _categoryService;

    @MutationMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    Category addCategory (@Argument CategoryInput category) {
        return _categoryService.addCategory(Category.builder().name(category.name).build());
    }

    @QueryMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    List<Category> findAllCategories () {
        return _categoryService.findAll();
    }

    @QueryMapping()
    Category findByCategoryId (@Argument Long id) {
        return _categoryService.findById(id);
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    Long deleteCategoryById (@Argument Long id) {
        _categoryService.delete(id);
        return id;
    }

    record CategoryInput (String name) {}
}
