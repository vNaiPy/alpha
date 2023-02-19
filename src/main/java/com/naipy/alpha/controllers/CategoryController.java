package com.naipy.alpha.controllers;

import com.naipy.alpha.entities.Category;
import com.naipy.alpha.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    @Autowired
    private CategoryService _categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> findAll () {
        List<Category> categoryList = _categoryService.findAll();
        return ResponseEntity.ok().body(categoryList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Category> findById (@PathVariable Long id) {
        Category category = _categoryService.findById(id);
        return ResponseEntity.ok().body(category);
    }

}
