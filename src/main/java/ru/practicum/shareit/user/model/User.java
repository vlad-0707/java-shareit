package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

/**
 * // TODO .
 */
@Data
@Builder
public class User {
    private Long id;
    private String name;
    private String email;
}
