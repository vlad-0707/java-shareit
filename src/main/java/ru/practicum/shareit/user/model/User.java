package ru.practicum.shareit.user.model;

import lombok.*;
import javax.validation.constraints.Email;
@Data
@Builder
public class User {
    private Long id;
    private String name;
    private String email;
}
