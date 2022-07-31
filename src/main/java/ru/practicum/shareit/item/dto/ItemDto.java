package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;



import javax.validation.constraints.NotEmpty;

/**
 * // TODO .
 */
@Data
@Builder
public class ItemDto {
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    private Boolean available;

}
