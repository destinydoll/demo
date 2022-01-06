package com.example.demo.service;

import com.example.demo.database.entity.UserData;
import com.example.demo.database.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService implements UserServiceInterface {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserData createUser(UserData userData) {
        return userRepository.save(userData);
    }

    @Override
    public Optional<UserData> updateUser(int id, UserData user) {
        Optional<UserData> userData = userRepository.findById(id);
        if (userData.isPresent()) {
            UserData userDataToUpdate = userData.get();
            if (!ObjectUtils.isEmpty(user.getName())) {
                userDataToUpdate.setName(user.getName());
            }
            if (!ObjectUtils.isEmpty(user.getSurname())) {
                userDataToUpdate.setSurname(user.getSurname());
            }
            if (!ObjectUtils.isEmpty(user.getEmail())) {
                userDataToUpdate.setEmail(user.getEmail());
            }
            return Optional.of(userRepository.save(userDataToUpdate));
        } else {
            log.info("user not found");
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteUserById(int id) {
        try {
            userRepository.deleteById(id);
            log.info("user deleted");
            return true;
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return false;
        }
    }

    @Override
    public List<UserData> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserData> findById(int userId) {
        return userRepository.findById(userId);
    }
}
