package com.slow3586.bettingplaftorm.api;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {
    String login;
    String password;
}
