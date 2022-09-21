package shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shareit.item.dto.ItemDto;
import shareit.user.model.User;


import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {

    private Long id;
    @NotNull
    private String description;
    private LocalDateTime created = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
    private User requestor;
    private List<ItemDto> items;

}
