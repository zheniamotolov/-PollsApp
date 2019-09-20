package com.example.poll.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class UserSummary {
    private Long id;
    private String name;
    private String username;


}
