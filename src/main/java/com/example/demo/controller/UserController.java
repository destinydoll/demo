package com.example.demo.controller;

import com.example.demo.database.entity.User;
import com.example.demo.database.repository.UserRepository;
import com.example.demo.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;


    @Autowired
    EmailValidator emailValidator;

    @GetMapping("/Users")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> Users = new ArrayList<User>();
            userRepository.findAll().forEach(Users::add);
            if (Users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(Users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/Users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
        Optional<User> UserData = userRepository.findById(id);

        if (UserData.isPresent()) {
            return new ResponseEntity<>(UserData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/Users")
    public ResponseEntity<User> createUser(@RequestBody User User) {
        try {
            if(emailValidator.validateEmail(User.getEmail())) {
                User _User = userRepository
                        .save(User);
                return new ResponseEntity<>(_User, HttpStatus.CREATED);
            }else{
                return  new ResponseEntity<>(null,HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/Users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") int id, @RequestBody User User) {
        if(emailValidator.validateEmail(User.getEmail())) {
            Optional<User> UserData = userRepository.findById(id);
            if (UserData.isPresent()) {
                User UserToUpdate = UserData.get();
                UserToUpdate.setName(User.getName());
                UserToUpdate.setSurname(User.getSurname());
                UserToUpdate.setEmail(User.getEmail());
                return new ResponseEntity<>(userRepository.save(UserToUpdate), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }else {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PatchMapping("/Users/{id}")
    public ResponseEntity<User> patchUser(@PathVariable("id") int id, @RequestBody User User) {
        Optional<User> UserData = userRepository.findById(id);
        if (UserData.isPresent()) {
            User UserToUpdate = UserData.get();
            UserToUpdate.setName(User.getName());
            UserToUpdate.setSurname(User.getSurname());
            UserToUpdate.setEmail(User.getEmail());
            return new ResponseEntity<>(userRepository.save(UserToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/Users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") int id) {
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
