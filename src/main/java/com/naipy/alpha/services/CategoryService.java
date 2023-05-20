package com.naipy.alpha.services;

import com.naipy.alpha.entities.Category;
import com.naipy.alpha.repositories.CategoryRepository;
import com.naipy.alpha.services.exceptions.DatabaseException;
import com.naipy.alpha.services.exceptions.ResourceNotFoundException;
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
            throw new ResourceNotFoundException(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
