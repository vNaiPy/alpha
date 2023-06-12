package com.naipy.alpha.modules.category.controller;

import com.naipy.alpha.modules.category.model.Category;
import com.naipy.alpha.modules.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

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
