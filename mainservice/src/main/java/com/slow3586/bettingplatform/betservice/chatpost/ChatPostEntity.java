package com.slow3586.bettingplatform.betservice.chatpost;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Table("chat_post")
public class ChatPostEntity {
    @Id
    UUID id;
    LocalDateTime createdAt;
    UUID userId;
    int typeId;
    String status;
}
