package com.example.poll.payload;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UserSummaryUpdateDTO {

    @Size(min = 4, max = 40)
    private String name;

}
