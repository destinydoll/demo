package com.example.demo;

import com.example.demo.database.entity.UserData;
import com.example.demo.database.repository.UserRepository;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserDataServiceTests {
    @Autowired
    UserService userService;
    @MockBean
    UserRepository userRepository;


    @Test
    void testCreateUserSuccess() {
        UserData testCreateUser = new UserData("testname", "testsurname", "abc@testmail.com");
        when(userRepository.save(any(UserData.class))).thenReturn(testCreateUser);
        assertEquals(testCreateUser,userService.createUser(testCreateUser));
    }

    @Test
    void testUpdateUserSuccess() {
        UserData testCreateUser = new UserData("testname", "testsurname", "abc@testmail.com");
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testCreateUser));
        when(userRepository.save(any(UserData.class))).thenReturn(testCreateUser);
        assertEquals(Optional.of(testCreateUser),userService.updateUser(1, testCreateUser));
    }

    @Test
    void testUpdateUserNotFound() {
        UserData testCreateUser = new UserData("testname", "testsurname", "abc@testmail.com");
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertEquals(Optional.empty(),userService.updateUser(1, testCreateUser));
    }

    @Test
    void testDeleteUserByIdSuccess() {
        doNothing().when(userRepository).deleteById(anyInt());
        assertTrue(userService.deleteUserById(1));
    }

    @Test
    void testGetAllUserSuccess() {
        UserData testData = new UserData("testname", "testsurname", "abc@testmail.com");
        List<UserData> userDataList = new ArrayList<>();
        userDataList.add(testData);

        when(userRepository.findAll()).thenReturn(userDataList);
        assertEquals(1, userService.getAllUsers().size());
    }

    @Test
    void testFindUserByIdSuccess() {
        UserData testData = new UserData("testname", "testsurname", "abc@testmail.com");
        when(userRepository.findById(1)).thenReturn(Optional.of(testData));

        assertEquals(Optional.of(testData), userService.findById(1));
    }
}

