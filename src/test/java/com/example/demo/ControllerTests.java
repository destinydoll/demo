package com.example.demo;

import com.example.demo.controller.UserController;
import com.example.demo.database.entity.User;
import com.example.demo.database.repository.UserRepository;
import com.example.demo.validator.EmailValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest()
@ContextConfiguration(classes = UserController.class)
class ControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private User user;

    @MockBean
    UserRepository userRepository;

    @MockBean
    EmailValidator emailValidator;
    @BeforeEach
    public void setUp() throws Exception {
        User newuser = new User("newtestname","newtestsurname", "newtest@gmail.com");
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(newuser);
    }
    
    @Test
    void testCreate_thenReturns200() throws Exception {
        User user = new User("testname","testsurname", "test@gmail.com");

        mockMvc.perform(post("/Users/")
                        .contentType("application/json")
                        .param("sendWelcomeMail", "true")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    void testCreate_thenReturns422() throws Exception {
        User user = new User("testname","testsurname", "gmail.com");

        mockMvc.perform(post("/Users/")
                        .contentType("application/json")
                        .param("sendWelcomeMail", "true")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isUnprocessableEntity());
    }
}
