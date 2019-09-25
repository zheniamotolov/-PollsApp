package com.example.poll.payload;

import jdk.jfr.DataAmount;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserSummaryUpdateDTO {

    @Size(min = 4, max = 40)
    private String name;

    @Size(min = 3, max = 15)
    private String username;
}
