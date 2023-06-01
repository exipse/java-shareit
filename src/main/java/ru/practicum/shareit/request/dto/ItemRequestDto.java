package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ItemRequestDto {

    private Long id;
    @NotBlank
    private String description;
    private LocalDateTime created;
    private UserDto requestor;

}
