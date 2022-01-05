package com.example.demo.validator;


import org.springframework.stereotype.Service;

@Service
public class EmailValidator {

    org.apache.commons.validator.routines.EmailValidator emailValidator;
        public  boolean validateEmail(String emailAddress) {
        return emailValidator.isValid(emailAddress);
    }
}
