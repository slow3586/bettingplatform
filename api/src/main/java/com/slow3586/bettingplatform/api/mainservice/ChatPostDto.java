package com.slow3586.bettingplatform.api.mainservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatPostDto {
    UUID id;
    UUID userId;
    int typeId;
}
