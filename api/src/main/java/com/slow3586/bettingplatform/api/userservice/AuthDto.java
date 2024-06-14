package com.slow3586.bettingplatform.api.userservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthDto {
    UUID id;
    String login;
    String password;
    String role;
}
