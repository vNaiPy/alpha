package com.naipy.alpha.services;

import com.naipy.alpha.entities.Category;
import com.naipy.alpha.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository _categoryRepository;

    public List<Category> findAll () {
        return _categoryRepository.findAll();
    }

    public Category findById (Long id) {
        Optional<Category> categoryOptional = _categoryRepository.findById(id);
        return categoryOptional.get();
    }

}
