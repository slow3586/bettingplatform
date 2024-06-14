package com.slow3586.bettingplatform.api.mainservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameChoiceDto {
   int id;
   String name;
   String type;
   BigInteger value;
}
