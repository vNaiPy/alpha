package com.naipy.alpha.modules.category.service;

import com.naipy.alpha.modules.category.model.Category;
import com.naipy.alpha.modules.category.repository.CategoryRepository;
import com.naipy.alpha.modules.exceptions.services.DatabaseException;
import com.naipy.alpha.modules.exceptions.services.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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
        if (categoryOptional.isEmpty()) throw new ResourceNotFoundException("Category not found. Id:" + id);
        return categoryOptional.get();
    }

    public Category addCategory(Category category) {
        return _categoryRepository.save(category);
    }

    public void delete (Long id) {
        try {
            _categoryRepository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e) {
            final String errorMessage = "Category not found by ID: " + id.toString();
            throw new ResourceNotFoundException(errorMessage);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
