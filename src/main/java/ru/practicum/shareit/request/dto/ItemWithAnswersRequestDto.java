package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemShortForRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemWithAnswersRequestDto {

    private Long id;
    private String description;
    private LocalDateTime created;
    private UserDto requestor;
    private List<ItemShortForRequestDto> items;
}
