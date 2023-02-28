package com.naipy.alpha.services;

import com.naipy.alpha.entities.User;
import com.naipy.alpha.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository _userRepository;

    public List<User> findAll () {
        return _userRepository.findAll();
    }

    public User findById (Long id) {
        Optional<User> userOptional = _userRepository.findById(id);
        return userOptional.get();
    }

    public User insert (User user) {
        return _userRepository.save(user);
    }

    public void delete (Long id) {
        _userRepository.deleteById(id);
    }
}
