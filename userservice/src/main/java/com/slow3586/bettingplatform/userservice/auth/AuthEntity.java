package com.slow3586.bettingplatform.userservice.auth;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@Table("auth")
public class AuthEntity {
    @Id
    UUID id;
    UUID userId;
    String login;
    String role;
    String email;
    @ToString.Exclude
    String password;
}
