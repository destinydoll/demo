package com.example.demo;

import com.example.demo.controller.UserController;
import com.example.demo.database.entity.UserData;
import com.example.demo.model.UserRequestModel;
import com.example.demo.service.UserService;
import com.example.demo.validator.Validator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest()
@ContextConfiguration(classes = UserController.class)
class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserData userData;

    @MockBean
    private UserService userService;

    @MockBean
    Validator emailValidator;
    @BeforeEach
    public void setUp(){
    }

    @Test
    void testGetAllUser_hasContent() throws Exception {
        UserData data = new UserData("testname","testsurname", "test@gmail.com");
        List<UserData> userData = new ArrayList<>();
        userData.add(data);

        when(userService.getAllUsers()).thenReturn(userData);

        mockMvc.perform(get("/Users/")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }
    @Test
    void testGetAllUser_hasNoContent() throws Exception {
        when(userService.getAllUsers()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/Users/")
                        .contentType("application/json"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllUser_thenThrowError() throws Exception {
        when(userService.getAllUsers()).thenThrow(new RuntimeException());

        mockMvc.perform(get("/Users/")
                        .contentType("application/json"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testUserById_hasContent() throws Exception {
        UserData userData = new UserData("testname","testsurname", "test@gmail.com");

        when(userService.findById(anyInt())).thenReturn(Optional.of(userData));

        mockMvc.perform(get("/Users/{id}",1)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }
    @Test
    void testUserById_hasNoContent() throws Exception {
        when(userService.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/Users/{id}",1)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }
    @Test
    void testCreate_thenReturns200() throws Exception {
        UserRequestModel userRequestModel = new UserRequestModel("testname","testsurname", "gmail.com");
        UserData userData = new UserData("testname","testsurname", "gmail.com");

        when(emailValidator.validateEmail(anyString())).thenReturn(true);
        when(userService.createUser(any(UserData.class))).thenReturn(userData);

        mockMvc.perform(post("/Users/")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userRequestModel)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreate_thenReturns422() throws Exception {
        UserRequestModel userRequestModel = new UserRequestModel("testname","testsurname", "gmail.com");
        when(emailValidator.validateEmail(anyString())).thenReturn(false);

        mockMvc.perform(post("/Users/")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userRequestModel)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testCreate_thenReturns500() throws Exception {
        UserRequestModel userRequestModel = new UserRequestModel("testname","testsurname", "gmail.com");
        when(emailValidator.validateEmail(anyString())).thenReturn(true);
        when(userService.createUser(any(UserData.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/Users/")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userRequestModel)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testPut_thenReturnsOK() throws Exception {
        UserRequestModel userRequestModel = new UserRequestModel("testname","testsurname", "gmail.com");
        UserData userData = new UserData("testname","testsurname", "gmail.com");
        when(emailValidator.validateEmail(anyString())).thenReturn(true);
        when(userService.updateUser(anyInt(),any(UserData.class))).thenReturn(Optional.of(userData));

        mockMvc.perform(put("/Users/{id}",1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userRequestModel)))
                .andExpect(status().isOk());
    }

    @Test
    void testPut_thenReturnsNotFound() throws Exception {
        UserRequestModel userRequestModel = new UserRequestModel("testname","testsurname", "gmail.com");
        when(emailValidator.validateEmail(anyString())).thenReturn(true);
        when(userService.updateUser(anyInt(),any(UserData.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/Users/{id}",1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userRequestModel)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPut_thenReturnsUnprocessedEntity() throws Exception {
        UserRequestModel userData = new UserRequestModel("testname","testsurname", "gmail.com");
        when(emailValidator.validateEmail(anyString())).thenReturn(false);
        when(userService.updateUser(anyInt(),any(UserData.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/Users/{id}",1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userData)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testPatch_thenReturnsOK() throws Exception {
        UserRequestModel userRequestModel = new UserRequestModel();
        userData.setName("updateName");

        UserData userData = new UserData("testname","testsurname", "gmail.com");
        when(userService.updateUser(anyInt(),any(UserData.class))).thenReturn(Optional.of(userData));

        mockMvc.perform(patch("/Users/{id}",1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userRequestModel)))
                .andExpect(status().isOk());
    }

    @Test
    void testPatchUpdateEmail_thenReturnsOK() throws Exception {
        UserRequestModel userRequestModel = new UserRequestModel();
        userData.setEmail("updateName@email.com");

        UserData userData = new UserData("testname","testsurname", "gmail.com");
        when(userService.updateUser(anyInt(),any(UserData.class))).thenReturn(Optional.of(userData));

        mockMvc.perform(patch("/Users/{id}",1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userRequestModel)))
                .andExpect(status().isOk());
    }

    @Test
    void testPatch_thenReturnsNotFound() throws Exception {
        UserRequestModel userData = new UserRequestModel();
        userData.setName("updateName");
        when(userService.updateUser(anyInt(),any(UserData.class))).thenReturn(Optional.empty());

        mockMvc.perform(patch("/Users/{id}",1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userData)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPatch_thenReturnsUnprocessedEntity() throws Exception {
        UserRequestModel userRequestModel = new UserRequestModel();
        userRequestModel.setEmail("testemail");

        when(emailValidator.validateEmail(anyString())).thenReturn(false);
        when(userService.updateUser(anyInt(),any(UserData.class))).thenReturn(Optional.empty());

        mockMvc.perform(patch("/Users/{id}",1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userRequestModel)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testDelete_thenReturnsOK() throws Exception {
        UserData userData = new UserData();
        userData.setName("updateName");
        when(userService.deleteUserById(anyInt())).thenReturn(true);
        mockMvc.perform(delete("/Users/{id}",1)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete_thenReturnsNoContent() throws Exception {
        UserData userData = new UserData();
        userData.setName("updateName");
        when(userService.deleteUserById(anyInt())).thenReturn(false);
        mockMvc.perform(delete("/Users/{id}",1)
                        .contentType("application/json"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDelete_thenReturnsNoError() throws Exception {
        UserData userData = new UserData();
        userData.setName("updateName");
        doThrow(new RuntimeException()).when(userService).deleteUserById(anyInt());
        mockMvc.perform(delete("/Users/{id}",1)
                        .contentType("application/json"))
                .andExpect(status().isInternalServerError());
    }
}
