package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemShortForRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemWithAnswersRequestDto {

    private Long id;
    @NotBlank
    private String description;
    private LocalDateTime created;
    private UserDto requestor;
    private List<ItemShortForRequestDto> items;
}
