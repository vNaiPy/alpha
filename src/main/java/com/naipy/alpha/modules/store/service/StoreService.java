package com.naipy.alpha.modules.store.service;

import com.naipy.alpha.modules.exceptions.services.StoreAlreadyRegisteredException;
import com.naipy.alpha.modules.store.enums.StoreStatus;
import com.naipy.alpha.modules.store.models.Store;
import com.naipy.alpha.modules.store.models.StoreDTO;
import com.naipy.alpha.modules.user.controllers.AddressInput;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.store.repository.StoreRepository;
import com.naipy.alpha.modules.exceptions.services.DatabaseException;
import com.naipy.alpha.modules.exceptions.services.ResourceNotFoundException;
import com.naipy.alpha.modules.user.repository.UserRepository;
import com.naipy.alpha.modules.user_address.service.UserAddressService;
import com.naipy.alpha.modules.user_address.service.impl.UserAddressServiceImpl;
import com.naipy.alpha.modules.utils.ServiceUtils;
import com.naipy.alpha.modules.utils.models.PaginatorInput;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class StoreService {

    private static final Logger logger = LoggerFactory.getLogger(StoreService.class);

    private final UserAddressService userAddressService;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Autowired
    public StoreService (StoreRepository storeRepository, UserAddressService userAddressService, UserRepository userRepository) {
        this.storeRepository = storeRepository;
        this.userAddressService = userAddressService;
        this.userRepository = userRepository;
    }

    public List<StoreDTO> findAll () {
        return storeRepository.findAll().stream().map(StoreDTO::createStoreDTO).toList();
    }

    public StoreDTO findById (String ownerId) {
        Optional<Store> storeOptional = storeRepository.findByOwnerId(ownerId);
        if (storeOptional.isEmpty()) throw new ResourceNotFoundException(ownerId);
        return StoreDTO.createStoreDTO(storeOptional.get());
    }

    public StoreDTO findByName (final String name) {
        Optional<Store> storeOptional = storeRepository.findByName(name);
        if (storeOptional.isEmpty()) throw new ResourceNotFoundException(name);
        return StoreDTO.createStoreDTO(storeOptional.get());
    }

    public List<StoreDTO> findByNameContaining (final String name, PaginatorInput paginatorInput) {
        Page<Store> storeList = storeRepository.findAllByName(name, ServiceUtils.createPageable(paginatorInput));
        if (storeList.isEmpty()) throw new ResourceNotFoundException(name);
        return StoreDTO.storePageToStoreDTOList(storeList);
    }

    public StoreDTO findStoreByCurrentUser () {
        Optional<Store> storeOptional = storeRepository.findByOwnerId(ServiceUtils.getCurrentUser().getId());
        if (storeOptional.isEmpty()) throw new ResourceNotFoundException("Store not exists");
        return StoreDTO.createStoreDTO(storeOptional.get());
    }

    @Transactional
    public Store register (StoreDTO storeDTO, AddressInput addressInput) {
        try {
            userAddressService.addAddressToUser(addressInput);
            User currentUser = ServiceUtils.getCurrentUser();
            Store store = Store.builder()
                    .id(ServiceUtils.generateUUID())
                    .name(storeDTO.name())
                    .logoUrl(storeDTO.logoUrl())
                    .bannerUrl(storeDTO.bannerUrl())
                    .description(storeDTO.description())
                    .createdAt(Instant.now())
                    .storeStatus(StoreStatus.ACTIVE)
                    .build();
            store.setOwner(currentUser);
            currentUser.setStore(store);
            return userRepository.save(currentUser).getStore();
        } catch (DataIntegrityViolationException e) {
            final String errorMessage = "Store already registered for this user with ID: ".concat(ServiceUtils.getCurrentUser().getId());
            logger.warn(errorMessage);
            throw new StoreAlreadyRegisteredException(errorMessage);
        }
    }

    @Transactional
    public void delete (String ownerId) {
        try {
            storeRepository.deleteByOwnerId(ownerId);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(ownerId);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    public Store update (StoreDTO storeDTO) {
        try {
            Store entity = storeRepository.getReferenceById(ServiceUtils.getCurrentUser().getId());
            updateData(storeDTO, entity);
            return storeRepository.save(entity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Transactional
    public StoreDTO desactivate () {
        try {
            Store entity = storeRepository.getReferenceById(ServiceUtils.getCurrentUser().getId());
            entity.setStoreStatus(StoreStatus.DESACTIVATED);
            return StoreDTO.createStoreDTO(storeRepository.save(entity));
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    private void updateData (StoreDTO store, Store entity) {
        entity.setLogoUrl(store.logoUrl());
        entity.setBannerUrl(store.bannerUrl());
        entity.setDescription(store.description());
        entity.setStoreStatus(store.storeStatus());
    }
}
