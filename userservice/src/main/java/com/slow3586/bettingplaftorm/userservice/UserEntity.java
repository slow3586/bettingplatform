package com.slow3586.bettingplaftorm.userservice;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@Table("users")
public class UserEntity {
    @Id
    UUID id;
    String email;
    String name;
    String password;
}
