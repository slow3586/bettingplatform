package com.slow3586.bettingplatform.betservice.game;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class GameChoiceEntity {
    @Id
    int id;
    String name;
    String type;
    int value;
}
