package com.example.demo.service;

import com.example.demo.database.entity.UserData;

import java.util.List;
import java.util.Optional;

public interface UserServiceInterface {
    UserData createUser(UserData userData);

    Optional<UserData> updateUser(int id, UserData userData);

    boolean deleteUserById(int id);

    List<UserData> getAllUsers();

    Optional<UserData> findById(int userId);
}
