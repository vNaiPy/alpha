package com.naipy.alpha.modules.store.service;

import com.naipy.alpha.modules.store.model.Store;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.store.repository.StoreRepository;
import com.naipy.alpha.modules.user.repository.UserRepository;
import com.naipy.alpha.modules.exceptions.services.DatabaseException;
import com.naipy.alpha.modules.exceptions.services.ResourceNotFoundException;
import com.naipy.alpha.modules.exceptions.services.StoreAlreadyRegisteredException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {

    @Autowired
    private final UserRepository _userRepository;

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

    public Store register (Store store) {
        try {
            User currentUser = _userRepository.getReferenceById(getIdCurrentUser().getId());

            Store newStore = Store.builder()
                    .name(store.getName())
                    .logoUrl(store.getLogoUrl())
                    .bannerUrl(store.getBannerUrl())
                    .instant(Instant.now())
                    .owner(currentUser)
                    .build();

            currentUser.setStore(newStore);
            return _userRepository.save(currentUser).getStore();
        } catch (DataIntegrityViolationException e) {
            throw new StoreAlreadyRegisteredException("User already owns a store");
        }
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
