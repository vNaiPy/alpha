package com.naipy.alpha.modules.user.service;

import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.user.models.UserDTO;
import com.naipy.alpha.modules.user.repository.UserRepository;
import com.naipy.alpha.modules.exceptions.services.DatabaseException;
import com.naipy.alpha.modules.exceptions.services.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository _userRepository;

    public List<UserDTO> findAll () {
        return _userRepository.findAll()
                .stream().map(UserDTO::createUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO findById (Long id) {
        Optional<User> userOptional = _userRepository.findById(id);
        if (userOptional.isEmpty()) throw new ResourceNotFoundException(id);
        return UserDTO.createUserDTO(userOptional.get());
    }

    public User insert (User user) {
        return _userRepository.save(user);
    }

    public void delete (Long id) {
        try {
            _userRepository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public User update (Long id, User user) {
        try {
            User entity = _userRepository.getReferenceById(id);
            updateData(user, entity);
            return _userRepository.save(entity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }

    }

    private void updateData(User user, User entity) {
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setPhone(user.getPhone());
    }
}
