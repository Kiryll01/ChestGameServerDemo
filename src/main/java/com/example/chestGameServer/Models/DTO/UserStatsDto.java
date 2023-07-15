package com.example.chestGameServer.Models.DTO;

import com.example.chestGameServer.Models.Abstract.AbstractUserStats;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserStatsDto extends AbstractUserStats {
    String inf;
}
