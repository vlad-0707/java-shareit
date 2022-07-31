package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.requests.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class Item {
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    private String description;
    private Boolean available;
    private Long owner;
    private ItemRequest request;
}
