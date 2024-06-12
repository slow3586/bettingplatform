package com.slow3586.bettingplaftorm.api;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ChatPostDto {
    UUID id;
    UUID userId;
    int typeId;
}
