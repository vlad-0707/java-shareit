package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.NestedBookingDto;

import java.util.List;

@Data
public class ItemOwnerDto {
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private List<CommentDto> comments;

    private NestedBookingDto lastBooking;

    private NestedBookingDto nextBooking;
}
