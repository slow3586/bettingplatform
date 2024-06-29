package com.slow3586.bettingplatform.api.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRegisterRequest {
    String email;
    @ToString.Exclude
    String password;
}
