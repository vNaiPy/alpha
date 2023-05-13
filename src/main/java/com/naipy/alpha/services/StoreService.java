package com.naipy.alpha.services;

import com.naipy.alpha.entities.Store;
import com.naipy.alpha.entities.User;
import com.naipy.alpha.repositories.StoreRepository;
import com.naipy.alpha.services.exceptions.DatabaseException;
import com.naipy.alpha.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {

    @Autowired
    private final StoreRepository _storeRepository;

    public List<Store> findAll () {
        return _storeRepository.findAll();
    }

    public Store findById (Long ownerId) {
        Optional<Store> storeOptional = _storeRepository.findByOwnerId(ownerId);

        if (storeOptional.isEmpty()) throw new ResourceNotFoundException(ownerId);
        return storeOptional.get();
    }

    public Store findStoreByCurrentUser () {
        Optional<Store> storeOptional = _storeRepository.findByOwnerId(getIdCurrentUser().getId());

        if (storeOptional.isEmpty()) throw new ResourceNotFoundException("Store not exists");
        return storeOptional.get();
    }

    public void delete (Long ownerId) {
        try {
            _storeRepository.deleteByOwnerId(ownerId);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(ownerId);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public Store update (Long ownerId, Store store) {
        try {
            Store entity = _storeRepository.getReferenceById(ownerId);
            updateData(store, entity);
            return _storeRepository.save(entity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }

    }

    private User getIdCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    private void updateData(Store store, Store entity) {;
        entity.setLogoUrl(store.getLogoUrl());
        entity.setBannerUrl(store.getBannerUrl());
    }
}
