package com.slow3586.bettingplaftorm.betservice.chatpost;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@Table("chat_post")
public class ChatPostEntity {
    @Id
    UUID id;
    UUID userId;
    int typeId;
}
