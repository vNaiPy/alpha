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
import com.naipy.alpha.modules.user_address.enums.AddressUsageType;
import com.naipy.alpha.modules.user_address.service.UserAddressService;
import com.naipy.alpha.modules.utils.ServiceUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class StoreService extends ServiceUtils {

    private static final Logger logger = LoggerFactory.getLogger(StoreService.class);

    private final UserAddressService _userAddressService;
    private final StoreRepository _storeRepository;
    private final UserRepository _userRepository;

    @Autowired
    public StoreService (StoreRepository storeRepository, UserAddressService userAddressService, UserRepository userRepository) {
        this._storeRepository = storeRepository;
        this._userAddressService = userAddressService;
        this._userRepository = userRepository;
    }

    public List<StoreDTO> findAll () {
        return _storeRepository.findAll().stream().map(StoreDTO::createStoreDTO).toList();
    }

    public StoreDTO findById (String ownerId) {
        Optional<Store> storeOptional = _storeRepository.findByOwnerId(ownerId);
        if (storeOptional.isEmpty()) throw new ResourceNotFoundException(ownerId);
        return StoreDTO.createStoreDTO(storeOptional.get());
    }

    public StoreDTO findByName (final String name) {
        Optional<Store> storeOptional = _storeRepository.findByName(name);
        if (storeOptional.isEmpty()) throw new ResourceNotFoundException(name);
        return StoreDTO.createStoreDTO(storeOptional.get());
    }

    public List<StoreDTO> findByNameContaining (final String name) {
        List<Store> storeList = _storeRepository.findAllByName(name);
        if (storeList.isEmpty()) throw new ResourceNotFoundException(name);
        return _storeRepository.findAllByName(name).stream().map(StoreDTO::createStoreDTO).toList();
    }

    public StoreDTO findStoreByCurrentUser () {
        Optional<Store> storeOptional = _storeRepository.findByOwnerId(getIdCurrentUser().getId());
        if (storeOptional.isEmpty()) throw new ResourceNotFoundException("Store not exists");
        return StoreDTO.createStoreDTO(storeOptional.get());
    }

    @Transactional
    public Store register (StoreDTO storeDTO, AddressInput addressInput) {
        try {
            _userAddressService.addAddressToUser(addressInput, AddressUsageType.BUSINESS);
            User currentUser = getIdCurrentUser();
            Store store = Store.builder()
                    .id(generateUUID())
                    .name(storeDTO.name())
                    .logoUrl(storeDTO.logoUrl())
                    .bannerUrl(storeDTO.bannerUrl())
                    .description(storeDTO.description())
                    .createdAt(Instant.now())
                    .storeStatus(StoreStatus.ACTIVE)
                    .build();
            store.setOwner(currentUser);
            currentUser.setStore(store);
            return _userRepository.save(currentUser).getStore();
        } catch (DataIntegrityViolationException e) {
            final String errorMessage = "Store already registered for this user with ID: ".concat(getIdCurrentUser().getId());
            logger.warn(errorMessage);
            throw new StoreAlreadyRegisteredException(errorMessage);
        }
    }

    @Transactional
    public void delete (String ownerId) {
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

    @Transactional
    public Store update (StoreDTO storeDTO) {
        try {
            Store entity = _storeRepository.getReferenceById(getIdCurrentUser().getId());
            updateData(storeDTO, entity);
            return _storeRepository.save(entity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Transactional
    public StoreDTO desactivate () {
        try {
            Store entity = _storeRepository.getReferenceById(getIdCurrentUser().getId());
            entity.setStoreStatus(StoreStatus.DESACTIVATED);
            return StoreDTO.createStoreDTO(_storeRepository.save(entity));
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
