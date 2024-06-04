package com.slow3586.bettingplaftorm.userservice.dto;

import lombok.Value;

import java.util.Date;

@Value
public class RegisterRequest {
    String email;
    String password;
}
