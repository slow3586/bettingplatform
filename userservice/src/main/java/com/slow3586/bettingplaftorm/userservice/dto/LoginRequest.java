package com.slow3586.bettingplaftorm.userservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {
    String login;
    String password;
}
