package com.example.demo.controller;

import com.example.demo.database.entity.UserData;
import com.example.demo.model.UserRequestModel;
import com.example.demo.service.UserService;
import com.example.demo.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    UserService userService;


    @Autowired
    Validator emailValidator;

    @GetMapping("/Users")
    public ResponseEntity<List<UserData>> getAllUsers() {
        try {
            List<UserData> userData = userService.getAllUsers();
            if (userData.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(userData, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/Users/{id}")
    public ResponseEntity<UserData> getUserById(@PathVariable("id") int id) {
        Optional<UserData> userData = userService.findById(id);
        return userData.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).
                orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/Users")
    public ResponseEntity<UserData> createUser(@RequestBody UserRequestModel user) {
        try {
            if (emailValidator.validateEmail(user.getEmail())) {
                UserData userDataToCreate = new UserData();
                userDataToCreate.setName(user.getName());
                userDataToCreate.setSurname(user.getSurname());
                userDataToCreate.setEmail(user.getEmail());
                return new ResponseEntity<>(userService.createUser(userDataToCreate), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/Users/{id}")
    public ResponseEntity<UserData> updateUser(@PathVariable("id") int id, @RequestBody UserRequestModel user) {
        if (emailValidator.validateEmail(user.getEmail())) {
            UserData userDataToUpdate = new UserData(user.getName(),user.getSurname(),user.getEmail());
            Optional<UserData> userData = userService.updateUser(id, userDataToUpdate);
            return userData.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }else{
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PatchMapping("/Users/{id}")
    public ResponseEntity<UserData> patchUser(@PathVariable("id") int id, @RequestBody UserRequestModel user) {
        boolean isValidRequest = user.getEmail() == null || emailValidator.validateEmail(user.getEmail());
        if (isValidRequest) {
            UserData userDataToUpdate = new UserData(user.getName(),user.getSurname(),user.getEmail());
            Optional<UserData> userData = userService.updateUser(id, userDataToUpdate);
            return userData.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }else{
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @DeleteMapping("/Users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") int id) {
        try {
            if(userService.deleteUserById(id)){
                return new ResponseEntity<>(HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
