package com.example.demo.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class UserRequestModel {
    private String name;
    private String surname;
    private String email;

    public UserRequestModel(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public UserRequestModel() {
    }
}
